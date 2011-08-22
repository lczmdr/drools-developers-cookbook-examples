package drools.cookbook.chapter09;

import java.util.Random;
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
import org.drools.runtime.process.ProcessInstance;
import org.drools.time.SessionPseudoClock;
import org.jbpm.process.audit.WorkingMemoryDbLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BAMReportingTest {

    private SessionPseudoClock clock;
    private StatefulKnowledgeSession ksession;
    private WorkingMemoryDbLogger historyLogger;

    @Before
    public void before() throws Exception {
        KnowledgeBase kbase = createKnowledgeBase();
        KnowledgeSessionConfiguration conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        conf.setOption(ClockTypeOption.get("pseudo"));
        ksession = kbase.newStatefulKnowledgeSession(conf, null);
        clock = ksession.getSessionClock();
        ksession.addEventListener(new CustomProcessEventListener(ksession));
        historyLogger = new WorkingMemoryDbLogger(ksession);
    }

    @Test
    public void businessActivityMonitoring() throws Exception {
        for (int i = 0; i < 20; i++) {
            clock.advanceTime(20, TimeUnit.SECONDS);
            ksession.startProcess("shoppingProcess");
        }
        for (int i = 0; i < 20; i++) {
            clock.advanceTime(2, TimeUnit.MINUTES);
            ksession.startProcess("shoppingProcess");
        }
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            ProcessInstance processInstance = ksession.startProcess("processWithSignalEvent");
            if (random.nextBoolean()) {
                Thread.sleep(random.nextInt(3000));
                ksession.signalEvent("mySignal", null, processInstance.getId());
            }
        }
    }

    @After
    public void after() {
        historyLogger.dispose();
    }

    private static KnowledgeBase createKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("processWithSignalEvent.bpmn2"), ResourceType.BPMN2);
        kbuilder.add(ResourceFactory.newClassPathResource("new.bpmn2"), ResourceType.BPMN2);
        kbuilder.add(ResourceFactory.newClassPathResource("rules.drl"), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            for (KnowledgeBuilderError error : kbuilder.getErrors()) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Unable to parse knowledge.");
        }
        return kbuilder.newKnowledgeBase();
    }

}
