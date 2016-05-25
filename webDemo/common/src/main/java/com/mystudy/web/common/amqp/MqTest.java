package com.mystudy.web.common.amqp;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by 程祥 on 16/5/5.
 * Function：
 */
public class MqTest {
    private final static String QUEUE_NAME = "hello";
    private static final String TASK_QUEUE_NAME = "task_queue";
    private final static String EXCHANGE_NAME="logs";
    public static void main(String[] args){
        MqTest test = new MqTest();
//        test.testSend();
//        test.testReceive();
//        test.taskSend();
        test.exchangeReceive();
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


    private void taskSend(){
        try {
            Connection connection = getConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            String message = "getMessage(argv) hello task ";

            channel.basicPublish( "", TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");

            channel.close();
            connection.close();
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
                    try {
                        doWork(message);
                    } finally {
                        System.out.println(" [x] Done");
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };
            channel.basicConsume(TASK_QUEUE_NAME, false, consumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }



    public void testSend(){

        Connection conn =null;
        Channel channel = null;
        try {
            conn = getConnection();
            channel = conn.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//            channel.exchangeDeclare(EXCHANGE_NAME, "direct",true);//fanout  direct
//            String queueName = channel.queueDeclare().getQueue();
//            channel.queueBind(queueName, EXCHANGE_NAME, "routingKey");
            byte[] messageBodyBytes = ("Hello, world!"+System.currentTimeMillis()).getBytes();
            channel.basicPublish("", QUEUE_NAME, null, messageBodyBytes);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            try {
                if(channel!=null){
                    channel.close();
                }
                if(conn!=null){
                    conn.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    public void testReceive() {
        Connection conn =null;
        Channel channel = null;
        try {
            conn = getConnection();
            channel = conn.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicQos(1);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                }
            };
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {

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

    private void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
