//package com.fit.normalizer;
//
//import com.fit.SummaryGeneration;
//import com.fit.callgraph.CallGraphGeneration;
//import com.fit.callgraph.ICallGraphGeneration;
//import com.fit.callgraph.object.CallGraphNode;
//import com.fit.callgraph.object.ICallGraphNode;
//import com.fit.gui.main.GUIController;
//import com.fit.parser.projectparser.ProjectParser;
//import com.fit.tree.object.IFunctionNode;
//import com.fit.utils.Utils;
//import com.fit.utils.search.FunctionNodeCondition;
//import com.fit.utils.search.Search;
//import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTFunctionCallExpression;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//
//public class FunctionCallNormalizer extends AbstractFunctionNormalizer implements IFunctionNormalizer{
//    String filePath = "F:\\New folder\\Sample_for_R1_2.xml";
//    String projectPath = "F:\\New folder\\ava_ver2\\data-test\\tsdv\\Sample_for_R1_2\\";
//    public String functionName = "main()";
//    String inTestGenerationFunction = functionName;
//    ICallGraphNode functionCallNode = new CallGraphNode();
//
//    public FunctionCallNormalizer (){
//    }
//
//    public FunctionCallNormalizer (IFunctionNode functionNode){
//        this.functionNode = functionNode;
//        summaryGenerationForFunctionInGraph();
//    }
//
//    public static void main(String[] args) throws Exception{
//        ProjectParser parser = new ProjectParser(new File("F:\\New folder\\ava_ver2\\data-test\\tsdv\\Sample_for_R1_2\\"));
//
//        IFunctionNode function = (IFunctionNode) Search
//                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "main()")
//                .get(0);
//
////        System.out.println(((IFunctionNode) function).getAST().getRawSignature());
//        FunctionCallNormalizer norm = new FunctionCallNormalizer(function);
////        System.out.println(norm.functionCallNode.getName());
//        norm.normalize();
//        System.out.println(norm.functionNode.getNormalizeFunctionToExecute().normalizeSourcecode);
//    }
//    @Override
//    public void normalize(){
//        try {
//            normalizeSourcecode = functionNode.getAST().getRawSignature();
//            List<ICPPASTFunctionCallExpression> functionCallASTs = Utils.getFunctionCallExpression(functionNode.getAST());
//            for (ICPPASTFunctionCallExpression functionCall : functionCallASTs){
//                String functionName = functionCall.getFunctionNameExpression().getRawSignature() + "(";
//                normalizeSourcecode = replaceFunctionCallWithSummary(functionCall.getRawSignature(), functionName);
//            }
//        } catch (Exception e) {
//            System.out.println("Loi");
//        }
//    }
//
//    public LinkedList<ICallGraphNode> getNoSummaryFunction(String functionName) throws Exception{
//        CallGraphGeneration callGraphGeneration = new CallGraphGeneration();
//        ICallGraphNode mainNode = ((CallGraphGeneration) callGraphGeneration).getRootCallGraph(callGraphGeneration, projectPath);
//        functionCallNode = ((CallGraphGeneration) callGraphGeneration).getFunctionCallGraph(mainNode, functionName);
//        List<String> sumList = getListFunctionWithSummary();
//        System.out.println(sumList);
//        LinkedList<ICallGraphNode> listNoSummaryFunction = new LinkedList<>();
//        Queue<ICallGraphNode> nodeQueue = new LinkedList<ICallGraphNode>();
//        ((LinkedList<ICallGraphNode>) nodeQueue).add(functionCallNode);
//        while (!nodeQueue.isEmpty()){
//            ICallGraphNode firstNode = nodeQueue.remove();
//            for (ICallGraphNode node : firstNode.getListTarget()) {
//                node.setName(node.getName().replace(", ", ","));
//                if (!sumList.contains(node.getName())){
//                    listNoSummaryFunction.add(node);
//                    ((LinkedList<ICallGraphNode>) nodeQueue).add(node);
//                }
//            }
//        }
//        return listNoSummaryFunction;
//    }
//
//    public void summaryGenerationForFunctionInGraph(){
//        String functionName = this.functionNode.getName();
//        functionName = functionName.replace(",", ", ");
//        try {
//            LinkedList<ICallGraphNode> noSummaryFunction = getNoSummaryFunction(functionName);
//            while (!noSummaryFunction.isEmpty()){
//                ICallGraphNode inSummaryGeneration = noSummaryFunction.removeLast();
//                SummaryGeneration summaryGeneration = new SummaryGeneration(filePath, projectPath, inSummaryGeneration.getName());
//                System.out.println(summaryGeneration.externalParameterFile);
//            }
//        } catch (Exception e){
//
//        }
//
//    }
//    public String replaceFunctionCallWithSummary (String functionCall, String functionName) throws Exception{
//        List<String> sumList = readSummaryFile(filePath);
//        String inGenTestProcess = this.functionNode.getName();
//        String summary="";
//        for (String sum : sumList){
//            if (sum.startsWith(functionName)){
//                String temp[] = sum.split("/");
//                if (!inGenTestProcess.equals(inTestGenerationFunction)){
//                    summary = temp[2];
//                } else {
//                    summary = temp[1];
//                }
//                summary = summary.replace("~", "<");
//                normalizeSourcecode = normalizeSourcecode.replace(functionCall,summary);
//            }
//        }
//        return normalizeSourcecode;
//    }
//
//    public ArrayList<String> getListFunctionWithSummary() throws Exception{
//        ArrayList<String> functionName = new ArrayList<>();
//        List<String> sumList = readSummaryFile(filePath);
//        for (String sum : sumList){
//            String temp[] = sum.split("/");
//            functionName.add(temp[0]);
//        }
//        return functionName;
//    }
//
//    public List<String> readSummaryFile (String filepath) throws Exception {
//        File xmlFile = new File(filepath);
//        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
//        Document document = documentBuilder.parse(xmlFile);
//
//        document.getDocumentElement().normalize();
//        ArrayList<String> summary = new ArrayList<>();
//        NodeList sumList = document.getElementsByTagName("function");
//        for (int i=0; i<sumList.getLength(); i++){
//            Node sumNode = sumList.item(i);
//            if (sumNode.getNodeType() == Node.ELEMENT_NODE){
//                Element sumElement = (Element) sumNode;
//                String sum = sumElement.getElementsByTagName("name").item(0).getTextContent() + "/" + sumElement.getElementsByTagName("full-summary").item(0).getTextContent()
//                        + "/" + sumElement.getElementsByTagName("short-summary").item(0).getTextContent(); ;
//                summary.add(sum);
//            }
//        }
//        return summary;
//    }
//}

