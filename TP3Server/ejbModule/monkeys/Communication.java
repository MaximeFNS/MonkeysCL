package monkeys;

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
	public void sendMonkeys(HashMap<Integer,Monkey> monkeys) {
		monkeys.forEach((k,v) -> {
			StreamMessage message = context.createStreamMessage();
			try {
				message.setStringProperty("id", String.valueOf(v.getId()));
				message.setJMSType("object");
				message.writeInt(v.getPosX());
				message.writeInt(v.getPosY());
				message.writeInt(v.getSpeed());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			context.createProducer().send(topic, message);
		});
		
		
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
	public void sendPirate(String deplacement, String id) {
		sendStringMessage(deplacement, id, "move");
		
	}
	
	private void sendStringMessage(String chaine, String id, String type){
    	StreamMessage message = context.createStreamMessage();
    	try {
    		message.setStringProperty("id", id);
    		message.setJMSType(type);
    		message.writeString(chaine);
		} catch (JMSException e) {
			e.printStackTrace();
		}
    	context.createProducer().send(topic, message);
    }

	@Override
	public void sendRum(HashMap<Integer, Rum> bottles) {
		bottles.forEach((k,v) -> {
			StreamMessage message = context.createStreamMessage();
			try {
				message.setStringProperty("id", String.valueOf(v.getId()));
				message.setJMSType("rum");
				message.writeInt(v.getPosX());
				message.writeInt(v.getPosY());
				message.writeInt(v.getVisibility());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			context.createProducer().send(topic, message);
		});
		
	}
}