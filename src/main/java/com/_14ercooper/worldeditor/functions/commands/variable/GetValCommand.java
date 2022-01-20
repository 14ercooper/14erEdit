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

package com._14ercooper.worldeditor.functions.commands.variable;

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.functions.commands.InterpreterCommand;

import java.util.List;

public class GetValCommand extends InterpreterCommand {

    @Override
    public void run(List<String> args, Function function) {
//	function.variables.set(Integer.parseInt(args.get(0).replaceAll("\\$v", "")), Double.parseDouble(function.templateArgs.get(Integer.parseInt(args.get(1).replaceAll("\\$", "")))));
        function.setVariable(args.get(0), Double.parseDouble(function.templateArgs.get(Integer.parseInt(args.get(1).replaceAll("\\$", "")))));
    }
}
