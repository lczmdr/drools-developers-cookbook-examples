package drools.cookbook.chapter06;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.drools.command.BatchExecutionCommand;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.runtime.help.BatchExecutionHelper;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class XStreamCommandsTest {

    @Test
    public void xstreamCommandsFactoryTest() throws InterruptedException {

        Server server1 = new Server("windows-nt", 1, 2048, 2048, 3);

        List<Command> commands = new ArrayList<Command>();
        BatchExecutionCommand batchExecutionCommand = CommandFactory.newBatchExecution(commands, "ksession1");

        Command insertObjectCommand = CommandFactory.newInsert(server1);
        Command fireAllRulesCommand = CommandFactory.newFireAllRules();
        commands.add(insertObjectCommand);
        commands.add(fireAllRulesCommand);

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
