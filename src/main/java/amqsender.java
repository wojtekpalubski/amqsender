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
        Queue queueResponse=session.createQueue("ResponseQ");
        TextMessage msg= session.createTextMessage("wiadomosc 1");
        msg.setJMSReplyTo(queueResponse);
        MessageProducer sender= session.createProducer(queueRequest);
        sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        sender.send(msg);
        System.out.println("Wyslano: >"+msg.getText()+"< "+msg.getJMSMessageID()+" "+msg.getJMSCorrelationID());

        String filtr = "JMSCorrelationID = '"+msg.getJMSMessageID()+"'";
        System.out.println("Czekam na odpowiedz "+filtr);
        MessageConsumer receiver= session.createConsumer(queueResponse, filtr);
        TextMessage msgResponse=(TextMessage)receiver.receive();
        System.out.println("Odebrano: >"+msgResponse.getText()+"< "+msgResponse.getJMSMessageID()+" "+msgResponse.getJMSCorrelationID());
        receiver.close();
        sender.close();
        session.close();
        connection.stop();
    }
}
