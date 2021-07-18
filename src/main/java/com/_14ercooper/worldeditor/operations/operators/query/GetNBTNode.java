package com._14ercooper.worldeditor.operations.operators.query;

import com._14ercooper.worldeditor.main.NBTExtractor;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.core.StringNode;
import org.bukkit.command.CommandSender;

public class GetNBTNode extends StringNode {

    String contents = "";

    @Override
    public GetNBTNode newNode(CommandSender currentPlayer) {
        return new GetNBTNode();
    }

    @Override
    public boolean performNode(OperatorState state) {
        String s = (new NBTExtractor()).getNBT(state.getCurrentBlock());
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
