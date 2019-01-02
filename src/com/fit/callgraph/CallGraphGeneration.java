package com.fit.callgraph;

import com.fit.callgraph.object.ICallGraphNode;
import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.config.Paths;
import com.fit.normalizer.FunctionNormalizer;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.utils.ASTUtils;
import com.fit.utils.Utils;
import com.fit.utils.UtilsVu;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.apache.log4j.Logger;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTFunctionCallExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionCallExpression;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author namdv
 *
 */
public class CallGraphGeneration implements ICallGraphGeneration {
    final static Logger logger = Logger.getLogger(CFGGenerationforBranchvsStatementCoverage.class);
    private IFunctionNode functionNode;
    private ICallGraphNode BEGIN;


    public static void main(String[] args) {
        String path = Paths.COMBINED_STATIC_AND_DYNAMIC_GENERATION;
        CallGraphGeneration graphGeneration = new CallGraphGeneration();
        ProjectParser parser = new ProjectParser(new File(path), null);
        INode projectRoot = parser.getRootTree();
        INode result = graphGeneration.findMainFunction(projectRoot);
        System.out.println(result.getName());
    }

    public void init(String path){
        try {
            ProjectParser parser = new ProjectParser(new File(path));

            INode function = Search
                    .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "Dim1InputAdvance(int[])")
                    .get(0);

            System.out.println(((IFunctionNode) function).getAST().getRawSignature());
            FunctionNormalizer fnNorm = null;
            fnNorm = ((IFunctionNode) function).normalizedASTtoInstrument();
            String normalizedCoverage = fnNorm.getNormalizedSourcecode();
            IFunctionNode clone = (IFunctionNode) function.clone();
            clone.setAST(Utils.getFunctionsinAST(normalizedCoverage.toCharArray()).get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String ast(IASTNode node){
        return node == null? "": node.getRawSignature();
    }

    public boolean isFunctionDeclaratorNode(IASTStatement statement){
        if(statement instanceof IASTFunctionDeclarator){
            return true;
        }else {
            return false;
        }
    }

    public boolean isFunctionCallExpression(IASTStatement statement){
        if(statement instanceof CASTFunctionCallExpression || statement instanceof CPPASTFunctionCallExpression){
            return true;
        }else {
            return false;
        }
    }



    public INode findMainFunction(INode node){

        return null;
    }

    public void visitFunction(IASTStatement statement, ICallGraphNode begin, String path){
        try {
            File file = new File(path);
            String content = null;
            content = UtilsVu.getContentFile(file);
            ILanguage lang = file.getName().toLowerCase().endsWith(".c") ? GCCLanguage.getDefault()
                    : GPPLanguage.getDefault();
            IASTTranslationUnit u = ASTUtils.getIASTTranslationUnit(content.toCharArray(), path, null, lang);
            IASTNode[] child = u.getChildren();
            for(IASTNode n: child){
                if(isFunctionCallExpression((IASTStatement) n)){
                    // TODO: do in function contain functionNode n
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doInFunction(){

    }

    @Override
    public ICallGraph generateCallGraph() {
        return null;
    }

    @Override
    public IFunctionNode getFunctionNode() {
        return functionNode;
    }

    @Override
    public void setFunctionNode(IFunctionNode functionNode) {
        functionNode = functionNode;
    }

    private ICallGraph parse(IFunctionNode node){
        return null;
    }
}
