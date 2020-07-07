package net.fabricmc.example.mixin;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class InstantSortingHopperFix extends LootableContainerBlockEntity {
	protected InstantSortingHopperFix(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
	}

	@Inject(at = @At("HEAD"), method = "insert", cancellable = true)
	private void Updates(CallbackInfoReturnable<Boolean> info) {
		if (!this.world.isClient){
			System.out.println("<"+this.world.getTime()+">  ["+this.pos.getX()+","+this.pos.getY()+","+this.pos.getZ()+"]");
		}//
	}
	//@Overwrite       //so I can see a hopper tick
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
