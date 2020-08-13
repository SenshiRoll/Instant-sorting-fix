package net.fabricmc.example.mixin;

import carpet.script.utils.ShapesRenderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;

@Mixin(ServerWorld.class)
public abstract class HopperMapLog extends World {
    private MinecraftClient client=MinecraftClient.getInstance();
    public ListTag shapes;
    protected HopperMapLog(LevelProperties levelProperties, DimensionType dimensionType, BiFunction<World, Dimension, ChunkManager> chunkManagerProvider, Profiler profiler, boolean isClient) {
        super(levelProperties, dimensionType, chunkManagerProvider, profiler, isClient);
    }
    @Inject(method="tick", at= @At("HEAD"), cancellable=false)
    private void mapper(CallbackInfo info){
    	ShapesRenderer map=new ShapesRenderer(client);
    	map.addShapes(shapes);
    	map.render(client.gameRenderer.getCamera(), 1.0f);

    }
    private static void getBlocks(){
        //get a list of all the BlockPos values in a chunk and put them in shapes

    
}

}