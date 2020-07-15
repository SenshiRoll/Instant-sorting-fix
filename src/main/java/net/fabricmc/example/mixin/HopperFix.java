package net.fabricmc.example.mixin;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.Maps;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
	@Shadow
	private final Map<BlockPos, BlockEntity> blockEntities;

	@Inject(method="init", at=@At(value="INVOKE", target = "Maps.newHashMap", ordinal = 1), cancellable=false)
	private void HopperOrderFix(World world, ChunkPos pos, BiomeArray biomes, UpgradeData upgradeData, TickScheduler<Block> blockTickScheduler, TickScheduler<Fluid> fluidTickScheduler, long inhabitedTime, @Nullable ChunkSection[] sections, @Nullable Consumer<WorldChunk> loadToWorldConsumer){
		this.blockEntities=Maps.newLinkedHashMap();
	}


}
