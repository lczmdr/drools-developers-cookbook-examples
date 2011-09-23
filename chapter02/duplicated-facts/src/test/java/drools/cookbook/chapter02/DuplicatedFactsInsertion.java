package drools.cookbook.chapter02;

import static org.junit.Assert.assertEquals;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.AssertBehaviorOption;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class DuplicatedFactsInsertion {

    private static KnowledgeBase kbase;

    @Test
    public void withoutDuplicatedFactsTest() {

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        Server debianServer = new Server("debianServer", 8, 8192, 2048, 0);
        ksession.insert(debianServer);

        Server winServer = new Server("winServer", 4, 4096, 2048, 0);
        ksession.insert(winServer);

        Server ubuntuServer = new Server("ubuntuServer", 4, 2048, 1024, 0);
        ksession.insert(ubuntuServer);

        assertEquals(3, ksession.getObjects().size());

        System.out.println("Current total facts count: " + ksession.getObjects().size());

        Server anotherDebianServer = new Server("debianServer", 8, 8192, 2048, 0);
        ksession.insert(anotherDebianServer);

        assertEquals(3, ksession.getObjects().size());

        System.out.println("Duplicated fact inserted. Total fact count: " + ksession.getObjects().size());

        Virtualization rhel = new Virtualization("rhel", "ubuntuServer", 2048, 160);
        VirtualizationRequest virtualizationRequest = new VirtualizationRequest(rhel);

        ksession.insert(virtualizationRequest);

        ksession.fireAllRules();

        assertEquals(false, virtualizationRequest.isSuccessful());

        ksession.fireAllRules();

    }

    @BeforeClass
    public static void createKnowledgeBase() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules.drl", DuplicatedFactsInsertion.class), ResourceType.DRL);

        if (kbuilder.hasErrors()) {
            if (kbuilder.getErrors().size() > 0) {
                for (KnowledgeBuilderError kerror : kbuilder.getErrors()) {
                    System.err.println(kerror);
                }
            }
        }

        KnowledgeBaseConfiguration kbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        kbaseConfig.setOption(AssertBehaviorOption.EQUALITY);
        kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseConfig);

        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
    }

}
