package com.fit.callgraph;

import com.fit.callgraph.object.CallGraphNode;
import com.fit.callgraph.object.ICallGraphNode;
import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.INode;
import org.eclipse.cdt.core.dom.ast.IASTFileLocation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CallGraph implements ICallGraph {

    List<ICallGraphNode> listCallGraphNodes = new ArrayList<>();

    INode node;
    public CallGraph() {
    }

    public CallGraph createCallGraph(String program, List<String> listSeeds){
        CallGraph callGraph = new CallGraph();
        return callGraph;
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
        String path = Paths.BTL;
        ProjectParser parser = new ProjectParser(new File(path),null);
        INode projecNode = parser.getRootTree();
        CallGraph callGraph = new CallGraph();
        ICallGraphNode mainNode = callGraph.getMainNode(projecNode);
        System.out.println(mainNode.getName() + " "+ mainNode.getAstLocation());
    }
}
