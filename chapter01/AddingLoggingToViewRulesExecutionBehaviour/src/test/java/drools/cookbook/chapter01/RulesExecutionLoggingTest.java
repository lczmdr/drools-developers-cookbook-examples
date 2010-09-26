package drools.cookbook.chapter01;

import static org.junit.Assert.assertNotNull;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.type.FactType;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;

import drools.cookbook.chapter01.CustomAgendaEventListener;
import drools.cookbook.chapter01.CustomWorkingMemoryEventListener;

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

        ksession.addEventListener( new CustomAgendaEventListener() );

        ksession.addEventListener( new CustomWorkingMemoryEventListener() );

        KnowledgeRuntimeLogger newConsoleLogger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        newConsoleLogger.close();

        FactType personType = kbase.getFactType("drools.cookbook.chapter01", "Person");

        assertNotNull(personType);

        Object lucaz = null;
        try {
            lucaz = personType.newInstance();
        } catch (InstantiationException e) {
            System.err.println("the class Person on com.lucazamador.drools.facts package hasn't a constructor");
        } catch (IllegalAccessException e) {
            System.err.println("unable to access the class Person on com.lucazamador.drools.facts package");
        }
        personType.set(lucaz, "name", "lucaz");

        ksession.insert(lucaz);
        ksession.fireAllRules();

    }

}
