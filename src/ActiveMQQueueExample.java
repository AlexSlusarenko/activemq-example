import javax.jms.JMSException;

public class ActiveMQQueueExample {

    private static final String QUEUE = "MyQueue";

    public void run() throws JMSException {

        String text = "Message to Queue";

        Publisher publisher = new Publisher(QUEUE, null);
        publisher.publish(text);

        publisher.close();

        Subscriber subscriber = new Subscriber(QUEUE);
        subscriber.receive();

        subscriber.close();
    }
}