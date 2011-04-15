package org.plugtree.drools.camel;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import drools.cookbook.chapter07.jms.JMSQueueProducer;

public class CamelActiveMQTest {

    @Test
    public void test() throws InterruptedException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/camel-amq.xml");
        applicationContext.start();

        String xmlCommand = "<batch-execution lookup=\"ksession1\">\n";
        xmlCommand += "  <insert out-identifier=\"debian-server\" return-object=\"true\" entry-point=\"DEFAULT\">\n";
        xmlCommand += "    <drools.cookbook.chapter07.model.Server>\n";
        xmlCommand += "      <name>debian</name>\n";
        xmlCommand += "      <processors>2</processors>\n";
        xmlCommand += "      <memory>2048</memory>\n";
        xmlCommand += "      <diskSpace>2048</diskSpace>\n";
        xmlCommand += "      <virtualizations/>\n";
        xmlCommand += "      <cpuUsage>0</cpuUsage>\n";
        xmlCommand += "      <online>false</online>\n";
        xmlCommand += "    </drools.cookbook.chapter07.model.Server>\n";
        xmlCommand += "  </insert>\n";
        xmlCommand += "  <insert out-identifier=\"win-server\" return-object=\"true\" entry-point=\"DEFAULT\">\n";
        xmlCommand += "    <drools.cookbook.chapter07.model.Server>\n";
        xmlCommand += "      <name>win</name>\n";
        xmlCommand += "      <processors>2</processors>\n";
        xmlCommand += "      <memory>1024</memory>\n";
        xmlCommand += "      <diskSpace>250</diskSpace>\n";
        xmlCommand += "      <virtualizations/>\n";
        xmlCommand += "      <cpuUsage>0</cpuUsage>\n";
        xmlCommand += "      <online>false</online>\n";
        xmlCommand += "    </drools.cookbook.chapter07.model.Server>\n";
        xmlCommand += "  </insert>\n";
        xmlCommand += "  <insert out-identifier=\"dev-virtualization\" return-object=\"true\" entry-point=\"DEFAULT\">\n";
        xmlCommand += "    <drools.cookbook.chapter07.model.Virtualization>\n";
        xmlCommand += "      <name>dev</name>\n";
        xmlCommand += "      <serverName>debian</serverName>\n";
        xmlCommand += "      <memory>512</memory>\n";
        xmlCommand += "      <diskSpace>30</diskSpace>\n";
        xmlCommand += "    </drools.cookbook.chapter07.model.Virtualization>\n";
        xmlCommand += "  </insert>\n";
        xmlCommand += "  <fire-all-rules out-identifier=\"executed-rules\"/>\n";
        xmlCommand += "</batch-execution>\n";

        JMSQueueProducer queueProducer = (JMSQueueProducer) applicationContext.getBean("queueProducer");
        queueProducer.send(xmlCommand);
        Thread.sleep(2000);
        applicationContext.stop();
    }

}
