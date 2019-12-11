package monkeys;

import java.util.HashMap;

import javax.ejb.Local;

@Local
public interface CommunicationLocal {
	public void sendMap(int[][] map, String id);
	public void sendMonkeys(HashMap<Integer,Monkey> monkeys);
	public void sendPirate(String deplacement, String id);
}