package com.vivi.cybernetics.compat.jei;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.recipe.CyberwareStationRecipe;
import com.vivi.cybernetics.common.registry.CybBlocks;
import com.vivi.cybernetics.common.util.RecipeUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CyberwareStationRecipeCategory implements IRecipeCategory<CyberwareStationRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(Cybernetics.MOD_ID, "cyberware_crafting");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/cyberware_station.png");



    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    private final int x = 15;
    private final int y = 12;

    public CyberwareStationRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, x, y, 168 - 11, 72);
        this.icon = helper.createDrawableItemStack(new ItemStack(CybBlocks.CYBERWARE_STATION_BLOCK.get()));
        this.arrow = helper.drawableBuilder(TEXTURE, 0, 176, 105, 14)
                .buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<CyberwareStationRecipe> getRecipeType() {
        return CyberneticsJeiPlugin.CYBERWARE_CRAFTING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei." + Cybernetics.MOD_ID + ".cyberware_crafting");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CyberwareStationRecipe recipe, IFocusGroup focuses) {


        builder.addSlot(RecipeIngredientRole.INPUT, 26 - x,  39 - y).addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 62 - x,  21 - y).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 80 - x,  21 - y).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 98 - x,  21 - y).addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 62 - x,  39 - y).addIngredients(recipe.getIngredients().get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 80 - x,  39 - y).addIngredients(recipe.getIngredients().get(4));
        builder.addSlot(RecipeIngredientRole.INPUT, 98 - x,  39 - y).addIngredients(recipe.getIngredients().get(5));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 148 - x,  56 - y).addItemStack(RecipeUtil.getResultItem(recipe));

    }

    @Override
    public void draw(CyberwareStationRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 31 - x, 58 - y);
    }
}
