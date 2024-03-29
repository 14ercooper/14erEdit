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

package com._14ercooper.worldeditor.macros.macros.nature;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.macros.MacroLauncher;
import com._14ercooper.worldeditor.macros.macros.Macro;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.OperatorState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ErodeMacro extends Macro {

    public int erodeRadius = -1; // The radius to actually erode within
    public int erodeType = -1; // 0 for melt, 1 for blendball, 2 for mix
    public int erodeSubtype = -1;
    public boolean targetAir = false;
    public Location erodeCenter;

    private void SetupMacro(String[] args, Location loc, OperatorState state) {
        try {
            erodeRadius = Integer.parseInt(args[0]);
        } catch (Exception e) {
            Main.logError("Could not parse erode macro. Is your radius a number?", state.getCurrentPlayer(), e);
        }
        erodeCenter = loc;

        // Determine the type of the erode brush
        try {
            if (args[1].equalsIgnoreCase("melt")) {
                erodeType = 0;
            } else if (args[1].equalsIgnoreCase("blend")) {
                erodeType = 1;
                erodeSubtype = Integer.parseInt(args[2]);
                targetAir = Boolean.parseBoolean(args[3]);
            } else if (args[1].equalsIgnoreCase("mix")) {
                erodeType = 2;
            } else if (args[1].equalsIgnoreCase("blockblend")) {
                erodeType = 3;
            }

            // Cut or raise melt?
            if (erodeType == 0) {
                if (args[2].equalsIgnoreCase("cut")) {
                    erodeSubtype = 0;
                } else if (args[2].equalsIgnoreCase("raise")) {
                    erodeSubtype = 1;
                } else if (args[2].equalsIgnoreCase("smooth")) {
                    erodeSubtype = 2;
                } else if (args[2].equalsIgnoreCase("lift")) {
                    erodeSubtype = 3;
                } else if (args[2].equalsIgnoreCase("carve")) {
                    erodeSubtype = 4;
                }
            }

            if (erodeType == 2) {
                if (args[2].equalsIgnoreCase("add")) {
                    erodeSubtype = 0;
                } else if (args[2].equalsIgnoreCase("subtract")) {
                    erodeSubtype = 1;
                } else if (args[2].equalsIgnoreCase("blend")) {
                    erodeSubtype = 2;
                }
            }
        } catch (Exception e) {
            Main.logError("Could not parse erode macro. Did you provide a valid mode?", state.getCurrentPlayer(), e);
        }
    }

    @Override
    public boolean performMacro(String[] args, Location loc, OperatorState state) {
        SetupMacro(args, loc, state);

        // Location of the brush
        double x = erodeCenter.getX();
        double y = erodeCenter.getY();
        double z = erodeCenter.getZ();

        List<BlockState> snapshotArray = generateSnapshotArray(x, y, z, state);

        // Melt cut erosion
        if (erodeType == 0 && erodeSubtype == 0) {
            snapshotArray = meltCutErosion(snapshotArray, state);
        }

        // Melt raise erosion
        if (erodeType == 0 && erodeSubtype == 1) {
            snapshotArray = meltRaiseErosion(snapshotArray, state);
        }

        // Melt smooth erosion
        if (erodeType == 0 && erodeSubtype == 2) {
            snapshotArray = meltSmoothErosion(snapshotArray, state);
        }

        // Melt lift erosion
        if (erodeType == 0 && erodeSubtype == 3) {
            snapshotArray = meltLiftErosion(snapshotArray, state);
        }

        // Melt carve erosion
        if (erodeType == 0 && erodeSubtype == 4) {
            snapshotArray = meltCarveErosion(snapshotArray, state);
        }

        // Blend erosion
        if (erodeType == 1) {
            snapshotArray = blendErode(snapshotArray, state);
        }

        // Mix erosion
        if (erodeType == 2 && erodeSubtype == 0) {
            snapshotArray = mixErosionAdd(snapshotArray, x, y, z, state);
        }

        if (erodeType == 2 && erodeSubtype == 1) {
            snapshotArray = mixErosionSubtract(snapshotArray, x, y, z, state);
        }

        if (erodeType == 2 && erodeSubtype == 2) {
            snapshotArray = mixErosionBlend(snapshotArray, x, y, z, state);
        }

        // Blockblend erosion
        if (erodeType == 3) {
            snapshotArray = blendBlockErode(snapshotArray, state);
        }

        // Apply the snapshot to the world, thus completing the erosion
        applyToWorld(snapshotArray, state);
        return true;
    }

    private List<BlockState> blendBlockErode(List<BlockState> snapshotArray, OperatorState state) {
        Main.logDebug("Starting blend block erode");
        List<Material> blockMaterials = new ArrayList<>();
        for (BlockState bs : snapshotArray) {
            if (bs.getType() != Material.AIR) {
                blockMaterials.add(bs.getType());
            }
        }
        Collections.shuffle(blockMaterials);
        int j = 0;
        for (BlockState blockState : snapshotArray) {
            if (blockState.getType() != Material.AIR) {
                blockState.setType(blockMaterials.get(j));
                j++;
            }
        }
        return snapshotArray;
    }

    private List<BlockState> blendErode(List<BlockState> snapshotArray, OperatorState state) {
        Main.logDebug("Starting blend erode"); // ----
        // Iterate through each block
        List<BlockState> snapshotCopy = new ArrayList<>();
        for (BlockState b : snapshotArray) {
            // If air, make sure we're editing air
            if (b.getType() == Material.AIR && !targetAir) {
                snapshotCopy.add(b);
                continue;
            }

            // Make sure the chance is met
            if (Main.getRand().nextInt(100) >= erodeSubtype) {
                snapshotCopy.add(b);
                continue;
            }

            // Get the adjacent blocks
            Block current = state.getCurrentWorld().getBlockAt(b.getLocation());
            List<Block> adjBlocks = new ArrayList<>();
            adjBlocks.add(current.getRelative(BlockFace.UP));
            adjBlocks.add(current.getRelative(BlockFace.DOWN));
            adjBlocks.add(current.getRelative(BlockFace.NORTH));
            adjBlocks.add(current.getRelative(BlockFace.SOUTH));
            adjBlocks.add(current.getRelative(BlockFace.EAST));
            adjBlocks.add(current.getRelative(BlockFace.WEST));

            // Pick a random one and update
            BlockState setMat = adjBlocks.get(Main.getRand().nextInt(adjBlocks.size())).getState();
            b.setType(setMat.getType());
            b.setBlockData(setMat.getBlockData());
            snapshotCopy.add(b);
        }
        return snapshotCopy;
    }

    private List<BlockState> generateSnapshotArray(double x, double y, double z, OperatorState state) {
        // Generate the erode sphere
        List<Block> erosionArray = new ArrayList<>();
        for (int rx = -erodeRadius; rx <= erodeRadius; rx++) {
            for (int rz = -erodeRadius; rz <= erodeRadius; rz++) {
                for (int ry = -erodeRadius; ry <= erodeRadius; ry++) {
                    if (rx * rx + ry * ry + rz * rz <= (erodeRadius + 0.5) * (erodeRadius + 0.5)) {
                        erosionArray.add(
                                state.getCurrentWorld().getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
                    }
                }
            }
        }
        Main.logDebug("Erosion array size: " + erosionArray.size()); // ----

        // Generate a snapshot to use for eroding (erode in this, read from world)
        List<BlockState> snapshotArray = new ArrayList<>();
        for (Block b : erosionArray) {
            snapshotArray.add(b.getState());
        }

        return snapshotArray;
    }

    private List<BlockState> mixErosionSubtract(List<BlockState> snapshotArray, double x, double y, double z, OperatorState state) {
        snapshotArray = meltCarveErosion(snapshotArray, state);
        applyToWorld(snapshotArray, state);
        snapshotArray = generateSnapshotArray(x, y, z, state);
        snapshotArray = meltCarveErosion(snapshotArray, state);
        applyToWorld(snapshotArray, state);
        snapshotArray = generateSnapshotArray(x, y, z, state);
        snapshotArray = meltRaiseErosion(snapshotArray, state);
        applyToWorld(snapshotArray, state);
        snapshotArray = generateSnapshotArray(x, y, z, state);
        snapshotArray = meltRaiseErosion(snapshotArray, state);
        applyToWorld(snapshotArray, state);
        snapshotArray = generateSnapshotArray(x, y, z, state);
        snapshotArray = meltSmoothErosion(snapshotArray, state);
        applyToWorld(snapshotArray, state);
        snapshotArray = generateSnapshotArray(x, y, z, state);
        snapshotArray = meltSmoothErosion(snapshotArray, state);
        return snapshotArray;
    }

    private List<BlockState> mixErosionBlend(List<BlockState> snapshotArray, double x, double y, double z, OperatorState state) {
        snapshotArray = meltCarveErosion(snapshotArray, state);
        applyToWorld(snapshotArray, state);
        snapshotArray = generateSnapshotArray(x, y, z, state);
        snapshotArray = meltRaiseErosion(snapshotArray, state);
        applyToWorld(snapshotArray, state);
        snapshotArray = generateSnapshotArray(x, y, z, state);
        snapshotArray = meltRaiseErosion(snapshotArray, state);
        applyToWorld(snapshotArray, state);
        snapshotArray = generateSnapshotArray(x, y, z, state);
        snapshotArray = meltSmoothErosion(snapshotArray, state);
        applyToWorld(snapshotArray, state);
        snapshotArray = generateSnapshotArray(x, y, z, state);
        snapshotArray = meltSmoothErosion(snapshotArray, state);
        return snapshotArray;
    }

    private List<BlockState> mixErosionAdd(List<BlockState> snapshotArray, double x, double y, double z, OperatorState state) {
        snapshotArray = meltRaiseErosion(snapshotArray, state);
        applyToWorld(snapshotArray, state);
        snapshotArray = generateSnapshotArray(x, y, z, state);
        snapshotArray = meltRaiseErosion(snapshotArray, state);
        applyToWorld(snapshotArray, state);
        snapshotArray = generateSnapshotArray(x, y, z, state);
        snapshotArray = meltSmoothErosion(snapshotArray, state);
        applyToWorld(snapshotArray, state);
        snapshotArray = generateSnapshotArray(x, y, z, state);
        snapshotArray = meltSmoothErosion(snapshotArray, state);
        return snapshotArray;
    }

    private void applyToWorld(List<BlockState> snapshotArray, OperatorState state) {
        for (BlockState b : snapshotArray) {
            Location l = b.getLocation();
            Block block = state.getCurrentWorld().getBlockAt(l);
            SetBlock.setMaterial(block, b.getType(), b.getBlockData(), state.getCurrentUndo(), state.getCurrentPlayer());
        }
    }

    private List<BlockState> meltSmoothErosion(List<BlockState> snapshotArray, OperatorState state) {
        Main.logDebug("Starting melt smooth erode"); // ----
        int airCut = 4; // Nearby air to make air
        int solidCut = 4; // Nearby solid to make solid
        // Iterate through each block
        return erodeSnapshotArray(snapshotArray, airCut, solidCut, state);
    }

    @NotNull
    private List<BlockState> erodeSnapshotArray(List<BlockState> snapshotArray, int airCut, int solidCut, OperatorState state) {
        List<BlockState> snapshotCopy = new ArrayList<>();
        for (BlockState b : snapshotArray) {
            // First get the adjacent blocks
            Block current = state.getCurrentWorld().getBlockAt(b.getLocation());
            List<Block> adjBlocks = new ArrayList<>();
            adjBlocks.add(current.getRelative(BlockFace.UP));
            adjBlocks.add(current.getRelative(BlockFace.DOWN));
            adjBlocks.add(current.getRelative(BlockFace.NORTH));
            adjBlocks.add(current.getRelative(BlockFace.SOUTH));
            adjBlocks.add(current.getRelative(BlockFace.EAST));
            adjBlocks.add(current.getRelative(BlockFace.WEST));

            // Logic for non-air blocks
            if (b.getType() != Material.AIR) {
                // Count how many adjacent blocks are air
                int airCount = 0;
                for (Block adjBlock : adjBlocks) {
                    if (adjBlock == null)
                        continue;
                    if (adjBlock.getType() == Material.AIR)
                        airCount++;
                }

                // If air count is large, make this air
                if (airCount >= airCut) {
                    b.setType(Material.AIR);
                }
                // Otherwise return in place
                snapshotCopy.add(b);
            }

            // Logic for air blocks
            else {
                int blockCount = 0;
                Material adjMaterial = Material.AIR;
                BlockData adjData = null;
                for (Block adjBlock : adjBlocks) {
                    if (adjBlock == null)
                        continue;
                    if (adjBlock.getType() != Material.AIR) {
                        blockCount++;
                        adjMaterial = adjBlock.getType();
                        adjData = adjBlock.getBlockData();
                    }
                }

                // If there are a lot of blocks nearby, make this solid
                if (blockCount >= solidCut) {
                    b.setType(adjMaterial);
                    if (adjData != null) {
                        b.setBlockData(adjData);
                    }
                }

                // Otherwise return in place
                snapshotCopy.add(b);
            }
        }
        return snapshotCopy;
    }

    private List<BlockState> meltRaiseErosion(List<BlockState> snapshotArray, OperatorState state) {
        Main.logDebug("Starting melt raise erode"); // ----
        int airCut = 4; // Nearby air to make air
        int solidCut = 2; // Nearby solid to make solid
        // Iterate through each block
        return erodeSnapshotArray(snapshotArray, airCut, solidCut, state);
    }

    private List<BlockState> meltLiftErosion(List<BlockState> snapshotArray, OperatorState state) {
        Main.logDebug("Starting melt lift erode"); // ----
        int airCut = 4; // Nearby air to make air
        int solidCut = 1; // Nearby solid to make solid
        // Iterate through each block
        return erodeSnapshotArray(snapshotArray, airCut, solidCut, state);
    }

    private List<BlockState> meltCutErosion(List<BlockState> snapshotArray, OperatorState state) {
        Main.logDebug("Starting melt cut erode"); // ----
        int airCut = 3; // Nearby air to make air
        int solidCut = 4; // Nearby solid to make solid
        // Iterate through each block
        return erodeSnapshotArray(snapshotArray, airCut, solidCut, state);
    }

    private List<BlockState> meltCarveErosion(List<BlockState> snapshotArray, OperatorState state) {
        Main.logDebug("Starting melt cut erode"); // ----
        int airCut = 1; // Nearby air to make air
        int solidCut = 4; // Nearby solid to make solid
        // Iterate through each block
        return erodeSnapshotArray(snapshotArray, airCut, solidCut, state);
    }
}
