package com.fit.callgraph;

import com.fit.callgraph.object.CallGraphNode;
import com.fit.callgraph.object.ICallGraphNode;
import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.config.Paths;
import com.fit.normalizer.FunctionNormalizer;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import com.vnu.fit.graph.GraphCreator;
import com.vnu.fit.graph.models.Project;
import com.vnu.fit.graph.models.ast.AbstractNode;
import com.vnu.fit.graph.models.ast.FunctionNode;
import com.vnu.fit.graph.models.dependences.Dependency;
import org.apache.log4j.Logger;
import org.eclipse.cdt.core.dom.ast.IASTNode;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author namdv
 *
 */
public class CallGraphGeneration implements ICallGraphGeneration {
    final static Logger logger = Logger.getLogger(CFGGenerationforBranchvsStatementCoverage.class);
    private AbstractNode abstractNode;
    private ICallGraphNode BEGIN;
    private String projectPath;


    public CallGraphGeneration() {
    }


    public void init(String path){
        try {
            ProjectParser parser = new ProjectParser(new File(path));

            INode function = Search
                    .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "Dim1InputAdvance(int[])")
                    .get(0);

            System.out.println(((IFunctionNode) function).getAST().getRawSignature());
            FunctionNormalizer fnNorm = null;
            fnNorm = ((IFunctionNode) function).normalizedASTtoInstrument();
            String normalizedCoverage = fnNorm.getNormalizedSourcecode();
            IFunctionNode clone = (IFunctionNode) function.clone();
            clone.setAST(Utils.getFunctionsinAST(normalizedCoverage.toCharArray()).get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String ast(IASTNode node){
        return node == null? "": node.getRawSignature();
    }


    public INode findMainFunction(INode node){

        return null;
    }

    public void doInFunction(){

    }

    @Override
    public ICallGraph generateCallGraph() {
        return parse(abstractNode, projectPath);
    }

    @Override
    public AbstractNode getAbstractNode() {
        return abstractNode;
    }

    @Override
    public void setAbstractNode(AbstractNode abstractNode) {
        this.abstractNode = abstractNode;
    }


    public ICallGraph parse(AbstractNode abstractNode, String path){
        List<ICallGraphNode> result = new ArrayList<>();
        abstractNode = Project.getInstance().getRoot();

        List<AbstractNode> lstChilds = abstractNode.getChildren();
        List<FunctionNode> functionNodeList = new ArrayList<>();

        List<ICallGraphNode> tmp = new ArrayList<>();

        for(AbstractNode childNode : lstChilds ){

            List<Dependency> lstDepend = childNode.getDependencyList();
            for(Dependency dependency : lstDepend){
                if(dependency.getType() == Dependency.Type.INVOCATION){
                    //  System.out.println(childNode.toString());
                    functionNodeList.add((FunctionNode) childNode);
                    ICallGraphNode callGraphNode = convertAbstractNodeToCallGraphNode2(childNode);
                    result.add(callGraphNode);
                }
            }

        }

        for(int i = 0; i< result.size(); i++){
            if(checkExist(tmp, result.get(i).getName()) == false){
                tmp.add(result.get(i));
            }
        }

        result = new ArrayList<>();
        result.addAll(tmp);

        tmp = new ArrayList<>();
        for(int i = 0; i< result.size(); i++){
            if(result.get(i).getName().contains("main()")){
                tmp.add(result.get(i));
            }
        }

        if(tmp.size() >0){
            addToList(tmp, tmp.get(0));
        }

        return new CallGraph(tmp);
    }

    public void addToList(List<ICallGraphNode> list, ICallGraphNode node){
        List<ICallGraphNode> listTarget = node.getListTarget();
        for(int i = 0; i< list.size(); i++){

            for(int j = 0; j< listTarget.size(); j++){
                if(checkExist(list, listTarget.get(j).getName()) == false){
                    list.add(listTarget.get(j));
                    addToList(list, listTarget.get(j));
                }
            }

        }
    }
    private static boolean checkExist(List<ICallGraphNode> functionNodeList, String funcName) {
        for (ICallGraphNode node : functionNodeList) {
            if (node.getName().equals(funcName)) { // I used getId(), replace that by the accessor you actually need
                return true;
            }
        }
        return false;
    }

    /*public List<ICallGraphNode> getListParentOfChildNode(AbstractNode node){
        List<ICallGraphNode> result = new ArrayList<>();
        List<Dependency> dependencyList = node.getDependencyList();
        for(int i = 0; i< dependencyList.size(); i++){
            if(dependencyList.get(i).getType() == Dependency.Type.INVOCATION){
                if(node.equals(dependencyList.get(i).getEndNode()) ){
                    ICallGraphNode tmp = convertAbstractNodeToCallGraphNode2(dependencyList.get(i).getBeginNode());
                    result.add(tmp);
                } else if(node.equals(dependencyList.get(i).getBeginNode())){
                    return result;
                }

            }

        }

        return result;
    }*/


    public List<ICallGraphNode> getListChildOfNode(AbstractNode node){
        List<ICallGraphNode> result = new ArrayList<>();

        List<Dependency> dependencyList = node.getDependencyList();
        for(int i = 0; i< dependencyList.size(); i++){
            if(dependencyList.get(i).getType().equals(Dependency.Type.INVOCATION)){
                if(!node.equals(dependencyList.get(i).getEndNode())){
                    ICallGraphNode tmpNode = convertAbstractNodeToCallGraphNode2(dependencyList.get(i).getEndNode());
                    result.add(tmpNode);
                }
            }
        }

        return result;
    }

    public ICallGraphNode convertAbstractNodeToCallGraphNode2 (AbstractNode abstractNode){
        ICallGraphNode callGraphNode = new CallGraphNode();
        callGraphNode.setName(abstractNode.toString());
        callGraphNode.setContent(abstractNode.getAst(0).getRawSignature());
        callGraphNode.setASTFileLocation("");
        //callGraphNode.setListParents(getListParentOfChildNode(abstractNode));
        callGraphNode.setListTarget(getListChildOfNode(abstractNode));
        return callGraphNode;
    }



    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public ICallGraphNode getRootCallGraph(CallGraphGeneration graphGeneration, String projectPath){
        graphGeneration.setProjectPath( projectPath);
        GraphCreator creator = new GraphCreator(graphGeneration.getProjectPath());
        creator.execute();
        graphGeneration.setAbstractNode(Project.getInstance().getRoot());
        ICallGraph callGraph = graphGeneration.generateCallGraph();
        callGraph.setIdforAllNodes();
        try {
            return callGraph.getAllNodes().get(0);
        }catch (Exception ex){
            return null;
        }
    }

    public ICallGraphNode getFunctionCallGraph (ICallGraphNode functionCallGraph, String functionName){
        Queue<ICallGraphNode> nodeQueue = new LinkedList<ICallGraphNode>();
        ICallGraphNode functionNode = new CallGraphNode();
        ((LinkedList<ICallGraphNode>) nodeQueue).add(functionCallGraph);
        while (!nodeQueue.isEmpty()){
            ICallGraphNode firstNode = nodeQueue.remove();
            if (firstNode.getName().equals(functionName)) {
                functionNode = firstNode;
                break;
            } else {
                for (ICallGraphNode node : firstNode.getListTarget()) {
                    ((LinkedList<ICallGraphNode>) nodeQueue).add(node);
                }
            }
        }
        return functionNode;
    }

    public static void main(String[] args) {

        CallGraphGeneration graphGeneration = new CallGraphGeneration();
        ICallGraphNode callGraphNode = graphGeneration.getRootCallGraph(graphGeneration, "..\\ava_ver2\\data-test\\tsdv\\Sample_for_R1_2\\");
        System.out.println(graphGeneration.getFunctionCallGraph(callGraphNode, "mmin3(int, int, int)").getContent());
    }
}
