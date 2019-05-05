package com.fit;

import com.fit.callgraph.CallGraphGeneration;
import com.fit.callgraph.object.CallGraphNode;
import com.fit.callgraph.object.ICallGraphNode;
import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.cfg.ICFG;
import com.fit.cfg.ICFGGeneration;
import com.fit.cfg.object.*;
import com.fit.cfg.testpath.*;
import com.fit.config.Bound;
import com.fit.config.FunctionConfig;
import com.fit.config.ISettingv2;
import com.fit.externalvariable.ReducedExternalVariableDetecter;
import com.fit.gui.main.GUIController;
import com.fit.normalizer.FunctionNormalizer;
import com.fit.normalizer.UnaryNormalizer;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdatagen.loop.PossibleTestpathGenerationForLoop;
import com.fit.testdatagen.se.*;
import com.fit.testdatagen.se.memory.ISymbolicVariable;
import com.fit.testdatagen.se.memory.VariableNodeTable;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.IVariableNode;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.apache.log4j.Logger;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTFunctionDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

import static com.fit.testdatagen.se.ISymbolicExecution.*;

public class SummaryGeneration {
    final static Logger logger = Logger.getLogger(SummaryGeneration.class);

    protected IFunctionNode functionNode;

    /**
     * Represent a test path generated from control flow graph
     */
    protected ITestpath testpath = null;

    /**
     * The variable passing to the function
     */
    protected Parameter paramaters = null;

    /**
     * Table of variables
     */
    protected VariableNodeTable tableMapping = new VariableNodeTable();

    /**
     * Store path constraints generated by performing symbolic execution the given
     * test path
     */

    /**
     * The return value of a test path, specified by "return ..."
     */
    private List<ICfgNode> listCfgNode;

    private HashMap<String,Integer> cfgMap = new HashMap<>();

//    public String projectPath = GUIController.projectPath;

    //    public String externalParameterFile = GUIController.projectPath + "_exeternVar.xml";
    public String externalParameterFile =  "F:\\New folder\\ava_ver2\\data-test\\tsdv\\Sample_for_R1_2\\" + "Sample_for_R1_2_exeternVar.xml";

//    public String summaryFile = summary.xml";

    ICallGraphNode functionCallNode = new CallGraphNode();

    public String functionInTest = "";

    public static boolean inSummaryGenProcess = true;

    public SummaryGeneration (String summaryFile, String projectPath, String functionInTest) throws Exception{
        this.functionInTest = functionInTest;
        summaryGenerationForFunctionInGraph(projectPath, summaryFile);
        inSummaryGenProcess = false;

    }

    public IFunctionNode getFunctionNode(String projectPath, String functionName) throws Exception {
        ProjectParser parser = new ProjectParser(new File(projectPath));
        parser.isMakeFileCreated = true;
        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), functionName).get(0);
//        System.out.println(function.getAST().getRawSignature());
//        logger.debug(function.getAST().getRawSignature());

        FunctionConfig functionConfig = new FunctionConfig();
        functionConfig.setCharacterBound(new Bound(30, 120));
        functionConfig.setIntegerBound(new Bound(10, 200));
        functionConfig.setSizeOfArray(5);
        functionConfig.setMaximumInterationsForEachLoop(1);
        functionConfig.setSolvingStrategy(ISettingv2.SUPPORT_SOLVING_STRATEGIES[0]);
        function.setFunctionConfig(functionConfig);

        // Normalize function
        FunctionNormalizer fnNormalizer = function.normalizedASTtoInstrument();
