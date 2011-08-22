package drools.cookbook.chapter09;

import java.util.Random;

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
import org.jbpm.process.audit.WorkingMemoryDbLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BAMReportingTest {

    private StatefulKnowledgeSession ksession;
    private WorkingMemoryDbLogger historyLogger;

    @Before
    public void before() throws Exception {
        KnowledgeBase kbase = createKnowledgeBase();
        KnowledgeSessionConfiguration conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        conf.setOption(ClockTypeOption.get("pseudo"));
        ksession = kbase.newStatefulKnowledgeSession(conf, null);
        historyLogger = new WorkingMemoryDbLogger(ksession);
    }

    @Test
    public void businessActivityMonitoring() throws Exception {
        for (int i = 0; i < 20; i++) {
            ksession.startProcess("processWithScriptTask");
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

    private static KnowledgeBase createKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("processWithSignalEvent.bpmn2"), ResourceType.BPMN2);
        kbuilder.add(ResourceFactory.newClassPathResource("processWithScriptTask.bpmn2"), ResourceType.BPMN2);
        if (kbuilder.hasErrors()) {
            for (KnowledgeBuilderError error : kbuilder.getErrors()) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Unable to parse knowledge.");
        }
        return kbuilder.newKnowledgeBase();
    }

    @After
    public void after() {
        historyLogger.dispose();
    }

}
