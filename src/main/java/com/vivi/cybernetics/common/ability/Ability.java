package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.common.registry.CybAbilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public class Ability implements INBTSerializable<CompoundTag> {

    private AbilityType type;
    private boolean enabled;
    private int cooldown;

    public Ability(AbilityType type) {
        this.type = type;
    }
    public Ability(CompoundTag tag) {
        deserializeNBT(tag);
    }

    private void onEnable(Player player) {
        this.getType().onEnable(this, player.level, player);
    }

    private void onDisable(Player player) {
        this.getType().onDisable(this, player.level, player);
    }

    public void tick(Player player) {
        if(cooldown > -1) cooldown--;
        this.getType().tick(this, player.level, player);
    }

    public void enable(Player player) {
        if(cooldown > -1) return;
        this.enabled = true;
        onEnable(player);
    }
    public void disable(Player player) {
        this.enabled = false;
        onDisable(player);
    }


    public AbilityType getType() {
        return type;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ResourceLocation id = CybAbilities.ABILITY_TYPE_REGISTRY.get().getKey(type);
        tag.putString("id", id.toString());
        tag.putBoolean("enabled", enabled);
        tag.putInt("cooldown", cooldown);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        ResourceLocation id = ResourceLocation.tryParse(tag.getString("id"));
        type = CybAbilities.ABILITY_TYPE_REGISTRY.get().getValue(id);
        enabled = tag.getBoolean("enabled");
        cooldown = tag.getInt("cooldown");
    }
}