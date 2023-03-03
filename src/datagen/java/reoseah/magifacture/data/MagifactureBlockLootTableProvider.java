package reoseah.magifacture.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import reoseah.magifacture.block.AlembicBlock;
import reoseah.magifacture.block.CrematoriumBlock;

class MagifactureBlockLootTableProvider extends FabricBlockLootTableProvider {
    public MagifactureBlockLootTableProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        this.addDrop(CrematoriumBlock.INSTANCE);
        this.addDrop(AlembicBlock.INSTANCE);
    }
}
