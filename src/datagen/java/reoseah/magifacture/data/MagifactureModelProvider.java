package reoseah.magifacture.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import reoseah.magifacture.Magifacture;

import java.util.Optional;

public class MagifactureModelProvider extends FabricModelProvider {
    public MagifactureModelProvider(FabricDataOutput generator) {
        super(generator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        gen.registerSimpleCubeAll(Magifacture.Blocks.EXPERIENCE);
        gen.blockStateCollector.accept(VariantsBlockStateSupplier.create(Magifacture.Blocks.CREMATORIUM).coordinate(BlockStateModelGenerator.createBooleanModelMap(Properties.LIT, new Identifier("magifacture:block/crematorium_on"), new Identifier("magifacture:block/crematorium"))).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));
        gen.registerSimpleState(Magifacture.Blocks.ALEMBIC);
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
        registerBlockItem(gen, Magifacture.Blocks.CREMATORIUM);
        registerBlockItem(gen, Magifacture.Blocks.ALEMBIC);
        gen.register(Magifacture.Items.EXPERIENCE_BUCKET, Models.GENERATED);
    }

    private static void registerBlockItem(ItemModelGenerator gen, Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        gen.register(block.asItem(), new Model(Optional.of(new Identifier(id.getNamespace(), "block/" + id.getPath())), Optional.empty()));
    }
}
