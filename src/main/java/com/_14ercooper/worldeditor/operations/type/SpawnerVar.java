package com._14ercooper.worldeditor.operations.type;

import com._14ercooper.worldeditor.operations.Operator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SpawnerVar implements Serializable {
    private static final long serialVersionUID = 1L;

    String spawnCount = "2";
    String spawnRange = "6";
    String delay = "0";
    String minDelay = "200";
    String maxDelay = "800";
    String maxNearby = "8";
    String requiredRange = "16";
    final Map<String, String> mobs = new HashMap<>();
    String firstName = "";

    public String getNBT() {
        StringBuilder s = new StringBuilder("{");
        // Spawner variables
        s.append("SpawnCount:").append(spawnCount);
        s.append(",SpawnRange:").append(spawnRange);
        s.append(",Delay:").append(delay);
        s.append(",MinSpawnDelay:").append(minDelay);
        s.append(",MaxSpawnDelay:").append(maxDelay);
        s.append(",MaxNearbyEntities:").append(maxNearby);
        s.append(",RequiredPlayerRange:").append(requiredRange);
        // First mob to spawn
        s.append(",SpawnData:").append(Operator.monsterVars.get(firstName).asNBT());
        // Spawn potentials
        s.append(",SpawnPotentials:[");
        for (Map.Entry<String, String> entry : mobs.entrySet()) {
            s.append("{Weight:").append(entry.getValue());
            s.append(",Entity:").append(Operator.monsterVars.get(entry.getKey()).asNBT());
            s.append("},");
        }
        s = new StringBuilder(s.substring(0, s.length() - 1));
        s.append("]");
        s.append("}");
        return s.toString();
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
