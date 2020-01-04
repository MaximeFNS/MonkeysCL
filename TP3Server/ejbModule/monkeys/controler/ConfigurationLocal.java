package monkeys.controler;

import javax.ejb.Local;

@Local
public interface ConfigurationLocal {
	
	public int[][] getMap();
	public int getNbHunterMonkeys();
	public int getNbErraticMonkeys();
	public int getHunterSpeed();
	public int getErraticSpeed();
	public int getRhumEnergy();
	public int getRhumTime();

}
