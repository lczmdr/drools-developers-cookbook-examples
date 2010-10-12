package drools.cookbook.chapter02;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.marshalling.Marshaller;
import org.drools.marshalling.MarshallerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 *
 */
public class MarshallingKnowledgeSessionTest {

    private KnowledgeBase kbase;
    private StatefulKnowledgeSession ksession;

    @Test
    public void marshallingKnowledgeSessionTest() throws InterruptedException {

        ksession = kbase.newStatefulKnowledgeSession();

        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        Server debianServer = new Server("debianServer", 4, 4096, 1024, 0);
        ksession.insert(debianServer);

        Virtualization rhel = new Virtualization("rhel", "debianServer", 2048, 160);
        VirtualizationRequest virtualizationRequest = new VirtualizationRequest(rhel);

        ksession.insert(virtualizationRequest);

        ksession.fireAllRules();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Marshaller marshaller = MarshallerFactory.newMarshaller(kbase);
        File file = new File("ksession.info");
        FileOutputStream foStream;
        try {
            foStream = new FileOutputStream(file);
            marshaller.marshall(baos, ksession);
            baos.writeTo(foStream);
            baos.close();
        } catch (IOException e) {
            System.err.println("Error marshalling ksession. " + e.getMessage());
        }

    }

    @Test
    public void readKnowledgeSessionFromFileTest() throws Exception {
        Marshaller marshaller = MarshallerFactory.newMarshaller(kbase);
        StatefulKnowledgeSession ksession = marshaller.unmarshall(new FileInputStream("ksession.info"));
        assertNotNull(ksession);
        assertEquals(1, ksession.getObjects().size());
    }

    @Before
    public void createKnowledgeBase() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules.drl", getClass()), ResourceType.DRL); 

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
