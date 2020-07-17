package net.fabricmc.example.mixin;

import java.util.HashMap;
import java.util.Map;

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
import net.minecraft.world.ChunkTickScheduler;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.block.entity.BlockEntity;

@Mixin(ProtoChunk.class)
public abstract class HopperFix implements Chunk{
	@Shadow @Final @Mutable
	private Map<BlockPos, BlockEntity> blockEntities;

	@Redirect(method="<init>(Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/chunk/UpgradeData;[Lnet/minecraft/world/chunk/ChunkSection;Lnet/minecraft/world/ChunkTickScheduler;Lnet/minecraft/world/ChunkTickScheduler;)V", at = @At(value="INVOKE", target = "Lcom/google/common/collect/Maps;newHashMap()Ljava/util/HashMap;"))
	private HashMap<BlockPos,BlockEntity> HopperOrderFix(ChunkPos chunkPos, UpgradeData upgradeData, ChunkSection[] chunkSections, ChunkTickScheduler<Block> chunkTickScheduler, ChunkTickScheduler<Fluid> chunkTickScheduler2){
		return Maps.newLinkedHashMap();
	}
}
