package monkeys.communication;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Local;

import monkeys.models.Monkey;
import monkeys.models.Rum;
import monkeys.models.Treasure;

@Local
public interface CommunicationLocal {
	public void sendMap(int[][] map, String id);
	public void sendMonkeys(ArrayList<Monkey> monkeys);
	public void sendTreasure(Treasure treasure);
	public void sendPirate(String deplacement, String id, String state, int energy);
	public void sendPirates();
	public void sendRum(ArrayList<Rum> bottles);
	public void removePirate(int id);
}