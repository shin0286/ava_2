package com.fit.callgraph;

import com.fit.callgraph.object.CallGraphNode;
import com.fit.callgraph.object.ICallGraphNode;
import com.fit.cfg.testpath.PossibleTestpathGeneration;
import com.fit.config.Paths;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.INode;
import com.vnu.fit.graph.GraphCreator;
import com.vnu.fit.graph.models.ast.AbstractNode;

import java.util.ArrayList;
import java.util.List;

public class CallGraph implements ICallGraph {
    private PossibleTestpathGeneration possibleTestpaths;

    private AbstractNode abstractNode;

    private List<ICallGraphNode> listCallGraphNodes = new ArrayList<>();


    public CallGraph(){}

    public CallGraph(List<ICallGraphNode> lstCallGraphNodes) {
        listCallGraphNodes = lstCallGraphNodes;
    }


    @Override
    public ICallGraphNode findFirstCallGraphNodeByName(String name) {
        for(ICallGraphNode node: getAllNodes()){
            if(node.getName().contains(name)){
                return node;
            }
        }
        return null;
    }

    @Override
    public ICallGraphNode findById(int id) {
        for(ICallGraphNode node: getAllNodes()){
            if(node.getId() == id){
                return node;
            }
        }
        return null;
    }
    public boolean checkMainFunction( INode projectRoot){
        if(projectRoot == null){
            return false;
        }else if(projectRoot instanceof FunctionNode && projectRoot.getName().contains("main()")){
            return true;
        }
        return false;
    }

    @Override
    public ICallGraphNode getMainNode(INode node) {
        List<INode> nodeList = node.getChildren();
        for(INode iNode : nodeList){
            for(INode detailNode : iNode.getChildren()){
                if(checkMainFunction(detailNode)){
                    ICallGraphNode callGraphNode = new CallGraphNode();
                    callGraphNode.setName(detailNode.getParent().getName());
                    callGraphNode.setASTFileLocation(detailNode.getParent().getAbsolutePath());
                    return callGraphNode;
                }
            }
        }
        return null;
    }



    @Override
    public List<ICallGraphNode> getAllNodes() {
        return listCallGraphNodes;
    }

    @Override
    public void setIdforAllNodes() {
        int id =0;
        for(ICallGraphNode node: getAllNodes()){
            node.setId(id ++);
        }
    }

    @Override
    public AbstractNode getAbstractNode() {
        return abstractNode;
    }

    @Override
    public void setAbstractNode(AbstractNode abstractNode) {
        this.abstractNode = abstractNode;
    }

    @Override
    public void updateVisitedNodes(String testpath) {

    }

    @Override
    public String toString() {
        String output = "";
        for(ICallGraphNode node: getAllNodes()){
            output += "[" + node.getId() + "]"  + node.getName() + "[" + node.getAstLocation() + "]";
        }
        return output;
    }

    public static void main(String[] args) {
        String path = Paths.QLSV;

        GraphCreator graphCreator = new GraphCreator(path);
        graphCreator.execute();



    }
}
