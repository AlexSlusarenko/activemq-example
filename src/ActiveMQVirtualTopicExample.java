import javax.jms.JMSException;

public class ActiveMQVirtualTopicExample {

    private static final String PRODUCER_TOPIC = "VirtualTopic.MyTopic";
    private static final String CONSUMER1_TOPIC = "Consumer.Consumer1." + PRODUCER_TOPIC;
    private static final String CONSUMER2_TOPIC = "Consumer.Consumer2." + PRODUCER_TOPIC;

    public void run() throws JMSException {

        String text = "Message to TOPIC";

        Subscriber subscriber = new Subscriber(CONSUMER1_TOPIC);
        Subscriber subscriber2 = new Subscriber(CONSUMER2_TOPIC);
        Publisher publisher = new Publisher(null, PRODUCER_TOPIC);


        publisher.publish(text);
        subscriber.receive();
        subscriber2.receive();

        publisher.close();
        subscriber.close();
        subscriber2.close();
    }
}