//        String [] projectPathComponent = projectPath.split("\\");
//        String projectName = projectPathComponent[projectPathComponent.length-1];
//        summaryFile = projectPath + projectName + "_summary";
//        externalParameterFile = projectPath + projectName + "_externVariable.xml"

        String newFunctionInStr = fnNormalizer.getNormalizedSourcecode();
        ICPPASTFunctionDefinition newAST = Utils.getFunctionsinAST(newFunctionInStr.toCharArray()).get(0);
        ((FunctionNode) function).setAST(newAST);
        return function;
    }

    public static void main(String[] args) throws Exception {
//        SummaryGeneration sg = new SummaryGeneration("F:\\New folder\\Sample_for_R1_2.xml","F:\\New folder\\ava_ver2\\data-test\\tsdv\\Sample_for_R1_2\\", "main()");
        SummaryGeneration sg = new SummaryGeneration("F:\\New folder\\Sample_for_R1_2.xml","F:\\New folder\\ava_ver2\\data-test\\tsdv\\Sample_for_R1_2\\", "mmin3(int,int,int)");
    }

    public ArrayList<String> getListFunctionWithSummary(String summaryFile) throws Exception{
        ArrayList<String> functionName = new ArrayList<>();
        List<String> sumList = readSummaryFile(summaryFile);
        for (String sum : sumList){
            String temp[] = sum.split("/");
            functionName.add(temp[0]);
        }
        return functionName;
    }

    public List<String> readSummaryFile (String filepath) throws Exception {
        File xmlFile = new File(filepath);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFile);

        document.getDocumentElement().normalize();
        ArrayList<String> summary = new ArrayList<>();
        NodeList sumList = document.getElementsByTagName("function");
        for (int i=0; i<sumList.getLength(); i++){
            Node sumNode = sumList.item(i);
            if (sumNode.getNodeType() == Node.ELEMENT_NODE){
                Element sumElement = (Element) sumNode;
                String sum = sumElement.getElementsByTagName("name").item(0).getTextContent() + "/" + sumElement.getElementsByTagName("full-summary").item(0).getTextContent()
                        + "/" + sumElement.getElementsByTagName("short-summary").item(0).getTextContent(); ;
                summary.add(sum);
            }
        }
        return summary;
    }


    public LinkedList<ICallGraphNode> getNoSummaryFunction(String functionName, String summaryFile, String projectPath) throws Exception{
        CallGraphGeneration callGraphGeneration = new CallGraphGeneration();
        ICallGraphNode mainNode = ((CallGraphGeneration) callGraphGeneration).getRootCallGraph(callGraphGeneration, projectPath);
        functionCallNode = ((CallGraphGeneration) callGraphGeneration).getFunctionCallGraph(mainNode, functionName);
        List<String> sumList = getListFunctionWithSummary(summaryFile);
        LinkedList<ICallGraphNode> listNoSummaryFunction = new LinkedList<>();
        Queue<ICallGraphNode> nodeQueue = new LinkedList<ICallGraphNode>();
        ((LinkedList<ICallGraphNode>) nodeQueue).add(functionCallNode);
        while (!nodeQueue.isEmpty()){
            ICallGraphNode firstNode = nodeQueue.remove();
            for (ICallGraphNode node : firstNode.getListTarget()) {
                node.setName(node.getName().replace(", ", ","));
                if (!sumList.contains(node.getName())){
                    listNoSummaryFunction.add(node);
                    ((LinkedList<ICallGraphNode>) nodeQueue).add(node);
                }
            }
        }
        return listNoSummaryFunction;
    }

    public void summaryGenerationForFunctionInGraph(String projectPath, String summaryFile) {
        try {
            functionInTest = functionInTest.replace(", ", ",");
            LinkedList<ICallGraphNode> noSummaryFunction = getNoSummaryFunction(functionInTest, summaryFile, projectPath);
            while (!noSummaryFunction.isEmpty()) {
                ICallGraphNode inSummaryGeneration = noSummaryFunction.removeLast();
                String functionName = inSummaryGeneration.getName();
                functionName = functionName.replace(", ", ",");
                IFunctionNode inSumGenerationFunction = getFunctionNode(projectPath, functionName);
                writeSummaryToFile(summaryFile, inSumGenerationFunction);
            }
        } catch (Exception e) {

        }
    }

    public void writeSummaryToFile(String sumFilepath, IFunctionNode function) throws Exception{
        ArrayList<String> summary = new ArrayList<>();
        String returnType = function.getAST().getChildren()[0].getRawSignature();
        if (returnType.equals("void")) {
            String instanceParameter = "";
            for (INode n : ((FunctionNode) function).getReducedExternalVariables())
                instanceParameter = instanceParameter + n.getName() + ",";
            writeToXml(sumFilepath, function.getName(), instanceParameter, function.getAST().getChildren()[0].getRawSignature(), "no");
        }
        else {
            summary = generateSummaryOfAllSimpleTestpath(function);
            writeToXml(sumFilepath, function.getName(), summary.get(0), summary.get(1), summary.get(2));
            System.out.println(function.getName() + "\tfull sum " + summary.get(0));
        }
    }


    public void writeToXml(String summaryFile, String functionName, String parameter, String fullSummary, String shortSummary) throws Exception{
        File file = new File(summaryFile);
        Document doc = null;
        Element root = null;
        if (!file.exists()) {
            doc = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().newDocument();
            root = doc.createElement("project-summaries");
            doc.appendChild(root);
        } else {
            doc = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().parse(file);
            root = doc.getDocumentElement();
        }
        Element function = doc.createElement("function");
        root.appendChild(function);

        Element name = doc.createElement("name");
        name.appendChild(doc.createTextNode(functionName));
        function.appendChild(name);

        Element param = doc.createElement("parameter");
        param.appendChild(doc.createTextNode(parameter));
        function.appendChild(param);

        Element fullsum = doc.createElement("full-summary");
        fullsum.appendChild(doc.createTextNode(fullSummary));
        function.appendChild(fullsum);

        Element shortSum = doc.createElement("short-summary");
        shortSum.appendChild(doc.createTextNode(shortSummary));
        function.appendChild(shortSum);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    ArrayList<String> generateSummaryOfAllSimpleTestpath (IFunctionNode function) throws Exception{
        ArrayList<String> summary = new ArrayList<>();
        String fullSum = "";
        String shortSum = "";
        String instanceParameter = "";

        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(
                new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES)
                        .generateCFG(),
                function.getFunctionConfig().getMaximumInterationsForEachLoop());
        tpGen.generateTestpaths();
        FullTestpaths testpath = tpGen.getPossibleTestpaths();

        Parameter paramaters = new Parameter();
        for (INode n : ((FunctionNode) function).getArguments()) {
            instanceParameter = instanceParameter + n.getName() + ",";
            paramaters.add(n);
        }
        for (INode n : ((FunctionNode) function).getReducedExternalVariables())
            paramaters.add(n);

        List<IVariableNode> externalVariables = function.getReducedExternalVariables();

        ArrayList<String> externParam = new ArrayList<>();

        int numOfPath = testpath.size();
        for (int i=0; i<numOfPath; i++) {
            IFullTestpath randomTestpath = testpath.get(i);
            ArrayList<String> constraintList = generateSummaryOfATestpath(randomTestpath, paramaters, function);
            int numOfConstraint = constraintList.size();
            if (i < numOfPath-1 && constraintList.get(numOfConstraint-1).contains("return")) {
                String postCond = constraintList.get(numOfConstraint-1);
                postCond = postCond.replace("return", "");
                postCond = postCond.replace(";", "");
                if (i == 0){
                    shortSum = postCond;
                }
                String pathSum = constraintList.get(numOfConstraint-2) + "?" + postCond + ":";
                fullSum += pathSum;
            }
        }
        ArrayList<String> constraintList = generateSummaryOfATestpath(testpath.get(numOfPath-1), paramaters, function);
        String postCond = constraintList.get(constraintList.size()-1);
        postCond = postCond.replace("return", "");
        postCond = postCond.replace(";", "");
        fullSum = fullSum + postCond;
        summary.add(instanceParameter);
        summary.add(fullSum);
        summary.add(shortSum);
        return summary;
    }

    private void replaceParameter(String parameter, String localValue){
        boolean isDef = false;
        for (int j=0; j<listCfgNode.size(); j++){
            ICfgNode cfgNode = listCfgNode.get(j);
            String node = cfgNode.getContent();
            if (node.contains(parameter + " ")){
                int statementType = cfgMap.get(node);
                if ((cfgMap.get(node) == BINARY_ASSIGNMENT || cfgMap.get(node) == DECLARATION)
                        && node.indexOf(parameter) < node.indexOf('=')){
                    isDef = true;
                    localValue = (node.split("="))[1];
                    localValue = localValue.replace(" ", "");
                } else if (cfgMap.get(node) == CONDITION && isDef == true){
                    node = node.replace(parameter, localValue);
                    cfgMap.put(node,statementType);
                    listCfgNode.get(j).setContent(node);
                } else if (cfgMap.get(node) == RETURN){
                    String returnValue = node.split(" ")[1];
                    node = node.replace(returnValue, localValue);
                    cfgMap.put(node,statementType);
                    listCfgNode.get(j).setContent(node);
                }
            }
        }
    }

    public void replaceUsedParameter(ArrayList<String> localParam, ArrayList<String> instanceParam) {
        String localValue = "";
        for (String instance : instanceParam){
            replaceParameter(instance, localValue);
        }

        for (String local : localParam){
            replaceParameter(local, localValue);
        }
    }

    void replaceExternalVariable(IFunctionNode function, List<IVariableNode> externalVariables) throws Exception{
        HashMap<String, String> externalVarMap = new HashMap<>();
        for (INode node : externalVariables){
            externalVarMap.put(node.getName(), node.getAbsolutePath());
        }
        File xmlFile = new File(externalParameterFile);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFile);

        document.getDocumentElement().normalize();
        NodeList varList = document.getElementsByTagName("extern-var");
        for (int i=0; i<varList.getLength(); i++) {
            Node varNode = varList.item(i);
            if (varNode.getNodeType() == Node.ELEMENT_NODE) {
                Element varElement = (Element) varNode;
                String varName = varElement.getElementsByTagName("name").item(0).getTextContent();
                String varPath = varElement.getElementsByTagName("location").item(0).getTextContent();
                if (externalVarMap.containsKey(varName) && externalVarMap.get(varName).equals(varPath)){
                    String externValue = ((varElement.getElementsByTagName("value").item(0).getTextContent()).split(","))[0];
                    replaceParameter(varName, externValue);
                }
            }
        }
    }

    public ArrayList<String> generateSummaryOfATestpath (IFullTestpath testpath, Parameter paramaters, IFunctionNode function) throws Exception {

        ArrayList<String> preCondition = new ArrayList<>();

        ArrayList<String> localParam = new ArrayList<>();

        ArrayList<String> instanceParam = new ArrayList<>();

        List<IVariableNode> externalVariables = function.getReducedExternalVariables();

        ArrayList<String> externParam = new ArrayList<>();

        for (IVariableNode extern : externalVariables) {
            externParam.add(extern.getName());
        }

        INormalizedTestpath normalizedTestpath = null;
        if (testpath instanceof INormalizedTestpath)
            normalizedTestpath = (INormalizedTestpath) testpath;
        else {
            UnaryNormalizer tpNormalizer = new UnaryNormalizer();
            tpNormalizer.setOriginalTestpath(testpath);
            tpNormalizer.normalize();
            normalizedTestpath = tpNormalizer.getNormalizedTestpath();
        }

        listCfgNode = normalizedTestpath.getAllCfgNodes();

        SymbolicExecution se = new SymbolicExecution(testpath, paramaters, function);

        String postCondition = "";

        for (ICfgNode cfgNode : listCfgNode) {
            if (!se.isAlwaysFalse())
                if (cfgNode instanceof BeginFlagCfgNode || cfgNode instanceof EndFlagCfgNode) {
                    // nothing to do
                } else
                    try {
                        if (cfgNode instanceof NormalCfgNode) {
                            IASTNode ast = ((NormalCfgNode) cfgNode).getAst();
                            int statementType = se.getStatementType(ast);
                            cfgMap.put(ast.getRawSignature(), statementType);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
        }
        VariableNodeTable tableMapping = (VariableNodeTable) se.getTableMapping();
        for (int i = 0; i < tableMapping.size(); i++) {
            ISymbolicVariable symbolicVariable = tableMapping.getVariables().get(i);
            if (symbolicVariable.getScopeLevel() == 2 && externParam.contains(symbolicVariable.getName()) == false) {
                localParam.add(symbolicVariable.getName());
            } else if (symbolicVariable.getScopeLevel() == 0){
                instanceParam.add(symbolicVariable.getName());
            }

        }
        if (!se.isAlwaysFalse()) {
            replaceUsedParameter(localParam, instanceParam);
            if (externParam.size() != 0) {
                replaceExternalVariable(function, externalVariables);
            }
            for (ICfgNode cfgNode : listCfgNode) {
                if (cfgNode instanceof BeginFlagCfgNode || cfgNode instanceof EndFlagCfgNode) {
                    // nothing to do
                } else
                    try {
                        if (cfgNode instanceof NormalCfgNode) {
                            String node = cfgNode.getContent();
                            if (cfgMap.get(node) != CONDITION) {
                                if (cfgMap.get(node) == RETURN) {
                                    postCondition = node;
                                }
                            } else {
                                if (cfgMap.get(node) == CONDITION)
                                    preCondition.add(node);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
            }

            String _preCond = "(" + preCondition.get(0);
            for (int i = 1; i < preCondition.size(); i++) {
                _preCond = _preCond + " && " + preCondition.get(i);
            }
            _preCond = _preCond + ")";
            preCondition.add(_preCond);
            preCondition.add(postCondition);
        }
        return preCondition;
    }
}

