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
public class SimpleProcessTest extends JbpmJUnitTestCase {

    public SimpleProcessTest() {
        super(false);
    }

    public void testProcessWithSignalEvent() throws Exception {
        KnowledgeBase kbase = createKnowledgeBase("processWithSignalEvent.bpmn2");
        StatefulKnowledgeSession ksession = createKnowledgeSession(kbase);
        ProcessInstance processInstance = ksession.startProcess("processWithSignalEvent");
        Assert.assertNotNull(processInstance);
        assertProcessInstanceActive(processInstance.getId(), ksession);
        assertNodeActive(processInstance.getId(), ksession, "Catch Signal Event");
        ksession.signalEvent("mySignal", null, processInstance.getId());
        assertProcessInstanceCompleted(processInstance.getId(), ksession);
    }

    public void testProcessWithScriptTask() throws Exception {
        KnowledgeBase kbase = createKnowledgeBase("processWithScriptTask.bpmn2");
        StatefulKnowledgeSession ksession = createKnowledgeSession(kbase);
        ProcessInstance processInstance = ksession.startProcess("processWithScriptTask");
        Assert.assertNotNull(processInstance);
        assertProcessInstanceCompleted(processInstance.getId(), ksession);
    }

}
