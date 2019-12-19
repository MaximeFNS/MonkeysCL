
package monkeys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MoveTimer {
	
	@EJB
	Configuration configuration;
	  
	@EJB
    Communication communication;
	  
    @PersistenceContext(unitName="MonkeysDS")
	private EntityManager em;
    
    private Element myElement;
	private HashMap<Integer,Monkey> monkeys = new HashMap<Integer,Monkey>();
	private HashMap<Integer,Rum> bottles = new HashMap<Integer,Rum>();
	  
	  
	public MoveTimer() {
		
	}
	
	@Schedule(second="*/5", minute="*", hour="*", dayOfWeek="*",
		      dayOfMonth="*", month="*", year="*", info="moveTimer")
    private void updateEMonkeys(final Timer t) throws IOException {
		int[][] map = configuration.getMap();
		boolean retour = true;
		Integer j = 2;
		
		
		while(retour) {
			myElement = em.find(Element.class, j);
			if (myElement != null) {
				if(myElement.getType().contentEquals("Monkey")) {
					monkeys.put(j, new Monkey(j,myElement.getPosX(),myElement.getPosY(),50));
				}
				
				if(myElement.getType().contentEquals("Rum")) {
					bottles.put(j, new Rum(j, myElement.getPosX(),myElement.getPosY(),1,25));
				}
				j++;
			}
			else {
				retour = false;
			}
		}
		
		if(monkeys != null) {
			monkeys.forEach((k,v) -> {
		        
		        int newX = v.getPosX();
		        int newY = v.getPosY();
		        
		        	
		        int[] newPosition = positionSuivanteAleatoire(map, monkeys, newX, newY);
		        
		        newX = newPosition[0];
		        newY = newPosition[1];
		        
		        v.setPosX(newX);
		        v.setPosY(newY);
		        
		        Element e = em.find(Element.class, k);
		        e.setPosX(newX);
		        e.setPosY(newY);
		        em.merge(e);
		        
		        Element e2 = em.find(Element.class, k);
		        
			        
			        
		        
		        
			});
			communication.sendMonkeys(monkeys);
			monkeys.clear();
			bottles.clear();
		}    
	}
	
	private int[] positionSuivanteAleatoire(int[][] map, HashMap<Integer,Monkey> monkeys,  int x, int y) {
		
		int alea = (int)(Math.random() * 4);
		int[] result = new int[2];
	
		int newX;
		int newY;
		
        if(alea == 0) {
          newX = x - 1;
          newY = y;
        } else if(alea == 1) {
        	newX = x + 1;
            newY = y;
        } else if(alea == 2) {
        	newX = x ;
            newY = y - 1;
        } else {
        	newX = x;
            newY = y + 1;
        }
        
        if(newX<1 || newX >= map[0].length || newY<1 || newY >= map[0].length) {
        	//positionSuivanteAleatoire(map, x, y);
        	newX = x;
        	newY = y;
        } else {
	        for(int i = 0; i < monkeys.size(); i++) {
        		if(monkeys.get(i + 6).getPosX() == newX && monkeys.get(i + 6).getPosY() == newY) {
        			newX = x;
                	newY = y;
        		}
	        }
        }
        
        result[0] = newX;
		result[1] = newY;
		return result;
	}
}
