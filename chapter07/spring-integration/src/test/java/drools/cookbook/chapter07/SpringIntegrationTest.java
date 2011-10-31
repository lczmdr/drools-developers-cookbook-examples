package drools.cookbook.chapter07;

import junit.framework.Assert;

import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import drools.cookbook.chapter07.model.Server;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class SpringIntegrationTest {

    @Test
    public void simpleSpringIntegration() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        applicationContext.start();
        StatefulKnowledgeSession ksession1 = (StatefulKnowledgeSession) applicationContext.getBean("ksession1");
        Assert.assertNotNull(ksession1);
        Server debianServer = new Server("debian-1", 4, 2048, 250, 0);
        ksession1.insert(debianServer);
        ksession1.fireAllRules();
        Assert.assertEquals(0, ksession1.getFactCount());
        applicationContext.stop();
    }
}
