package com.fit.callgraph.components;


import com.vnu.fit.graph.analyser.components.TypedefAnalyser;
import com.vnu.fit.graph.builder.preprocessor.Dictionary;
import com.vnu.fit.graph.models.ast.AbstractNode;
import com.vnu.fit.graph.models.ast.FunctionNode;
import com.vnu.fit.graph.models.ast.FunctionPointer;
import com.vnu.fit.graph.searcher.ByClassCondition;
import com.vnu.fit.graph.searcher.Searcher;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.internal.core.dom.parser.cpp.*;

import java.util.ArrayList;

public class FunctionBuilder implements Builder {

    private final IASTNode node;
    private FunctionNode functionNode;
    private final AbstractNode parentNode;

    @Override
    public Object build() {
        functionNode  = new FunctionNode();
        functionNode.setParent(parentNode);
        functionNode.addAst(node);
        functionNode.setChildren(new ArrayList<>());
        functionNode.getDictionary().addAllUsingNamespace(parentNode.getDictionary());
        switch (typeFunction(node)) {
            case DEFINITION:
                return definitionBuilder(node);
            case OUTER_DEFINITION:
                return onClassDefinitionBuilder(node);
            case PROTOTYPE:
                return phototypeBuilder(node);
            default:
                return functionNode;
        }
    }

    private enum Type{
        DEFINITION ,
        OUTER_DEFINITION,
        PROTOTYPE,
        UNKNOWN
    }
    public FunctionBuilder(IASTNode node, AbstractNode parentNode) {
        this.node = node;
        this.parentNode = parentNode;
    }

    // check node is function
    public static boolean isFunction(IASTNode node){
        // check neu node la mot khai bao ham
        if(node instanceof CPPASTFunctionDefinition){
            return true;
        }

        //check neu node la mot prototype cua ham
        if(node instanceof CPPASTSimpleDeclaration){
            if (node.getChildren().length > 1 && node.getChildren()[1] instanceof CPPASTFunctionDeclarator) {
                return true;
            }
        }
        return false;
    }

    // ham tra ve type cua function trong ast
    private Type typeFunction(IASTNode node){
        // node la dinh nghia cua mot ham
        if(node instanceof CPPASTFunctionDefinition){
            IASTNode child = node.getChildren()[1];
            // neu la kieu nam thi la dinh nghia mot ham doc lap
            if(child.getChildren()[0] instanceof CPPASTName || child.getChildren()[0] instanceof CPPASTSimpleDeclSpecifier){
                return Type.DEFINITION;
            }
            // neu la kieu CPPASTQualifiedName thi no la dinh nghia mot ham trong class hoac ham ngoai namespace
            if(child.getChildren()[0] instanceof CPPASTQualifiedName){
                return Type.OUTER_DEFINITION;
            }
            // neu la kieu con tro hoac tham chieu
            if(child.getChildren()[0] instanceof CPPASTReferenceOperator || child.getChildren()[0] instanceof CPPASTPointer){

                if(child.getChildren()[1] instanceof CPPASTName){
                    return Type.DEFINITION;
                }

                if (child.getChildren()[1] instanceof CPPASTQualifiedName){
                    return Type.OUTER_DEFINITION;
                }
            }
        }
        // neu la kieu prototype
        if(node instanceof CPPASTSimpleDeclaration){
            if(node.getChildren().length > 1 && node.getChildren()[1] instanceof CPPASTFunctionDeclarator){
                return Type.PROTOTYPE;
            }
        }
        return Type.UNKNOWN;
    }


    private void addParam(IASTNode node){
        for (IASTNode n : node.getChildren()){
            if(n instanceof CPPASTParameterDeclaration){
                functionNode.getParams().add((IASTParameterDeclaration) n);
            }
        }
    }

    private FunctionNode definitionBuilder(IASTNode node){
        IASTNode childTwo = node.getChildren()[1];
        if(childTwo.getChildren()[0] instanceof CPPASTPointer || childTwo.getChildren()[0] instanceof CPPASTReferenceOperator){
            String name = childTwo.getChildren()[1].getRawSignature();
            functionNode.setName(name);
        }else {
            String name = childTwo.getChildren()[0].getRawSignature();
            functionNode.setName(name);
        }

        IASTNode childThird;
        if(node.getChildren().length >2){
            childThird = node.getChildren()[2];
            Dictionary dictionary = new TypedefAnalyser(childThird).analyser().getDictionary();
            functionNode.getDictionary().add(dictionary);
        }else {
            System.out.println(this.getClass()+" definitionBuilder(): node.getChildren()[2] null or node null");
        }
        addParam(childTwo);
        return functionNode;
    }

    private FunctionNode phototypeBuilder(IASTNode node){
        IASTSimpleDeclaration sd = (IASTSimpleDeclaration) node;
        IASTNode childTwo = node.getChildren()[1];
        if(childTwo.getChildren()[0] instanceof CPPASTPointer || childTwo.getChildren()[0] instanceof CPPASTReferenceOperator){
            String name = childTwo.getChildren()[1].getRawSignature();
            functionNode.setName(name);
        }else if(childTwo.getChildren()[0] instanceof CPPASTDeclarator){
            FunctionPointer pointer = new FunctionPointer();
            if(childTwo.getChildren() != null && childTwo.getChildren().length >0
                    && childTwo.getChildren()[0].getChildren() != null || childTwo.getChildren()[0].getChildren().length >1 ){
                String name = childTwo.getChildren()[0].getChildren()[1].getRawSignature();
                pointer.setName(name);
                pointer.setParent(parentNode);
                pointer.addAst(node);
            }

            IASTNode equalAST = childTwo.getChildren()[childTwo.getChildren().length - 1];
            if(equalAST instanceof CPPASTEqualsInitializer){
                String functionName = equalAST.getChildren()[0].getRawSignature();
                AbstractNode callFunc = Searcher.findbyName(parentNode, parentNode.getDictionary().getUsingNamespaceList(),
                                                            functionName, new ByClassCondition<>(FunctionNode.class));
            }

        }
        return functionNode;
    }

    // neu no la dinh nghia ben ngoai cua 1 function trong class
    private FunctionNode onClassDefinitionBuilder(IASTNode node){
        IASTNode childTwo = node.getChildren()[1];
        if(childTwo.getChildren()[0] instanceof CPPASTPointer || childTwo.getChildren()[0] instanceof CPPASTReferenceOperator){
            String className = childTwo.getChildren()[1].getChildren()[0].getRawSignature();
            functionNode.setParentClassName(className);

            String name = childTwo.getChildren()[1].getChildren()[1].getRawSignature();
            functionNode.setName(name);
        }else {
            String className = childTwo.getChildren()[0].getChildren()[0].getRawSignature();
            functionNode.setParentClassName(className);

            String name = childTwo.getChildren()[0].getChildren()[1].getRawSignature();
            functionNode.setName(name);
        }

        if(node.getChildren() != null || node.getChildren().length > 2){
            IASTNode childThird = node.getChildren()[2];
            Dictionary dictionary = new TypedefAnalyser(childThird).analyser().getDictionary();
            functionNode.getDictionary().add(dictionary);
        }

        addParam(childTwo);
        return functionNode;
    }
}
