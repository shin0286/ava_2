package com.fit.config;

/**
 * The list of paths (in samples), etc.
 *
 * @author DucAnh
 */
public class Paths {

	public static boolean START_FROM_COMMANDLINE = false;
	public static String DATA_GEN_TEST = "..\\ava\\data-test\\ducanh\\DataGenTest\\";
	public static String VARIABLE_NODE_TEST = "..\\ava\\data-test\\ducanh\\VariableNodeTest\\";
	public static String CPP_AND_HEADER_PARSER_TEST = "..\\ava\\data-test\\ducanh\\CPPandHeaderParserTest\\";
	public static String FUNCTION_NODE_NAME_TEST = "..\\ava\\data-test\\ducanh\\FunctionNodeNameTest\\";
	public static String GET_NAME_OF_EXE_IN_DEVCPP_MAKEFILE = "..\\ava\\data-test\\ducanh\\GetNameOfExeInDevCppMakeFile\\";
	public static String RANDOM_GENERATION_TEST2 = "..\\ava\\data-test\\ducanh\\RandomGenerationTest2";
	public static String TYPEDEF_NODE_NAME_TEST = "..\\ava\\data-test\\ducanh\\TypedefNodeNameTest";
	public static String TYPE_DEPENDENCY_GENERATION_TEST = "..\\ava\\data-test\\ducanh\\TypeDependencyGenerationTest\\";
	public static String TEST_PATH_GENERATION_TEST = "..\\ava\\data-test\\ducanh\\TestpathGenerationTest\\";
	public static String SYMBOLIC_EXECUTION_TEST = "..\\ava\\data-test\\ducanh\\SymbolicExecutionTest\\";
	public static String COMBINED_STATIC_AND_DYNAMIC_GENERATION = "..\\ava_ver2\\data-test\\ducanh\\CombinedStaticAndDynamicGen\\";
	public static String STATEMENT_COVERAGE_COMPUTATION_TEST = "..\\ava\\data-test\\ducanh\\StatementCoverageComputationTest\\";
	public static String FUNCTION_TRANSFORMER_TEST = "..\\ava\\data-test\\ducanh\\FunctionTransformerTest\\";
	public static String SEPARATE_FUNCTION_TEST = "..\\ava\\data-test\\ducanh\\SeparateFunctionTest";
	public static String INCLUDE_DEPENDENCY_GENERATION_TEST = "..\\ava\\data-test\\ducanh\\IncludeHeaderDependencyGeneration";
	public static String EXPECTED_OUTPUT_PANELV2_TEST = "..\\ava\\data-test\\ducanh\\ExpectedOutputPanelv2Test";
	public static String GTEST_LIB = "..\\ava\\data-test\\nartoan\\lib\\GoogleTest";
	public static String GTEST_FOR_CHECK = "..\\ava\\data-test\\nartoan\\GTestForCheck\\";
	public static String RUN_AND_EXPORT_RESULTS = "..\\ava\\local";
	public static String TSDV_LOG4CPP = "..\\ava\\data-test\\ducanh\\TSDV_log4cpp";
	public static String TSDV_TMATH = "..\\ava\\data-test\\ducanh\\TSDV_ttmath-0.9.3";
	public static String TYPE_SIMPLE = "..\\ava\\data-test\\ducanh\\TypeDependencyGenerationTest\\Eclipse_Type Simple";
	public static String BTL = "..\\ava\\data-test\\ducanh\\BTL\\";
	public static String SIMBOLIC_EXECUTION_VS = "..\\ava\\data-test\\nartoan\\VisualStudioSolution\\";
	public static String SUB_CONDITION = "..\\ava\\data-test\\nartoan\\SubConditionTest\\";
	public static String CFG_GENERATION_SOURCECODE = "..\\ava\\data-test\\ducanh\\CFGGeneration\\";
	public static String SHIP_GAME = "..\\ava\\data-test\\ducanh\\Ship Destroyer Game1";
	public static String MAKEFILE_SAMPLES = "..\\ava\\data-test\\ducanh\\makefile samples";
	public static String SAMPLE01 = "..\\ava\\data-test\\ducanh\\Sample1\\";
	public static String SAMPLE02 = "..\\ava\\data-test\\ducanh\\Sample2\\";
	public static String SAMPLE03 = "..\\ava\\data-test\\ducanh\\Sample3\\";
	public static String SWT = "..\\ava\\data-test\\nartoan\\SwitchTestCase\\";
	public static String RANDOM_GENERATION = "..\\ava\\data-test\\ducanh\\RandomGenerationTest2";
	public static String TREE_EXPRESSION_GENERATION_TEST = "..\\ava\\data-test\\ducanh\\TreeExpressionGenerationTest\\";
	public static String SAMPLE_CODE = "..\\ava\\data-test\\ducanh\\SampleCode\\";
	public static String ASTYLE = "..\\ava\\lib\\AStyle.exe";
	public static String TSDV_R1 = "..\\ava\\data-test\\tsdv\\Sample_for_R1\\";
	public static String TSDV_R1_10 = "..\\ava\\data-test\\tsdv\\Sample_for_R1_10file\\";
	public static String TSDV_R1_2 = "..\\ava\\data-test\\tsdv\\Sample_for_R1_2\\";
	public static String TSDV_R1_3 = "..\\ava\\data-test\\tsdv\\Sample_for_R1_3_Cpp11\\";
	public static String TSDV_R1_4 = "..\\ava\\data-test\\tsdv\\Sample_for_R1_4\\";
	public static String CORE_UTILS = "..\\ava\\data-test\\ducanh\\coreutils-8.24";
	public static String RETURN_ENUM = "..\\ava\\data-test\\tsdv\\SampleSource-2017A\\return_enum\\";
	public static String GLOBAL_VARIABLE = "..\\ava\\data-test\\tsdv\\SampleSource-2017A\\global_variable\\";

