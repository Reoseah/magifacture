package reoseah.magifacture.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import reoseah.magifacture.Magifacture;

public class SimpleCremationRecipe extends CremationRecipe {
    protected final Ingredient ingredient;
    protected final int count;
    protected final ItemStack output;
    protected final int duration;

    public SimpleCremationRecipe(Identifier id, Ingredient ingredient, int count, ItemStack output, int duration) {
        super(id);
        this.ingredient = ingredient;
        this.count = count;
        this.output = output;
        this.duration = duration;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return this.ingredient.test(inventory.getStack(0)) && inventory.getStack(0).getCount() >= this.count;
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        return this.output.copy();
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Magifacture.RecipeSerializers.SIMPLE_CREMATION;
    }

    @Override
    public Ingredient getInputIngredient() {
        return this.ingredient;
    }

    @Override
    public int getInputCount() {
        return this.count;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    public static class Serializer implements RecipeSerializer<SimpleCremationRecipe> {
        protected final int defaultDuration;

        public Serializer(int defaultDuration) {
            this.defaultDuration = defaultDuration;
        }

        @Override
        public SimpleCremationRecipe read(Identifier id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            int count = JsonHelper.hasJsonObject(json, "ingredient") ? JsonHelper.getInt(JsonHelper.getObject(json, "ingredient"), "count", 1) : 1;
            ItemStack result = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
            int duration = JsonHelper.getInt(json, "duration", this.defaultDuration);

            return new SimpleCremationRecipe(id, ingredient, count, result, duration);
        }

        @Override
        public SimpleCremationRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient ingredient = Ingredient.fromPacket(buf);
            int count = buf.readVarInt();
            ItemStack result = buf.readItemStack();
            int duration = buf.readVarInt();

            return new SimpleCremationRecipe(id, ingredient, count, result, duration);
        }

        @Override
        public void write(PacketByteBuf buf, SimpleCremationRecipe recipe) {
            recipe.ingredient.write(buf);
            buf.writeVarInt(recipe.count);
            buf.writeItemStack(recipe.output);
            buf.writeVarInt(recipe.duration);
        }
    }
}
