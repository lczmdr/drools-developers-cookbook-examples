package drools.cookbook.chapter02;

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.net.URLClassLoader;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class KnowledgeAgentClassloaderTest {

    @Test
    public void customClassloaderTest() {

        KnowledgeBase kbase = createKnowledgeBase();

        assertTrue(kbase.getKnowledgePackages().size() == 1);

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        ksession.fireAllRules();

    }

    private KnowledgeBase createKnowledgeBase() {

        URL modelJarURL = getClass().getResource("model.jar");
        URLClassLoader customURLClassloader = new URLClassLoader(new URL[] { modelJarURL });

        KnowledgeBuilderConfiguration kbuilderConfig = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(null, customURLClassloader);

        KnowledgeBaseConfiguration kbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration(null, customURLClassloader);
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseConfig);

        KnowledgeAgentConfiguration aconf = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();
        KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent("test", kbase, aconf, kbuilderConfig);

        kagent.applyChangeSet(new ClassPathResource("change-set.xml", getClass()));

        return kagent.getKnowledgeBase();
    }

}
