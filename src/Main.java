import javax.jms.JMSException;

public class Main {

    public static void main(String[] args) throws JMSException {
        ActiveMQQueueExample queueExample = new ActiveMQQueueExample();
        ActiveMQVirtualTopicExample virtualTopicExample = new ActiveMQVirtualTopicExample();
        ActiveMQRoutingExample routingExample = new ActiveMQRoutingExample();

        queueExample.run();
        virtualTopicExample.run();
        routingExample.run();
    }
}
