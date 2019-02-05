import javax.jms.JMSException;

public class ActiveMQRoutingExample {

    private static final String PRODUCER_TOPIC = "VirtualTopic.Orders.*";
    private static final String CONSUMER_ALL_TOPIC = "Consumer.Consumer1." + PRODUCER_TOPIC;
    private static final String CONSUMER_USA_TOPIC = "Consumer.Consumer2.VirtualTopic.Orders.USA";
    private static final String CONSUMER_UK_TOPIC = "Consumer.Consumer2.VirtualTopic.Orders.UK";
    private static final String CONSUMER_CUSTOMER_UK_TOPIC = "Consumer.Consumer2.VirtualTopic.Customer.UK";
    private static final String CONSUMER_ALL_UK_TOPIC = "Consumer.Consumer2.VirtualTopic.*.UK";

    public void run() throws JMSException {

        String text = "Message to TOPIC With Routing";

        Subscriber subscriber = new Subscriber(CONSUMER_ALL_TOPIC);
        Subscriber subscriberUK = new Subscriber(CONSUMER_UK_TOPIC);
        Subscriber subscriberUSA = new Subscriber(CONSUMER_USA_TOPIC);
        Subscriber subscriber_CUSTOMER_UK = new Subscriber(CONSUMER_CUSTOMER_UK_TOPIC);
        Subscriber subscriber_ALL_UK = new Subscriber(CONSUMER_ALL_UK_TOPIC);

        Publisher publisher = new Publisher(null, PRODUCER_TOPIC);
        Publisher publisherUK = new Publisher(null, "VirtualTopic.Orders.UK");
        Publisher publisherUSA = new Publisher(null, "VirtualTopic.Orders.USA");
        Publisher publisherALL = new Publisher(null, "VirtualTopic.Orders.>");
        Publisher publisherALL_UK = new Publisher(null, "VirtualTopic.*.UK");

        publisher.publish(text);
        subscriber.receive();
        subscriberUK.receive();
        subscriberUSA.receive();
;
        publisherUK.publish("Message to TOPIC With Routing only for UK");
        subscriber.receive();
        subscriberUK.receive();
        subscriberUSA.receive();

        publisherUSA.publish("Message to TOPIC With Routing only for USA");
        subscriber.receive();
        subscriberUK.receive();
        subscriberUSA.receive();

        publisherALL.publish("Message to TOPIC With Routing ALL '>'");
        subscriber.receive();
        subscriberUK.receive();
        subscriberUSA.receive();

        publisherALL_UK.publish("Message to TOPIC FOR UK ONLY");
        subscriber.receive();
        subscriberUK.receive();
        subscriberUSA.receive();
        subscriber_ALL_UK.receive();
        subscriber_CUSTOMER_UK.receive();

        publisher.close();
        publisherUK.close();
        publisherUSA.close();
        publisherALL.close();
        publisherALL_UK.close();

        subscriber.close();
        subscriberUK.close();
        subscriberUSA.close();
        subscriber_ALL_UK.close();
        subscriber_CUSTOMER_UK.close();
    }
}
