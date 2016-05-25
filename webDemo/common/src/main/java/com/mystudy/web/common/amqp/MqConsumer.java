package com.mystudy.web.common.amqp;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by 程祥 on 16/5/6.
 * Function：
 */
public class MqConsumer {

    private final static String QUEUE_NAME = "hello";
    private static final String TASK_QUEUE_NAME = "task_queue";
    private final static String EXCHANGE_NAME="logs";

    public static void main(String[] args){
        MqConsumer consumer = new MqConsumer();
        consumer.exchangeReceive();
    }

    private void exchangeReceive(){

        try {
            Connection connection = getConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                }
            };
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void taskReceive(){

        try {
            Connection connection = getConnection();
            final Channel channel = connection.createChannel();
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            channel.basicQos(1);

            final Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");

                    System.out.println(" [x] Received '" + message + "'");
                }
            };
            channel.basicConsume(TASK_QUEUE_NAME, false, consumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }


    private Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("m_test");
        factory.setPassword("nopass.2");
        factory.setVirtualHost("hoset_test");
        factory.setHost("192.168.9.92");
        factory.setPort(5672);
        factory.setConnectionTimeout(10000);
//        factory.sets
        Connection connection = factory.newConnection();
        return connection;
    }


}
