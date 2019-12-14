package monkeys;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Local;

@Local
public interface CommunicationLocal {
	public void sendMap(int[][] map, String id);
	public void sendMonkeys(HashMap<Integer,Monkey> monkeys);
	public void sendPirate(String deplacement, String id);
	public void sendPirates();
	public void sendRum(HashMap<Integer,Rum> bottles);
}