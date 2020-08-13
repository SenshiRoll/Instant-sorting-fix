package net.fabricmc.example.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;

@Mixin(HashMap.class)
public abstract class HashMapMixin<K,V> extends AbstractMap<K,V> implements Serializable, Cloneable {
    @Shadow
    private static final long serialVersionUID = 362498820763181265L;

    @Inject(method = "putVal", at = @At(value = "INVOKE", target = "newNode", ordinal = 0))
    public void getBin(int hash, K key, V value, boolean onlyIfAbsent, boolean evict, CallbackInfoReturnable<Integer> info) {
        info.setReturnValue(15 & hash);
    }
}
