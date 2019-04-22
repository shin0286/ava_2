package com.fit.callgraph.object;




import java.util.List;

public interface ICallGraphNode {
    String getName();
    void setName(String name);
    int getId();
    void setId(int id);
    String getContent();
    void setContent(String content);
    String getAstLocation();
    void setASTFileLocation(String astLoc);
    List<ICallGraphNode> getListTarget();
    void setListTarget(List<ICallGraphNode> listTarget);
    boolean isVisited();
    void setVisited(boolean isVisited);
    boolean isMultipleTarget();
    List<ICallGraphNode> getListParents();
    void setListParents(List<ICallGraphNode> listParents);
    boolean contains(ICallGraphNode child);
    boolean shouldDisplayInCG();

    /**
     * @return true if the statement is displayed in the same line
     */
    boolean shouldDisplayInSameLine();

    /**
     * @return true if the statement is displayed in a block in GUI
     */
    boolean shouldInBlock();

}
