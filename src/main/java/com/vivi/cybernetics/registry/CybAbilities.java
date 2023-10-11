package com.vivi.cybernetics.registry;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.ability.AbilityType;
import com.vivi.cybernetics.cyberware.CyberwareSectionType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class CybAbilities {

    public static final DeferredRegister<AbilityType> ABILITY_TYPES = DeferredRegister.create(new ResourceLocation(Cybernetics.MOD_ID, "ability_types"), Cybernetics.MOD_ID);
    public static final Supplier<IForgeRegistry<AbilityType>> ABILITY_TYPE_REGISTRY = ABILITY_TYPES.makeRegistry(RegistryBuilder::new);



}
