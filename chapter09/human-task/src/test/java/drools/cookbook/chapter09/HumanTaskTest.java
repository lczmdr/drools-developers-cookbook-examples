package drools.cookbook.chapter09;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.SystemEventListener;
import org.drools.SystemEventListenerFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkItemHandler;
import org.jbpm.process.workitem.wsht.WSHumanTaskHandler;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.task.Status;
import org.jbpm.task.User;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.TaskClientConnector;
import org.jbpm.task.service.TaskServer;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.TaskServiceSession;
import org.jbpm.task.service.mina.MinaTaskClientConnector;
import org.jbpm.task.service.mina.MinaTaskClientHandler;
import org.jbpm.task.service.mina.MinaTaskServer;
import org.jbpm.task.service.responsehandlers.BlockingTaskOperationResponseHandler;
import org.jbpm.task.service.responsehandlers.BlockingTaskSummaryResponseHandler;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class HumanTaskTest {

    private static final long DEFAULT_WAIT_TIME = 2000;
    private static EntityManagerFactory emf;
    private static TaskServer server;
    private static TaskClient client;
    private static TaskService taskService;
    private static TaskServiceSession taskSession;
    private static WorkItemHandler handler;

    @BeforeClass
    public static void setUp() throws Exception {

        emf = Persistence.createEntityManagerFactory("drools.cookbook.jbpm.task");

        SystemEventListener systemEventListener = SystemEventListenerFactory.getSystemEventListener();

        taskService = new TaskService(emf, systemEventListener);
        taskSession = taskService.createSession();

        User writer = new User("writer");
        User translator = new User("translator");
        User reviewer = new User("reviewer");
        User administrator = new User("Administrator");
        taskSession.addUser(writer);
        taskSession.addUser(translator);
        taskSession.addUser(reviewer);
        taskSession.addUser(administrator);

        server = new MinaTaskServer(taskService);
        Thread thread = new Thread(server);
        thread.start();
        Thread.sleep(DEFAULT_WAIT_TIME);
        System.out.println("Human task server started");

        handler = new WSHumanTaskHandler();

        MinaTaskClientHandler minaTaskClientHandler = new MinaTaskClientHandler(systemEventListener);
        TaskClientConnector connector = new MinaTaskClientConnector("client", minaTaskClientHandler);
        client = new TaskClient(connector);
        client.connect("127.0.0.1", 9123);
        System.out.println("Human task client connected");
    }

    @Test
    public void simpleHumanTask() throws InterruptedException {

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("editorial.bpmn2"), ResourceType.BPMN2);

        if (kbuilder.hasErrors()) {
            if (kbuilder.getErrors().size() > 0) {
                for (KnowledgeBuilderError error : kbuilder.getErrors()) {
                    System.err.println(error);
                }
                throw new RuntimeException("Unable to compile process definition");
            }
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);

        ProcessInstance processInstance = ksession.startProcess("document-publication");

        Assert.assertTrue(processInstance.getState() == ProcessInstance.STATE_ACTIVE);

        Assert.assertEquals("Write Document", ((RuleFlowProcessInstance) processInstance).getNodeInstances().iterator()
                .next().getNodeName());

        // sleeping time to create the process
        Thread.sleep(DEFAULT_WAIT_TIME);

        List<TaskSummary> tasks = getUserTasks("writer", "en-UK");

        Assert.assertEquals(1, tasks.size());

        TaskSummary writerTask = tasks.get(0);
        Assert.assertEquals(Status.Reserved, writerTask.getStatus());

        startTask(writerTask.getId(), "writer");
        completeTask(writerTask.getId(), "writer");

        // now the writer should have zero pending tasks
        tasks = getUserTasks("writer", "en-UK");
        Assert.assertEquals(0, tasks.size());

        Thread.sleep(DEFAULT_WAIT_TIME);

        // what about the translator and the reviewer? let's check them
        tasks = getUserTasks("translator", "en-UK");
        Assert.assertEquals(1, tasks.size());

        TaskSummary translatorTask = tasks.get(0);

        startTask(translatorTask.getId(), "translator");
        completeTask(translatorTask.getId(), "translator");

        tasks = getUserTasks("reviewer", "en-UK");
        Assert.assertEquals(1, tasks.size());

        TaskSummary reviewerTask = tasks.get(0);

        startTask(reviewerTask.getId(), "reviewer");
        completeTask(reviewerTask.getId(), "reviewer");

        Thread.sleep(DEFAULT_WAIT_TIME);

        Assert.assertTrue(processInstance.getState() == ProcessInstance.STATE_COMPLETED);
    }

    private static List<TaskSummary> getUserTasks(String userId, String language) {
        BlockingTaskSummaryResponseHandler taskSummaryResponseHandler = new BlockingTaskSummaryResponseHandler();
        client.getTasksAssignedAsPotentialOwner(userId, language, taskSummaryResponseHandler);
        taskSummaryResponseHandler.waitTillDone(DEFAULT_WAIT_TIME);
        return taskSummaryResponseHandler.getResults();
    }

    private static void startTask(long taskId, String userId) {
        System.out.println(userId + " - Starting task " + taskId + "...");
        BlockingTaskOperationResponseHandler operationResponseHandler = new BlockingTaskOperationResponseHandler();
        client.start(taskId, userId, operationResponseHandler);
        operationResponseHandler.waitTillDone(DEFAULT_WAIT_TIME);
        System.out.println(userId + " - Task " + taskId + " started");
    }

    private static void completeTask(long taskId, String userId) {
        System.out.println(userId + " - Completing task " + taskId + "...");
        BlockingTaskOperationResponseHandler operationResponseHandler = new BlockingTaskOperationResponseHandler();
        client.complete(taskId, userId, null, operationResponseHandler);
        operationResponseHandler.waitTillDone(DEFAULT_WAIT_TIME);
        System.out.println(userId + " - Task " + taskId + " completed");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        client.disconnect();
        server.stop();
        taskSession.dispose();
        emf.close();
    }

}
