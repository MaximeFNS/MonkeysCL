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
	
	private HashMap<Integer,Monkey> monkeys;
	
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
		int j = 1;
		int x = 0;
		int y = 0;
		
		while(retour) {
			myElement = em.find(Element.class, j);
			if (myElement != null) {
				j++;
		}
			else {
				retour = false;
			}
		}
		pirate = new Pirate(j, x, y, 100);
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
		
		
		createMonkey();
		createMonkey();
		createMonkey();
		createMonkey();
		communication.sendMonkeys(monkeys);
	}
	
	private void createMonkey() {
		boolean retour = true;
		int j = 1;
		
		while(retour) {
			myElement = em.find(Element.class, j);
			if (myElement != null) {
				if (myElement.getType().contentEquals("Monkey")) {
					monkeys.put(j, (Monkey) myElement);
				}
				j++;
		}
			else {
				retour = false;
			}
		}
		int[] position = positionAleatoire();
		Monkey monkey = new Monkey(j, position[0], position[1], 50);
		monkey.setType("Monkey");
		monkeys.put(j,monkey);
		System.out.println("Singe : " + monkeys.get(j));
		em.persist(monkey);
	}
	
	private int[] positionAleatoire() {
		Random random = new Random();
		int[] result = new int[2];
		System.out.println("Iciiiiiiiiiii");
		System.out.println(random.nextInt(8)+1);
		int x = random.nextInt(8)+1;
		int y = random.nextInt(8)+1;
		if(monkeys != null) {
			monkeys.forEach((k,v) -> {
				if(v.getPosX() == x && v.getPosY() == y) {
					positionAleatoire();
				} 
			});
		} else {
			result[0] = x;
			result[1] = y;
		}
		
		return result;
	}
	
	
}
