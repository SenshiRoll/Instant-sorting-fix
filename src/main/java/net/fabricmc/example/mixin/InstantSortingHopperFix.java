package net.fabricmc.example.mixin;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.util.math.BlockPos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HopperBlockEntity.class)
public class InstantSortingHopperFix {
	@Inject(at = @At("HEAD"), method = "tick", cancellable=true)
	private void Updates(BlockPos pos,final CallbackInfo info) {
		System.out.println("["+pos.getX()+","+pos.getY()+","+pos.getZ()+"}");
	}
	//@Overwrite
	/*public void tick() {
		if (this.world != null && !this.world.isClient) {
		   --this.transferCooldown;
		   this.lastTickTime = this.world.getTime();
		   if (!this.needsCooldown()) {
			  this.setCooldown(0);
			  this.insertAndExtract(() -> {
				 return extract(this);
			  });
		   }
  
		}
	}*/

}
