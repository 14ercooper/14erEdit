package com._14ercooper.worldeditor.testing.dummies;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.util.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class DummyBlock implements Block {
    @Override
    public byte getData() {
        return 0;
    }

    @NotNull
    @Override
    public BlockData getBlockData() {
        return null;
    }

    @NotNull
    @Override
    public Block getRelative(int i, int i1, int i2) {
        return new DummyBlock();
    }

    @NotNull
    @Override
    public Block getRelative(@NotNull BlockFace blockFace) {
        return new DummyBlock();
    }

    @NotNull
    @Override
    public Block getRelative(@NotNull BlockFace blockFace, int i) {
        return new DummyBlock();
    }

    @NotNull
    @Override
    public Material getType() {
        return Material.STONE;
    }

    @Override
    public byte getLightLevel() {
        return 0;
    }

    @Override
    public byte getLightFromSky() {
        return 0;
    }

    @Override
    public byte getLightFromBlocks() {
        return 0;
    }

    @NotNull
    @Override
    public World getWorld() {
        return new DummyWorld();
    }

    @Override
    public int getX() {
        return 14;
    }

    @Override
    public int getY() {
        return 1414;
    }

    @Override
    public int getZ() {
        return 141414;
    }

    @NotNull
    @Override
    public Location getLocation() {
        return new Location(new DummyWorld(), 14, 1414, 141414);
    }

    @Nullable
    @Override
    public Location getLocation(@Nullable Location location) {
        return null;
    }

    @NotNull
    @Override
    public Chunk getChunk() {
        return null;
    }

    @Override
    public void setBlockData(@NotNull BlockData blockData) {

    }

    @Override
    public void setBlockData(@NotNull BlockData blockData, boolean b) {

    }

    @Override
    public void setType(@NotNull Material material) {

    }

    @Override
    public void setType(@NotNull Material material, boolean b) {

    }

    @Nullable
    @Override
    public BlockFace getFace(@NotNull Block block) {
        return null;
    }

    @NotNull
    @Override
    public BlockState getState() {
        return null;
    }

    @NotNull
    @Override
    public Biome getBiome() {
        return null;
    }

    @Override
    public void setBiome(@NotNull Biome biome) {

    }

    @Override
    public boolean isBlockPowered() {
        return false;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return false;
    }

    @Override
    public boolean isBlockFacePowered(@NotNull BlockFace blockFace) {
        return false;
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(@NotNull BlockFace blockFace) {
        return false;
    }

    @Override
    public int getBlockPower(@NotNull BlockFace blockFace) {
        return 0;
    }

    @Override
    public int getBlockPower() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isLiquid() {
        return false;
    }

    @Override
    public double getTemperature() {
        return 0;
    }

    @Override
    public double getHumidity() {
        return 0;
    }

    @NotNull
    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return null;
    }

    @Override
    public boolean breakNaturally() {
        return false;
    }

    @Override
    public boolean breakNaturally(@Nullable ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean applyBoneMeal(@NotNull BlockFace blockFace) {
        return false;
    }

    @NotNull
    @Override
    public Collection<ItemStack> getDrops() {
        return null;
    }

    @NotNull
    @Override
    public Collection<ItemStack> getDrops(@Nullable ItemStack itemStack) {
        return null;
    }

    @NotNull
    @Override
    public Collection<ItemStack> getDrops(@NotNull ItemStack itemStack, @Nullable Entity entity) {
        return null;
    }

    @Override
    public boolean isPreferredTool(@NotNull ItemStack itemStack) {
        return false;
    }

    @Override
    public float getBreakSpeed(@NotNull Player player) {
        return 0;
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Nullable
    @Override
    public RayTraceResult rayTrace(@NotNull Location location, @NotNull Vector vector, double v, @NotNull FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    @NotNull
    @Override
    public BoundingBox getBoundingBox() {
        return null;
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape() {
        return null;
    }

    @Override
    public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {

    }

    @NotNull
    @Override
    public List<MetadataValue> getMetadata(@NotNull String s) {
        return null;
    }

    @Override
    public boolean hasMetadata(@NotNull String s) {
        return false;
    }

    @Override
    public void removeMetadata(@NotNull String s, @NotNull Plugin plugin) {

    }
}
