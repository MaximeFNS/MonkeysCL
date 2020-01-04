package monkeys.communication;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.StreamMessage;
import javax.jms.Topic;

import monkeys.models.Monkey;
import monkeys.models.Rum;
import monkeys.models.Treasure;

/**
 * Session Bean implementation class Communication
 */
@Stateless
@LocalBean
public class Communication implements CommunicationLocal {

	@Inject
	private JMSContext context;
	
	@Resource(mappedName = "java:jboss/exported/topic/monkeys")
    private Topic topic;
	
    /**
     * Default constructor. 
     */
    public Communication() {
    }

	@Override
	public void sendMap(int[][] map, String id) {
		sendIntArrayMessage(map, id, "map");
	}
	
	private void sendIntArrayMessage(int[][] array, String id, String type){
    	StreamMessage message = context.createStreamMessage();
    	try {
    		message.setStringProperty("id", id);
    		message.setJMSType(type);
    		message.writeInt(array.length);
    		for(int i=0;i<array.length;i++){
    			for(int j=0;j<array[i].length;j++){
            		message.writeInt(array[i][j]);
    			}
    		}
		} catch (JMSException e) {
			e.printStackTrace();
		}
    	context.createProducer().send(topic, message);
    }

	@Override
	public void sendMonkeys(ArrayList<Monkey> monkeys) {
		StreamMessage message = context.createStreamMessage();
		try {
			message.setJMSType("monkeys");
			message.writeInt(monkeys.size());
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0; i < monkeys.size(); i++) {
			
			try {
				message.writeInt(monkeys.get(i).getId());
				message.writeInt(monkeys.get(i).getPosX());
				message.writeInt(monkeys.get(i).getPosY());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		context.createProducer().send(topic, message);
	}
	
	@Override
	public void sendPirates() {
		StreamMessage message = context.createStreamMessage();
		try {
    		message.setJMSType("allPirates");
		} catch (JMSException e) {
			e.printStackTrace();
		}
    	context.createProducer().send(topic, message);
		
	}

	@Override
	public void sendPirate(String deplacement, String id, String state, int energy) {
		sendStringMessage(deplacement, id, "move", state, energy);
		
	}
	
	private void sendStringMessage(String chaine, String id, String type, String state, int energy){
    	StreamMessage message = context.createStreamMessage();
    	try {
    		message.setStringProperty("id", id);
    		message.setJMSType(type);
    		message.writeString(chaine);
    		message.writeString(state);
    		message.writeInt(energy);
		} catch (JMSException e) {
			e.printStackTrace();
		}
    	context.createProducer().send(topic, message);
    }

	@Override
	public void sendRum(ArrayList<Rum> bottles) {
		StreamMessage message = context.createStreamMessage();
		try {
			message.setJMSType("rum");
			message.writeInt(bottles.size());
			for(int i=0; i < bottles.size(); i++) {
				message.writeInt(bottles.get(i).getId());
				message.writeInt(bottles.get(i).getPosX());
				message.writeInt(bottles.get(i).getPosY());
				message.writeInt(bottles.get(i).getVisibility());	
			}
			context.createProducer().send(topic, message);
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	@Override
	public void removePirate(int id) {
		StreamMessage message = context.createStreamMessage();
		try {
			message.setStringProperty("id", String.valueOf(id));
    		message.setJMSType("pirateLeft");
		} catch (JMSException e) {
			e.printStackTrace();
		}
    	context.createProducer().send(topic, message);
	}

	@Override
	public void sendTreasure(Treasure treasure) {
		StreamMessage message = context.createStreamMessage();
		try {
			message.setJMSType("treasure");
				message.writeInt(treasure.getPosX());
				message.writeInt(treasure.getPosY());
				message.writeInt(treasure.getVisibility());	
			context.createProducer().send(topic, message);
		} catch (JMSException e1) {

			e1.printStackTrace();
		}
		
	}
}