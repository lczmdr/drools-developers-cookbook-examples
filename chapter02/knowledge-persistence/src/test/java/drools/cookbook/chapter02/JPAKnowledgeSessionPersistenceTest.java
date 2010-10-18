package drools.cookbook.chapter02;

import javax.naming.InitialContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.base.MapGlobalResolver;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
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

    @Test
    public void persistenceTest() throws Exception {

        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        UserTransaction ut = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        ut.begin();

        SimplePojo pojo = new SimplePojo();
        pojo.setName("lucaz");

        ksession.insert(pojo);

        Server debianServer = new Server("debianServer", 4, 4096, 1024, 0);
        ksession.insert(debianServer);

        Virtualization rhel = new Virtualization("rhel", "debianServer", 2048, 160);
        VirtualizationRequest virtualizationRequest = new VirtualizationRequest(rhel);

        ksession.insert(virtualizationRequest);

        ksession.fireAllRules();

        ut.commit();

    }

    @Before
    public void setUp() {

        dataSource = new PoolingDataSource();
        dataSource.setUniqueName("jdbc/testDS1");
        dataSource.setMaxPoolSize(5);
        dataSource.setAllowLocalTransactions(true);

        // ds1.setClassName( "org.h2.jdbcx.JdbcDataSource" );
        // ds1.setMaxPoolSize( 3 );
        // ds1.getDriverProperties().put( "user", "sa" );
        // ds1.getDriverProperties().put( "password", "sasa" );
        // ds1.getDriverProperties().put( "URL", "jdbc:h2:mem:" );

        dataSource.setClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        dataSource.getDriverProperties().put("user", "root");
        dataSource.getDriverProperties().put("password", "");
        dataSource.getDriverProperties().put("databaseName", "signalstress");
        dataSource.getDriverProperties().put("serverName", "localhost");

        dataSource.init();

        Environment env = KnowledgeBaseFactory.newEnvironment();
        emf = Persistence.createEntityManagerFactory("drools.cookbook.persistence.jpa");
        env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
        env.set(EnvironmentName.TRANSACTION_MANAGER, TransactionManagerServices.getTransactionManager());
        env.set(EnvironmentName.GLOBALS, new MapGlobalResolver());

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules.drl", JPAKnowledgeSessionPersistenceTest.class), ResourceType.DRL);

        if (kbuilder.hasErrors()) {
            if (kbuilder.getErrors().size() > 0) {
                for (KnowledgeBuilderError kerror : kbuilder.getErrors()) {
                    System.err.println(kerror);
                }
            }
        }

        KnowledgeBase kbase = kbuilder.newKnowledgeBase();
        ksession = JPAKnowledgeService.newStatefulKnowledgeSession(kbase, null, env);

    }

    @After
    public void tearDown() {
        emf.close();
        dataSource.close();
    }

}
