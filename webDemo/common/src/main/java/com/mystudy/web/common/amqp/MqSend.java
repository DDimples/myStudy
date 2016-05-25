package com.mystudy.web.common.amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by 程祥 on 16/5/6.
 * Function：
 */
public class MqSend {

    private final static String QUEUE_NAME = "hello";
    private static final String TASK_QUEUE_NAME = "task_queue";
    private final static String EXCHANGE_NAME="logs";

    public static void main(String[] args){
        MqSend send = new MqSend();
        send.testExchangeSend();
    }

    private void testExchangeSend(){
        try {
            Connection connection = getConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            String message = "getMessage(argv) aa exchange";

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
//            System.out.println(" [x] Sent '" + message + "'");

            channel.close();
            connection.close();
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



            for (int i=0;i<100;i++){
                String message = "getMessage(argv) hello task ~"+i;
                channel.basicPublish( "", TASK_QUEUE_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes());
            }
//            System.out.println(" [x] Sent '" + message + "'");

            channel.close();
            connection.close();
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
