package net.fabricmc.example.mixin;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.Direction;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class InstantSortingHopperFix extends LootableContainerBlockEntity {
	@Shadow 
	private int transferCooldown;

	protected InstantSortingHopperFix(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
	}

	@Inject(at = @At("RETURN"), method = "insert", cancellable = true)
	private void insertLog(CallbackInfoReturnable<Boolean> info) {
		if (!this.world.isClient && this.transferCooldown==8){
			System.out.println("I:  <"+this.world.getTime()+">  ["+this.pos.getX()+","+this.pos.getY()+","+this.pos.getZ()+"]");
		}
	}
	@Inject(at=@At(value="INVOKE", target="extract"), method="tick", cancellable=true)
	private void extractLog(CallbackInfo info){
		if (!this.world.isClient && transferCooldown==8){
			System.out.println("E:  <"+this.world.getTime()+">  ["+pos.getX()+","+this.pos.getY()+","+this.pos.getZ()+"]");
		}
	}
}
