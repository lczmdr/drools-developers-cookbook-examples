package drools.cookbook.chapter07;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.persistence.jpa.KnowledgeStoreService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.h2.tools.DeleteDbFiles;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import drools.cookbook.chapter07.model.Server;
import drools.cookbook.chapter07.model.Virtualization;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class SpringJpaIntegrationTest {

    private static org.h2.tools.Server h2Server;

    @BeforeClass
    public static void startH2Database() throws Exception {
        DeleteDbFiles.execute("", "Drools", true);
        h2Server = org.h2.tools.Server.createTcpServer(new String[0]);
        h2Server.start();
    }

    @Test
    public void springJpaIntegration() {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        applicationContext.start();

        StatefulKnowledgeSession ksession1 = (StatefulKnowledgeSession) applicationContext.getBean("ksession1");
        int sessionId = ksession1.getId();

        Assert.assertNotNull(ksession1);

        Server debianServer = new Server("debianServer", 4, 2048, 1222, 0);

        ksession1.insert(debianServer);
        ksession1.fireAllRules();

        Assert.assertEquals(1, ksession1.getObjects().size());

        ksession1.dispose();

        Environment env = KnowledgeBaseFactory.newEnvironment();
        env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, applicationContext.getBean("entityManagerFactory"));
        env.set(EnvironmentName.TRANSACTION_MANAGER, applicationContext.getBean("txManager"));

        Virtualization virtualization = new Virtualization("dev", "debian", 512, 30);

        KnowledgeStoreService kstore = (KnowledgeStoreService) applicationContext.getBean("kstore1");
        KnowledgeBase kbase1 = (KnowledgeBase) applicationContext.getBean("kbase1");
        ksession1 = kstore.loadStatefulKnowledgeSession(sessionId, kbase1, null, env);

        Assert.assertNotNull(ksession1);

        ksession1.insert(virtualization);
        ksession1.fireAllRules();

        Assert.assertEquals(1, ksession1.getObjects().size());

        applicationContext.stop();
    }

    @AfterClass
    public static void stopH2Database() throws Exception {
        h2Server.stop();
        DeleteDbFiles.execute("", "Drools", true);
    }

}
