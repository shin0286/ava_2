package com.fit.normalizer;

import com.fit.SummaryGeneration;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.eclipse.cdt.core.dom.ast.IASTNode;
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
    String functionInTest = "mmin3(int, int, int)";
    String functionInExecute = "";

    public FunctionCallNormalizer () throws Exception{
//        functionInTest = functionInTest.replace(", ", ",");
//        if (functionInExecute.equals(functionInTest)){
//            SummaryGeneration summaryGeneration = new SummaryGeneration(filePath, projectPath, functionInTest);
//        }
    }

    public FunctionCallNormalizer (IFunctionNode functionNode) throws Exception{
        this.functionNode = functionNode;
    }

    public static void main(String[] args) throws Exception{
        ProjectParser parser = new ProjectParser(new File("F:\\New folder\\ava_ver2\\data-test\\tsdv\\Sample_for_R1_2\\"));

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
            ArrayList<String> listParameter = new ArrayList<>();
            normalizeSourcecode = functionNode.getAST().getRawSignature();
            List<ICPPASTFunctionCallExpression> functionCallASTs = Utils.getFunctionCallExpression(functionNode.getAST());
            for (ICPPASTFunctionCallExpression functionCall : functionCallASTs){
                IASTNode [] functionDef = functionCall.getChildren();
                for (int i = 1; i < functionDef.length; i++){
                    listParameter.add(functionDef[i].getRawSignature());
                }
                String functionName = functionCall.getFunctionNameExpression().getRawSignature() + "(";
                System.out.println(functionName);
                normalizeSourcecode = replaceFunctionCallWithSummary(functionCall.getRawSignature(), functionName, listParameter);
            }
        } catch (Exception e) {
            System.out.println("Loi");
        }
    }

    public String replaceFunctionCallWithSummary (String functionCall, String functionName, ArrayList<String> parameter) throws Exception{
        List<String> sumList = readSummaryFile(filePath);
        System.out.println(sumList);
        String oldParameter = "";
        String summary="";
        functionInTest = functionInTest.replace(", ", ",");
        functionInExecute = functionNode.getName();
        for (String sum : sumList){
            if (sum.startsWith(functionName)){
                String sumArr[] = sum.split("/");
                oldParameter = sumArr[1];
                if (!this.functionNode.getName().equals(functionInTest) && !sumArr[2].equals("no")){
                    summary = sumArr[3];
                } else {
                    summary = sumArr[2];
                }
            }
        }
        String [] oldParam = oldParameter.split(",");
        for (int i = 0; i < parameter.size(); i++){
            summary = summary.replace(oldParam[i], parameter.get(i));
        }

        normalizeSourcecode = normalizeSourcecode.replace(functionCall,summary);
        System.out.println(normalizeSourcecode);
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
                String sum = sumElement.getElementsByTagName("name").item(0).getTextContent() + "/"
                        + sumElement.getElementsByTagName("parameter").item(0).getTextContent() + "/"
                        + sumElement.getElementsByTagName("full-summary").item(0).getTextContent() + "/"
                        + sumElement.getElementsByTagName("short-summary").item(0).getTextContent();
                summary.add(sum);
            }
        }
        return summary;
    }
}