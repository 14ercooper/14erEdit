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

package com._14ercooper.worldeditor.functions;

import com._14ercooper.worldeditor.functions.commands.logic.*;
import com._14ercooper.worldeditor.functions.commands.math.*;
import com._14ercooper.worldeditor.functions.commands.player.*;
import com._14ercooper.worldeditor.functions.commands.variable.*;
import com._14ercooper.worldeditor.functions.commands.world.BlockCommand;

public class RegisterFunctions {
    public static void RegisterAll() {
        RegisterPlayer();
        RegisterWorld();
        RegisterLogic();
        RegisterVariable();
        RegisterMath();
    }

    private static void RegisterPlayer() {
        Function.commands.put("prtdbg", new PrintDebugCommand());
        Function.commands.put("prterr", new PrintErrorCommand());
        Function.commands.put("setslot", new SetSlotCommand());
        Function.commands.put("swap", new SwapCommand());
        Function.commands.put("getpos", new GetPosCommand());
        Function.commands.put("gettarget", new GetTargetCommand());
    }

    private static void RegisterWorld() {
        Function.commands.put("blk", new BlockCommand());
    }

    private static void RegisterLogic() {
        Function.commands.put("cmpvar", new CompareVariableCommand());
        Function.commands.put("cmptext", new CompareTextCommand());
        Function.commands.put("cmpslot", new CompareSlotCommand());
        Function.commands.put("goif", new GoIfCommand());
        Function.commands.put("goifnot", new GoIfNotCommand());
        Function.commands.put("return", new ReturnCommand());
        Function.commands.put("exit", new ExitCommand());
        Function.commands.put("wait", new WaitCommand());
        Function.commands.put("waittime", new WaitTimeCommand());
    }

    private static void RegisterVariable() {
        Function.commands.put("store", new StoreCommand());
        Function.commands.put("get", new GetCommand());
        Function.commands.put("remove", new RemoveCommand());
        Function.commands.put("getval", new GetValCommand());
        Function.commands.put("defaultarg", new DefaultArgCommand());
    }

    private static void RegisterMath() {
        Function.commands.put("setvar", new SetVarCommand());
        Function.commands.put("rand", new RandCommand());
        Function.commands.put("inc", new IncrementCommand());
        Function.commands.put("dec", new DecrementCommand());
        Function.commands.put("add", new AddCommand());
        Function.commands.put("sub", new SubtractCommand());
        Function.commands.put("mult", new MultiplyCommand());
        Function.commands.put("mod", new ModCommand());
        Function.commands.put("div", new DivideCommand());
        Function.commands.put("sin", new SineCommand());
        Function.commands.put("cos", new CosineCommand());
        Function.commands.put("pow", new PowerCommand());
        Function.commands.put("ceil", new CeilingCommand());
        Function.commands.put("floor", new FloorCommand());
        Function.commands.put("round", new RoundCommand());
        Function.commands.put("single", new SingleMathCommand());
        Function.commands.put("double", new DoubleMathCommand());
        Function.commands.put("log", new LogCommand());
        Function.commands.put("asin", new ArcsineCommand());
        Function.commands.put("acos", new ArccosineCommand());
        Function.commands.put("asinh", new HyperbolicSineCommand());
        Function.commands.put("acosh", new HyperbolicCosineCommand());
    }
}
