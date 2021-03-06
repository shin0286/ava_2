package com.console;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fit.config.AbstractSetting;
import com.fit.config.Bound;
import com.fit.config.FunctionConfig;
import com.fit.config.IFunctionConfig;
import com.fit.config.ISettingv2;
import com.fit.config.Paths;
import com.fit.config.Settingv2;
import com.fit.gui.testreport.object.ITestedFunctionReport;
import com.fit.gui.testreport.object.ProjectReport;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdatagen.AbstractTestdataGeneration;
import com.fit.testdatagen.FastTestdataGeneration;
import com.fit.testdatagen.ITestdataGeneration;
import com.fit.testdatagen.MarsTestdataGeneration;
import com.fit.testdatagen.MarsTestdataGeneration2;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.IProjectNode;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;

/**
 * Created by ducanhnguyen on 5/26/2017.
 */
public class ConsoleInput {
	public static final int STATEMENT_COVERAGE = 0;
	public static final int BRANCH_COVERAGE = 1;
	public static final int UNSUPPORT_COVERAGE = -1;
	public static final int UNKNOWN_COVERAGE = -2;
	final static Logger logger = Logger.getLogger(ConsoleInput.class);
	protected File projectFile;
	protected File variableConfigurationFile;
	protected File testFunctionsFile;
	protected int coverage = UNKNOWN_COVERAGE;
	protected List<ConsoleOutput> output = new ArrayList<ConsoleOutput>();
	protected List<String> unreachCoverageMethods = new ArrayList<>();
	protected List<String> overCoverageMethods = new ArrayList<>();
	protected List<String> unreachNumTestpaths = new ArrayList<>();
	protected List<String> exceptionMethods = new ArrayList<>();

	public void findTestdata() throws Exception {
		ProjectReport.getInstance().removeAll();

		FunctionConfig functionConfig = loadVariablesConfiguration(variableConfigurationFile);

		File cloneProject = Utils.copy(projectFile.getCanonicalPath());
		Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH = cloneProject.getAbsolutePath();
		logger.info("Create a clone project done at " + projectFile.getCanonicalPath());

		IProjectNode root = new ProjectParser(cloneProject, null).getRootTree();

		for (String function : loadTestedFunctions(testFunctionsFile)) {
			logger.info("");
			logger.info("");
			logger.info("Function: " + function);
			List<INode> functionNodes = Search.searchNodes(root, new FunctionNodeCondition(), function);

			if (functionNodes.size() == 1) {
				logger.info("Founded only a function");
				IFunctionNode functionNode = (IFunctionNode) functionNodes.get(0);
				((FunctionNode) functionNode).setFunctionConfig(functionConfig);
				logger.info("Function: " + functionNode.getAST().getRawSignature());

				ConsoleOutput consoleOutput = generateTestdata((FunctionNode) functionNode);
				consoleOutput.setFunctionNode(functionNode);
				output.add(consoleOutput);

			} else if (functionNodes.size() == 0)
				logger.error("Function dont exist");

			else if (functionNodes.size() > 1)
				logger.error("Find many functions. Ignore.");
		}

		logger.debug("Delete the clone project");
		Utils.deleteFileOrFolder(cloneProject);
	}

