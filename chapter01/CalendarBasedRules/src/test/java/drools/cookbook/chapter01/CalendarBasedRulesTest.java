package drools.cookbook.chapter01;

import java.util.Calendar;

import junit.framework.Assert;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.help.QuartzHelper;
import org.junit.Test;
import org.quartz.impl.calendar.WeeklyCalendar;

public class CalendarBasedRulesTest {

    @Test
    public void virtualizationRequestTest() throws InterruptedException {

        StatefulKnowledgeSession ksession = createKnowledgeSession();

        WeeklyCalendar calendar = new WeeklyCalendar();
        org.drools.time.Calendar onlyWeekDays = QuartzHelper.quartzCalendarAdapter(calendar);

        ksession.getCalendars().set("only-weekdays", onlyWeekDays);

        Server debianServer = new Server("debianServer", 4, 4096, 1024, 0);
        ksession.insert(debianServer);

        Virtualization rhel = new Virtualization("rhel", "debianServer", 2048, 160);
        VirtualizationRequest virtualizationRequest = new VirtualizationRequest(rhel);

        ksession.insert(virtualizationRequest);

        ksession.fireAllRules();

        if (isWeekday()) {
            Assert.assertEquals(true, virtualizationRequest.isSuccessful());
            System.out.println("The virtualization request was accepted on server: " + rhel.getServerName());
        }
        else {
            Assert.assertEquals(false, virtualizationRequest.isSuccessful());
            System.out.println("The virtualization request was rejected because is weekend.");
        }

    }

    private boolean isWeekday() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        return !(currentDay==Calendar.SATURDAY || currentDay==Calendar.SUNDAY);
    }

    private StatefulKnowledgeSession createKnowledgeSession() {
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
        return ksession;
    }

}
