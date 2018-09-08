package com.fit.callgraph.object;

import java.util.List;

public interface ICallGraphNode {
    String getContent();
    void setContent(String content);
    int getId();
    void setId(int id);
    List<ICallGraphNode> getListTarget();
    void setListTarget(List<ICallGraphNode> listTarget);
    boolean isVisited();
    void setVisited(boolean isVisited);
}
