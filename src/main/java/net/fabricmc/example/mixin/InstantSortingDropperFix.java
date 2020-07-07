package net.fabricmc.example.mixin;

import net.minecraft.block.entity.DropperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DropperBlockEntity.class)
public class InstantSortingDropperFix {
    @Inject()
    
}