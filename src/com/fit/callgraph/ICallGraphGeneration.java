package com.fit.callgraph;


import com.vnu.fit.graph.models.ast.AbstractNode;

public interface ICallGraphGeneration {

    ICallGraph generateCallGraph();
    AbstractNode getAbstractNode();
    void setAbstractNode(AbstractNode abstractNode);
}
