package reoseah.magifacture;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.*;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reoseah.magifacture.block.AlembicBlock;
import reoseah.magifacture.block.CrematoriumBlock;
import reoseah.magifacture.block.ExperienceBlock;
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

    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier("magifacture:main")) //
            .icon(() -> new ItemStack(Items.EXPERIENCE_BUCKET)).build();

    @Override
    public void onInitialize() {
        Blocks.initialize();
        Items.initialize();
        Fluids.initialize();
        BlockEntityTypes.initialize();
        RecipeTypes.initialize();
        RecipeSerializers.initialize();
        ScreenHandlerTypes.initialize();
    }

    public static class Blocks {
        public static final Block EXPERIENCE = register("experience", new ExperienceBlock(FabricBlockSettings.of(Material.LAVA, MapColor.LIME).luminance(15).noCollision()));

        public static final Block CREMATORIUM = register("crematorium", new CrematoriumBlock(FabricBlockSettings.of(Material.METAL, MapColor.GRAY).strength(3F).luminance(state -> state.get(Properties.LIT) ? 15 : 0)));
        public static final Block ALEMBIC = register("alembic", new AlembicBlock(FabricBlockSettings.of(Material.METAL, MapColor.GRAY).strength(3F)));

        private static <T extends Block> T register(String name, T block) {
            return Registry.register(Registries.BLOCK, new Identifier("magifacture", name), block);
        }

        public static void initialize() {
        }
    }

    public static class Items {
        public static final Item CREMATORIUM = registerItemBlock(Blocks.CREMATORIUM);
        public static final Item ALEMBIC = registerItemBlock(Blocks.ALEMBIC);
        public static final Item EXPERIENCE_BUCKET = register("experience_bucket", new ExperienceBucketItem(Fluids.EXPERIENCE, new Item.Settings().rarity(Rarity.RARE).recipeRemainder(net.minecraft.item.Items.BUCKET)));
        public static final Item ASH = register("ash", new Item(new Item.Settings()));

        private static <T extends Item> T register(String name, T item) {
            return Registry.register(Registries.ITEM, new Identifier("magifacture", name), item);
        }

        private static Item registerItemBlock(Block block) {
            return Registry.register(Registries.ITEM, Registries.BLOCK.getId(block), new BlockItem(block, new Item.Settings()));
        }

        public static void initialize() {
            ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(entries -> {
                entries.add(Items.CREMATORIUM);
                entries.add(Items.ALEMBIC);
                entries.add(Items.EXPERIENCE_BUCKET);
            });

            CompostingChanceRegistry.INSTANCE.add(ASH, 0.05F);
        }
    }

    public static class Fluids {
        public static final Fluid EXPERIENCE = register("experience", new ExperienceFluid());

        private static <T extends Fluid> T register(String name, T fluid) {
            return Registry.register(Registries.FLUID, new Identifier("magifacture", name), fluid);
        }

        @SuppressWarnings("UnstableApiUsage")
        public static void initialize() {
            FluidStorage.combinedItemApiProvider(net.minecraft.item.Items.GLASS_BOTTLE).register(context -> new EmptyItemFluidStorage(context, net.minecraft.item.Items.EXPERIENCE_BOTTLE, Fluids.EXPERIENCE, FluidConstants.BOTTLE));
            FluidStorage.ITEM.registerForItems((stack, context) -> new FullItemFluidStorage(context, net.minecraft.item.Items.GLASS_BOTTLE, FluidVariant.of(Fluids.EXPERIENCE), FluidConstants.BOTTLE), net.minecraft.item.Items.EXPERIENCE_BOTTLE);
        }
    }

    public static class BlockEntityTypes {
        public static final BlockEntityType<AlembicBlockEntity> ALEMBIC = register("alembic", FabricBlockEntityTypeBuilder.create(AlembicBlockEntity::new, Blocks.ALEMBIC).build());
        public static final BlockEntityType<CrematoriumBlockEntity> CREMATORIUM = register("crematorium", FabricBlockEntityTypeBuilder.create(CrematoriumBlockEntity::new, Blocks.CREMATORIUM).build());

        public static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> entry) {
            return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier("magifacture", name), entry);
        }

        public static void initialize() {
            FluidStorage.SIDED.registerForBlockEntity((be, side) -> side != Direction.UP ? be.getTank() : null, ALEMBIC);
        }
    }

    public static class RecipeTypes {
        public static final RecipeType<CremationRecipe> CREMATION = register("cremation", new RecipeType<>() {
            @Override
            public String toString() {
                return "magifacture:cremation";
            }
        });

        public static <T extends Recipe<?>> RecipeType<T> register(String name, RecipeType<T> entry) {
            return Registry.register(Registries.RECIPE_TYPE, new Identifier("magifacture", name), entry);
        }

        public static void initialize() {

        }
    }

    public static class RecipeSerializers {
        public static final RecipeSerializer<?> SIMPLE_CREMATION = register("simple_cremation", new SimpleCremationRecipe.Serializer(200));

        public static <T extends Recipe<?>> RecipeSerializer<T> register(String name, RecipeSerializer<T> entry) {
            return Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("magifacture", name), entry);
        }

        public static void initialize() {

        }
    }

    public static class ScreenHandlerTypes {
        public static final ScreenHandlerType<CrematoriumScreenHandler> CREMATORIUM = register("crematorium", new ScreenHandlerType<>(CrematoriumScreenHandler::new));
        public static final ScreenHandlerType<AlembicScreenHandler> ALEMBIC = register("alembic", new ScreenHandlerType<>(AlembicScreenHandler::new));

        public static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerType<T> entry) {
            return Registry.register(Registries.SCREEN_HANDLER, new Identifier("magifacture", name), entry);
        }

        public static void initialize() {

        }
    }

}