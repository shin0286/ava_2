package com.fit.normalizer;

import com.fit.SummaryGeneration;
import com.fit.gui.main.GUIController;
import com.fit.tree.object.IFunctionNode;

public class IntegrationNormalizer{
    String projectPath = GUIController.projectPath;
    String summaryFile = GUIController.projectPath + GUIController.projectName + "_summary.xml";
    String function = GUIController.functionName;

    public IntegrationNormalizer() throws Exception{
        SummaryGeneration sm = new SummaryGeneration("F:\\New folder\\Sample_for_R1_2.xml","F:\\New folder\\ava_ver2\\data-test\\tsdv\\Sample_for_R1_2\\", "main()");
        IFunctionNode function = sm.getFunctionNode("F:\\New folder\\ava_ver2\\data-test\\tsdv\\Sample_for_R1_2\\", "main()");
        FunctionCallNormalizer fn = new FunctionCallNormalizer(function);
        fn.normalize();
        System.out.println(fn.getNormalizedSourcecode());
    }
    public static void main(String[] args) throws Exception{
        IntegrationNormalizer in = new IntegrationNormalizer();
    }
}
