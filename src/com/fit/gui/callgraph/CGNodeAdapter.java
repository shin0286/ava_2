package com.fit.gui.callgraph;

import com.fit.callgraph.ICallGraph;
import com.fit.callgraph.object.ICallGraphNode;
import com.fit.gui.swing.Node;
import com.fit.gui.swing.NodeAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CGNodeAdapter extends NodeAdapter<ICallGraphNode> {
    private static final long serialVersionUID = 1L;

    private int[] receiveCount;

    public CGNodeAdapter (ICallGraph callGraph){
        List<ICallGraphNode> callGraphNodeList = callGraph.getAllNodes();
        receiveCount = new int[callGraphNodeList.size()];
        int i = 0;

        for(ICallGraphNode node: callGraphNodeList){
            node.setVisited(false);
            node.setId(i++);
        }
        for(ICallGraphNode node: callGraphNodeList){
            for(ICallGraphNode target : node.getListTarget()){
                receiveCount[target.getId()] ++;
            }
        }
        for(ICallGraphNode node: callGraphNodeList){
            if(node.isVisited()){
                continue;
            }
            node.setVisited(true);

            ICallGraphNode next = lastInBlock(node);
            if(next.equals(node)){
                this.add(new CGNode(node));
            }else {
                ArrayList<ICallGraphNode> nodeList = new ArrayList<>();
                while (node != next){
                    if(node.shouldDisplayInCG()){
                        nodeList.add(node);
                    }

                    node = node.getListTarget().get(0);
                    node.setVisited(true);
                }
                if(next.shouldDisplayInCG()){
                    nodeList.add(next);
                }

                this.add(new CGNode(nodeList));
            }
        }

        for (Node<ICallGraphNode> node: this){
            ICallGraphNode current = node.getElement();
            if(current.getListTarget().size() >0){
                List<ICallGraphNode> targets = current.getListTarget();
                Node<ICallGraphNode>[] refer = (Node<ICallGraphNode>[]) Array.newInstance(node.getClass(), targets.size());
                for(i = 0; i< targets.size(); i++){
                    refer[i] = this.getNodeByElement(targets.get(i));
                }
                node.setRefer(refer);
                continue;
            }


        }
    }

    private ICallGraphNode lastInBlock(ICallGraphNode node){
        if(!node.shouldInBlock()){
            return node;
        }
        ICallGraphNode next = node.getListTarget().get(0);

        while (next!= null && receiveCount[next.getId()] == 1 && next.shouldInBlock()){
            node = next;
            next = node.getListTarget().get(0);
        }
        return node;
    }

    private ICallGraphNode next(ICallGraphNode node){
        while (node != null && node.shouldDisplayInCG()){
            node = node.getListTarget().get(0);
        }
        return node;
    }
}
