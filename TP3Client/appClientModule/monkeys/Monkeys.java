package monkeys;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.JFrame;

import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

import guybrush.view.Fenetre;

/**
 * @author Mickael Clavreul
 */
public class Monkeys implements MessageListener{
	
	static Monkeys monkeys;
	static Keychecker kc = new Keychecker();
	private Properties props;
	
	private static Fenetre fenetre;
	private static Pirate pirate;
	static MIRemote rw;
	public static void main(String[] args) throws Exception {
		
		fenetre = new Fenetre("MonkeysIsland");
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.pack();
		fenetre.setVisible(true);
		
		monkeys = new Monkeys();
		rw = lookup();
		
		monkeys.subscribeTopic(monkeys);
		pirate = rw.subscribe("1");
		boolean ok = true;
		while (ok) {
			
			switch(kc.code) {
			  case 37:
				  pirate.setPosX(pirate.getPosX()-1);
				  rw.move(String.valueOf(pirate.getId()), "-1-0");
				  kc.code = 0;
			    break;
			  case 38:
				  pirate.setPosY(pirate.getPosY()-1);
				  rw.move(String.valueOf(pirate.getId()), "0-1");
				  kc.code = 0;
			    break;
			  case 39:
				  pirate.setPosX(pirate.getPosX()+1);
				  rw.move(String.valueOf(pirate.getId()), "1-0");
				  kc.code = 0;
				    break;
			  case 40:
				  pirate.setPosY(pirate.getPosY()+1);
				  rw.move(String.valueOf(pirate.getId()), "0--1");
				  kc.code = 0;
				    break;
			  default:
			}
			
		}
		
	}

	public Monkeys() {
		super();
		fenetre.addKeyListener(kc);
	}
	
	private static MIRemote lookup() throws Exception {
		Properties props = new Properties();
		props.put("java.remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		props.put("remote.connections","default");
		props.put("remote.connection.default.host","localhost");
		props.put("remote.connection.default.port","8080");
		props.put("remote.connection.default.username","afonsoma");
		props.put("remote.connection.default.password","network");
		
		EJBClientConfiguration clientConfiguration = new PropertiesBasedEJBClientConfiguration(props);
		ConfigBasedEJBClientContextSelector ejbClientContext = new ConfigBasedEJBClientContextSelector(clientConfiguration);
		EJBClientContext.setSelector(ejbClientContext);
		Context context = new InitialContext(props);

		MIRemote remoteWelcome = (MIRemote) context.lookup("ejb:/TP3Server/MonkeyIsland!monkeys.MIRemote?stateful");

		return remoteWelcome;
	}
	
	public void subscribeTopic(Monkeys monkeys) {
		props = new Properties();
		InputStream is = monkeys.getClass().getClassLoader().getResourceAsStream("META-INF/jndi-topic-client.properties");
		
		try {
			props.load(is);
			InitialContext context = new InitialContext(props);
			String tcfuri = props.getProperty("connectionFactoryURI");
			TopicConnectionFactory tcf = (TopicConnectionFactory) context.lookup(tcfuri);
			
			String topicUri = props.getProperty("topicURI");
			Topic topic = (Topic) context.lookup(topicUri);
			
			TopicConnection topicConnection = tcf.createTopicConnection("afonsoma", "network");
			String idString = "" + monkeys.hashCode();
			topicConnection.setClientID(String.valueOf(idString));
			TopicSession topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			
			MessageConsumer mc = topicSession.createDurableConsumer(topic, "");
			mc.setMessageListener(monkeys);
			topicConnection.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {

			e.printStackTrace();
		}
		
	}
	
	private void notifyDisconnect() {
		//TODO:Implementer quitter partie
	}

	@Override
	public void onMessage(Message message) {
		
		try {

			if(message.getJMSType().contains("map")) {
				
				int[][] map = new int[10][10];

				StreamMessage streamMessage = (StreamMessage) message;
				int taille = streamMessage.readInt();
				for(int i = 0; i<taille; i++) {
					for(int j=0; j<taille; j++) {
						map[i][j] = streamMessage.readInt();
					}
				}
				
				fenetre.creationCarte(map);
				fenetre.suppressionPirate(pirate.getId());
				
				fenetre.ajoutPirate(pirate.getId(), pirate.getPosX(), pirate.getPosY(), "img/Mon_Pirate.png", pirate.getEnergy());
				fenetre.repaint();
				
				
			} else if (message.getJMSType().contains("monkeys")) {
				
				fenetre.removeEMonkeys();
				StreamMessage streamMessage = (StreamMessage) message;
				int taille = streamMessage.readInt();
				for(int i = 0; i<taille; i++) {
					int id = streamMessage.readInt();
					int posX = streamMessage.readInt();
					int posY = streamMessage.readInt();
					fenetre.creationEMonkey(id, posX, posY);
				}
				
				
				
				
			}	else if(message.getJMSType().contains("move")) {
				int id = Integer.valueOf(message.getStringProperty("id"));
				if(id==pirate.getId()) {
					System.out.println("State : " + pirate.getState());
					fenetre.suppressionPirate(Integer.valueOf(message.getStringProperty("id")));
					if(!pirate.getState().contentEquals("DEAD")) {
						fenetre.ajoutPirate(pirate.getId(), pirate.getPosX(), pirate.getPosY(), "img/Mon_Pirate.png", pirate.getEnergy());
					} else {
						fenetre.ajoutPirate(pirate.getId(), pirate.getPosX(), pirate.getPosY(), "img/Pirate_Mort.png", pirate.getEnergy());
					}
					
					//fenetre.repaint();
				}

			}	else if(message.getJMSType().contains("allPirates")) {
				ArrayList<Pirate> pirates= new ArrayList<>();
				pirates= rw.sendAllPirates(pirate);
				for(int k =0; k<pirates.size();k++) {
					fenetre.suppressionPirate(pirates.get(k).getId());
					fenetre.ajoutPirate(pirates.get(k).getId(), pirates.get(k).getPosX(), pirates.get(k).getPosY(), "img/Autres_Pirates.jpg", 100);
				}

			} else if (message.getJMSType().contains("rum")) {
				
				StreamMessage streamMessage = (StreamMessage) message;
				int posX = streamMessage.readInt();
				int posY = streamMessage.readInt();
				int intv = streamMessage.readInt();
				boolean v = false;
				if(intv==1) {
					v = true;
				}
				
				fenetre.creationRhum(posX, posY, v);
			}
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		fenetre.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				
				notifyDisconnect();
			}

			
		});
	}

}
class Keychecker extends KeyAdapter {

	int code;
	
    @Override
    public void keyPressed(KeyEvent event) {

    char ch = event.getKeyChar();
    int s = event.getKeyCode();
    
    code = s;

    }
}