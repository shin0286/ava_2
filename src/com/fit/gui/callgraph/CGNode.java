package com.fit.gui.callgraph;

import com.fit.callgraph.object.ICallGraphNode;
import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.gui.cfg.CFGNode;
import com.fit.gui.swing.Node;
import com.fit.utils.UtilsVu;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class CGNode extends Node<ICallGraphNode> {
    public static final int NORMAL = 0, CONDITION = 1, MARK = 2, PADDING_X = 20, PADDING_Y = 10, MARK_SIZE = 25;
    static final String MORE = "...", NEW_LINE = "<br>";
    private static final long serialVersionUID = 1L;
    private int type;
    private Color borderColor;
    private ArrayList<CGNode> listDrag;

    public CGNode (ICallGraphNode callGraphNode){
        super(callGraphNode);
        Dimension size = getPreferredSize();

        type += CGNode.NORMAL;
        size.width += CGNode.PADDING_X;
        size.height += CGNode.PADDING_Y;
        this.setSize(size);
        setBorderColor(Color.BLACK);
        setBackground(SystemColor.inactiveCaptionBorder);
    }


    public CGNode (List<ICallGraphNode> nodeList){
        setElement(nodeList.get(0));
        type = CGNode.NORMAL;

        StringBuilder txt = new StringBuilder(), real = new StringBuilder();
        boolean overWidth = false, overLine = false, newLine = true;
        int count = 1, width = 0, MAX_LINE = Integer.parseInt(AbstractSetting.getValue(ISettingv2.MAX_CG_NODE_LINE)),
                MAX_WIDTH = Integer.parseInt(AbstractSetting.getValue(ISettingv2.MAX_CG_NODE_WIDTH));

        if (MAX_LINE == 0)
            MAX_LINE = Integer.MAX_VALUE;

        for(ICallGraphNode node : nodeList){
            String s = String.valueOf(node);
            real.append(UtilsVu.htmlEscape(s)).append(CGNode.NEW_LINE);

            if(count <= MAX_LINE){
                int length = s.length();

                if(width >0){
                    if(node.shouldDisplayInSameLine() && width + length < MAX_WIDTH){
                        width += length + 1;
                        txt.append(' ').append(UtilsVu.htmlEscape(s));
                        newLine = false;
                    } else {
                        txt.append(CGNode.NEW_LINE);
                        newLine = true;
                        count++;
                        width = 0;
                    }
                }

                if (count > MAX_LINE) {
                    overLine = true;
                    continue;
                }

                // Không thêm được, tạo dòng mới cho node
                if (width == 0){
                    if (length <= MAX_WIDTH) {
                        width = length;
                        txt.append(UtilsVu.htmlEscape(s));
                        newLine = false;

                        if (!node.shouldDisplayInSameLine()) {
                            txt.append(CGNode.NEW_LINE);
                            newLine = true;
                            count++;
                            width = 0;
                        }
                    } else {
                        txt.append(UtilsVu.htmlEscape(s.substring(0, MAX_WIDTH - CGNode.MORE.length())))
                                .append(CGNode.MORE).append(CGNode.NEW_LINE);
                        newLine = true;
                        count++;
                        width = 0;
                        overWidth = true;
                    }
                }
            } else {
                overLine = true;
            }
        }
        if (overLine) {
            if (!newLine)
                txt.append(CGNode.NEW_LINE);
            txt.append(CGNode.MORE);
        }

        setText(UtilsVu.htmlCenter(txt.toString()));
        if (overWidth || overLine)
            setToolTipText(UtilsVu.html(real.toString()));

        Dimension size = getPreferredSize();
        size.width += CFGNode.PADDING_X;
        size.height += CFGNode.PADDING_Y;
        this.setSize(size);
        setBorderColor(Color.BLACK);
        setBackground(SystemColor.inactiveCaptionBorder);

    }


    public void paint(Graphics2D g, int x, int y, int width, int height){
        g.setColor(getBackground());
        g.fillRect(x, y, width, height);
        g.setColor(getBorderColor());
        g.drawRect(x, y, width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(g instanceof Graphics2D){
            Graphics2D g2d = (Graphics2D) g;
            Color oldColor = g2d.getColor();
            this.paint(g2d, 0, 0, getWidth() -1, getHeight() - 1);
            g2d.setColor(oldColor);
        }
        super.paintComponent(g);
    }

    @Override
    public void triggerMouseDrag(MouseEvent e) {
        super.triggerMouseDrag(e);

        if (!e.isControlDown())
            return;

        // Xử lý nhóm các node khi ấn vào nút điều khiển
        if (getElement().isMultipleTarget()) {
            if (listDrag == null) {
                listDrag = new ArrayList<>();
                CGCanvas cv = (CGCanvas) getParent();

                for (Node<ICallGraphNode> n : cv.getAdapter())
                    if (getElement().contains(n.getElement()))
                        listDrag.add((CGNode)n);
            }
            for (CGNode n : listDrag)
                if (n != this)
                    n.triggerSuperMouseDrag(e);
        } // Xử lý nhóm các node thông thường
        else {
            CGNode next = (CGNode) getRefer()[0];

            // Nếu node tiếp theo cũng là node thông thường, di chuyển theo
            if (!(next == null || next.getElement().isMultipleTarget()))
                next.triggerMouseDrag(e);
        }
    }

    private void triggerSuperMouseDrag(MouseEvent e) {
        super.triggerMouseDrag(e);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
}
