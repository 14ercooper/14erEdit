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

package com._14ercooper.worldeditor.testing.dummies;

import com._14ercooper.worldeditor.undo.UndoElement;
import kotlin.coroutines.Continuation;
import org.bukkit.block.BlockState;

public class DummyUndoElement extends UndoElement {
    public DummyUndoElement() {
        super("dummy", new DummyUserUndo());
    }

    @Override
    public boolean addBlock(BlockState blockFrom, BlockState blockTo) {
        return false;
    }

    @Override
    public boolean startApplyUndo() {
        return false;
    }

    @Override
    public boolean applyUndo(long blockCount) {
        return false;
    }

    @Override
    public boolean finalizeUndo() {
        return false;
    }

    @Override
    public boolean startApplyRedo() {
        return false;
    }

    @Override
    public boolean applyRedo(long blockCount) {
        return false;
    }

    @Override
    public boolean finalizeRedo() {
        return false;
    }

    @Override
    public boolean setBlock(String world, int x, int y, int z, String type, String data, String nbt) {
        return false;
    }

    @Override
    public Object flush(Continuation<? super Boolean> suspend) {
        return false;
    }
}
