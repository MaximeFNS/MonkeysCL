package monkeys.controler;

import java.util.ArrayList;

import javax.ejb.Remote;

import monkeys.models.Pirate;

/**
 * @author Mickael Clavreul
 */
@Remote
public interface MIRemote {
	
	public Pirate subscribe(String id);
	public void disconnect(String pId);
	public void move(String id, String deplacement);
	public ArrayList<Pirate> sendAllPirates(Pirate pirateExclude);
	public int getEnergyIfRum(Pirate pirate);
	public void informLeaving(Pirate pirate);
}
