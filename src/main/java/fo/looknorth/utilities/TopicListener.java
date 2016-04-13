package fo.looknorth.utilities;

import fo.looknorth.looknorthapi.database.DAOFactory;
import fo.looknorth.looknorthapi.database.LooknorthDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author Jakup
 */
public class TopicListener implements MqttCallback {

    private MqttClient mqttClient;
    private MqttConnectOptions connOpt;
    
    private final LooknorthDAO dao;
    private final String BROKER_URL;
    private final String PORT;
    private final String FULL_CONNECTION_STRING;
    private final String LISTENER_ID;
    private final String TOPIC;
    private final int QUALITY_OF_SERVICE;
    private final Organiser o;
 
    //to be used for authentication
    private final String USERNAME;
    private final String PASSWORD;
    
    /*
    This constructor can be used if authentication for the broker is not enabled.
    */
        public TopicListener(String brokerUrl, String port, String listenerId, String topic, int qos) {
            BROKER_URL = brokerUrl;
            PORT = port;
            FULL_CONNECTION_STRING = "tcp://" + BROKER_URL + ":" + PORT;
            LISTENER_ID = listenerId;
            TOPIC = topic;
            QUALITY_OF_SERVICE = qos;
            o = new Organiser();            
            DAOFactory mariaDbFactory =   
            DAOFactory.getDAOFactory(DAOFactory.MARIADB);
            dao = mariaDbFactory.getLooknorthDAO();
            this.USERNAME = "machineListener1";
            this.PASSWORD = "";
    
        }
        
        public TopicListener(String brokerUrl, String port, String listenerId, String topic, int qos, String username, String password) {
            BROKER_URL = brokerUrl;
            PORT = port;
            FULL_CONNECTION_STRING = "tcp://" + BROKER_URL + ":" + PORT;
            LISTENER_ID = listenerId;
            TOPIC = topic;
            QUALITY_OF_SERVICE = qos;
            this.USERNAME = username;
            this.PASSWORD = password;
            o = new Organiser();
            DAOFactory mariaDbFactory =   
            DAOFactory.getDAOFactory(DAOFactory.MARIADB);
            dao = mariaDbFactory.getLooknorthDAO();
        }
        
	/**
	 * 
	 * connectionLost
	 * This callback is invoked upon losing the MQTT connection.
	 * 
        * @param t
	 */
	@Override
	public void connectionLost(Throwable t) {
		System.out.println("Connection lost!");
                System.out.println("....");
                System.out.println("Reconnecting");
                this.listen();
		// code to reconnect to the broker would go here if desired
	}


	/**
	 * The main functionality of the listener.
	 * Create a MQTT client, connect to broker, subscribe to topic.
	 * 
	 */
	public void listen() {
		//LISTENER_ID needed reconnecting purposes.
                //Broker will close remaining connection when LISTENER_ID
                //reconnects, therefore canceling out half open connections.
                
		connOpt = new MqttConnectOptions();
		connOpt.setCleanSession(true);  // true = not a permanent session.
		connOpt.setKeepAliveInterval(60); //seconds

		connOpt.setUserName(USERNAME);
		connOpt.setPassword(PASSWORD.toCharArray());
		
		try {
			mqttClient = new MqttClient(FULL_CONNECTION_STRING, LISTENER_ID);
			mqttClient.setCallback(this);
			mqttClient.connect(connOpt);
                        System.out.println("Connected to " + FULL_CONNECTION_STRING);
                        System.out.println("Topic: " + TOPIC);
                        System.out.println("qos: " + QUALITY_OF_SERVICE);
                        mqttClient.subscribe(TOPIC, QUALITY_OF_SERVICE);
		} catch (MqttException e) {
			e.printStackTrace();
                        System.out.println("Fatal error, shutting down!");
			System.exit(-1);
		}
	}

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
        System.out.println("Message received!");
        
        o.fill(string, mm);       
        dao.saveMessage(o);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
      try {
          System.out.println("Pub complete" + new String(imdt.getMessage().getPayload()));
      } catch (MqttException ex) {
          Logger.getLogger(TopicListener.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
}
