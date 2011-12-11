package drools.cookbook.chapter01;

import static org.junit.Assert.assertNotNull;
import junit.framework.Assert;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.type.FactType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class RulesExecutionLoggingTest {

    @Test
    public void simpleTest() {

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

        ksession.addEventListener(new CustomAgendaEventListener());

        ksession.addEventListener(new CustomWorkingMemoryEventListener());

        FactType serverType = kbase.getFactType("drools.cookbook.chapter01", "Server");

        assertNotNull(serverType);

        Object debianServer = null;
        try {
            debianServer = serverType.newInstance();
        } catch (InstantiationException e) {
            System.err.println("the class Server on drools.cookbook.chapter01 package hasn't a constructor");
        } catch (IllegalAccessException e) {
            System.err.println("unable to access the class Server on drools.cookbook.chapter01 package");
        }
        serverType.set(debianServer, "name", "server001");
        serverType.set(debianServer, "processors", 1);
        serverType.set(debianServer, "memory", 2048); // 2 gigabytes
        serverType.set(debianServer, "diskSpace", 2048); // 2 terabytes
        serverType.set(debianServer, "cpuUsage", 3);

        ksession.insert(debianServer);

        ksession.fireAllRules();

        Assert.assertEquals(0, ksession.getObjects().size());

    }

}
