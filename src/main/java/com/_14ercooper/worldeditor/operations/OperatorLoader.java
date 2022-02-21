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

package com._14ercooper.worldeditor.operations;

import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.*;
import com._14ercooper.worldeditor.operations.operators.fun.SmallRuinNode;
import com._14ercooper.worldeditor.operations.operators.function.*;
import com._14ercooper.worldeditor.operations.operators.logical.*;
import com._14ercooper.worldeditor.operations.operators.loop.WhileNode;
import com._14ercooper.worldeditor.operations.operators.math.*;
import com._14ercooper.worldeditor.operations.operators.query.*;
import com._14ercooper.worldeditor.operations.operators.world.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class OperatorLoader {
    public static void LoadOperators(CommandSender commandSender) {
        LoadCore(commandSender);
        LoadFunction(commandSender);
        LoadLogical(commandSender);
        LoadLoop(commandSender);
        LoadQuery(commandSender);
        LoadWorld(commandSender);
        LoadFun(commandSender);
        LoadMath(commandSender);
    }

    public static void LoadOperators() {
        LoadOperators(Bukkit.getConsoleSender());
    }

    public static List<String> nodeNames = new ArrayList<>();
    public static List<String> blockNodeNames = new ArrayList<>();
    public static List<String> numberNodeNames = new ArrayList<>();
    public static List<String> rangeNodeNames = new ArrayList<>();
    public static List<String> nextRange = new ArrayList<>();
    public static List<String> nextBlock = new ArrayList<>();

    private static void loadNode(String name, Node node) {
        nodeNames.add(name);
        if (node instanceof BlockNode) {
            blockNodeNames.add(name);
        }
        if (node instanceof NumberNode) {
            numberNodeNames.add(name);
        }
        if (node instanceof RangeNode) {
            rangeNodeNames.add(name);
        }
        if (node.isNextNodeRange()) {
            nextRange.add(name);
        }
        if (node.isNextNodeBlock()) {
            nextBlock.add(name);
        }
        Parser.addOperator(name, node);
    }

    private static void LoadCore(CommandSender commandSender) {
        loadNode("$$", new CraftscriptNode());
        loadNode("craftscript", Parser.getOperator(commandSender, "$$"));
        loadNode("script", Parser.getOperator(commandSender, "$$"));
        loadNode("/", new LinkerNode());
        loadNode("link", Parser.getOperator(commandSender, "/"));
        loadNode("$", new MacroNode());
        loadNode("macro", Parser.getOperator(commandSender, "$"));
        loadNode("x", new XNode());
        loadNode("y", new YNode());
        loadNode("z", new ZNode());
        loadNode("br", new BrushNode());
        loadNode("brush", Parser.getOperator(commandSender, "br"));
    }

    private static void LoadFunction(CommandSender commandSender) {
        loadNode("%", new OddsNode());
        loadNode("odds", Parser.getOperator(commandSender, "%"));
        loadNode("chance", Parser.getOperator(commandSender, "%"));
        loadNode("-", new RangeNode());
        loadNode("range", Parser.getOperator(commandSender, "-"));
        loadNode("inrange", Parser.getOperator(commandSender, "-"));
        loadNode("in_range", Parser.getOperator(commandSender, "-"));
        loadNode(";", new RemainderNode());
        loadNode("rem", Parser.getOperator(commandSender, ";"));
        loadNode("remainder", Parser.getOperator(commandSender, ";"));
        loadNode("mod", new ModulusNode());
        loadNode("limit", new LimitNode());
        loadNode(";;", new EveryXNode());
        loadNode("every", Parser.getOperator(commandSender, ";;"));
        loadNode("step", Parser.getOperator(commandSender, ";;"));
        loadNode("#", new SimplexNode());
        loadNode("simplex", Parser.getOperator(commandSender, "#"));
        loadNode("##", new NoiseNode());
        loadNode("noise", Parser.getOperator(commandSender, "##"));
        loadNode("#a", new NoiseAtNode());
        loadNode("noiseat", Parser.getOperator(commandSender, "#a"));
        loadNode("fx", new FunctionNode());
        loadNode("function", Parser.getOperator(commandSender, "fx"));
        loadNode("m#", new MultiNoiseNode());
        loadNode("multinoise", Parser.getOperator(commandSender, "m#"));
    }

    private static void LoadLogical(CommandSender commandSender) {
        loadNode("&", new AndNode());
        loadNode("and", Parser.getOperator(commandSender, "&"));
        loadNode("both", Parser.getOperator(commandSender, "&"));
        loadNode("false", new FalseNode());
        loadNode("?", new IfNode());
        loadNode("if", Parser.getOperator(commandSender, "?"));
        loadNode(":", new ElseNode());
        loadNode("else", Parser.getOperator(commandSender, ":"));
        loadNode("~", new NotNode());
        loadNode("not", Parser.getOperator(commandSender, "~"));
        loadNode("|", new OrNode());
        loadNode("or", Parser.getOperator(commandSender, "|"));
        loadNode("either", Parser.getOperator(commandSender, "|"));
        loadNode("true", new TrueNode());
        loadNode("<", new XorNode());
        loadNode("xor", Parser.getOperator(commandSender, "<"));
        loadNode("one_of", Parser.getOperator(commandSender, "<"));
        loadNode("anyof", new AnyOfNode());
        loadNode("any_of", Parser.getOperator(commandSender, "anyof"));
    }

    private static void LoadLoop(CommandSender commandSender) {
        loadNode("loop", new WhileNode());
        loadNode("while", Parser.getOperator(commandSender, "loop"));
    }

    private static void LoadQuery(CommandSender commandSender) {
        loadNode("@", new BlocksAdjacentNode());
        loadNode("adj", Parser.getOperator(commandSender, "@"));
        loadNode("adjacent", Parser.getOperator(commandSender, "@"));
        loadNode("blocks_adjacent", Parser.getOperator(commandSender, "@"));
        loadNode("^", new BlocksAboveNode());
        loadNode("blocks_above", Parser.getOperator(commandSender, "^"));
        loadNode("above", Parser.getOperator(commandSender, "^"));
        loadNode("_", new BlocksBelowNode());
        loadNode("blocks_below", Parser.getOperator(commandSender, "_"));
        loadNode("below", Parser.getOperator(commandSender, "_"));
        loadNode("*", new FacesExposedNode());
        loadNode("exposed", Parser.getOperator(commandSender, "*"));
        loadNode("faces_exposed", Parser.getOperator(commandSender, "*"));
        loadNode("<<n", new GetNBTNode());
        loadNode("get_nbt", Parser.getOperator(commandSender, "<<n"));
        loadNode("at", new BlockAtNode());
        loadNode("block_at", Parser.getOperator(commandSender, "at"));
        loadNode("sky", new SkylightNode());
        loadNode("skylight", Parser.getOperator(commandSender, "sky"));
        loadNode("bl", new BlocklightNode());
        loadNode("blocklight", Parser.getOperator(commandSender, "bl"));
        loadNode("ang", new AngleNode());
        loadNode("angle", Parser.getOperator(commandSender, "ang"));
        loadNode("slope", Parser.getOperator(commandSender, "ang"));
        loadNode("biome_is", new CheckBiomeNode());
        loadNode("get_biome", Parser.getOperator(commandSender, "biome_is"));
        loadNode("is_biome", Parser.getOperator(commandSender, "biome_is"));
        loadNode("@@", new NearbyNode());
        loadNode("near", Parser.getOperator(commandSender, "@@"));
        loadNode("nearby", Parser.getOperator(commandSender, "@@"));
        loadNode("@v", new BlocksAdjacentVerticalNode());
        loadNode("adjv", Parser.getOperator(commandSender, "@v"));
        loadNode("adjacent_vertical", Parser.getOperator(commandSender, "@v"));
        loadNode("@h", new BlocksAdjacentHorizontalNode());
        loadNode("adjh", Parser.getOperator(commandSender, "@h"));
        loadNode("adjacent_horizontal", Parser.getOperator(commandSender, "@h"));
        loadNode("@d", new BlocksAdjacentHorizontalNode());
        loadNode("adjd", Parser.getOperator(commandSender, "@h"));
        loadNode("adjacent_diagonal", Parser.getOperator(commandSender, "@h"));
    }

    private static void LoadWorld(CommandSender commandSender) {
        loadNode("<<", new GetBlockDataNode());
        loadNode("get_data", Parser.getOperator(commandSender, "<<"));
        loadNode("data", Parser.getOperator(commandSender, "<<"));
        loadNode("get_block_data", Parser.getOperator(commandSender, "<<"));
        loadNode("!", new IgnorePhysicsNode());
        loadNode("physics", Parser.getOperator(commandSender, "!"));
        loadNode("ignore_physcics", Parser.getOperator(commandSender, "!"));
        loadNode("same", new SameNode());
        loadNode(">>n", new SetNBTNode());
        loadNode("set_nbt", Parser.getOperator(commandSender, ">>n"));
        loadNode(">", new SetNode());
        loadNode("set", Parser.getOperator(commandSender, ">"));
        loadNode("set_block", Parser.getOperator(commandSender, ">"));
        loadNode(">>", Parser.getOperator(commandSender, ">"));
        loadNode("grav", new GravityNode());
        loadNode("gravity", Parser.getOperator(commandSender, "grav"));
        loadNode("biome", new SetBiomeNode());
        loadNode("set_biome", Parser.getOperator(commandSender, "biome"));
        loadNode("schem", new SchemBlockNode());
        loadNode("schematic", Parser.getOperator(commandSender, "schem"));
        loadNode(">r", new ReplaceNode());
        loadNode("replace", Parser.getOperator(commandSender, ">r"));
        loadNode("bonemeal", new BonemealNode());
        loadNode("bone", Parser.getOperator(commandSender, "bonemeal"));
    }

    private static void LoadFun(CommandSender commandSender) {
        loadNode("smallruin", new SmallRuinNode());
        loadNode("template", new TemplateNode());
        loadNode("tpl", Parser.getOperator(commandSender, "template"));
    }

    private static void LoadMath(CommandSender commandSender) {
        loadNode("rx", new RXNode());
        loadNode("ry", new RYNode());
        loadNode("rz", new RZNode());
        loadNode("r", new RadiusNode());
        loadNode("r2", new RadiusSquaredNode());
        loadNode("theta", new ThetaNode());
        loadNode("phi", new PhiNode());
        loadNode("add", new AddNode());
        loadNode("sub", new SubtractNode());
        loadNode("mult", new MultiplyNode());
        loadNode("div", new DivideNode());
        loadNode("neg", new NegateNode());
        loadNode("sin", new SineNode());
        loadNode("cos", new CosineNode());
        loadNode("tan", new TangentNode());
        loadNode("cot", new CotangentNode());
        loadNode("sec", new SecantNode());
        loadNode("csc", new CosecantNode());
        loadNode("sqrt", new SquareRootNode());
        loadNode("square", new SquareNode());
        loadNode("abs", new AbsoluteValueNode());
        loadNode("log10", new Log10Node());
        loadNode("log2", new Log2Node());
        loadNode("ln", new NaturalLogNode());
        loadNode("2deg", new ToDegreesNode());
        loadNode("2rad", new ToRadiansNode());
        loadNode("asin", new ArcsineNode());
        loadNode("acos", new ArccosineNode());
        loadNode("atan", new ArctangentNode());
        loadNode("ceil", new CeilingNode());
        loadNode("floor", new FloorNode());
        loadNode("round", new RoundNode());
        loadNode("sinh", new HyperbolicSineNode());
        loadNode("cosh", new HyperbolicCosineNode());
        loadNode("tanh", new HyperbolicTangentNode());
        loadNode("sign", new SignNode());
        loadNode("sigmoid", new SigmoidNode());
        loadNode("e", new ENode());
        loadNode("pi", new PiNode());
        loadNode("rand", new RandomNode());
        loadNode("log", new LogarithmNode());
        loadNode("pow", new PowerNode());
        loadNode("eq", new EqualsNode());
        loadNode("equals", Parser.getOperator(commandSender, "eq"));
        loadNode("lt", new LessThanNode());
        loadNode("lessthan", Parser.getOperator(commandSender, "lt"));
        loadNode("gt", new GreaterThanNode());
        loadNode("greaterthan", Parser.getOperator(commandSender, "gt"));
        loadNode("between", new BetweenNode());
    }
}
