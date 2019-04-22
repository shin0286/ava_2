package com.fit.callgraph;


import com.fit.callgraph.object.ICallGraphNode;
import com.fit.tree.object.INode;
import com.vnu.fit.graph.models.ast.AbstractNode;

import java.util.List;

public interface ICallGraph {
    ICallGraphNode findFirstCallGraphNodeByName(String name);
    ICallGraphNode findById(int id);
    ICallGraphNode getMainNode(INode node);
    List<ICallGraphNode> getAllNodes();
    void setIdforAllNodes();
    AbstractNode getAbstractNode();
    void setAbstractNode(AbstractNode abstractNode);
    @Deprecated
    void updateVisitedNodes(String testpath);
}
