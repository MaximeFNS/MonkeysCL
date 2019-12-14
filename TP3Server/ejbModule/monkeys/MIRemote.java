package monkeys;

import java.util.ArrayList;

import javax.ejb.Remote;

/**
 * @author Mickael Clavreul
 */
@Remote
public interface MIRemote {
	
	public Pirate subscribe(String id);
	public void disconnect(String pId);
	public void move(String id, String deplacement);
	public ArrayList<Pirate> sendAllPirates(Pirate pirateExclude);
}
