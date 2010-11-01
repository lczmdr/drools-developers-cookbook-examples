package drools.cookbook.chapter02;

import static org.junit.Assert.assertEquals;

import javax.naming.InitialContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class JPAKnowledgeSessionPersistenceTest {

    private StatefulKnowledgeSession ksession;
    private PoolingDataSource dataSource;
    private EntityManagerFactory emf;
    private Environment env;
    private KnowledgeBase kbase;

    @Test
    public void persistenceTest() throws Exception {

        UserTransaction ut = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        ut.begin();

        Server debianServer = new Server("debianServer", 4, 4096, 1024, 0);
        ksession.insert(debianServer);

        Server ubuntuServer = new Server("ubuntuServer", 2, 4096, 2048, 0);
        ksession.insert(ubuntuServer);

        Virtualization rhel = new Virtualization("rhel", "debianServer", 2048, 160);
        VirtualizationRequest virtualizationRequest = new VirtualizationRequest(rhel);

        ksession.insert(virtualizationRequest);

        ksession.fireAllRules();

        ut.commit();

        assertEquals(2, ksession.getObjects().size());

        ksession.dispose();

        ksession = JPAKnowledgeService.loadStatefulKnowledgeSession(1, kbase, null, env);

        assertEquals(2, ksession.getObjects().size());

    }

    @Before
    public void setUp() {

        dataSource = new PoolingDataSource();
        dataSource.setUniqueName("jdbc/testDatasource");
        dataSource.setMaxPoolSize(5);
        dataSource.setAllowLocalTransactions(true);

        dataSource.setClassName("org.h2.jdbcx.JdbcDataSource");
        dataSource.setMaxPoolSize(3);
        dataSource.getDriverProperties().put("user", "sa");
        dataSource.getDriverProperties().put("password", "sasa");
        dataSource.getDriverProperties().put("URL", "jdbc:h2:mem:");

        dataSource.init();

        env = KnowledgeBaseFactory.newEnvironment();
        emf = Persistence.createEntityManagerFactory("drools.cookbook.persistence.jpa");
        env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
        env.set(EnvironmentName.TRANSACTION_MANAGER, TransactionManagerServices.getTransactionManager());

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules.drl", JPAKnowledgeSessionPersistenceTest.class), ResourceType.DRL);

        if (kbuilder.hasErrors()) {
            if (kbuilder.getErrors().size() > 0) {
                for (KnowledgeBuilderError kerror : kbuilder.getErrors()) {
                    System.err.println(kerror);
                }
            }
        }

        kbase = kbuilder.newKnowledgeBase();
        ksession = JPAKnowledgeService.newStatefulKnowledgeSession(kbase, null, env);

    }

    @After
    public void tearDown() {
        ksession.dispose();
        emf.close();
        dataSource.close();
    }

}
