package monkeys;

import javax.ejb.Local;

@Local
public interface CommunicationLocal {
	public void sendMap(int[][] map, String id);
}