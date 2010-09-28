package drools.cookbook.chapter01;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.DefaultAgendaEventListener;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;

public class ActivationGroupTest {

    @Test
    public void serverInitialization() {

        List<String> firedRules = new ArrayList<String>();

        StatefulKnowledgeSession ksession = createKnowledgeSession(firedRules);

        Server winServer = new Server("winServer", 4, 4096, 2048, 0);
        ksession.insert(winServer);

        Server ubuntuServer = new Server("ubuntuServer", 4, 2048, 1024, 0);
        ksession.insert(ubuntuServer);

        Virtualization rhel = new Virtualization("rhel", "debianServer", 2048, 160);
        VirtualizationRequest virtualizationRequest = new VirtualizationRequest(rhel);

        ksession.insert(virtualizationRequest);

        ksession.getAgenda().getAgendaGroup("server-configuration-loaded").setFocus();

        ksession.fireAllRules();

        assertTrue(firedRules.remove("Load server configuration from file"));
        assertTrue(firedRules.remove("Check the required server configuration"));

    }

    private StatefulKnowledgeSession createKnowledgeSession(final List<String> firedRules) {
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
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        if (firedRules != null) {
            ksession.addEventListener(new DefaultAgendaEventListener() {
                @Override
                public void afterActivationFired(AfterActivationFiredEvent event) {
                    firedRules.add(event.getActivation().getRule().getName());
                }
            });
        }

        return ksession;
    }

}