	private ConsoleOutput generateTestdata(IFunctionNode function) {
		logger.debug("Start generating test data for the current function");

		ConsoleOutput consoleOutput = new ConsoleOutput();
		ProjectReport.getInstance().addFunction(function);
		ITestedFunctionReport fnReport = ProjectReport.getInstance().getSourcecodeFiles().get(0)
				.getTestedFunctionReports().get(0);

		try {
			AbstractTestdataGeneration mars = null;
			switch (Settingv2.getValue(ISettingv2.TESTDATA_STRATEGY)) {
			case ITestdataGeneration.TESTDATA_GENERATION_STRATEGIES.MARS + "":
				mars = new MarsTestdataGeneration(function, fnReport);
				break;
			case ITestdataGeneration.TESTDATA_GENERATION_STRATEGIES.MARS2 + "":
				mars = new MarsTestdataGeneration2(function, fnReport);
				break;
			case ITestdataGeneration.TESTDATA_GENERATION_STRATEGIES.FAST_MARS + "":
				mars = new FastTestdataGeneration(function, fnReport);
				break;

			default:
				throw new Exception("Wrong test data generation strategy");
			}

			if (mars != null) {
				mars.generateTestdata();

				consoleOutput.setRunningTime(mars.totalRunningTime);
				consoleOutput.setSolverRunningTime(mars.solverRunningTime);
				consoleOutput.setNormalizationTime(mars.normalizationTime);
				consoleOutput.setSymbolicExecutionTime(mars.symbolicExecutionTime);
				consoleOutput.setExecutionTime(mars.executionTime);
				consoleOutput.setMakeCommandRunningTime(mars.makeCommandRunningTime);
				consoleOutput.setMakeCommandRunningNumber(mars.makeCommandRunningNumber);
				consoleOutput.setNumOfSolverCalls(mars.numOfSolverCalls);
				consoleOutput.setNumOfSymbolicExecutions(mars.numOfSymbolicExecutions);
				consoleOutput.setNumOfSymbolicStatements(mars.numOfSymbolicStatements);
				consoleOutput.setNumOfExecutions(mars.numOfExecutions);
				consoleOutput.setNumOfNoChangeToCoverage(mars.numOfNotChangeToCoverage);
				consoleOutput.setNumOfSolverCallsbutCannotSolve(mars.numOfSolverCallsbutCannotSolve);
				consoleOutput.setMacroNormalizationTime(mars.macroNormalizationTime);
				consoleOutput.setCoverge(fnReport.computeCoverage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return consoleOutput;
	}

	public boolean checkVariablesConfiguration() throws Exception {
		if (projectFile == null)
			throw new Exception("Project folder is null");
		if (variableConfigurationFile == null)
			throw new Exception("Variable configuration file is null");
		if (testFunctionsFile == null)
			throw new Exception("Tested functions file is null");
		if (!variableConfigurationFile.exists())
			throw new Exception("Variable configuration file does not exist");
		if (!projectFile.exists())
			throw new Exception("Project file does not exist");
		if (!testFunctionsFile.exists())
			throw new Exception("Tested function file does not exist");

		if (!new File(AbstractSetting.getValue(ISettingv2.SOLVER_Z3_PATH)).exists()
				|| !new File(AbstractSetting.getValue(ISettingv2.MCPP_EXE_PATH)).exists()
				|| !new File(AbstractSetting.getValue(ISettingv2.GNU_GCC_PATH)).exists()
				|| !new File(AbstractSetting.getValue(ISettingv2.GNU_GPlusPlus_PATH)).exists())
			throw new Exception("Wrong path of compiler");

		// TODO: may need some checkings on variables configuration
		return true;
	}

	private FunctionConfig loadVariablesConfiguration(File config) throws IOException {
		logger.info("Load variables configuration from file at " + config.getCanonicalPath());
		FunctionConfig functionConfig = new FunctionConfig();
		functionConfig.setCharacterBound(
				new Bound(Utils.toInt(AbstractSetting.getValue(ISettingv2.DEFAULT_CHARACTER_LOWER_BOUND)),
						Utils.toInt(AbstractSetting.getValue(ISettingv2.DEFAULT_CHARACTER_UPPER_BOUND))));
		functionConfig
				.setIntegerBound(new Bound(Utils.toInt(AbstractSetting.getValue(ISettingv2.DEFAULT_NUMBER_LOWER_BOUND)),
						Utils.toInt(AbstractSetting.getValue(ISettingv2.DEFAULT_NUMBER_UPPER_BOUND))));
		functionConfig.setSizeOfArray(Utils.toInt(AbstractSetting.getValue(ISettingv2.DEFAULT_TEST_ARRAY_SIZE)));
		functionConfig.setMaximumInterationsForEachLoop(
				Utils.toInt(AbstractSetting.getValue(ISettingv2.MAX_ITERATION_FOR_EACH_LOOP)));
		functionConfig.setSolvingStrategy(AbstractSetting.getValue(ISettingv2.SELECTED_SOLVING_STRATEGY));

		String coverage = AbstractSetting.getValue(ISettingv2.COVERAGE);
		if (coverage.equals(ISettingv2.SUPPORT_COVERAGE_CRITERIA[0]))
			functionConfig.setTypeofCoverage(IFunctionConfig.BRANCH_COVERAGE);
		else if (coverage.equals(ISettingv2.SUPPORT_COVERAGE_CRITERIA[1]))
			functionConfig.setTypeofCoverage(IFunctionConfig.STATEMENT_COVERAGE);
		else if (coverage.equals(ISettingv2.SUPPORT_COVERAGE_CRITERIA[2]))
			functionConfig.setTypeofCoverage(IFunctionConfig.SUBCONDITION);

		// only for testing
		logger.info("Character bound: " + functionConfig.getCharacterBound().toString());
		logger.info("Integer bound: " + functionConfig.getIntegerBound().toString());
		logger.info("Max loop:" + functionConfig.getMaximumInterationsForEachLoop());
		logger.info("Max size of array:" + functionConfig.getSizeOfArray());
		if (functionConfig.getTypeofCoverage() == IFunctionConfig.BRANCH_COVERAGE)
			logger.info("Coverage: branch");
		else if (functionConfig.getTypeofCoverage() == IFunctionConfig.STATEMENT_COVERAGE)
			logger.info("Coverage: statement");
		else if (functionConfig.getTypeofCoverage() == IFunctionConfig.SUBCONDITION)
			logger.info("Coverage: sub-Condition");

		logger.info("Solving strategy: " + functionConfig.getSolvingStrategy());
		logger.info("Test data generation: " + Settingv2.getValue(ISettingv2.TESTDATA_STRATEGY));
		return functionConfig;
	}

	private String[] loadTestedFunctions(File fFunctions) throws Exception {
		logger.info("Load tested functions from file at " + fFunctions.getCanonicalPath());
		return Utils.readFileContent(fFunctions.getCanonicalPath()).replace("\r", "").split("\n");
	}

	public List<ConsoleOutput> getOutput() {
		return output;
	}

	protected class ExpectedOutput {

		int nTestPath;
		float reachCoverage;

		public ExpectedOutput(int nTestPath, float reachCoverage) {
			super();
			this.nTestPath = nTestPath;
			this.reachCoverage = reachCoverage;
		}

		public int getnTestPath() {
			return nTestPath;
		}

		public float getReachCoverage() {
			return reachCoverage;
		}
	}
}
