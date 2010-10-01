package drools.cookbook.chapter01;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.LiveQuery;
import org.drools.runtime.rule.Row;
import org.junit.Test;

import ca.odell.glazedlists.SortedList;
import drools.cookbook.chapter01.listener.CustomViewChangedEventListener;
import drools.cookbook.chapter01.listener.GlazedListViewChangedEventListener;

/**
 * 
 * @author Lucas Amador
 *
 */
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

    @Test
    public void serverCpuUsageGlazedListQuery() {

        StatefulKnowledgeSession ksession = createKnowledgeSession();

        Server winServer = new Server("winServer", 4, 4096, 2048, 25);
        FactHandle winServerFactHandle = ksession.insert(winServer);

        Server ubuntuServer = new Server("ubuntuServer", 4, 2048, 1024, 70);
        FactHandle ubuntuServerFactHandle = ksession.insert(ubuntuServer);

        Server debianServer = new Server("debianServer", 4, 2048, 1024, 10);
        ksession.insert(debianServer);

        GlazedListViewChangedEventListener listener = new GlazedListViewChangedEventListener();
        LiveQuery query = ksession.openLiveQuery("serverCpuUsage", new Object[]{20}, listener);


        SortedList<Row> serverSortedList = new SortedList<Row>(listener, new Comparator<Row>() {
            public int compare(Row r1, Row r2) {
                Server server1 = (Server) r1.get("$serve");
                Server server2 = (Server) r2.get("$server");
                return (server1.getCpuUsage() - server2.getCpuUsage());
            }
        });

        System.out.println("#######################################################################################");
        for (Row row : serverSortedList) {
            System.out.println(row.get("$server"));
        }

        assertEquals(10, ((Server)serverSortedList.get(0).get("$server")).getCpuUsage());

        ubuntuServer.setCpuUsage(13);
        ksession.update(ubuntuServerFactHandle, ubuntuServer);

        System.out.println("#######################################################################################");
        for (Row row : serverSortedList) {
            System.out.println(row.get("$server"));
        }

        assertEquals(10, ((Server)serverSortedList.get(0).get("$server")).getCpuUsage());
        assertEquals(13, ((Server)serverSortedList.get(1).get("$server")).getCpuUsage());

        ubuntuServer.setCpuUsage(5);
        ksession.update(ubuntuServerFactHandle, ubuntuServer);
        winServer.setCpuUsage(0);
        ksession.update(winServerFactHandle, winServer);

        System.out.println("#######################################################################################");
        for (Row row : serverSortedList) {
            System.out.println(row.get("$server"));
        }

        assertEquals(0, ((Server)serverSortedList.get(0).get("$server")).getCpuUsage());
        assertEquals(5, ((Server)serverSortedList.get(1).get("$server")).getCpuUsage());
        assertEquals(10, ((Server)serverSortedList.get(2).get("$server")).getCpuUsage());

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
