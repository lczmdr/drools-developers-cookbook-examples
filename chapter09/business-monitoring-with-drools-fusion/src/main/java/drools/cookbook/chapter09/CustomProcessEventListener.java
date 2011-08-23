package drools.cookbook.chapter09;

import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.event.process.ProcessVariableChangedEvent;
import org.drools.runtime.StatefulKnowledgeSession;

public class CustomProcessEventListener implements ProcessEventListener {

    private StatefulKnowledgeSession eventKsession;

    public CustomProcessEventListener(StatefulKnowledgeSession ksession) {
        eventKsession = ksession;
    }

    public void beforeProcessStarted(ProcessStartedEvent event) {
        eventKsession.insert(event);
        eventKsession.fireAllRules();
    }

    public void afterNodeLeft(ProcessNodeLeftEvent arg0) {
    }

    public void afterNodeTriggered(ProcessNodeTriggeredEvent arg0) {
    }

    public void afterProcessCompleted(ProcessCompletedEvent event) {
    }

    public void afterProcessStarted(ProcessStartedEvent arg0) {
    }

    public void beforeNodeLeft(ProcessNodeLeftEvent arg0) {
    }

    public void beforeNodeTriggered(ProcessNodeTriggeredEvent arg0) {
    }

    public void beforeProcessCompleted(ProcessCompletedEvent arg0) {
    }

    public void afterVariableChanged(ProcessVariableChangedEvent arg0) {
    }

    public void beforeVariableChanged(ProcessVariableChangedEvent arg0) {
    }

}
