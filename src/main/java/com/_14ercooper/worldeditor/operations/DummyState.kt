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

package com._14ercooper.worldeditor.operations

import com._14ercooper.worldeditor.blockiterator.BlockWrapper
import com._14ercooper.worldeditor.main.Main
import com._14ercooper.worldeditor.undo.UndoElement
import com._14ercooper.worldeditor.undo.UserUndo
import org.bukkit.Location
import org.bukkit.command.CommandSender

class DummyState(var currentPlayerThis: CommandSender) : OperatorState(
    BlockWrapper(null, 14, 14, 14),
    currentPlayerThis,
    Main.plugin!!.server.worlds[0],
    UndoElement("temp", UserUndo("dummy")),
    Location(Main.plugin!!.server.worlds[0], 14.0, 14.0, 14.0)
)