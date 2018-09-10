package com.fit.callgraph.object;

import org.eclipse.cdt.core.dom.ast.IASTFileLocation;

import java.util.List;

public interface ICallGraphNode {
    String getName();
    void setName(String name);
    int getId();
    void setId(int id);
    String getAstLocation();
    void setASTFileLocation(String astLoc);
    List<ICallGraphNode> getListTarget();
    void setListTarget(List<ICallGraphNode> listTarget);
    boolean isVisited();
    void setVisited(boolean isVisited);
}
