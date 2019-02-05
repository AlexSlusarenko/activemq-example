import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.*;

public class Subscriber {

    private static final int DELIVERY_MODE = DeliveryMode.NON_PERSISTENT;
    private static final int ACKNOWLEDGE_MODE = Session.AUTO_ACKNOWLEDGE;

    // The Endpoint, Username, Password, and Queue should be externalized and
    // configured through environment variables or dependency injection.
    //private static final String ENDPOINT; // OpenWire protocol "ssl://x-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx-x.mq.us-east-1.amazonaws.com:61617"
    private static final String ENDPOINT = "tcp://localhost:61616";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    final Connection connection;
    final Session session;
    final MessageConsumer consumer;
    final String subName;


    public Subscriber(String queue) throws JMSException {
        subName = queue;
        // Create a connection factory.
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ENDPOINT);

        // Specify the username and password.
        connectionFactory.setUserName(USERNAME);
        connectionFactory.setPassword(PASSWORD);

        // Establish a connection for the consumer.
        // Note: Consumers should not use PooledConnectionFactory.
        connection = connectionFactory.createConnection();
        connection.start();

        // Create a session.
        session = connection.createSession(false, ACKNOWLEDGE_MODE);

        // Create a queue
        Destination consumerDestination = session.createQueue(queue);

        // Create a message consumer from the session to the queue.
        // MessageListener:onMessage(Message message); can be set to consumer
        consumer = session.createConsumer(consumerDestination);
    }

    public void receive() throws JMSException {
        // Begin to wait for messages.
        Message consumerMessage = consumer.receive(1000);

        if (consumerMessage == null) {
            System.err.println(subName + " NO MESSAGE");
            return;
        }

        // Receive the message when it arrives.
        TextMessage consumerTextMessage = (TextMessage) consumerMessage;
        System.out.println(subName + " received: " + consumerTextMessage.getText());
    }

    public void close() throws JMSException {
        consumer.close();
        session.close();
        connection.close();
    }
}
