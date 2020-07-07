package net.fabricmc.example.mixin;

import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HopperBlockEntity.class)
public class InstantSortingHopperFix {
	@Inject(at = @At("HEAD"), method = "tick")
	private void Updates(final CallbackInfo info) {
		
	}
	@Overwrite
	public void tick() {
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
	 }

}
