package drools.cookbook.chapter09;

import java.util.List;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;

public class EmailNotificationWorkItem implements WorkItemHandler {

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
    }

    @SuppressWarnings("unchecked")
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        List<String> emails = (List<String>) workItem.getParameter("emails");
        StringBuffer message = new StringBuffer("Sending email notifications to: ");
        for (String email : emails) {
            message.append(email + ", ");
        }
        System.out.println(message.substring(0, message.length() - 2));
        manager.completeWorkItem(workItem.getId(), null);
    }
}
