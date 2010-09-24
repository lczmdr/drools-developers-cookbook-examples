package drools.cookbook.chapter01;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;

public class WorkingMemoryTest {

    private static final String DATE_PATTERN = "dd-MMM-yyyy HH:mm:ss";

    @Test
    public void simpleTest() {

        System.setProperty("drools.dateformat", DATE_PATTERN);

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules.drl", getClass()), ResourceType.DRL); 

        if (kbuilder.hasErrors()) {
            if (kbuilder.getErrors().size() > 0) {
                for (KnowledgeBuilderError kerror : kbuilder.getErrors()) {
                    System.err.println(kerror);
                }
            }
        }

        KnowledgeBase kbase = kbuilder.newKnowledgeBase();
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        Person lucaz = new Person();
        lucaz.setName("lucaz");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2010);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DATE, 17);
        calendar.set(Calendar.HOUR, 22);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

        System.out.println(dateFormat.format(calendar.getTime()));

        lucaz.setBirthDate(calendar.getTime());

        ksession.insert(lucaz);
        ksession.fireAllRules();

    }

}
