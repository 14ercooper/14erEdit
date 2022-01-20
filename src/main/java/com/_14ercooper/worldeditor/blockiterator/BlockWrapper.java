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

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockWrapper {
    public Block block;
    public int x, y, z;
    public List<String> otherArgs = new ArrayList<>();

    public BlockWrapper(Block block, int x, int y, int z) {
        this.block = block;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
