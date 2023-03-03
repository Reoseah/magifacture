package reoseah.magifacture.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import reoseah.magifacture.Magifacture;
import reoseah.magifacture.block.AlembicBlock;
import reoseah.magifacture.block.CrematoriumBlock;
import reoseah.magifacture.block.ExperienceBlock;
import reoseah.magifacture.item.ExperienceBucketItem;

import java.util.Optional;

public class MagifactureModelProvider extends FabricModelProvider {
    public MagifactureModelProvider(FabricDataOutput generator) {
        super(generator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        gen.registerSimpleCubeAll(ExperienceBlock.INSTANCE);
        gen.blockStateCollector.accept(VariantsBlockStateSupplier.create(CrematoriumBlock.INSTANCE).coordinate(BlockStateModelGenerator.createBooleanModelMap(Properties.LIT, new Identifier("magifacture:block/crematorium_on"), new Identifier("magifacture:block/crematorium"))).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));
        gen.registerSimpleState(AlembicBlock.INSTANCE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
        registerBlockItem(gen, CrematoriumBlock.INSTANCE);
        registerBlockItem(gen, AlembicBlock.INSTANCE);
        gen.register(ExperienceBucketItem.BUCKET, Models.GENERATED);
        gen.register(Magifacture.ASH, Models.GENERATED);
    }

    private static void registerBlockItem(ItemModelGenerator gen, Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        gen.register(block.asItem(), new Model(Optional.of(new Identifier(id.getNamespace(), "block/" + id.getPath())), Optional.empty()));
    }
}
