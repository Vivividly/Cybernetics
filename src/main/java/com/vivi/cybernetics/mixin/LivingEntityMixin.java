package com.vivi.cybernetics.mixin;

import com.vivi.cybernetics.registry.ModCyberware;
import com.vivi.cybernetics.registry.ModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing);

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "decreaseAirSupply", at = @At("HEAD"), cancellable = true)
    public void cybenetics_decreaseAirSupply(int currentAir, CallbackInfoReturnable<Integer> cir) {
        this.getCapability(ModCyberware.CYBERWARE).ifPresent(cyberwareInventory -> {
            for(int i = 0; i < cyberwareInventory.getSlots(); i++) {
                if(cyberwareInventory.getStackInSlot(i).is(ModItems.OXYGEN_RECYCLER.get())) {
                    if(this.random.nextInt(4) > 0) {
                        cir.setReturnValue(currentAir);
                    }
                }
            }
        });
    }
}
