package drools.cookbook.chapter02;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
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
import org.drools.marshalling.ObjectMarshallingStrategy;
import org.drools.marshalling.ObjectMarshallingStrategyAcceptor;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.BeforeClass;
import org.junit.Test;

import drools.cookbook.chapter02.virtualization.Virtualization;
import drools.cookbook.chapter02.virtualization.VirtualizationRequest;

/**
 * 
 * @author Lucas Amador
 *
 */
public class MarshallingKnowledgeSessionTest {

    private static KnowledgeBase kbase;
    private static StatefulKnowledgeSession ksession;
    private static Marshaller marshaller;

    @Test
    public void marshallingKnowledgeSessionTest() throws InterruptedException, ClassNotFoundException {

        ksession = kbase.newStatefulKnowledgeSession();

        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        Server debianServer = new Server("debianServer", 4, 4096, 1024, 0);
        ksession.insert(debianServer);

        Virtualization rhel = new Virtualization("rhel", "debianServer", 2048, 160);
        VirtualizationRequest virtualizationRequest = new VirtualizationRequest(rhel);

        ksession.insert(virtualizationRequest);

        ksession.fireAllRules();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        File file = new File("ksession.info");
        FileOutputStream foStream;
        try {
            foStream = new FileOutputStream(file);
            marshaller.marshall(baos, ksession);
            baos.writeTo(foStream);
            baos.close();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            System.out.println(inputStream.hashCode());
            StatefulKnowledgeSession ksession = marshaller.unmarshall(inputStream);
            System.out.println(ksession);
            System.out.println(baos);
            FileInputStream fis = new FileInputStream("ksession.info");
            ksession = marshaller.unmarshall(fis);
            System.out.println(ksession);
        } catch (IOException e) {
            System.err.println("Error marshalling ksession. " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Test
    public void readKnowledgeSessionFromFileTest() throws Exception {
        File file = new File("ksession.info");
        FileInputStream fis = new FileInputStream(file);
        byte fileContent[] = new byte[(int)file.length()];
        fis.read(fileContent);
        ByteArrayInputStream bais = new ByteArrayInputStream(fileContent);
//        System.out.println("file content: " + new String(fileContent) + "\n" + bais.hashCode());
        StatefulKnowledgeSession ksession = marshaller.unmarshall(bais);
        assertNotNull(ksession);
        assertEquals(1, ksession.getObjects().size());
    }
    

    @BeforeClass
    public static void createKnowledgeBase() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules.drl", MarshallingKnowledgeSessionTest.class), ResourceType.DRL); 

        if (kbuilder.hasErrors()) {
            if (kbuilder.getErrors().size() > 0) {
                for (KnowledgeBuilderError kerror : kbuilder.getErrors()) {
                    System.err.println(kerror);
                }
            }
        }

        kbase = kbuilder.newKnowledgeBase();
        
        String[] patterns = new String[] {"drools.cookbook.chapter02.virtualization.*"};
        ObjectMarshallingStrategyAcceptor identityAcceptor = MarshallerFactory.newClassFilterAcceptor(patterns);
        ObjectMarshallingStrategy identityStrategy = MarshallerFactory.newIdentityMarshallingStrategy(identityAcceptor);
        ObjectMarshallingStrategy serializeStrategy = MarshallerFactory.newSerializeMarshallingStrategy();

        marshaller = MarshallerFactory.newMarshaller(kbase, new ObjectMarshallingStrategy[]{identityStrategy, serializeStrategy});
    }

}
