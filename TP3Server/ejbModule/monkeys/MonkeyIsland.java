package monkeys;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
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
	
	private HashMap<Integer,Rum> bottles = new HashMap<Integer,Rum>();
	
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
		if(!newPirate.getState().equals("DEAD")) {
			switch(deplacement) {
			  case "-1-0":
				  newPirate.setPosX(newPirate.getPosX()-1);
			    break;
			  case "0-1":
				  newPirate.setPosY(newPirate.getPosY()-1);
				break;
			  case "1-0":
				  newPirate.setPosX(newPirate.getPosX()+1);
				break;
			  case "0--1":
				  newPirate.setPosY(newPirate.getPosY()+1);
				break;
			  default:
			}
			
		
			for(int i = 0; i < monkeys.size(); i++) {
        		if(monkeys.get(i + 6).getPosX() == newPirate.getPosX() && monkeys.get(i + 6).getPosY() == newPirate.getPosY()) {
        			newPirate.setState("DEAD");
        			deplacement = "0-0";
        		}
	        }
			pirates.replace(idInt, newPirate);
			
			Element toUpdate = new Element();
			toUpdate.setId(newPirate.getId());
			toUpdate.setPosX(newPirate.getPosX());
			toUpdate.setPosY(newPirate.getPosY());
			toUpdate.setType("Pirate");
			toUpdate.setState(newPirate.getState());
			toUpdate.setIsland(myLand);
			System.out.println(newPirate.getState());
			em.merge(toUpdate);
			communication.sendPirate(deplacement, id, newPirate.getState());
			communication.sendPirates();
			
		} else {
			communication.sendPirate("0-0", id, newPirate.getState());
			communication.sendPirates();
		}
	}
	
	@Override
	public ArrayList<Pirate> sendAllPirates(Pirate newPirate) {
		ArrayList<Pirate> autresPirates = new ArrayList<>();
		ArrayList<Pirate> autresPirates2 = new ArrayList<>();
		Element e = null;
		for(int k = 2; k < 20;k++) {
			e = em.find(Element.class, k);
			if(e!=null) {
				if(e.getType()=="Pirate" && e.getId()!=newPirate.getId()) {
					Pirate pirat = new Pirate(e.getId(),e.getPosX(),e.getPosY(),100);
					autresPirates.add(pirat);
				}
			}
			e = null;
		}
		myLand = em.find(Island.class, 1);
		Collection<Element> elementsList = myLand.getElements();
		System.out.println("Taille : " + elementsList.size());
		int i = 0;
		for (Element elem : elementsList) {
			System.out.println(elem.toString());
			if(elem.getType()=="Pirate" && elem.getId()!=newPirate.getId()) {
				Pirate pirat = new Pirate(elem.getId(),elem.getPosX(),elem.getPosY(),100);
				autresPirates2.add(pirat);
			}
		}
		//System.out.println(autresPirates2.get(0).toString());
		System.out.println("Size AAAAAAAAA : " +autresPirates.size()+" " + autresPirates2.size());
		return autresPirates2;
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
		boolean isMonkeyPresent = false;
		boolean isBottlePresent = false;
		while(retour) {
			myElement = em.find(Element.class, j);
			if (myElement!=null) {
				if(myElement.getType().contentEquals("Monkey")) {
					isMonkeyPresent = true;
					monkeys.put(j, new Monkey(j,myElement.getPosX(),myElement.getPosY(),50));
				}
				
				if(myElement.getType().contentEquals("Rum")) {
					isBottlePresent = true;
					bottles.put(j, new Rum(j, myElement.getPosX(),myElement.getPosY(),1,25));
				}
				
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
		e.setState("SOBER");
		e.setIsland(myLand);
		em.persist(e);
		pirates.put(pirate.getId(), pirate);
		
		if(!isBottlePresent) {
			createRumBottles();
			createRumBottles();
			createRumBottles();
			createRumBottles();
		}
		communication.sendRum(bottles);
		
		if(!isMonkeyPresent) {
			createMonkey();
			createMonkey();
			createMonkey();
			createMonkey();
		}
		communication.sendMonkeys(monkeys);
		
		communication.sendPirates();
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
		int[] position = positionAleatoire();
		Monkey monkey = new Monkey(j, position[0], position[1], 50);
		monkeys.putIfAbsent(j,monkey);
		
		Element e = new Element();
		
		e.setPosX(monkey.getPosX());
		e.setPosY(monkey.getPosY());
		e.setType("Monkey");
		e.setIsland(myLand);
		em.persist(e);
	}
	
	private int[] positionAleatoire() {
		Random random = new Random();
		int[] result = new int[2];
		int x = random.nextInt(8)+1;
		int y = random.nextInt(8)+1;
		if(monkeys != null) {
			monkeys.forEach((k,v) -> {
				if(v.getPosX() == x && v.getPosY() == y) {
					positionAleatoire();
				} 
			});
			if(bottles != null) {
				bottles.forEach((k,v) -> {
					if(v.getPosX() == x && v.getPosY() == y) {
						positionAleatoire();
					} 
				});
			}
		}
		
		result[0] = x;
		result[1] = y;
		return result;
	}
	
	private void createRumBottles() {
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
		int[] position = positionAleatoire();
		Rum rum = new Rum(j, position[0], position[1], 1,25);
		rum.setType("Rum");
		bottles.putIfAbsent(j,rum);
		
		Element e = new Element();
		
		e.setPosX(rum.getPosX());
		e.setPosY(rum.getPosY());
		e.setType("Rum");
		e.setIsland(myLand);
		em.persist(e);
	}

	@Override
	public int getEnergyIfRum(Pirate pirate) {
		ArrayList<Rum> allRums = new ArrayList<>();
		Element e = null;
		for(int k = 2; k < 20;k++) {
			e = em.find(Element.class, k);
			if(e!=null) {
				if(e.getType()=="Rum") {
					Rum rum = new Rum(e.getId(),e.getPosX(),e.getPosY(),100,25);
					allRums.add(rum);
				}
			}
			e = null;
		}
		int energy = 0;
		for(int i = 0; i<allRums.size();i++) {
			if(pirate.getPosX()==allRums.get(i).getPosX() && pirate.getPosY()==allRums.get(i).getPosY()) {
				Rum rum = bottles.get(allRums.get(i).getId());
				energy = rum.getEnergy();
				//TODO : Change Visibility
			}
		}
		return energy;
	}

	@Override
	public void informLeaving(Pirate pirate) {
		Element e = em.find(Element.class, pirate.getId());
		e.setPosX(pirate.getPosX());
		e.setPosY(pirate.getPosY());
		e.setState(pirate.getState());
		e.setType("Pirate");
		communication.removePirate(pirate.getId());
		System.out.println("Element : " + e.toString());
		pirates.remove(pirate.getId());
		//em.getTransaction().begin();
		 // em.remove(e);
		  //em.getTransaction().commit();
	}
	
	
}
