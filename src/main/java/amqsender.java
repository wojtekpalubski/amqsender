import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class amqsender {
    public static void main(String[] args) throws JMSException {
        Connection connection =new ActiveMQConnectionFactory("tcp://localhost:61616").createConnection();
        connection.start();
        System.out.println("ClientID: "+connection.getClientID());
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queueRequest=session.createQueue("RequestQ");
        TextMessage msg= session.createTextMessage("wiadomosc 1");
        MessageProducer sender= session.createProducer(queueRequest);
        sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        sender.send(msg);
        System.out.println("Wyslano: >"+msg.getText()+"< "+msg.getJMSMessageID()+" "+msg.getJMSCorrelationID());

        connection.stop();
    }
}
