import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.*;

public class Publisher {
    private static final int DELIVERY_MODE = DeliveryMode.NON_PERSISTENT;
    private static final int ACKNOWLEDGE_MODE = Session.AUTO_ACKNOWLEDGE;

    // The Endpoint, Username, Password, and Queue should be externalized and
    // configured through environment variables or dependency injection.
    //private static final String ENDPOINT; // OpenWire protocol "ssl://x-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx-x.mq.us-east-1.amazonaws.com:61617"
    private static final String ENDPOINT = "tcp://localhost:61616";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    final Connection connection;
    final PooledConnectionFactory pooledConnectionFactory;
    final Session producerSession;
    final MessageProducer producer;


    public Publisher(String queue, String topic) throws JMSException {
        // Create a connection factory.
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ENDPOINT);

        // Specify the username and password.
        connectionFactory.setUserName(USERNAME);
        connectionFactory.setPassword(PASSWORD);

        // Create a pooled connection factory.
        pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(10);

        // Establish a connection for the producer.
        connection = pooledConnectionFactory.createConnection();
        connection.start();

        // Create a session.
        producerSession = connection.createSession(false, ACKNOWLEDGE_MODE);

        // create Topic or Queue
        Destination producerDestination = queue != null ? producerSession.createQueue(queue) : producerSession.createTopic(topic);

        // Create a producer from the session to the queue.
        producer = producerSession.createProducer(producerDestination);
        producer.setDeliveryMode(DELIVERY_MODE);
    }

    public void publish(String message) throws JMSException {
        // Create a message.
        TextMessage producerMessage = producerSession.createTextMessage(message);

        // Send the message.
        producer.send(producerMessage);
        System.out.println("Message sent: " + message);
    }

    public void close() throws JMSException {
        // Clean up the producer.
        producer.close();
        producerSession.close();
        connection.close();
        pooledConnectionFactory.stop();
    }

}
