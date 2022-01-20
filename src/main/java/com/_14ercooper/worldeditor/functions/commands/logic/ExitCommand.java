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

package com._14ercooper.worldeditor.functions.commands.logic;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

import java.util.List;

public class ExitCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
        function.exit = true;
        if (args.size() > 0) {
            if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("false")) {
                if (Boolean.parseBoolean(args.get(0)))
                    function.exitVal = 1;
                else function.exitVal = 0;
//		Main.logDebug("Function returning as boolean with value " + args.get(0));
            } else {
                try {
                    function.exitVal = function.parseVariable(args.get(0));
//		    Main.logDebug("Function returning as variable with value " + function.exitVal);
                } catch (Exception e) {
                    function.exitVal = Double.parseDouble(args.get(0));
//		    Main.logDebug("Function returning as double with value " + function.exitVal);
                }
            }
        }
    }
}
