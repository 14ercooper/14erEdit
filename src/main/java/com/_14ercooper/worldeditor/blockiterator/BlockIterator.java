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

package com._14ercooper.worldeditor.blockiterator;

import com._14ercooper.worldeditor.player.PlayerManager;
import com._14ercooper.worldeditor.player.PlayerWrapper;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BlockIterator {

    public Map<String, Object> objectArgs = new HashMap<>();

    private int originX, originY, originZ;

    // Returns a new instance of the block iterator based on the passed arguments
    // First 3 are the origin of the iterator, the rest vary
    public abstract BlockIterator newIterator(List<String> args, World world, CommandSender player);

    public void setOrigin(int originX, int originY, int originZ) {
        this.originX = originX;
        this.originY= originY;
        this.originZ = originZ;
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public int getOriginZ() {
        return originZ;
    }

    public void setObjectArgs(String key, Object value) {
        objectArgs.put(key, value);
    }

    // Gets the next block in this block iterator
    public abstract BlockWrapper getNextBlock(CommandSender player, boolean getBlock);

    public List<BlockWrapper> getNext(int num, CommandSender player, boolean getBlock) {
        List<BlockWrapper> blocks = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            BlockWrapper next = getNextBlock(player, getBlock);
            if (next != null) {
                blocks.add(next);
            } else {
                break;
            }
        }
        return blocks;
    }

    // Gets the number of total blocks in this iterator
    // This cannot underestimate, but may slightly overestimate
    public abstract long getTotalBlocks();

    // Gets the number of remaining blocks in this iterator
    public abstract long getRemainingBlocks();

    // Increment cubic XYZ. Returns true when time to end
    public int x, y, z;
    public long doneBlocks = 0;
    protected World iterWorld;

    public boolean incrXYZ(int radX, int radY, int radZ, int xOff, int yOff, int zOff, CommandSender player) {

        PlayerWrapper playerManager = PlayerManager.getPlayerWrapper(player);

        x++;
        doneBlocks++;
        if (x > radX || x + xOff > playerManager.getMaxEditX()) {
            y++;
            x = -radX;

            if (x + xOff < playerManager.getMinEditX()) {
                x = (int) playerManager.getMinEditX() - xOff;
            }
        }
        if (y > radY || y + yOff > playerManager.getMaxEditY()) {
            z++;

            if (z + zOff < playerManager.getMinEditZ()) {
                z = (int) playerManager.getMinEditZ() - zOff;
            }

            y = -radY;
            while (y + yOff < playerManager.getMinEditY()) {
                y++;
                doneBlocks++;
            }
        }

        return z > radZ || z + zOff > playerManager.getMaxEditZ();
    }
}
