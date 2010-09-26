package drools.cookbook.chapter01;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 *
 */
public class TimeBasedRulesTest {

    private FactHandle debianServerFactHandle;

    @Test
    public void historicalCpuUsageTest() throws InterruptedException {

        final StatefulKnowledgeSession ksession = createKnowledgeSession();

        ksession.setGlobal("alerts", new ServerAlert());

        final Server debianServer = new Server("debianServer", 4, 2048, 2048, 4);
        debianServer.setOnline(true);

        new Thread(new Runnable() {
            public void run() {
                ksession.fireUntilHalt();
            }
        }).start();

        debianServerFactHandle = ksession.insert(debianServer);

        Thread simulationThread = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(7000);
                    debianServer.setOnline(false);
                    ksession.update(debianServerFactHandle, debianServer);
                    Thread.sleep(16000);
                    debianServer.setOnline(true);
                    ksession.update(debianServerFactHandle, debianServer);
                } catch (InterruptedException e) {
                    System.err.println("An error ocurrs in the simulation thread");
                }
            }
        });

        simulationThread.start();

        // sleep 30 seconds
        Thread.sleep(30000);
        simulationThread.interrupt();
        ksession.halt();

    }

    private StatefulKnowledgeSession createKnowledgeSession() {
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
        return ksession;
    }

}
