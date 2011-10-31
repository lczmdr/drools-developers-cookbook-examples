package drools.cookbook.chapter07;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.drools.command.BatchExecutionCommand;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.command.runtime.rule.FireAllRulesCommand;
import org.drools.command.runtime.rule.InsertObjectCommand;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.help.BatchExecutionHelper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import drools.cookbook.chapter07.model.Server;
import drools.cookbook.chapter07.model.Virtualization;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class CamelSpringIntegrationTest {

    private static ClassPathXmlApplicationContext applicationContext;

    @BeforeClass
    public static void start() {
        applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
        applicationContext.start();
    }

    @Test
    public void camelSpringIntegration() throws InterruptedException {

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

        CamelContext camelContext = (CamelContext) applicationContext.getBean("camelContext");

        Assert.assertNotNull(camelContext);

        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        String outXml = new String((byte[]) producerTemplate.requestBody("direct:test-with-session", xmlCommand));

        ExecutionResults result = (ExecutionResults) BatchExecutionHelper.newXStreamMarshaller().fromXML(outXml);

        Assert.assertEquals(2, result.getValue("executed-rules"));

        applicationContext.stop();
    }

    @AfterClass
    public static void stop() {
        applicationContext.stop();
    }

}
