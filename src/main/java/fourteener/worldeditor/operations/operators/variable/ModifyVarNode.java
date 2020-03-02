package fourteener.worldeditor.operations.operators.variable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.BlockVar;
import fourteener.worldeditor.operations.type.ColorText;
import fourteener.worldeditor.operations.type.ItemVar;
import fourteener.worldeditor.operations.type.MonsterVar;
import fourteener.worldeditor.operations.type.NumericVar;
import fourteener.worldeditor.operations.type.SpawnerVar;

public class ModifyVarNode extends Node {
	
	String type, name;
	List<String> mod;
	
	public static ModifyVarNode newNode (String arg1, String arg2, String arg3) {
		ModifyVarNode node = new ModifyVarNode();
		node.type = arg1;
		node.name = arg2;
		node.mod = Arrays.asList(arg3.split(","));
		return node;
	}
	
	public boolean performNode () {
		// Modify a numeric
		if (type.equalsIgnoreCase("num")) {
			// Set mod
			if (mod.get(0).equalsIgnoreCase("set")) {
				NumericVar var = Operator.numericVars.get(name);
				var.setValue(Integer.parseInt(mod.get(1)));
				Operator.numericVars.put(name, var);
				return true;
			}
			// Increment by 1 mod
			if (mod.get(0).equalsIgnoreCase("+")) {
				NumericVar var = Operator.numericVars.get(name);
				var.setValue(var.getValue() + 1);
				Operator.numericVars.put(name, var);
				return true;
			}
			// Increment mod
			if (mod.get(0).equalsIgnoreCase("inc")) {
				NumericVar var = Operator.numericVars.get(name);
				var.setValue(var.getValue() + Integer.parseInt(mod.get(1)));
				Operator.numericVars.put(name, var);
				return true;
			}
			// Multiply mod
			if (mod.get(0).equalsIgnoreCase("mult")) {
				NumericVar var = Operator.numericVars.get(name);
				var.setValue(var.getValue() * Integer.parseInt(mod.get(1)));
				Operator.numericVars.put(name, var);
				return true;
			}
			// Power mod
			if (mod.get(0).equalsIgnoreCase("pow")) {
				NumericVar var = Operator.numericVars.get(name);
				var.setValue(Math.pow(var.getValue(), Integer.parseInt(mod.get(1))));
				Operator.numericVars.put(name, var);
				return true;
			}
			// Exponent mod
			if (mod.get(0).equalsIgnoreCase("exp")) {
				NumericVar var = Operator.numericVars.get(name);
				var.setValue(Math.pow(Integer.parseInt(mod.get(1)), var.getValue()));
				Operator.numericVars.put(name, var);
				return true;
			}
			// Truncate mod
			if (mod.get(0).equalsIgnoreCase("trn")) {
				NumericVar var = Operator.numericVars.get(name);
				var.setValue(Math.floor(var.getValue()));
				Operator.numericVars.put(name, var);
				return true;
			}
			
			return false;
		}
		
		// Modify a block
		if (type.equalsIgnoreCase("blk")) {
			// Type mod
			if (mod.get(0).equalsIgnoreCase("type")) {
				BlockVar var = Operator.blockVars.get(name);
				var.setType(mod.get(1));
				Operator.blockVars.put(name, var);
				return true;
			}
			// Data mod
			if (mod.get(0).equalsIgnoreCase("data")) {
				BlockVar var = Operator.blockVars.get(name);
				var.setData(mod.get(1));
				Operator.blockVars.put(name, var);
				return true;
			}
			// NBT mod
			if (mod.get(0).equalsIgnoreCase("nbt")) {
				BlockVar var = Operator.blockVars.get(name);
				var.setNbt(mod.get(1));
				Operator.blockVars.put(name, var);
				return true;
			}
			
			return false;
		}
		
		// Modify an item
		if (type.equalsIgnoreCase("itm")) {
			// Type mod
			if (mod.get(0).equalsIgnoreCase("type")) {
				ItemVar var = Operator.itemVars.get(name);
				var.setType(mod.get(1));
				Operator.itemVars.put(name, var);
				return true;
			}
			// Name mod
			if (mod.get(0).equalsIgnoreCase("name")) {
				if (mod.get(1).equalsIgnoreCase("color")) {
					ItemVar var = Operator.itemVars.get(name);
					ColorText ct = new ColorText(mod.get(2), mod.get(3));
					if (mod.size() > 4) {
						ct.setBold(mod.get(4));
					}
					if (mod.size() > 5) {
						ct.setItalic(mod.get(5));
					}
					if (mod.size() > 6) {
						ct.setUnderlined(mod.get(6));
					}
					if (mod.size() > 7) {
						ct.setStrikethrough(mod.get(7));
					}
					if (mod.size() > 8) {
						ct.setObfuscated(mod.get(8));
					}
					var.setName(ct);
					Operator.itemVars.put(name, var);
				}
				else {
					ItemVar var = Operator.itemVars.get(name);
					var.setName(new ColorText(mod.get(1)));
					Operator.itemVars.put(name, var);
				}
				return true;
			}
			// Lore mod
			if (mod.get(0).equalsIgnoreCase("lore")) {
				ItemVar var = Operator.itemVars.get(name);
				List<ColorText> ls = var.getLore();
				if (mod.get(1).equalsIgnoreCase("color")) {
					ColorText ct = new ColorText(mod.get(2), mod.get(3));
					if (mod.size() > 4) {
						ct.setBold(mod.get(4));
					}
					if (mod.size() > 5) {
						ct.setItalic(mod.get(5));
					}
					if (mod.size() > 6) {
						ct.setUnderlined(mod.get(6));
					}
					if (mod.size() > 7) {
						ct.setStrikethrough(mod.get(7));
					}
					if (mod.size() > 8) {
						ct.setObfuscated(mod.get(8));
					}
					ls.add(ct);
				}
				else {
					ls.add(new ColorText(mod.get(1)));
				}
				var.setLore(ls);
				Operator.itemVars.put(name, var);
				return true;
			}
			// Enchant mod
			if (mod.get(0).equalsIgnoreCase("ench")) {
				ItemVar var = Operator.itemVars.get(name);
				Map<String, Integer> mp = var.getEnchants();
				mp.put(mod.get(1), Integer.parseInt(mod.get(2)));
				var.setEnchants(mp);
				Operator.itemVars.put(name, var);
				return true;
			}
			// Attribute mod
			if (mod.get(0).equalsIgnoreCase("attr")) {
				ItemVar var = Operator.itemVars.get(name);
				Map<String, String> mp = var.getAttributes();
				mp.put(mod.get(1), mod.get(2) + "," + mod.get(3) + "," + mod.get(4));
				var.setAttributes(mp);
				Operator.itemVars.put(name, var);
				return true;
			}
			// Count mod
			if (mod.get(0).equalsIgnoreCase("cnt")) {
				ItemVar var = Operator.itemVars.get(name);
				var.setCount(Integer.parseInt(mod.get(1)));
				Operator.itemVars.put(name, var);
				return true;
			}
			// Damage mod
			if (mod.get(0).equalsIgnoreCase("dmg")) {
				ItemVar var = Operator.itemVars.get(name);
				var.setDamage(Integer.parseInt(mod.get(1)));
				Operator.itemVars.put(name, var);
				return true;
			}
			// Flag mod
			if (mod.get(0).equalsIgnoreCase("flag")) {
				ItemVar var = Operator.itemVars.get(name);
				var.setFlags(Integer.parseInt(mod.get(1)));
				Operator.itemVars.put(name, var);
				return true;
			}
			// Unbreakable mod
			if (mod.get(0).equalsIgnoreCase("unbreak")) {
				ItemVar var = Operator.itemVars.get(name);
				if (mod.get(1).equalsIgnoreCase("true")) {
					var.setUnbreakable(true);
				}
				else if (mod.get(1).equalsIgnoreCase("false")) {
					var.setUnbreakable(false);
				}
				Operator.itemVars.put(name, var);
				return true;
			}
			
			return false;
		}

		
		// Modify a monster
		if (type.equalsIgnoreCase("mob")) {
			// Type mod
			if (mod.get(0).equalsIgnoreCase("type")) {
				MonsterVar var = Operator.monsterVars.get(name);
				var.setType(mod.get(1));
				Operator.monsterVars.put(name, var);
				return true;
			}
			// Name mod
			if (mod.get(0).equalsIgnoreCase("name")) {
				if (mod.get(1).equalsIgnoreCase("color")) {
					MonsterVar var = Operator.monsterVars.get(name);
					ColorText ct = new ColorText(mod.get(2), mod.get(3));
					if (mod.size() > 4) {
						ct.setBold(mod.get(4));
					}
					if (mod.size() > 5) {
						ct.setItalic(mod.get(5));
					}
					if (mod.size() > 6) {
						ct.setUnderlined(mod.get(6));
					}
					if (mod.size() > 7) {
						ct.setStrikethrough(mod.get(7));
					}
					if (mod.size() > 8) {
						ct.setObfuscated(mod.get(8));
					}
					var.setName(ct);
					Operator.monsterVars.put(name, var);
				}
				else {
					MonsterVar var = Operator.monsterVars.get(name);
					var.setName(new ColorText(mod.get(1)));
					Operator.monsterVars.put(name, var);
				}
				return true;
			}
			// Base mod
			if (mod.get(0).equalsIgnoreCase("base")) {
				MonsterVar var = Operator.monsterVars.get(name);
				var.setBase(mod.get(1), mod.get(2));
				Operator.monsterVars.put(name, var);
				return true;
			}
			// Attribute mod
			if (mod.get(0).equalsIgnoreCase("attr")) {
				MonsterVar var = Operator.monsterVars.get(name);
				var.setAttribute(mod.get(1), mod.get(2));
				Operator.monsterVars.put(name, var);
				return true;
			}
			// Gear mod
			if (mod.get(0).equalsIgnoreCase("gear")) {
				MonsterVar var = Operator.monsterVars.get(name);
				var.setGear(mod.get(1), mod.get(2));
				Operator.monsterVars.put(name, var);
				return true;
			}
			// Drop chance mod
			if (mod.get(0).equalsIgnoreCase("drop")) {
				MonsterVar var = Operator.monsterVars.get(name);
				var.setDrop(mod.get(1), mod.get(2));
				Operator.monsterVars.put(name, var);
				return true;
			}
			// Passenger mod
			if (mod.get(0).equalsIgnoreCase("pass")) {
				MonsterVar var = Operator.monsterVars.get(name);
				var.addPassenger(mod.get(1));
				Operator.monsterVars.put(name, var);
				return true;
			}
			// Tag mod
			if (mod.get(0).equalsIgnoreCase("tag")) {
				MonsterVar var = Operator.monsterVars.get(name);
				var.addTag(mod.get(1));
				Operator.monsterVars.put(name, var);
				return true;
			}
			return false;
		}
		
		// Modify a spawner
		if (type.equalsIgnoreCase("spwn")) {
			// Count mod
			if (mod.get(0).equalsIgnoreCase("cnt")) {
				SpawnerVar var = Operator.spawnerVars.get(name);
				var.setCount(mod.get(1));
				Operator.spawnerVars.put(name, var);
				return true;
			}
			// Range mod
			if (mod.get(0).equalsIgnoreCase("rng")) {
				SpawnerVar var = Operator.spawnerVars.get(name);
				var.setRange(mod.get(1));
				Operator.spawnerVars.put(name, var);
				return true;
			}
			// Delay mod
			if (mod.get(0).equalsIgnoreCase("del")) {
				SpawnerVar var = Operator.spawnerVars.get(name);
				var.setDelay(mod.get(1));
				Operator.spawnerVars.put(name, var);
				return true;
			}
			// Min delay mod
			if (mod.get(0).equalsIgnoreCase("mindel")) {
				SpawnerVar var = Operator.spawnerVars.get(name);
				var.setMinDelay(mod.get(1));
				Operator.spawnerVars.put(name, var);
				return true;
			}
			// Max delay mod
			if (mod.get(0).equalsIgnoreCase("maxdel")) {
				SpawnerVar var = Operator.spawnerVars.get(name);
				var.setMaxDelay(mod.get(1));
				Operator.spawnerVars.put(name, var);
				return true;
			}
			// Max nearby mod
			if (mod.get(0).equalsIgnoreCase("maxnear")) {
				SpawnerVar var = Operator.spawnerVars.get(name);
				var.setMaxNearby(mod.get(1));
				Operator.spawnerVars.put(name, var);
				return true;
			}
			// Player range mod
			if (mod.get(0).equalsIgnoreCase("plrrng")) {
				SpawnerVar var = Operator.spawnerVars.get(name);
				var.setRequiredRange(mod.get(1));
				Operator.spawnerVars.put(name, var);
				return true;
			}
			// Add mob mod
			if (mod.get(0).equalsIgnoreCase("mob")) {
				SpawnerVar var = Operator.spawnerVars.get(name);
				var.addMob(mod.get(1), mod.get(2));
				Operator.spawnerVars.put(name, var);
				return true;
			}
			
			return false;
		}
		
		// Invalid type
		return false;
	}
	
	public static int getArgCount () {
		return 3;
	}
}
