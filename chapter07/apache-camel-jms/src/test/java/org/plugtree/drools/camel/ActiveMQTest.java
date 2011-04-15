package org.plugtree.drools.camel;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import drools.cookbook.chapter07.jms.JMSQueueProducer;

public class ActiveMQTest {

    @Test
    public void test() throws InterruptedException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/amq.xml");
        applicationContext.start();

        JMSQueueProducer queueProducer = (JMSQueueProducer) applicationContext.getBean("queueProducer");
        queueProducer.send("hello moto");
        Thread.sleep(2000);
        applicationContext.stop();
    }

}
