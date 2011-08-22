package drools.cookbook.chapter09;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.junit.Assert;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class SimpleProcessWithPersistenceTest extends JbpmJUnitTestCase {

    public void testProcessWithSignalEventUsingPersistence() throws Exception {
        KnowledgeBase kbase = createKnowledgeBase("processWithSignalEvent.bpmn2");
        StatefulKnowledgeSession ksession = createKnowledgeSession(kbase);
        ProcessInstance processInstance = ksession.startProcess("processWithSignalEvent");
        Assert.assertNotNull(processInstance);
        assertProcessInstanceActive(processInstance.getId(), ksession);
        ksession = restoreSession(ksession, false);
        ksession.signalEvent("mySignal", null, processInstance.getId());
        assertProcessInstanceCompleted(processInstance.getId(), ksession);
    }

}
