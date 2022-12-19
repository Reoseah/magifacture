package reoseah.magifacture.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/*
 * Contains common features for blocks that have an inventory:
 *   - drop inventory when broken
 *   - copy item name to inventory name if BE extends LockableContainerBlockEntity
 *   - open GUI on right click if BE implements NamedScreenHandlerFactory (override otherwise!)
 *
 * Additional features for blocks from the mod:
 *   - adds tooltip if lang key exist ("block.magifacture.<name>.tooltip")
 *   - facing the player if block state contains Properties.FACING or HORIZONTAL_FACING
 */
public abstract class MagifactureBlock extends BlockWithEntity {
    protected MagifactureBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        Identifier id = Registries.BLOCK.getId(this);
        String langKey = "block." + id.getNamespace() + "." + id.getPath() + ".tooltip";
        if (I18n.hasTranslation(langKey)) {
            Arrays.stream(I18n.translate(langKey).split("\\n")).forEach(s -> {
                tooltip.add(Texts.setStyleIfAbsent(Text.literal(s), Style.EMPTY.withColor(Formatting.GRAY)));
            });
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomName() && world.getBlockEntity(pos) instanceof LockableContainerBlockEntity nameable) {
            nameable.setCustomName(stack.getName());
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
            if (factory == null) {
                return ActionResult.PASS;
            }
            player.openHandledScreen(factory);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            if (world.getBlockEntity(pos) instanceof Inventory inventory) {
                ItemScatterer.spawn(world, pos, inventory);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if (this.getDefaultState().contains(Properties.HORIZONTAL_FACING)) {
            return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
        }
        return this.getDefaultState();
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        if (state.contains(Properties.HORIZONTAL_FACING)) {
            return state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)));
        }
        if (state.contains(Properties.FACING)) {
            return state.with(Properties.FACING, rotation.rotate(state.get(Properties.FACING)));
        }
        return state;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        if (state.contains(Properties.HORIZONTAL_FACING)) {
            return state.rotate(mirror.getRotation(state.get(Properties.HORIZONTAL_FACING)));
        }
        if (state.contains(Properties.FACING)) {
            return state.rotate(mirror.getRotation(state.get(Properties.FACING)));
        }
        return state;
    }
}
