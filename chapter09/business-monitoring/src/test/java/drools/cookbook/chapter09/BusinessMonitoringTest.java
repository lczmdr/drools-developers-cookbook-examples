package drools.cookbook.chapter09;

import java.util.concurrent.TimeUnit;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.time.SessionPseudoClock;
import org.junit.Test;

import drools.cookbook.chapter09.CustomProcessEventListener;

public class BusinessMonitoringTest {

    @Test
    public void withdrawalProcess() throws Exception {

        KnowledgeBase kbase = createKnowledgeBase();
        KnowledgeSessionConfiguration conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        conf.setOption(ClockTypeOption.get("pseudo"));

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(conf, null);
        SessionPseudoClock clock = ksession.getSessionClock();
        ksession.addEventListener(new CustomProcessEventListener(ksession));

        for (int i = 0; i < 20; i++) {
            clock.advanceTime(20, TimeUnit.SECONDS);
            ksession.startProcess("withdrawalProcess");
        }
        for (int i = 0; i < 20; i++) {
            clock.advanceTime(2, TimeUnit.MINUTES);
            ksession.startProcess("withdrawalProcess");
        }

    }

    private static KnowledgeBase createKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("withdrawalProcess.bpmn2"), ResourceType.BPMN2);
        kbuilder.add(ResourceFactory.newClassPathResource("withdrawalRules.drl"), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            for (KnowledgeBuilderError error : kbuilder.getErrors()) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Unable to parse knowledge.");
        }
        return kbuilder.newKnowledgeBase();
    }

}
