package drools.cookbook.chapter01;

import java.lang.reflect.Method;
import java.math.BigInteger;

import junit.framework.Assert;

import org.drools.KnowledgeBase;
import org.drools.builder.JaxbConfiguration;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.common.InternalRuleBase;
import org.drools.impl.KnowledgeBaseImpl;
import org.drools.io.impl.ClassPathResource;
import org.drools.rule.DroolsCompositeClassLoader;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Before;
import org.junit.Test;

import com.sun.tools.xjc.Language;
import com.sun.tools.xjc.Options;

/**
 * 
 * @author Lucas Amador
 *
 */
public class XSDFactsDeclarationTest {

    private KnowledgeBase kbase;

    @Test
    public void checkServerConfiguration() throws Exception {

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        DroolsCompositeClassLoader classLoader = ((InternalRuleBase) ((KnowledgeBaseImpl) kbase).getRuleBase()).getRootClassLoader();

        Class<?> serverClass = classLoader.loadClass("drools.cookbook.chapter01.Server");
        Assert.assertNotNull(serverClass);
        Object debianServer = serverClass.newInstance();

        Method setName = serverClass.getMethod("setName", String.class);
        setName.invoke(debianServer, "debianServer");

        Method setProcessors = serverClass.getMethod("setProcessors", BigInteger.class);
        setProcessors.invoke(debianServer, new BigInteger("4"));

        Method setMemory = serverClass.getMethod("setMemory", BigInteger.class);
        setMemory.invoke(debianServer, new BigInteger("1024"));

        Method setDiskSpace = serverClass.getMethod("setDiskSpace", BigInteger.class);
        setDiskSpace.invoke(debianServer, new BigInteger("320"));

        ksession.insert(debianServer);

        ksession.fireAllRules();

    }

    @Before
    public void createKnowledgeSession() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        Options xjcOpts = new Options();
        xjcOpts.setSchemaLanguage( Language.XMLSCHEMA );

        JaxbConfiguration jaxbConfiguration = KnowledgeBuilderFactory.newJaxbConfiguration(xjcOpts, "xsd");

        kbuilder.add(new ClassPathResource("model.xsd", getClass()), ResourceType.XSD, jaxbConfiguration);
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
