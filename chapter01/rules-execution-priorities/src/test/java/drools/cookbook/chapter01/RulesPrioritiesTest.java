package drools.cookbook.chapter01;

import junit.framework.Assert;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 *
 */
public class RulesPrioritiesTest {

    private static KnowledgeBase kbase;


    @Test
    public void virtualizationRequestFailedTest() throws InterruptedException {

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        Server debianServer = new Server("debianServer", 8, 8192, 2048, 0);
        ksession.insert(debianServer);

        Server winServer = new Server("winServer", 4, 4096, 2048, 0);
        ksession.insert(winServer);

        Server ubuntuServer = new Server("ubuntuServer", 4, 2048, 1024, 0);
        ksession.insert(ubuntuServer);

        Virtualization rhel = new Virtualization("rhel", "ubuntuServer", 2048, 160);
        VirtualizationRequest virtualizationRequest = new VirtualizationRequest(rhel);

        ksession.insert(virtualizationRequest);

        ksession.fireAllRules();

        Assert.assertEquals(false, virtualizationRequest.isSuccessful());

    }

    @Test
    public void virtualizationRequestSuccessfulTest() throws InterruptedException {

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        Server debianServer = new Server("debianServer", 8, 8192, 2048, 0);
        ksession.insert(debianServer);

        Server winServer = new Server("winServer", 4, 4096, 2048, 0);
        ksession.insert(winServer);

        Server ubuntuServer = new Server("ubuntuServer", 4, 2048, 1024, 0);
        ksession.insert(ubuntuServer);

        Virtualization rhel = new Virtualization("debian", "debianServer", 2048, 160);
        VirtualizationRequest virtualizationRequest = new VirtualizationRequest(rhel);

        ksession.insert(virtualizationRequest);

        ksession.fireAllRules();

        Assert.assertEquals(true, virtualizationRequest.isSuccessful());

    }

    @BeforeClass
    public static void createKnowledgeBase() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules.drl", RulesPrioritiesTest.class), ResourceType.DRL); 

        if (kbuilder.hasErrors()) {
            if (kbuilder.getErrors().size() > 0) {
                for (KnowledgeBuilderError kerror : kbuilder.getErrors()) {
                    System.err.println(kerror);
                }
            }
        }

        kbase = kbuilder.newKnowledgeBase();
    }

}
