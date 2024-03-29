/**
 * This file is part of 14erEdit.
 *
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.DummyState;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TemplateNode extends Node {

    String filename;
    final List<String> args = new ArrayList<>();

    @Override
    public TemplateNode newNode(ParserState parserState) {
        TemplateNode node = new TemplateNode();
        node.filename = "plugins/14erEdit/templates/" + Parser.parseStringNode(parserState).getText();
        int argCount;
        if (parserState.getTestDummyState() == null) {
            argCount = (int) Parser.parseNumberNode(parserState).getValue(new DummyState(parserState.getCurrentPlayer()));
        }
        else {
            argCount = (int) Parser.parseNumberNode(parserState).getValue(parserState.getTestDummyState());
        }
        for (int i = 0; i < argCount; i++) {
            node.args.add(Parser.parseStringNode(parserState).getText());
        }

        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        CommandSender player = state.getCurrentPlayer();

        // Extension expansion
        if (Files.exists(Paths.get(filename))) {
            // Filename is good, keep it
        } else if (Files.exists(Paths.get(filename + ".txt"))) {
            filename += ".txt";
        } else if (Files.exists(Paths.get(filename + ".fx"))) {
            filename += ".fx";
        } else {
            Main.logError("Template not found.", player, null);
            return false;
        }

        // Grab the command
        String command;
        try {
            command = readFile(filename);
        } catch (IOException e) {
            Main.logError("Error reading template file.", player, e);
            return false;
        }

        // Clean up newlines
        command = command.replaceAll("[\\n\\r]+", " ");

        // Fill in template
        for (int i = args.size() - 1; i >= 0; i--) {
            String thisArg = args.get(i);
            // TODO migrate the args list to support parsing nodes as nodes, but this requires a refactor of how block nodes are parsed so that they know their "text" form
            if (thisArg.equalsIgnoreCase("x")) {
                thisArg = Integer.toString(state.getCurrentBlock().x);
            }
            else if (thisArg.equalsIgnoreCase("y")) {
                thisArg = Integer.toString(state.getCurrentBlock().y);
            }
            else if (thisArg.equalsIgnoreCase("z")) {
                thisArg = Integer.toString(state.getCurrentBlock().z);
            }
            command = command.replaceAll("\\$" + (i + 1), thisArg);
        }

        Main.logDebug("Template command: " + command);

        // Run the command
        try {
            if (player instanceof Player) {
                Player plyr = (Player) player;
                Location loc = plyr.getLocation();
                plyr.teleport(state.getCurrentBlock().block.getLocation());
                boolean retVal = Bukkit.dispatchCommand(player, command);
                plyr.teleport(loc);
                return retVal;
            }
            return false;
        } catch (Exception e) {
            Main.logError("Could not run command in template.", player, e);
            return false;
        }
    }

    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    @Override
    public int getArgCount() {
        return 2;
    }

}
