package net.fabricmc.example.mixin;

import java.util.function.BiFunction;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.*;

import carpet.script.utils.ShapesRenderer;

@Mixin(ServerWorld.class)
public abstract class HopperMapLog extends World {
    protected HopperMapLog(LevelProperties levelProperties, DimensionType dimensionType, BiFunction<World, Dimension, ChunkManager> chunkManagerProvider, Profiler profiler, boolean isClient) {
        super(levelProperties, dimensionType, chunkManagerProvider, profiler, isClient);
    }
    @Inject(method="tick", at= @At("HEAD"), cancellable=false)
    private void mapper(CallbackInfo info){
    	ShapesRenderer map=new ShapesRenderer();
    	map.addShapes(something);
    	map.render(client camera, 1.0f);

    }
    @Mixin(HashMap.class)
    public abstract class HashMapMixin<K,V> extends AbstractMap<K,V> implements Serializable, Cloneable {
        @Shadow
        private static final long serialVersionUID = 362498820763181265L;
    
        @Shadow
        transient Node<K,V>[] table;

         /**
         * Copypasta'd straight from HashMap
         * <p> Nothing special about it
         * 
         */
        static class Node<K,V> implements Map.Entry<K,V> {
            final int hash;
            final K key;
            V value;
            Node<K,V> next;

            Node(int hash, K key, V value, Node<K,V> next) {
                this.hash = hash;
                this.key = key;
                this.value = value;
                this.next = next;
            }

            public final K getKey()        { return key; }
            public final V getValue()      { return value; }
            public final String toString() { return key + "=" + value; }

            public final int hashCode() {
                return Objects.hashCode(key) ^ Objects.hashCode(value);
            }
            
            public final V setValue(V newValue) {
                V oldValue = value;
                value = newValue;
                return oldValue;
            }

            public final boolean equals(Object o) {
                if (o == this)
                    return true;
                if (o instanceof Map.Entry) {
                    Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                    if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()))
                        return true;
                }
                return false;
            }
        }

    @Inject(method="putVal", at= @At(value="INVOKE", target="newNode", ordinal=0))
    public int getBin(int hash, K key, V value, boolean onlyIfAbsent, boolean evict, CallbackInfoReturnable<Integer> info){
        int i;
        i=(15 & hash);
        return i;
    }
    
}

}