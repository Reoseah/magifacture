package reoseah.magifacture;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reoseah.magifacture.block.AlembicBlock;
import reoseah.magifacture.block.CrematoriumBlock;
import reoseah.magifacture.block.ExperienceBlock;
import reoseah.magifacture.block.InfusionTableBlock;
import reoseah.magifacture.block.entity.AlembicBlockEntity;
import reoseah.magifacture.block.entity.CrematoriumBlockEntity;
import reoseah.magifacture.fluid.ExperienceFluid;
import reoseah.magifacture.item.ExperienceBucketItem;
import reoseah.magifacture.recipe.CremationRecipe;
import reoseah.magifacture.recipe.SimpleCremationRecipe;
import reoseah.magifacture.screen.AlembicScreenHandler;
import reoseah.magifacture.screen.CrematoriumScreenHandler;

public class Magifacture implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("magifacture");

    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier("magifacture:main")).icon(() -> new ItemStack(ExperienceBucketItem.BUCKET)).build();

    public static final Item ASH = new Item(new Item.Settings());

    @Override
    public void onInitialize() {
        Registry.register(Registries.BLOCK, "magifacture:experience", ExperienceBlock.INSTANCE);
        Registry.register(Registries.BLOCK, "magifacture:crematorium", CrematoriumBlock.INSTANCE);
        Registry.register(Registries.BLOCK, "magifacture:alembic", AlembicBlock.INSTANCE);
        Registry.register(Registries.BLOCK, "magifacture:infusion_table", InfusionTableBlock.INSTANCE);

        Registry.register(Registries.ITEM, "magifacture:crematorium", CrematoriumBlock.ITEM);
        Registry.register(Registries.ITEM, "magifacture:alembic", AlembicBlock.ITEM);
        Registry.register(Registries.ITEM, "magifacture:infusion_table", InfusionTableBlock.ITEM);
        Registry.register(Registries.ITEM, "magifacture:experience_bucket", ExperienceBucketItem.BUCKET);
        Registry.register(Registries.ITEM, "magifacture:ash", ASH);

        Registry.register(Registries.FLUID, "magifacture:experience", ExperienceFluid.EXPERIENCE);

        Registry.register(Registries.BLOCK_ENTITY_TYPE, "magifacture:alembic", AlembicBlockEntity.TYPE);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, "magifacture:crematorium", CrematoriumBlockEntity.TYPE);

        Registry.register(Registries.RECIPE_TYPE, "magifacture:cremation", CremationRecipe.TYPE);
        Registry.register(Registries.RECIPE_SERIALIZER, "magifacture:simple_cremation", SimpleCremationRecipe.SERIALIZER);

        Registry.register(Registries.SCREEN_HANDLER, "magifacture:crematorium", CrematoriumScreenHandler.TYPE);
        Registry.register(Registries.SCREEN_HANDLER, "magifacture:alembic", AlembicScreenHandler.TYPE);

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(entries -> {
            entries.add(CrematoriumBlock.ITEM);
            entries.add(AlembicBlock.ITEM);
            entries.add(InfusionTableBlock.ITEM);
            entries.add(ExperienceBucketItem.BUCKET);
        });
        CompostingChanceRegistry.INSTANCE.add(ASH, 0.05F);
        // noinspection UnstableApiUsage
        FluidStorage.combinedItemApiProvider(Items.GLASS_BOTTLE).register(context -> new EmptyItemFluidStorage(context, Items.EXPERIENCE_BOTTLE, ExperienceFluid.EXPERIENCE, FluidConstants.BOTTLE));
        // noinspection UnstableApiUsage
        FluidStorage.ITEM.registerForItems((stack, context) -> new FullItemFluidStorage(context, Items.GLASS_BOTTLE, FluidVariant.of(ExperienceFluid.EXPERIENCE), FluidConstants.BOTTLE), Items.EXPERIENCE_BOTTLE);
        // noinspection UnstableApiUsage
        FluidStorage.SIDED.registerForBlockEntity((be, side) -> side != Direction.UP ? be.getTank() : null, AlembicBlockEntity.TYPE);
    }
}