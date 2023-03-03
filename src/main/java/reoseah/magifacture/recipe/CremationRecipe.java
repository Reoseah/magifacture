package reoseah.magifacture.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public abstract class CremationRecipe implements Recipe<Inventory> {
    public static final RecipeType<CremationRecipe> TYPE = new RecipeType<>() {
        @Override
        public String toString() {
            return "magifacture:cremation";
        }
    };

    protected final Identifier id;

    public CremationRecipe(Identifier id) {
        this.id = id;
    }

    public abstract int getDuration();

    public abstract Ingredient getInputIngredient();

    public abstract int getInputCount();

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

}
