package drools.cookbook.chapter06;

import junit.framework.Assert;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.runtime.BatchExecutionCommandImpl;
import org.drools.command.runtime.rule.FireAllRulesCommand;
import org.drools.command.runtime.rule.InsertObjectCommand;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.help.BatchExecutionHelper;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class RestExecutionTest {

    @SuppressWarnings("restriction")
    @Test
    public void xstreamCommandsTest() throws InterruptedException {

        StatefulKnowledgeSession ksession = createKnowledgeSession();
        Assert.assertNotNull(ksession);

        Server server1 = new Server("windows-nt", 1, 2048, 2048, 3);

        ksession.insert(server1);
        ksession.fireAllRules();

        BatchExecutionCommandImpl batchExecutionCommand = new BatchExecutionCommandImpl();
        batchExecutionCommand.setLookup("ksession1");
        InsertObjectCommand insertObjectCommand = new InsertObjectCommand(server1);
        FireAllRulesCommand fireAllRulesCommand = new FireAllRulesCommand();
        batchExecutionCommand.getCommands().add(insertObjectCommand);
        batchExecutionCommand.getCommands().add(fireAllRulesCommand);
        
        String xml = BatchExecutionHelper.newXStreamMarshaller().toXML(batchExecutionCommand);
        System.out.println(xml);
    }

    private StatefulKnowledgeSession createKnowledgeSession() {
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
        return kbase.newStatefulKnowledgeSession();
    }

}
