package drools.cookbook.chapter01;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import junit.framework.Assert;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;

public class CustomDateFormatTest {

    private static final String SERVER_NAME = "debianServer";
    private static final int YEAR = 2010;
    private static final int MONTH = Calendar.SEPTEMBER;
    private static final int DATE = 10;
    private static final int HOUR = 22;

    private static final String DATE_FORMAT = "dd-MMM-yyyy HH:mm:ss";

    private StatefulKnowledgeSession ksession;

    @Test
    public void historicalCpuUsageTest() {

        System.setProperty("drools.dateformat", DATE_FORMAT);

        ksession = createKnowledgeSession();

        createHistoricalCpuUsage();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, YEAR);
        calendar.set(Calendar.MONTH, MONTH);
        calendar.set(Calendar.DATE, DATE);
        calendar.set(Calendar.HOUR, HOUR);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 0);

        Date from = calendar.getTime();

        calendar.set(Calendar.MINUTE, 40);
        Date to = calendar.getTime();

        HistoricalCpuUsageQuery query = new HistoricalCpuUsageQuery(SERVER_NAME, from, to);
        ksession.insert(query);
        ksession.fireAllRules();

        System.out.println(query);

        Assert.assertTrue(query.getMaxValue() > 0);
        Assert.assertTrue(query.getAverageValue() > 0);
        Assert.assertTrue(query.getMinValue() <= query.getMaxValue());

    }

    private void createHistoricalCpuUsage() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, YEAR);
        calendar.set(Calendar.MONTH, MONTH);
        calendar.set(Calendar.DATE, DATE);
        calendar.set(Calendar.HOUR, HOUR);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        for(int i=0; i < 60; i+=3) {
            calendar.set(Calendar.MINUTE, i);
//            System.out.println(calendar.getTime());
            HistoricalCpuUsage historicalValue = new HistoricalCpuUsage(SERVER_NAME, calendar.getTime(), generateRandomCpuUsage());
            ksession.insert(historicalValue);
        }
    }

    private int generateRandomCpuUsage() {
        Random random = new Random();
        return random.nextInt(100);
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
