package drools.cookbook.chapter06;

import junit.framework.Assert;

import org.drools.command.runtime.BatchExecutionCommandImpl;
import org.drools.command.runtime.rule.FireAllRulesCommand;
import org.drools.command.runtime.rule.InsertObjectCommand;
import org.drools.runtime.help.BatchExecutionHelper;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class XStreamCommandsTest {

    @Test
    public void xstreamCommandsTest() throws InterruptedException {

        Server server1 = new Server("windows-nt", 1, 2048, 2048, 3);

        BatchExecutionCommandImpl batchExecutionCommand = new BatchExecutionCommandImpl();
        batchExecutionCommand.setLookup("ksession1");
        InsertObjectCommand insertObjectCommand = new InsertObjectCommand(server1);
        FireAllRulesCommand fireAllRulesCommand = new FireAllRulesCommand();
        batchExecutionCommand.getCommands().add(insertObjectCommand);
        batchExecutionCommand.getCommands().add(fireAllRulesCommand);

        String result = BatchExecutionHelper.newXStreamMarshaller().toXML(batchExecutionCommand);

        String expected = "<batch-execution lookup=\"ksession1\">\n";
        expected += "  <insert>\n";
        expected += "    <drools.cookbook.chapter06.Server>\n";
        expected += "      <name>windows-nt</name>\n";
        expected += "      <processors>1</processors>\n";
        expected += "      <memory>2048</memory>\n";
        expected += "      <diskSpace>2048</diskSpace>\n";
        expected += "      <virtualizations/>\n";
        expected += "      <cpuUsage>3</cpuUsage>\n";
        expected += "      <online>false</online>\n";
        expected += "    </drools.cookbook.chapter06.Server>\n";
        expected += "  </insert>\n";
        expected += "  <fire-all-rules/>\n";
        expected += "</batch-execution>";

        Assert.assertEquals(expected, result);

    }

}
