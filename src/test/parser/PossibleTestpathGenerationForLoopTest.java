package test.parser;

import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.cfg.ICFG;
import com.fit.cfg.ICFGGeneration;
import com.fit.cfg.object.AbstractConditionLoopCfgNode;
import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdatagen.loop.PossibleTestpathGenerationForLoop;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

/**
 * This class is used to check whether the standard iteration for testing loop
 * is correct or not. Two cases are happened as follows:
 * <p>
 * Case 1. The standard iteration for the unbounded loops is a constant k (will
 * be set by users)
 * <p>
 * Case 2. The standard iteration for bounded loops is the maximum iterations of
 * that loop (detected by the program automatically)
 * <p>
 * Created by ducanhnguyen on 7/05/2017.
 */
@Ignore
public class PossibleTestpathGenerationForLoopTest {
    @Test
    public void testLoop1() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "loop1(int)").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("a!=2");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(0);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(tpGen.getIterationForUnboundedTestingLoop(), maxLoop);
    }

    @Test
    public void testLoop2() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "loop2(int)").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("a<100");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(0);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(tpGen.getIterationForUnboundedTestingLoop(), maxLoop);
    }

    @Test
    public void testLoop3() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "loop3(int)").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("a<100");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(0);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(100, maxLoop);
    }

    @Test
    public void testLoop4() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "loop4(int)").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("a<-100");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(0);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(102, maxLoop);
    }

    @Test
    public void testLoop5() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "loop5(int)").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("a<=100");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(0);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(tpGen.getIterationForUnboundedTestingLoop(), maxLoop);
    }

    @Test
    public void testLoop6() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "loop6(int)").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("a<=100");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(0);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(101, maxLoop);
    }

    @Test
    public void testLoop7() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "loop7(int)").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("a<=-100");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(0);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(101, maxLoop);
    }

    @Test
    public void testLoop8() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "loop8(int)").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("a<=-100");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(0);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(100, maxLoop);
    }

    @Test
    public void testLoop9() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "loop9(int[3],int[3],int[6])").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("i < 3 && j < 3");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(0);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(tpGen.getIterationForUnboundedTestingLoop(), maxLoop);
    }

    @Test
    public void testLoop10() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "loop9(int[3],int[3],int[6])").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("i < 3");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(0);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(3, maxLoop);
    }

    @Test
    public void testLoop11() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "loop9(int[3],int[3],int[6])").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("j < 3");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(0);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(3, maxLoop);
    }

    @Test
    public void testLoop12() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "loop10(int[3],int[3])").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("i<2");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(0);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(2, maxLoop);
    }

    @Test
    public void testLoop13() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "loop10(int[3],int[3])").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("b[k] != a[i]");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(1);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(tpGen.getIterationForUnboundedTestingLoop(), maxLoop);
    }

    @Test
    public void testLoop14() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(),
                "loop11(int,unsigned char*,unsigned char*)").get(0);

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedState();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("i < n");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setMaximumIterationsForOtherLoops(1);
        tpGen.generateTestpaths();
        int maxLoop = tpGen.getRealMaximumIterationForTestingLoop();

        Assert.assertEquals(tpGen.getIterationForUnboundedTestingLoop(), maxLoop);
    }
}
