package drools.cookbook.chapter01;

import static org.junit.Assert.assertEquals;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.LiveQuery;
import org.junit.Test;

import drools.cookbook.chapter01.listener.CustomViewChangedEventListener;

public class LiveQuerriesTest {

    @Test
    public void serverCpuUsageQuery() {

        StatefulKnowledgeSession ksession = createKnowledgeSession();

        Server winServer = new Server("winServer", 4, 4096, 2048, 25);
        ksession.insert(winServer);

        Server ubuntuServer = new Server("ubuntuServer", 4, 2048, 1024, 70);
        FactHandle ubuntuServerFactHandle = ksession.insert(ubuntuServer);

        Server debianServer = new Server("debianServer", 4, 2048, 1024, 10);
        ksession.insert(debianServer);

        CustomViewChangedEventListener listener = new CustomViewChangedEventListener();
        LiveQuery query = ksession.openLiveQuery("serverCpuUsage", new Object[]{20}, listener);

        assertEquals(1, listener.getCurrentServers().size());
        assertEquals(0, listener.getRemovedServers().size());
        assertEquals(0, listener.getUpdatedServers().size());

        ubuntuServer.setCpuUsage(10);
        ksession.update(ubuntuServerFactHandle, ubuntuServer);

        assertEquals(2, listener.getCurrentServers().size());
        assertEquals(0, listener.getRemovedServers().size());
        assertEquals(0, listener.getUpdatedServers().size());

        ubuntuServer.setCpuUsage(5);
        ksession.update(ubuntuServerFactHandle, ubuntuServer);

        assertEquals(2, listener.getCurrentServers().size());
        assertEquals(0, listener.getRemovedServers().size());
        assertEquals(1, listener.getUpdatedServers().size());

        query.close();
        ksession.dispose();

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
