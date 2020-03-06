package fourteener.worldeditor.operations.type;

import java.util.HashMap;
import java.util.Map;

import fourteener.worldeditor.operations.Operator;

public class SpawnerVar {
	String spawnCount = "2";
	String spawnRange = "6";
	String delay = "0";
	String minDelay = "200";
	String maxDelay = "800";
	String maxNearby = "8";
	String requiredRange = "16";
	Map<String, String> mobs = new HashMap<String, String>();
	String firstName = "";
	
	public String getNBT() {
		String s = "{";
		// Spawner variables
		s += "SpawnCount:" + spawnCount;
		s += ",SpawnRange:" + spawnRange;
		s += ",Delay:" + delay;
		s += ",MinSpawnDelay:" + minDelay;
		s += ",MaxSpawnDelay:" + maxDelay;
		s += ",MaxNearbyEntities:" + maxNearby;
		s += ",RequiredPlayerRange:" + requiredRange;
		// First mob to spawn
		s += ",SpawnData:" + Operator.monsterVars.get(firstName).asNBT();
		// Spawn potentials
		s += ",SpawnPotentials:[";
		for (Map.Entry<String, String> entry : mobs.entrySet()) {
			s += "{Weight:" + entry.getValue();
			s += ",Entity:" + Operator.monsterVars.get(entry.getKey()).asNBT();
			s += "},";
		}
		s = s.substring(0, s.length() - 1);
		s += "]";
		s += "}";
		return s;
	}
	
	public void setCount(String cnt) {
		spawnCount = cnt;
	}
	public void setRange(String rng) {
		spawnRange = rng;
	}
	public void setDelay(String del) {
		delay = del;
	}
	public void setMinDelay(String minDel) {
		minDelay = minDel;
	}
	public void setMaxDelay(String maxDel) {
		maxDelay = maxDel;
	}
	public void setMaxNearby(String nearby) {
		maxNearby = nearby;
	}
	public void setRequiredRange(String range) {
		requiredRange = range;
	}
	public void addMob(String name, String weight) {
		mobs.put(name, weight);
		firstName = name;
	}
}
