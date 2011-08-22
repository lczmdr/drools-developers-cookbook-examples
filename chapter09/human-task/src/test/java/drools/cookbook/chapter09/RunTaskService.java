package drools.cookbook.chapter09;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.SystemEventListenerFactory;
import org.jbpm.task.User;
import org.jbpm.task.service.TaskServer;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.TaskServiceSession;
import org.jbpm.task.service.mina.MinaTaskServer;

public class RunTaskService {

    private EntityManagerFactory emf;
    private TaskService taskService;
    private TaskServiceSession taskSession;
    private TaskServer server;

    public static void main(String[] args) throws Exception {
        new RunTaskService().start();
    }

    private void start() throws Exception {

        emf = Persistence.createEntityManagerFactory("drools.cookbook.jbpm.task");

        taskService = new TaskService(emf, SystemEventListenerFactory.getSystemEventListener());
        taskSession = taskService.createSession();

        User writer = new User("writer");
        User translator = new User("translator");
        User reviewer = new User("reviewer");
        User Administrator = new User("Administrator");
        taskSession.addUser(Administrator);
        taskSession.addUser(writer);
        taskSession.addUser(translator);
        taskSession.addUser(reviewer);

        server = new MinaTaskServer(taskService);
        Thread thread = new Thread(server);
        thread.start();
        Thread.sleep(500);
        System.out.println("Server started ...");
    }

    protected void stop() throws Exception {
        server.stop();
        taskSession.dispose();
        emf.close();
    }
}
