package com.fit.normalizer;

import com.fit.config.IFunctionConfig;
import com.fit.testdatagen.se.memory.ISymbolicVariable;
import com.fit.testdatagen.se.memory.IVariableNodeTable;
import com.fit.testdatagen.se.memory.OneLevelCharacterSymbolicVariable;
import com.fit.testdatagen.se.memory.VariableNodeTable;
import com.fit.utils.Utils;
import org.eclipse.cdt.core.dom.ast.*;

import java.util.ArrayList;
import java.util.List;

public class StrlenNormalizer extends AbstractStatementNormalizer implements IStatementNormalizer {

    /**
     * Belong header <cstring>
     */
    public static final String STRLEN = "strlen";

    protected IVariableNodeTable tableVar;

    protected IFunctionConfig functionConfig;

    public static void main(String[] args) {
        IVariableNodeTable tableVar = new VariableNodeTable();
        OneLevelCharacterSymbolicVariable nameStudent = new OneLevelCharacterSymbolicVariable("str", "char*",
                ISymbolicVariable.GLOBAL_SCOPE);
        nameStudent.setSize("100");
        tableVar.cast().add(nameStudent);

        String[] tests = new String[]{"int len = strlen(str);"};

        for (String test : tests) {
            StrlenNormalizer norm = new StrlenNormalizer();
            norm.setTableVar(tableVar);
            norm.setOriginalSourcecode(test);
            norm.normalize();
            System.out.println(norm.getNormalizedSourcecode());
        }
    }

    @Override
    public void normalize() {
        normalizeSourcecode = originalSourcecode;
        IASTNode ast = Utils.convertToIAST(originalSourcecode);

        List<IASTFunctionCallExpression> functionCalls = new ArrayList<>();
        ASTVisitor visitor = new ASTVisitor() {

            @Override
            public int visit(IASTExpression statement) {
                if (statement instanceof IASTFunctionCallExpression) {
                    IASTFunctionCallExpression functionCall = (IASTFunctionCallExpression) statement;
                    functionCalls.add(functionCall);
                }
                return ASTVisitor.PROCESS_CONTINUE;
            }
        };

        visitor.shouldVisitExpressions = true;

        ast.accept(visitor);
        for (IASTFunctionCallExpression functionCall : functionCalls) {
            String functionName = functionCall.getFunctionNameExpression().getRawSignature();
            if (functionName.equals(StrlenNormalizer.STRLEN)) {
                IASTInitializerClause firstArgument = functionCall.getArguments()[0];
                String nameVar = firstArgument.getRawSignature();
                ISymbolicVariable var = tableVar.findorCreateVariableByName(nameVar);

                if (var != null && var instanceof OneLevelCharacterSymbolicVariable) {
                    String size = ((OneLevelCharacterSymbolicVariable) var).getSize();
                    normalizeSourcecode = normalizeSourcecode.replace(functionCall.getRawSignature(), size);
                }
            }
        }
    }

    public IVariableNodeTable getTableVar() {
        return tableVar;
    }

    public void setTableVar(IVariableNodeTable tableVar) {
        this.tableVar = tableVar;
    }

}
