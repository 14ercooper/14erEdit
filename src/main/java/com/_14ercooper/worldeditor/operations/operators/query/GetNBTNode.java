package com._14ercooper.worldeditor.operations.operators.query;

import com._14ercooper.worldeditor.main.NBTExtractor;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.core.StringNode;

public class GetNBTNode extends StringNode {

    String contents = "";

    @Override
    public GetNBTNode newNode() {
	return new GetNBTNode();
    }

    @Override
    public boolean performNode() {
	String s = (new NBTExtractor()).getNBT(Operator.currentBlock);
	contents = s;
	return s.length() > 2;
    }

    @Override
    public String getText() {
	return contents;
    }

    @Override
    public int getArgCount() {
	return 0;
    }

}
