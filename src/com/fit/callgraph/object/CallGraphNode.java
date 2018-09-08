package com.fit.callgraph.object;

import java.util.ArrayList;
import java.util.List;

public class CallGraphNode implements ICallGraphNode {
    private String content;
    private ICallGraphNode parentNode;
    private List<ICallGraphNode> targetNodeList = new ArrayList<>();
    private boolean isVisited;
    private int id;

    public CallGraphNode(){

    }

    public CallGraphNode(String content){
        this.content = content;
    }
    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        content = content;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        id = id;
    }

    @Override
    public List<ICallGraphNode> getListTarget() {
        return targetNodeList;
    }

    @Override
    public void setListTarget(List<ICallGraphNode> listTarget) {
        targetNodeList = listTarget;
    }

    @Override
    public boolean isVisited() {
        return isVisited;
    }

    @Override
    public void setVisited(boolean isVisited) {
        isVisited = isVisited;
    }
}
