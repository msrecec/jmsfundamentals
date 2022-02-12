package com.bharath.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class RequestReplyDemo {

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();
        Queue queue = (Queue)context.lookup("queue/requestQueue");
//        Queue replyQueue = (Queue)context.lookup("queue/replyQueue");

        try(
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext()
        ) {
            JMSProducer producer = jmsContext.createProducer();
            TemporaryQueue replyQueue = jmsContext.createTemporaryQueue();
            TextMessage message = jmsContext.createTextMessage("Arise Awake and stop not till the goal is reached");
            message.setJMSReplyTo(replyQueue);
            producer.send(queue, message);

            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage messageReceived = (TextMessage)consumer.receive();
            System.out.println(messageReceived.getText());

            JMSProducer replyProducer = jmsContext.createProducer();
            replyProducer.send(messageReceived.getJMSReplyTo(), "You are awesome");

            JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
            System.out.println(replyConsumer.receiveBody(String.class));
        }
    }
}
