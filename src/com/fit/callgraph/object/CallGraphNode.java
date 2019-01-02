package com.fit.callgraph.object;

import org.eclipse.cdt.core.dom.ast.IASTFileLocation;

import java.util.ArrayList;
import java.util.List;

public class CallGraphNode implements ICallGraphNode {
    private String name;
    private ICallGraphNode parentNode;
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
}
