/*
This class make a more organized 
message from a retrived MQTT message.
This is done so that the messages can be stored
in a relation database, where topics and publishers
are also organised.
*/

package fo.looknorth.utilities;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author Jakup
 */
public class Organiser {
    
    private String COMPANY;
    private String LOCATION;
    private String SUBJECT;
    private String SENSOR;
    private String MESSAGE;
    
    public Organiser() {}
    
    public Organiser(String topic, MqttMessage message) {
        String[] parts = topic.split("/");

        COMPANY = parts[0];
        LOCATION = parts[1];
        SUBJECT = parts[2];
        SENSOR = parts[3];
        MESSAGE = new String(message.getPayload());
    }
    
    public void fill(String topic, MqttMessage message) {
        String[] parts = topic.split("/");
        
        setCOMPANY(parts[0]);
        setLOCATION(parts[1]);
        setSUBJECT(parts[2]);
        setSENSOR(parts[3]);
        setMESSAGE(new String(message.getPayload()));
    }

    public String getCOMPANY() {
        return COMPANY;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public String getSUBJECT() {
        return SUBJECT;
    }

    public String getSENSOR() {
        return SENSOR;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setCOMPANY(String COMPANY) {
        this.COMPANY = COMPANY;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public void setSUBJECT(String SUBJECT) {
        this.SUBJECT = SUBJECT;
    }

    public void setSENSOR(String SENSOR) {
        this.SENSOR = SENSOR;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }
    
    @Override
    public String toString() {
        return "Company: " + COMPANY + "\n" + "Location: " + LOCATION + "\n" + "SUBJECT: " + SUBJECT + "\n" + "SENSOR: " + SENSOR + "\n" + "MESSAGE: " + MESSAGE + "\n";
    }
    
    public static void main(String[] args) {
        String payload = "Hello";
        MqttMessage m = new MqttMessage();
        m.setPayload(payload.getBytes());        
        String topic = "looknorth/production/machines/1";        
        Organiser organiser = new Organiser(topic, m);        
        System.out.println(organiser.toString());
    }
    
}
