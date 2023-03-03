package reoseah.magifacture.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import reoseah.magifacture.block.ExperienceBlock;
import reoseah.magifacture.fluid.ExperienceFluid;

public class ExperienceBucketItem extends BucketItem {
    public static final Item BUCKET = new ExperienceBucketItem(ExperienceFluid.EXPERIENCE, new Settings().rarity(Rarity.RARE).maxCount(1).recipeRemainder(Items.BUCKET));

    protected ExperienceBucketItem(Fluid fluid, Settings settings) {
        super(fluid, settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        user.addExperience(ExperienceBlock.XP_PER_BUCKET);

        if (user.isCreative()) {
            if (!user.getInventory().contains(new ItemStack(Items.BUCKET))) {
                user.getInventory().insertStack(new ItemStack(Items.BUCKET));
            }
        } else {
            stack.decrement(1);
            if (!user.getInventory().insertStack(new ItemStack(Items.BUCKET))) {
                user.dropItem(new ItemStack(Items.BUCKET), false);
            }
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}
