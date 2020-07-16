package net.fabricmc.example.mixin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.Maps;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.TickScheduler;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.block.entity.BlockEntity;

@Mixin(WorldChunk.class)
public abstract class HopperFix implements Chunk{
	@Shadow @Final @Mutable
	private Map<BlockPos, BlockEntity> blockEntities;

	@Redirect(method="<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/biome/source/BiomeArray;Lnet/minecraft/world/chunk/UpgradeData;Lnet/minecraft/world/TickScheduler;Lnet/minecraft/world/TickScheduler;J[Lnet/minecraft/world/chunk/ChunkSection;Ljava/util/function/Consumer;)V", at=@At(value="INVOKE", target = "Lcom/google/common/collect/Maps;newHashMap()Ljava/util/HashMap;", ordinal = 1))
	private HashMap HopperOrderFix(World world, ChunkPos pos, BiomeArray biomes, UpgradeData upgradeData, TickScheduler<Block> blockTickScheduler, TickScheduler<Fluid> fluidTickScheduler, long inhabitedTime,ChunkSection[] sections,Consumer<WorldChunk> loadToWorldConsumer){
		return Maps.newLinkedHashMap();
	}
}
