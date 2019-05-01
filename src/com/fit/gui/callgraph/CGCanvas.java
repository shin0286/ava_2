package com.fit.gui.callgraph;

import com.fit.callgraph.object.ICallGraphNode;
import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.gui.swing.Canvas;
import com.fit.gui.swing.Node;
import com.fit.gui.swing.NodeAdapter;
import com.fit.tree.object.IProjectNode;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//TODO: ve do thi tai day
public class CGCanvas extends Canvas<ICallGraphNode> {

    public static final Color DEFAULT = Color.BLACK;
    private static final long serialVersionUID = 1L;

    static double DELTA = 0.26629401711818285;
    private IProjectNode projectNode;

    private MouseListener notifyAllNodePress = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            for (Node<ICallGraphNode> node : CGCanvas.this.adapter) {
                node.registerMousePress(e);
            }
        }
    };

    public CGCanvas(IProjectNode projectNode) {
        this.projectNode = projectNode;
    }

    static int[] getPoint(int x1, int y1, int x2, int y2, boolean left) {
        double anpha = Math.atan2(y2 - y1, x2 - x1), anph6 = anpha * 6 / Math.PI;

        if (left && 5 > anph6 || !left && 0 < anph6 && anph6 <= 1)
            anpha += CGCanvas.DELTA;
        else
            anpha -= CGCanvas.DELTA;

        return new int[]{x1 + (int) (38 * Math.cos(anpha)), y1 + (int) (38 * Math.sin(anpha))};
    }

    @Override
    protected void onAddedNode(Node<ICallGraphNode> node) {
        node.addMouseListener(notifyAllNodePress);
    }

    @Override
    protected void paintCanvas(Graphics g, NodeAdapter<ICallGraphNode> adapter) {
        int x1, y1, x2, y2, xs, ys;
        Node<ICallGraphNode>[] refer;
        int d = 12, h = 5, gap = 25;
        Graphics2D graphics2D = (Graphics2D) g;
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setStroke(Canvas.NORMAL_STROKE);

        for (Node<ICallGraphNode> n1 : adapter) {
            //TODO: refer null
            refer = n1.getRefer();
            xs = n1.getX() + n1.getWidth() / 2;
            ys = n1.getY() + n1.getHeight();
            boolean isMultiple = n1.getElement().isMultipleTarget();
            int length = isMultiple ? refer.length :1;

            for(int i =0 ; i< length; i++){
                Node<ICallGraphNode> n2 = refer[i];
                Color color;
                int [] marks = null;
                if(n2 == null){
                    continue;
                }

                color = CGCanvas.DEFAULT;
                graphics2D.setColor(color);
                x1 = xs;
                y1 = ys;
                x2 = n2.getX() + n2.getWidth() / 2;
                y2 = n2.getY();

                boolean rightSide = x2 > x1;
                int cy1 = n1.getY() + n1.getHeight() / 2, cy2 = n2.getY() + n2.getHeight() / 2;

                // Node 2 ở bên dưới hoặc bằng node 1
                if (cy2 >= cy1) {
                    double angle = Math.atan((y2 - y1) * 1.0 / Math.abs(x2 - x1));

                    // Nếu góc nghiêng ngang nhỏ hơn PI/8, đầu mũi tên chỉ sang
                    // bên
                    if (angle < Math.PI / 8) {
                        x1 = n1.getX() + (rightSide ? n1.getWidth() : 0);
                        y1 = n1.getY() + n1.getHeight() / 2;

                        x2 = n2.getX() + (rightSide ? 0 : n2.getWidth());
                        y2 = y2 + n2.getHeight() / 2;
                    }
                } // Node 2 ở trên, vẽ các đường vuông góc
                else {
                    int nearSide = n2.getX() + (rightSide ? 0 : n2.getWidth()), tmpX;
                    int distance = Math.abs(nearSide - (x1 + n1.getWidth() / 2 * (rightSide ? 1 : -1)));
                    boolean outOfPadding = (n1.getX() + n1.getWidth() < n2.getX()
                            || n2.getX() + n2.getWidth() < n1.getX()) && distance > gap * 2;

                    // Vẽ xuống
                    graphics2D.drawLine(x1, y1, x1, y1 + gap);

                    if (outOfPadding)
                        tmpX = x2 + (n2.getWidth() / 2 + gap) * (rightSide ? -1 : 1);
                    else
                        tmpX = n2.getX() + (rightSide ? n2.getWidth() + gap : -gap);

                    graphics2D.drawLine(x1, y1 + gap, tmpX, y1 + gap);
                    x1 = tmpX;
                    y2 = y2 + n2.getHeight() / 2;
                    graphics2D.drawLine(x1, y1 + gap, x1, y2);
                    y1 = y2;
                    x2 = n2.getX() + (rightSide ^ outOfPadding ? n2.getWidth() : 0);

                    drawArrowLine(graphics2D, x1, y1, x2, y2, d, h);
                }

            }

        }
        graphics2D.setStroke(oldStroke);

    }

    @Override
    protected void parseAdapter(NodeAdapter<ICallGraphNode> adapter) {

        adapter.get(0).setLocation(getWidth()/2, Canvas.PADDING_Y);

        final int MARGIN_X = Integer.parseInt(AbstractSetting.getValue(ISettingv2.CFG_MARGIN_X)),
                MARGIN_Y = Integer.parseInt(AbstractSetting.getValue(ISettingv2.CFG_MARGIN_Y));

        for(Node<ICallGraphNode> node : adapter){
            CGNode cgNode = (CGNode) node;
            Node<ICallGraphNode>[] refer = node.getRefer();

            int x = cgNode.getX() + cgNode.getWidth() / 2;
            int y = cgNode.getY() + cgNode.getHeight();

            if(node.getElement().isMultipleTarget()){
                int begin = x - (refer.length - 1)* MARGIN_X / 2;
                int cy = y + MARGIN_Y;

                for(int i = 0; i< refer.length; i++){
                    CGNode rNode = (CGNode) refer[i];
                    if(rNode == null){
                        continue;
                    }

                    int cx = begin + i * MARGIN_X - rNode.getWidth() /2;
                    rNode.setLocation(cx, cy);
                }
                continue;
            }

        }
    }

    public IProjectNode getProjectNode() {
        return projectNode;
    }

    public void setProjectNode(IProjectNode projectNode) {
        this.projectNode = projectNode;
    }
}
