package drools.cookbook.chapter07.jms;

import org.springframework.jms.core.JmsTemplate;

public class JMSQueueProducer {

    private String queueName;
    private JmsTemplate jmsTemplate;

    public void send(String message) {
        jmsTemplate.convertAndSend(queueName, message);
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

}
