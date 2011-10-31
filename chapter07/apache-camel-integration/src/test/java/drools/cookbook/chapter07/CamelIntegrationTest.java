package drools.cookbook.chapter07;

import static org.apache.camel.builder.PredicateBuilder.or;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.BatchExecutionCommand;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.command.runtime.rule.FireAllRulesCommand;
import org.drools.command.runtime.rule.InsertObjectCommand;
import org.drools.grid.GridNode;
import org.drools.grid.impl.GridImpl;
import org.drools.grid.service.directory.WhitePages;
import org.drools.grid.service.directory.impl.WhitePagesImpl;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Assert;
import org.junit.Test;

import drools.cookbook.chapter07.model.Server;
import drools.cookbook.chapter07.model.Virtualization;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class CamelIntegrationTest {

    @Test
    public void programaticallyIntegration() throws Exception {

        CamelContext camelContext = createCamelContext();
        camelContext.start();

        Server debianServer = new Server("debian", 2, 2048, 2048, 0);
        Server winServer = new Server("win", 2, 1024, 250, 0);

        Virtualization virtualization = new Virtualization("dev", "debian", 512, 30);

        InsertObjectCommand insertServerCommand = (InsertObjectCommand) CommandFactory.newInsert(debianServer,
                "debian-server");

        InsertObjectCommand insertBadServerCommand = (InsertObjectCommand) CommandFactory.newInsert(winServer,
                "win-server");

        InsertObjectCommand insertVirtualizationCommand = (InsertObjectCommand) CommandFactory.newInsert(
                virtualization, "dev-virtualization");

        FireAllRulesCommand fireAllRulesCommand = (FireAllRulesCommand) CommandFactory
                .newFireAllRules("executed-rules");

        List<Command> commands = new ArrayList<Command>();
        commands.add(insertServerCommand);
        commands.add(insertBadServerCommand);
        commands.add(insertVirtualizationCommand);
        commands.add(fireAllRulesCommand);
        BatchExecutionCommand batchExecutionCommand = CommandFactory.newBatchExecution(commands, "ksession1");

        ProducerTemplate template = camelContext.createProducerTemplate();
        ExecutionResults response = (ExecutionResults) template.requestBodyAndHeader("direct:test-with-session",
                batchExecutionCommand, "priority", "low");
        Assert.assertNotNull(response);

        Assert.assertNotNull(response.getFactHandle("debian-server"));

        Assert.assertEquals(2, response.getValue("executed-rules"));
        camelContext.stop();
    }

    private CamelContext createCamelContext() throws Exception, NamingException {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules.drl", getClass()), ResourceType.DRL);

        if (kbuilder.hasErrors()) {
            if (kbuilder.getErrors().size() > 0) {
                for (KnowledgeBuilderError kerror : kbuilder.getErrors()) {
                    System.err.println(kerror);
                }
            }
        }
        KnowledgeBase kbase = kbuilder.newKnowledgeBase();
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        GridImpl grid = new GridImpl();
        grid.addService(WhitePages.class, new WhitePagesImpl());
        GridNode node = grid.createGridNode("node");
        Context context = new JndiContext();
        context.bind("node", node);
        node.set("ksession1", ksession);
        CamelContext camelContext = new DefaultCamelContext(context);
        RouteBuilder rb = new RouteBuilder() {
            public void configure() throws Exception {
                from("direct:test-with-session").filter(
                        or(header("priority").isEqualTo("low"), header("priority").isEqualTo("medium"))).to(
                        "drools://node/ksession1");
            }
        };
        camelContext.addRoutes(rb);
        return camelContext;
    }

}
