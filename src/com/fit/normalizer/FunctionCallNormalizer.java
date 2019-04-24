package com.fit.normalizer;

import com.fit.callgraph.CallGraphGeneration;
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
    String filePath;

    public FunctionCallNormalizer (){
    }

    public FunctionCallNormalizer (IFunctionNode functionNode){ this.functionNode = functionNode; }

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
            normalizeSourcecode = functionNode.getAST().getRawSignature();
            List<ICPPASTFunctionCallExpression> functionCallASTs = Utils.getFunctionCallExpression(functionNode.getAST());
            for (ICPPASTFunctionCallExpression functionCall : functionCallASTs){
                String functionName = functionCall.getFunctionNameExpression().getRawSignature();
                normalizeSourcecode = replaceFunctionCallWithSummary(functionCall.getRawSignature(), functionName);
            }
        } catch (Exception e) {
            System.out.println("Loi");
        }
    }

    public String replaceFunctionCallWithSummary (String functionCall, String functionName) throws Exception{
        List<String> sumList = readSummaryFile(filePath);
        CallGraphGeneration callGraphGeneration = new CallGraphGeneration();
        String summary="";
        for (String sum : sumList){
            if (sum.startsWith(functionName)){
                String temp[] = sum.split("/");
                summary = temp[1];
                summary = summary.replace("~", "<");
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