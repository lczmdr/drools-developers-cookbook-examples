package drools.cookbook.chapter02;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.impl.KnowledgeBaseImpl;
import org.drools.impl.StatefulKnowledgeSessionImpl;
import org.drools.io.impl.ClassPathResource;
import org.drools.management.DroolsManagementAgent;
import org.drools.reteoo.ReteooRuleBase;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * 
 * @author Lucas Amador
 *
 */
public class ServiceRequestSimulation {

    private KnowledgeBase kbase;
    private StatefulKnowledgeSession ksession;

    public static void main(String[] args) {
        ServiceRequestSimulation simulation = new ServiceRequestSimulation();
        simulation.start();
    }

    public ServiceRequestSimulation() {
        kbase = createKnowledgeSession();
        ksession = kbase.newStatefulKnowledgeSession();
    }

    public void start() {

        // initializing the monitoring feature
        DroolsManagementAgent kmanagement = DroolsManagementAgent.getInstance();

        // registering a Knowledge Base
        kmanagement.registerKnowledgeBase((ReteooRuleBase) ((KnowledgeBaseImpl)kbase).getRuleBase());

        // registering a Stateful Knowledge Session
        kmanagement.registerKnowledgeSession(((StatefulKnowledgeSessionImpl)ksession).getInternalWorkingMemory());

        Server debianServer = new Server("debianServer", 8, 8192, 2048, 0);
        Virtualization server01 = new Virtualization("server01", debianServer.getName(), 2048, 1024);
        Service ftpService = new Service("ftp.server01.com", ServiceType.FTP);
        Service httpService = new Service("www.server01.com", ServiceType.HTTP);
        server01.addService(ftpService);
        server01.addService(httpService);
        debianServer.addVirtualization(server01);
        ksession.insert(debianServer);

        Server ubuntuServer = new Server("ubuntuServer", 4, 2048, 1024, 0);
        Virtualization server02 = new Virtualization("server02", debianServer.getName(), 1024, 1024);
        ftpService = new Service("ftp.server02.com", ServiceType.FTP);
        server02.addService(ftpService);
        ubuntuServer.addVirtualization(server02);

        ksession.insert(ubuntuServer);

        Thread requestSimulationThread = new Thread(new Runnable() {
            public void run() {
                for (int i=0; i < 10000; i++) {
                    ksession.insert(ServiceRequestFactory.create());
                    ksession.fireAllRules();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        requestSimulationThread.start();

    }

    private KnowledgeBase createKnowledgeSession() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules.drl", getClass()), ResourceType.DRL);

        if (kbuilder.hasErrors()) {
            if (kbuilder.getErrors().size() > 0) {
                for (KnowledgeBuilderError kerror : kbuilder.getErrors()) {
                    System.err.println(kerror);
                }
            }
        }

        return kbuilder.newKnowledgeBase();
    }

}
