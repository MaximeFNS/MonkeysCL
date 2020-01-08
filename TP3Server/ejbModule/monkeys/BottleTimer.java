package monkeys;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import monkeys.communication.Communication;
import monkeys.models.Element;
import monkeys.models.Rum;

@Stateless
public class BottleTimer {
	@EJB
	Configuration configuration;
	  
	@EJB
    Communication communication;
	  
    @PersistenceContext(unitName="MonkeysDS")
	private EntityManager em;
    
    private Element myElement;
	
	private ArrayList<Rum> bottles = new ArrayList<>();
	  
	  
	public BottleTimer() {

	}
	
	@Schedule(second="*/25", minute="*", hour="*", dayOfWeek="*",
		      dayOfMonth="*", month="*", year="*", info="moveTimer")
    private void updateBottles(final Timer t) throws IOException {

		boolean retour = true;
		int j = 1;
		while(retour) {
			myElement = em.find(Element.class, j);
			if (myElement!=null) {			
				if(myElement.getType().contentEquals("Rum")) {
					bottles.add(new Rum(j, myElement.getPosX(),myElement.getPosY(),myElement.getVisibility(),25));
					if(myElement.getVisibility() == 0) {
						myElement.setVisibility(1);
						for(int i = 0; i< bottles.size(); i++) {
							if(bottles.get(i).getId() == j) {
								bottles.get(i).setVisibility(1);
							}
						}
						em.merge(myElement);
					}
					communication.sendRum(bottles);
				}
				
				j++;
		}
			else {
				retour = false;
			}
		}

	

		
	}
}
