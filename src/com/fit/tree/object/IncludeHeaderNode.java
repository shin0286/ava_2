package com.fit.tree.object;

import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;

public class IncludeHeaderNode extends CustomASTNode<IASTPreprocessorIncludeStatement> {

    @Override
    public String getNewType() {
        return getAST().getName().getRawSignature();
    }
}
