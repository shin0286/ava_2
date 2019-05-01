package com.fit.gui.callgraph;

import com.fit.callgraph.CallGraphGeneration;
import com.fit.callgraph.ICallGraph;
import com.fit.gui.swing.DragScrollPane;
import com.fit.gui.swing.LightTabbedPane;
import com.fit.tree.object.IProjectNode;
import com.vnu.fit.graph.GraphCreator;
import com.vnu.fit.graph.models.Project;
import com.vnu.fit.graph.models.ast.AbstractNode;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;

public class CGVisualizer extends DragScrollPane implements
        LightTabbedPane.EqualsConstruct, ComponentListener {

    public static final int OVERVIEW_CALL_GRAPH =  1;
    private static final long serialVersionUID = 1L;
    private IProjectNode projectNode;
    private CGCanvas canvas;
    private MouseListener mouseListener;

    public CGVisualizer(IProjectNode projectNode, MouseListener mouseListener) {
        this.projectNode = projectNode;
        this.mouseListener = mouseListener;

        canvas = new CGCanvas(projectNode);
        setViewportView(canvas);
        setBorder(null);
        addComponentListener(this);
    }

    @Override
    public boolean equalsConstruct(Object... constructItem) {
        return false;
    }

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {
        try {
            if(!canvas.hasAdapter()){
                ICallGraph callGraph = null;
                String projectPath = projectNode.getAbsolutePath();
                GraphCreator graphCreator = new GraphCreator(projectPath);
                graphCreator.execute();
                AbstractNode abstractNode = Project.getInstance().getRoot();
                CallGraphGeneration generation = new CallGraphGeneration();
                generation.setAbstractNode(abstractNode);

                callGraph = generation.generateCallGraph();

                if(callGraph != null){
                    CGNodeAdapter adapter = new CGNodeAdapter(callGraph);
                    canvas.setAdapter(adapter);

                    adapter.forEach(n -> n.addMouseListener(mouseListener));
                }else {
                    throw new Exception();
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(CGVisualizer.this,
                    "Error in displaying call graph for this project",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
