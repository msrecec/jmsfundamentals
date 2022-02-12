package com.bharath.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class MessagePropertiesDemo {

    public static void main(String[] args) throws Exception {
        InitialContext context = new InitialContext();
        Queue queue = (Queue)context.lookup("queue/myQueue");

        try(
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext()
        ) {
            JMSProducer producer = jmsContext.createProducer();
            TextMessage textMessage = jmsContext.createTextMessage("Arise Awake and stop not till the goal is reached");
            textMessage.setBooleanProperty("loggedIn", true);
            textMessage.setStringProperty("userToken", "abc123");
            producer.send(queue, textMessage);

            Message messageReceived = jmsContext.createConsumer(queue).receive(5000);
            System.out.println(messageReceived);
            System.out.println(messageReceived.getBooleanProperty("loggedIn"));
            System.out.println(messageReceived.getStringProperty("userToken"));
        }
    }
}
