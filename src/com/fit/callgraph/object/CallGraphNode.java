package com.fit.callgraph.object;

import java.util.ArrayList;
import java.util.List;

public class CallGraphNode implements ICallGraphNode {
    private String name;
    private String content;
    private List<ICallGraphNode> parentsNodeList = new ArrayList<>();
    private List<ICallGraphNode> targetNodeList = new ArrayList<>();
    private boolean isVisited;
    private int id;
    private String astLoc;

    public CallGraphNode(){

    }

    public CallGraphNode(String name){
        this.name = name;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getAstLocation() {
        return astLoc;
    }

    @Override
    public void setASTFileLocation(String astLoc) {
        this.astLoc = astLoc;
    }

    @Override
    public List<ICallGraphNode> getListTarget() {
        return targetNodeList;
    }

    @Override
    public void setListTarget(List<ICallGraphNode> listTarget) {
        this.targetNodeList = listTarget;
    }

    @Override
    public boolean isVisited() {
        return isVisited;
    }

    @Override
    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    @Override
    public boolean isMultipleTarget() {
        return false;
    }

    @Override
    public List<ICallGraphNode> getListParents() {
        return parentsNodeList;
    }

    @Override
    public void setListParents(List<ICallGraphNode> listParents) {
        this.parentsNodeList = listParents;
    }

    @Override
    public boolean contains(ICallGraphNode child) {
        while (child != null) {
            if (child == this)
                return true;
            //child = child.getParent();
        }
        return false;
    }

    @Override
    public boolean shouldDisplayInCG() {
        return false;
    }

    @Override
    public boolean shouldDisplayInSameLine() {
        return false;
    }

    @Override
    public boolean shouldInBlock() {
        return false;
    }
}
