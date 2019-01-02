package com.fit.callgraph.test;

import com.fit.config.Paths;
import com.vnu.fit.graph.GraphCreator;
import com.vnu.fit.graph.models.Project;
import com.vnu.fit.graph.models.ast.AbstractNode;
import com.vnu.fit.graph.models.dependences.Dependency;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String path = Paths.QLSV;

        GraphCreator creator = new GraphCreator(path);
        creator.execute();

        AbstractNode root = Project.getInstance().getRoot();
        List<AbstractNode> lstChilds = root.getChildren();
        List<CallGraphNode2> result = new ArrayList<>();

        for(AbstractNode childNode : lstChilds ){

            List<Dependency> lstDepend = childNode.getDependencyList();
            for(Dependency dependency : lstDepend){
                if(dependency.getType() == Dependency.Type.INVOCATION){
                    AbstractNode beginNode = dependency.getBeginNode();

                    AbstractNode endNode = dependency.getEndNode();

                    CallGraphNode2 callGraphNode2 = new CallGraphNode2(beginNode,endNode);

                    result.add(callGraphNode2);
                }
            }

        }

        for(int i=0; i< result.size(); i++){
            for(int j = result.size() - 1; j>i; j--){
                if(result.get(i).getBeginNode().toString().contentEquals(result.get(j).getBeginNode().toString())
                        && result.get(i).getEndNode().toString().contentEquals(result.get(j).getEndNode().toString())){
                    result.remove(result.get(j));
                }

            }
        }
        System.out.println(result.size());
        //result.clear();
        //result.addAll(tmp);
        for(CallGraphNode2 callGraphNode: result){
            System.out.println("======================");
            System.out.println("beginNode : " + callGraphNode.getBeginNode().toString());
            System.out.println("endNode : " + callGraphNode.getEndNode().toString());
            System.out.println("----------------------");
        }
    }


    /*public void printTree(AbstractNode parentNode, String s){
        List<AbstractNode> lstChilds = parentNode.getChildren();
        for(AbstractNode childnNode : lstChilds ){
            if(childnNode.getName().contains("main")){
                List<Dependency> lstDepend = childnNode.getDependencyList();
                System.out.println(lstDepend.size());
                int i= 0;
                for(Dependency dependency : lstDepend){
                    AbstractNode beginNode = dependency.getBeginNode();
                    AbstractNode endNode = dependency.getEndNode();
                    System.out.println("beginNode " + i + " " + beginNode.toString());
                    System.out.println("endNode " + i + " " + endNode.toString());
                }

            }
        }
    }*/
}
