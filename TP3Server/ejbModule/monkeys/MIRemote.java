package monkeys;

import javax.ejb.Remote;

/**
 * @author Mickael Clavreul
 */
@Remote
public interface MIRemote {
	
	public Pirate subscribe(String id);
	public void disconnect(String pId);
	public void move(String id, String deplacement);
}
