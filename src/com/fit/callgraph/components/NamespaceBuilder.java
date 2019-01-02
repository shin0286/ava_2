package com.fit.callgraph.components;

import com.vnu.fit.graph.builder.hierarchy.components.ClassBuilder;
import com.vnu.fit.graph.models.ast.AbstractNode;
import com.vnu.fit.graph.models.ast.FunctionNode;
import com.vnu.fit.graph.models.ast.NamespaceNode;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTNamespaceDefinition;

public class NamespaceBuilder implements Builder {
    private NamespaceNode namespaceNode;
    private NamespaceNode usingNamespaceNode;
    private IASTNode node;
    private AbstractNode parent;


    public static boolean isNamespace(IASTNode node){
        return node instanceof CPPASTNamespaceDefinition;
    }

    public NamespaceBuilder(IASTNode node, AbstractNode parent) {
        this.node = node;
        this.parent = parent;
    }


    @Override
    public NamespaceNode build() {
        namespaceNode = new NamespaceNode();
        String name = node.getChildren()[0].getRawSignature();
        if(name == null || name.equals("")){
            name = "N" + ClassBuilder.getID();
        }
        return namespaceNode;
    }
}
