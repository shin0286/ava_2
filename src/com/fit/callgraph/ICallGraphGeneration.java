package com.fit.callgraph;

import com.fit.tree.object.IFunctionNode;

public interface ICallGraphGeneration {

    public ICallGraph generateCallGraph();
    IFunctionNode getFunctionNode();

    void setFunctionNode(IFunctionNode functionNode);

}