package com.fit.normalizer;

import com.fit.SummaryGeneration;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTFunctionCallExpression;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FunctionCallNormalizer extends AbstractFunctionNormalizer implements IFunctionNormalizer{
    String filePath = "F:\\New folder\\Sample_for_R1_2.xml";

    public FunctionCallNormalizer (){

    }

    public FunctionCallNormalizer (IFunctionNode functionNode) throws Exception{
        this.functionNode = functionNode;
    }

    public static void main(String[] args) throws Exception{
        ProjectParser parser = new ProjectParser(new File("F:\\New folder\\ava\\data-test\\tsdv\\Sample_for_R1_2\\"));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "mmin3(int,int,int)")
                .get(0);

//        System.out.println(((IFunctionNode) function).getAST().getRawSignature());
        FunctionCallNormalizer norm = new FunctionCallNormalizer(function);
        norm.normalize();
        System.out.println(norm.getNormalizedSourcecode());
    }
    @Override
    public void normalize(){
        try {
            normalizeSourcecode = functionNode.getAST().getRawSignature();
            List<ICPPASTFunctionCallExpression> functionCallASTs = Utils.getFunctionCallExpression(functionNode.getAST());
            for (ICPPASTFunctionCallExpression functionCall : functionCallASTs){
                String functionName = functionCall.getFunctionNameExpression().getRawSignature() + "(";
                System.out.println(functionName);
                normalizeSourcecode = replaceFunctionCallWithSummary(functionCall.getRawSignature(), functionName);
            }
        } catch (Exception e) {
            System.out.println("Loi");
        }
    }

    public String replaceFunctionCallWithSummary (String functionCall, String functionName) throws Exception{
        List<String> sumList = readSummaryFile(filePath);
        System.out.println(sumList);
        String summary="";
        for (String sum : sumList){
            if (sum.startsWith(functionName)){
                String temp[] = sum.split("/");
                if (!this.functionNode.getName().equals("main()")){
                    summary = temp[2];
                } else {
                    summary = temp[1];
                }
                normalizeSourcecode = normalizeSourcecode.replace(functionCall,summary);
            }
        }
        return normalizeSourcecode;
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
}