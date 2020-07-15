package net.fabricmc.example.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;

@Mixin(HopperBlockEntity.class)
public abstract class HopperMapLog extends LootableContainerBlockEntity {
    private static int wait=0;
    protected HopperMapLog(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void log(CallbackInfo info){
        if(wait==100 && !this.world.isClient){
            System.out.println("<"+this.world.getTime()+">  ["+this.pos.getX()+","+this.pos.getY()+","+this.pos.getZ()+"]");
        }
        wait++;
    }

}