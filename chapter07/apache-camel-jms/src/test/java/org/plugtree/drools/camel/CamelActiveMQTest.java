package org.plugtree.drools.camel;

import java.util.ArrayList;
import java.util.List;

import org.drools.command.BatchExecutionCommand;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.command.runtime.rule.FireAllRulesCommand;
import org.drools.command.runtime.rule.InsertObjectCommand;
import org.drools.runtime.help.BatchExecutionHelper;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import drools.cookbook.chapter07.jms.JMSQueueProducer;
import drools.cookbook.chapter07.model.Server;
import drools.cookbook.chapter07.model.Virtualization;

public class CamelActiveMQTest {

    @Ignore
    @Test
    public void standaloneActiveMQ() throws InterruptedException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/camel-amq.xml");
        applicationContext.start();

        Server debianServer = new Server("debian", 2, 2048, 2048, 0);
        Server winServer = new Server("win", 2, 1024, 250, 0);

        Virtualization virtualization = new Virtualization("dev", "debian", 512, 30);

        InsertObjectCommand insertServerCommand = (InsertObjectCommand) CommandFactory.newInsert(debianServer,
                "debian-server", true, "DEFAULT");

        InsertObjectCommand insertBadServerCommand = (InsertObjectCommand) CommandFactory.newInsert(winServer,
                "win-server", true, "DEFAULT");

        InsertObjectCommand insertVirtualizationCommand = (InsertObjectCommand) CommandFactory.newInsert(
                virtualization, "dev-virtualization", true, "DEFAULT");

        FireAllRulesCommand fireAllRulesCommand = (FireAllRulesCommand) CommandFactory
                .newFireAllRules("executed-rules");

        List<Command> commands = new ArrayList<Command>();
        commands.add(insertServerCommand);
        commands.add(insertBadServerCommand);
        commands.add(insertVirtualizationCommand);
        commands.add(fireAllRulesCommand);
        BatchExecutionCommand batchExecutionCommand = CommandFactory.newBatchExecution(commands, "ksession1");

        String xmlCommand = BatchExecutionHelper.newXStreamMarshaller().toXML(batchExecutionCommand);

        JMSQueueProducer queueProducer = (JMSQueueProducer) applicationContext.getBean("queueProducer");
        queueProducer.send(xmlCommand);
        Thread.sleep(2000);
        applicationContext.stop();
    }

}
