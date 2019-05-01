package com.fit.callgraph.test;

import com.vnu.fit.graph.models.ast.AbstractNode;

public class CallGraphNode2 {

    AbstractNode beginNode ;
    AbstractNode endNode;

    public CallGraphNode2(AbstractNode beginNode, AbstractNode endNode) {
        this.beginNode = beginNode;
        this.endNode = endNode;
    }

    public AbstractNode getBeginNode() {
        return beginNode;
    }

    public void setBeginNode(AbstractNode beginNode) {
        this.beginNode = beginNode;
    }

    public AbstractNode getEndNode() {
        return endNode;
    }

    public void setEndNode(AbstractNode endNode) {
        this.endNode = endNode;
    }
}
