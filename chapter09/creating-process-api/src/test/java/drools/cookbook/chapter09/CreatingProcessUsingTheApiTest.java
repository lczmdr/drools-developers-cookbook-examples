package drools.cookbook.chapter09;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.common.AbstractRuleBase;
import org.drools.impl.InternalKnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessContext;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.process.core.event.EventTypeFilter;
import org.jbpm.process.instance.impl.Action;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.jbpm.workflow.core.Node;
import org.jbpm.workflow.core.impl.ConnectionImpl;
import org.jbpm.workflow.core.impl.DroolsConsequenceAction;
import org.jbpm.workflow.core.node.ActionNode;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.EventNode;
import org.jbpm.workflow.core.node.StartNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class CreatingProcessUsingTheApiTest {

    @Test
    public void processWithSignalEvent() throws Exception {
        RuleFlowProcess process = new RuleFlowProcess();
        String processId = "processWithEventNode";
        String eventType = "eventType";
        process.setId(processId);

        StartNode startNode = new StartNode();
        startNode.setName("Start");
        startNode.setId(1);

        EventNode eventNode = new EventNode();
        eventNode.setName("EventNode");
        eventNode.setId(2);
        eventNode.setScope("external");
        EventTypeFilter eventFilter = new EventTypeFilter();
        eventFilter.setType(eventType);
        eventNode.addEventFilter(eventFilter);

        EndNode endNode = new EndNode();
        endNode.setName("End");
        endNode.setId(3);

        connect(startNode, eventNode);
        connect(eventNode, endNode);

        process.addNode(startNode);
        process.addNode(eventNode);
        process.addNode(endNode);

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        ((AbstractRuleBase) ((InternalKnowledgeBase) kbase).getRuleBase()).addProcess(process);
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        ProcessInstance processInstance = ksession.startProcess(processId);
        Assert.assertNotNull(processInstance);
        long processInstanceId = processInstance.getId();
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, processInstance.getState());
        ksession.signalEvent(eventType, null, processInstanceId);
        Assert.assertEquals(ProcessInstance.STATE_COMPLETED, processInstance.getState());
    }

    @Test
    public void newProcessWithScriptTask() {
        String processId = "processWithScriptTask";

        StartNode startNode = new StartNode();
        startNode.setName("Start");
        startNode.setId(1);
        EndNode endNode = new EndNode();
        endNode.setName("EndNode");
        endNode.setId(2);

        ActionNode actionNode = new ActionNode();
        actionNode.setId(3);
        DroolsConsequenceAction insertAction = new DroolsConsequenceAction("java", null);
        insertAction.setMetaData("Action", new Action() {

            public void execute(ProcessContext context) {
                System.out.println("Script task node executed");
            }

        });
        actionNode.setAction(insertAction);

        connect(startNode, actionNode);
        connect(actionNode, endNode);

        RuleFlowProcess process = new RuleFlowProcess();
        process.setId(processId);
        process.addNode(startNode);
        process.addNode(actionNode);
        process.addNode(endNode);

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        ((AbstractRuleBase) ((InternalKnowledgeBase) kbase).getRuleBase()).addProcess(process);
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        ProcessInstance processInstance = ksession.startProcess(processId);
        Assert.assertNotNull(processInstance);
        Assert.assertEquals(ProcessInstance.STATE_COMPLETED, processInstance.getState());
    }

    private void connect(Node sourceNode, Node targetNode) {
        new ConnectionImpl(sourceNode, Node.CONNECTION_DEFAULT_TYPE, targetNode, Node.CONNECTION_DEFAULT_TYPE);
    }

}