	public static class CURRENT_FUNCTION_CONFIGURATION {

		public static int SIZE_OF_ARRAY = 10; // default
		public static int MAX_ITERATION_FOR_EACH_LOOP = 1; // default
		public static int LENGTH_OF_LINKED_LIST = 1; // default

		public static class BOUND_PARAMETERS {

			public static class CHARACTER_TYPE {

				public static int LOWER = 0; // default
				public static int UPPER = 126; // default
			}

			public static class INTEGER_TYPE {

				public static int LOWER = -100; // default
				public static int UPPER = 100; // default
			}
		}
	}

	public static class CURRENT_PROJECT {

		/**
		 * The path of selected project
		 */
		public static String ORIGINAL_PROJECT_PATH = "";

		/**
		 * The path of cloned project
		 */
		public static String CLONE_PROJECT_NAME = "cloneProject";
		public static String CLONE_PROJECT_PATH = "";

		/**
		 * Type of project
		 */
		public static int TYPE_OF_PROJECT;

		/**
		 * The path of makefile
		 */
		public static String MAKEFILE_PATH = "";

		/**
		 * The path of .exe after the project is compiled
		 */
		public static String EXE_PATH = "";

		/**
		 * The name of file that contains test path after the project is executed
		 */
		public static String TESTDRIVER_EXECUTION_NAME_POSTFIX = "testdriver_execution.txt";

		public static String CURRENT_TESTDRIVER_EXECUTION_NAME = "";

		public static String TESTDATA_INPUT_FILE_NAME = "input.txt";
		public static String TESTDATA_INPUT_FILE_PATH = "";

		public static String CURRENT_TESTDRIVER_EXECUTION_PATH = "";

		public static String LOCAL_FOLDER = "./local";

		public static void reset() {
			CURRENT_PROJECT.TESTDRIVER_EXECUTION_NAME_POSTFIX = CURRENT_PROJECT.CLONE_PROJECT_PATH = CURRENT_PROJECT.MAKEFILE_PATH = CURRENT_PROJECT.EXE_PATH = "";
			TESTDATA_INPUT_FILE_NAME = TESTDATA_INPUT_FILE_PATH = CURRENT_TESTDRIVER_EXECUTION_PATH = "";
		}
	}
}
