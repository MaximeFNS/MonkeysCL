package monkeys;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class MonkeyIsland
 * @author Mickael Clavreul
 */
@Stateful
public class MonkeyIsland implements MIRemote {

	@PersistenceContext(unitName= "MonkeysDS")
	private EntityManager em;
	
	private Island myLand;
	
	private Pirate pirate;
	
	private Element myElement;
	
	private HashMap<Integer, Pirate> pirates = new HashMap<>();
	
	private HashMap<Integer,Monkey> monkeys = new HashMap<Integer,Monkey>();
	
	@EJB
	Configuration configuration;
	
	@EJB
	Communication communication;
	
	/**
     * Default constructor
     */
    public MonkeyIsland() {}

	@Override
	public Pirate subscribe(String id) {
		int idInt = Integer.valueOf(id);
		this.newGame(idInt);

		return pirate;
	
	}
	
	@Override
	public void disconnect(String id) {
		
	}

	@Override
	public void move(String id, String deplacement) {
		int idInt = Integer.valueOf(id);
		
		Pirate newPirate = pirates.get(idInt);
		String requete = "";
		switch(deplacement) {
		  case "-1-0":
			  newPirate.setPosX(newPirate.getPosX()-1);
			  requete = "UPDATE Element SET POSX = "+newPirate.getPosX()+ " WHERE ID = " + newPirate.getId();
		    break;
		  case "0-1":
			  newPirate.setPosY(newPirate.getPosY()-1);
			  requete = "UPDATE Element SET POSY = "+newPirate.getPosY()+ " WHERE ID = " + newPirate.getId();
		    break;
		  case "1-0":
			  newPirate.setPosX(newPirate.getPosX()+1);
			  requete = "UPDATE Element SET POSX = "+newPirate.getPosX()+ " WHERE ID = " + newPirate.getId();
			    break;
		  case "0--1":
			  newPirate.setPosY(newPirate.getPosY()+1);
			  requete = "UPDATE Element SET POSY = "+newPirate.getPosY()+ " WHERE ID = " + newPirate.getId();
			    break;
		  default:
		}
		pirates.replace(idInt, newPirate);
		
		Element toUpdate = new Element();
		toUpdate.setId(newPirate.getId());
		toUpdate.setPosX(newPirate.getPosX());
		toUpdate.setPosY(newPirate.getPosY());
		toUpdate.setType("Pirate");
		em.merge(toUpdate);
		communication.sendPirate(deplacement, id);
		
	}
	
	private void newGame(int id) {
		
		myLand = em.find(Island.class, id);

		if (myLand != null) {
			
			String idString = "" + myLand.getId();
			communication.sendMap(myLand.getMap(), idString);
			
		}else {
			myLand = new Island();
			myLand.setMap(configuration.getMap());
			em.persist(myLand);
			
			String idString = "" + myLand.getId();
			communication.sendMap(myLand.getMap(), idString);
			
		}
		boolean retour = true;
		int j = 2;
		int x = 0;
		int y = 0;
		pirate = new Pirate(j, x, y, 100);
		while(retour) {
			myElement = em.find(Element.class, j);
			if (myElement != null) {
				j++;
		}
			else {
				retour = false;
			}
		}
		
		pirate.setId(j);
		Random random = new Random();
		
		x = random.nextInt(8)+1;
		y = random.nextInt(8)+1;
		
		pirate.setPosX(x);
		pirate.setPosY(y);
		Element e = new Element();

		e.setPosX(x);
		e.setPosY(y);
		e.setType("Pirate");
		
		em.persist(e);
		pirates.put(pirate.getId(), pirate);
		
		createMonkey();
		createMonkey();
		createMonkey();
		createMonkey();
		communication.sendMonkeys(monkeys);
	}
	
	private void createMonkey() {
		boolean retour = true;
		Integer j = 2;
		
		while(retour) {
			myElement = em.find(Element.class, j);
			if (myElement != null) {
				j++;
			}
			else {
				retour = false;
			}
		}
		System.out.println("------>"+j);
		int[] position = positionAleatoire();
		Monkey monkey = new Monkey(j, position[0], position[1], 50);
		monkey.setType("Monkey");
		monkeys.putIfAbsent(j,monkey);
		System.out.println("Singe : " + monkeys.get(j));
		System.out.println("Singe x : " + monkey.getPosX());
		System.out.println("Singe y : " + monkey.getPosY());
		
		Element e = new Element();
		
		e.setPosX(monkey.getPosX());
		e.setPosY(monkey.getPosY());
		e.setType("Monkey");
		em.persist(e);
	}
	
	private int[] positionAleatoire() {
		Random random = new Random();
		int[] result = new int[2];
		int x = random.nextInt(8)+1;
		int y = random.nextInt(8)+1;
		System.out.println("Aléa x:" +x);
		System.out.println("Aléa x:" +y);
		if(monkeys != null) {
			monkeys.forEach((k,v) -> {
				if(v.getPosX() == x && v.getPosY() == y) {
					positionAleatoire();
				} 
			});
		}
		
		result[0] = x;
		result[1] = y;
		return result;
	}
	
	
}
