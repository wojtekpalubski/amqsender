import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.UUID;

public class amqsender {
    public static void main(String[] args) throws JMSException {

        ConnectionFactory cf = new com.sun.messaging.ConnectionFactory();
        try (JMSContext jc = cf.createContext();) {
            Queue queueRequest = jc.createQueue("RequestQ");
            Queue queueResponse = jc.createQueue("ResponseQ");
            String uuid = UUID.randomUUID().toString();

            jc
                    .createProducer()
                    .setJMSReplyTo(queueResponse)
                    .setProperty("Uuid", uuid)
                    .send(queueRequest, "wiadomosc 2");
            System.out.println("Wyslano");

            String filtr = "MessageLink = '" + uuid + "'";
            String conf = jc.createConsumer(queueResponse, filtr).receiveBody(String.class);
            System.out.println("Odebrano " + conf);
        }


//        Connection connection = new ActiveMQConnectionFactory("tcp://localhost:61616").createConnection();
//        connection.start();
//        System.out.println("ClientID: " + connection.getClientID());
//        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        Queue queueRequest = session.createQueue("RequestQ");
//        Queue queueResponse = session.createQueue("ResponseQ");
//        TextMessage msg = session.createTextMessage("wiadomosc 1");
//        msg.setJMSReplyTo(queueResponse);
//        MessageProducer sender = session.createProducer(queueRequest);
//        sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//        sender.send(msg);
//        System.out.println("Wyslano: >" + msg.getText() + "< " + msg.getJMSMessageID() + " " + msg.getJMSCorrelationID());
//
//        String filtr = "JMSCorrelationID = '" + msg.getJMSMessageID() + "'";
//        System.out.println("Czekam na odpowiedz " + filtr);
//        MessageConsumer receiver = session.createConsumer(queueResponse, filtr);
//        TextMessage msgResponse = (TextMessage) receiver.receive();
//        System.out.println("Odebrano: >" + msgResponse.getText() + "< " + msgResponse.getJMSMessageID() + " " + msgResponse.getJMSCorrelationID());
//        receiver.close();
//        sender.close();
//        session.close();
//        connection.stop();
    }
}
