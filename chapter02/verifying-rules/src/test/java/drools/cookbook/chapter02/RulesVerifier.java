package drools.cookbook.chapter02;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.verifier.Verifier;
import org.drools.verifier.VerifierError;
import org.drools.verifier.builder.VerifierBuilder;
import org.drools.verifier.builder.VerifierBuilderFactory;
import org.drools.verifier.data.VerifierReport;
import org.drools.verifier.report.VerifierReportWriter;
import org.drools.verifier.report.VerifierReportWriterFactory;
import org.drools.verifier.report.components.MissingRange;
import org.drools.verifier.report.components.Severity;
import org.drools.verifier.report.components.VerifierMessageBase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class RulesVerifier {

    private static Verifier verifier;

    @Test
    public void rulesVerification() {

        boolean works = verifier.fireAnalysis();

        assertTrue(works);

        VerifierReport result = verifier.getResult();
        assertNotNull(result);

        assertEquals(3, result.getBySeverity(Severity.ERROR).size());
        assertEquals(3, result.getBySeverity(Severity.WARNING).size());
        assertEquals(0, result.getBySeverity(Severity.NOTE).size());

        Collection<VerifierMessageBase> noteMessages = result.getBySeverity(Severity.NOTE);
        for (VerifierMessageBase msg : noteMessages) {
            System.out.println("Note: " + msg.getMessage() + " type: " + msg.getMessageType() + " on: " + msg.getFaulty());
        }

        Collection<VerifierMessageBase> errorMessages = result.getBySeverity(Severity.ERROR);
        for (VerifierMessageBase msg : errorMessages) {
            System.out.println("Error: " + msg.getMessage() + " type: " + msg.getMessageType() + " on: " + msg.getFaulty());
        }

        Collection<VerifierMessageBase> warningMessages = result.getBySeverity(Severity.WARNING);
        for (VerifierMessageBase msg : warningMessages) {
            System.out.println("Warning: " + msg.getMessage() + " type: " + msg.getMessageType() + " on: " + msg.getFaulty());
        }

        Collection<MissingRange> rangeCheckCauses = result.getRangeCheckCauses();
        for (MissingRange missingRange : rangeCheckCauses) {
            System.out.println(missingRange);
        }

    }

    @Test
    public void generateVerifierReport() throws IOException {

        boolean works = verifier.fireAnalysis();

        assertTrue(works);

        VerifierReport result = verifier.getResult();
        assertNotNull(result);

        assertEquals(3, result.getBySeverity(Severity.ERROR).size());
        assertEquals(3, result.getBySeverity(Severity.WARNING).size());
        assertEquals(0, result.getBySeverity(Severity.NOTE).size());

        VerifierReportWriter htmlWriter = VerifierReportWriterFactory.newHTMLReportWriter();
        // VerifierReportWriter xmlWriter =
        // VerifierReportWriterFactory.newXMLReportWriter();
        // VerifierReportWriter txtWriter =
        // VerifierReportWriterFactory.newPlainTextReportWriter();

        // Write to disk
        FileOutputStream htmlOut = new FileOutputStream("report.zip");
        // FileOutputStream xmlOut = new FileOutputStream("report.xml");
        // FileOutputStream txtOut = new FileOutputStream("report.txt");

        htmlWriter.writeReport(htmlOut, verifier.getResult());
        // xmlWriter.writeReport(xmlOut, verifier.getResult());
        // txtWriter.writeReport(txtOut, verifier.getResult());

        htmlOut.close();
    }

    @BeforeClass
    public static void beforeClass() {
        VerifierBuilder vbuilder = VerifierBuilderFactory.newVerifierBuilder();

        verifier = vbuilder.newVerifier();

        verifier.addResourcesToVerify(new ClassPathResource("rules.drl", RulesVerifier.class), ResourceType.DRL);

        if (verifier.hasErrors()) {
            List<VerifierError> errors = verifier.getErrors();
            for (VerifierError verifierError : errors) {
                System.err.println(verifierError.getMessage());
            }
            fail("rules with errors");
        }

    }

    @AfterClass
    public static void afterClass() {
        verifier.dispose();
    }

}
