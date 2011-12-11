package drools.cookbook.chapter01;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.type.FactType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class FactsDeclarationTest {

    private KnowledgeBase kbase;

    @Test
    public void checkServerConfiguration() {

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        FactType serverType = kbase.getFactType("drools.cookbook.chapter01", "Server");

        assertNotNull(serverType);

        Object debianServer = null;
        try {
            debianServer = serverType.newInstance();
        } catch (InstantiationException e) {
            System.err.println("the class Server on drools.cookbook.chapter01 package hasn't a constructor");
        } catch (IllegalAccessException e) {
            System.err.println("unable to access the class Server on drools.cookbook.chapter01 package");
        }
        serverType.set(debianServer, "name", "server001");
        serverType.set(debianServer, "processors", 1);
        serverType.set(debianServer, "memory", 2048); // 2 gigabytes
        serverType.set(debianServer, "diskSpace", 2048); // 2 terabytes
        serverType.set(debianServer, "cpuUsage", 3);

        ksession.insert(debianServer);

        ksession.fireAllRules();

        assertEquals(ksession.getObjects().size(), 0);

    }

    @Test
    public void availableServers() {

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        FactType serverType = kbase.getFactType("drools.cookbook.chapter01", "Server");
        FactType serverStatusType = kbase.getFactType("drools.cookbook.chapter01", "ServerStatus");
        FactType virtualizationType = kbase.getFactType("drools.cookbook.chapter01", "Virtualization");

        assertNotNull(serverType);

        Object debianServer = null;
        try {
            debianServer = serverType.newInstance();
        } catch (InstantiationException e) {
            System.err.println("the class Server on drools.cookbook.chapter01 package hasn't a constructor");
        } catch (IllegalAccessException e) {
            System.err.println("unable to access the class Server on drools.cookbook.chapter01 package");
        }
        serverType.set(debianServer, "name", "server002");
        serverType.set(debianServer, "processors", 4);
        serverType.set(debianServer, "memory", 8192); // 8 gigabytes
        serverType.set(debianServer, "diskSpace", 2048); // 2 terabytes
        serverType.set(debianServer, "cpuUsage", 3);

        Object fedoraServer = null;
        try {
            fedoraServer = serverType.newInstance();
        } catch (InstantiationException e) {
            System.err.println("the class Server on drools.cookbook.chapter01 package hasn't a constructor");
        } catch (IllegalAccessException e) {
            System.err.println("unable to access the class Server on drools.cookbook.chapter01 package");
        }
        serverType.set(fedoraServer, "name", "server003");
        serverType.set(fedoraServer, "processors", 2);
        serverType.set(fedoraServer, "memory", 2048); // 2 gigabytes
        serverType.set(fedoraServer, "diskSpace", 1048); // 1 terabytes
        serverType.set(fedoraServer, "cpuUsage", 80);

        Object instance001 = null;
        Object instance002 = null;
        Object instance003 = null;
        Object request = null;
        try {
            instance001 = virtualizationType.newInstance();
            instance002 = virtualizationType.newInstance();
            instance003 = virtualizationType.newInstance();
            request = virtualizationType.newInstance();
        } catch (InstantiationException e) {
            System.err.println("the class Virtualization on drools.cookbook.chapter01 package hasn't a constructor");
        } catch (IllegalAccessException e) {
            System.err.println("unable to access the class Virtualization on drools.cookbook.chapter01 package");
        }

        virtualizationType.set(instance001, "name", "instance001");
        virtualizationType.set(instance001, "diskSpace", 10);
        virtualizationType.set(instance001, "memory", 2048);

        virtualizationType.set(instance002, "name", "instance002");
        virtualizationType.set(instance002, "diskSpace", 25);
        virtualizationType.set(instance002, "memory", 2048);

        virtualizationType.set(instance003, "name", "instance003");
        virtualizationType.set(instance003, "diskSpace", 25);
        virtualizationType.set(instance003, "memory", 2048);

        virtualizationType.set(request, "name", "instance003");
        virtualizationType.set(request, "diskSpace", 10);
        virtualizationType.set(request, "memory", 3072);

        List<Object> virtualizations = new ArrayList<Object>();
        virtualizations.add(instance001);
        virtualizations.add(instance002);

        serverType.set(debianServer, "virtualizations", virtualizations);

        virtualizations = new ArrayList<Object>();
        virtualizations.add(instance003);

        serverType.set(fedoraServer, "virtualizations", virtualizations);

        ksession.setGlobal("serversAvailability", new ArrayList<Object>());

        ksession.insert(debianServer);
        ksession.insert(fedoraServer);
        ksession.insert(request);

        ksession.fireAllRules();

        @SuppressWarnings("unchecked")
        List<Object> servers = (List<Object>) ksession.getGlobal("serversAvailability");
        assertEquals(1, servers.size());
        for (Object server : servers) {
            String name = (String) serverStatusType.get(server, "name");
            Integer freeDiskSpace = (Integer) serverStatusType.get(server, "freeDiskSpace");
            System.out.println("Server \"" + name + "\" has " + freeDiskSpace + " MB of free disk space");
        }
    }

    @Before
    public void createKnowledgeBase() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules.drl", getClass()), ResourceType.DRL);

        if (kbuilder.hasErrors()) {
            if (kbuilder.getErrors().size() > 0) {
                for (KnowledgeBuilderError kerror : kbuilder.getErrors()) {
                    System.err.println(kerror);
                }
            }
        }
        kbase = kbuilder.newKnowledgeBase();
    }

}
