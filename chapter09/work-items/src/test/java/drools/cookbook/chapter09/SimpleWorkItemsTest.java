package drools.cookbook.chapter09;

import java.util.HashMap;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("serial")
public class SimpleWorkItemsTest {

    @Test
    public void startProcessWithWorkItem() {
        StatefulKnowledgeSession ksession = createSession();
        ksession.getWorkItemManager().registerWorkItemHandler("time_consuming_work", new EmailNotificationWorkItem());

        ProcessInstance processInstance = ksession.startProcess("sampleWorkitemProcess", new HashMap<String, Object>() {
            {
                put("description", "God Of War III");
            }
        });

        Assert.assertEquals(ProcessInstance.STATE_COMPLETED, processInstance.getState());
    }

    private StatefulKnowledgeSession createSession() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("workitem.bpmn2"), ResourceType.BPMN2);

        Assert.assertTrue(getErrorMessages(kbuilder.getErrors()), kbuilder.getErrors().isEmpty());

        KnowledgeBase kbase = kbuilder.newKnowledgeBase();

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        return ksession;
    }

    private String getErrorMessages(KnowledgeBuilderErrors errors) {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder out = new StringBuilder();
        for (KnowledgeBuilderError error : errors) {
            out.append(error.getMessage());
            out.append(lineSeparator);
        }
        if (!errors.isEmpty())
            out.delete(out.length() - lineSeparator.length(), out.length());
        return out.toString();
    }
}
