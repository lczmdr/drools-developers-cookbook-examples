package drools.cookbook;

import java.util.Date;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import drools.cookbook.model.FlightControl;
import drools.cookbook.model.FlightStatus;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class FlightControlTest {

    private StatefulKnowledgeSession ksession;

    @Before
    public void start() {
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
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        ksession = kbase.newStatefulKnowledgeSession();
    }

    @Test
    public void initial() {
        Assert.assertNotNull(ksession);

        FlightControl control = new FlightControl();
        control.setAirport("LAX");

        ksession.setGlobal("control", control);

        FlightStatus plane1 = new FlightStatus();
        plane1.setTimestamp(new Date());
        plane1.setFlight("AR01");
        ksession.insert(plane1);

        ksession.fireAllRules();
        FlightStatus plane2 = new FlightStatus();
        plane2.setTimestamp(new Date());
        plane2.setFlight("AR01");
        ksession.insert(plane2);

        ksession.fireAllRules();
        FlightStatus plane3 = new FlightStatus();
        plane3.setTimestamp(new Date());
        plane3.setFlight("AR02");
        ksession.insert(plane3);

        ksession.fireAllRules();

    }

    @After
    public void stop() {
        ksession.dispose();
    }

}
