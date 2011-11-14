package drools.cookbook.chapter09;

import java.util.concurrent.TimeUnit;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.time.SessionPseudoClock;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class BusinessMonitoring {

    public static void main(String[] args) throws Exception {

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
            clock.advanceTime(30, TimeUnit.MINUTES);
            ksession.startProcess("withdrawalProcess");
        }
        ksession.dispose();
    }

    private static KnowledgeBase createKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("withdrawalProcess.bpmn"), ResourceType.BPMN2);
        kbuilder.add(ResourceFactory.newClassPathResource("withdrawalRules.drl"), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            for (KnowledgeBuilderError error : kbuilder.getErrors()) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Unable to parse knowledge.");
        }
        KnowledgeBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        config.setOption(EventProcessingOption.STREAM);

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(config);
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase;
    }

}
