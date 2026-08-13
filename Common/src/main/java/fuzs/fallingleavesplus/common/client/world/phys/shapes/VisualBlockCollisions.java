package fuzs.fallingleavesplus.common.client.world.phys.shapes;

import com.google.common.collect.AbstractIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Cursor3D;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

/**
 * @see net.minecraft.world.level.BlockCollisions
 */
public class VisualBlockCollisions<T> extends AbstractIterator<T> {
    private final AABB box;
    private final CollisionContext context;
    private final Cursor3D cursor;
    private final BlockPos.MutableBlockPos pos;
    private final VoxelShape entityShape;
    private final CollisionGetter collisionGetter;
    private final boolean onlySuffocatingBlocks;
    @Nullable
    private BlockGetter cachedBlockGetter;
    private long cachedBlockGetterPos;
    private final BiFunction<BlockPos.MutableBlockPos, VoxelShape, T> resultProvider;

    public VisualBlockCollisions(CollisionGetter collisionGetter, @Nullable Entity entity, AABB box, boolean onlySuffocatingBlocks, BiFunction<BlockPos.MutableBlockPos, VoxelShape, T> resultProvider) {
        this.context = entity == null ? CollisionContext.empty() : CollisionContext.of(entity);
        this.pos = new BlockPos.MutableBlockPos();
        this.entityShape = Shapes.create(box);
        this.collisionGetter = collisionGetter;
        this.box = box;
        this.onlySuffocatingBlocks = onlySuffocatingBlocks;
        this.resultProvider = resultProvider;
        int x0 = Mth.floor(box.minX - 1.0E-7) - 1;
        int x1 = Mth.floor(box.maxX + 1.0E-7) + 1;
        int y0 = Mth.floor(box.minY - 1.0E-7) - 1;
        int y1 = Mth.floor(box.maxY + 1.0E-7) + 1;
        int z0 = Mth.floor(box.minZ - 1.0E-7) - 1;
        int z1 = Mth.floor(box.maxZ + 1.0E-7) + 1;
        this.cursor = new Cursor3D(x0, y0, z0, x1, y1, z1);
    }

    @Nullable
    private BlockGetter getChunk(int x, int z) {
        int i = SectionPos.blockToSectionCoord(x);
        int j = SectionPos.blockToSectionCoord(z);
        long l = ChunkPos.asLong(i, j);
        if (this.cachedBlockGetter != null && this.cachedBlockGetterPos == l) {
            return this.cachedBlockGetter;
        } else {
            BlockGetter blockGetter = this.collisionGetter.getChunkForCollisions(i, j);
            this.cachedBlockGetter = blockGetter;
            this.cachedBlockGetterPos = l;
            return blockGetter;
        }
    }

    @Override
    protected T computeNext() {
        while (this.cursor.advance()) {
            int x = this.cursor.nextX();
            int y = this.cursor.nextY();
            int z = this.cursor.nextZ();
            int cursorFaceType = this.cursor.getNextType();
            if (cursorFaceType != 3) {
                BlockGetter blockGetter = this.getChunk(x, z);
                if (blockGetter != null) {
                    this.pos.set(x, y, z);
                    BlockState blockState = blockGetter.getBlockState(this.pos);
                    if ((!this.onlySuffocatingBlocks || blockState.isSuffocating(blockGetter, this.pos)) && (
                            cursorFaceType != 1 || blockState.hasLargeCollisionShape()) && (cursorFaceType != 2
                            || blockState.is(Blocks.MOVING_PISTON))) {
                        VoxelShape blockShape = this.getCollisionShape(blockState);
                        if (blockShape == Shapes.block()) {
                            if (this.box.intersects(x, y, z, x + 1.0, y + 1.0, z + 1.0)) {
                                return this.resultProvider.apply(this.pos, blockShape.move(x, y, z));
                            }
                        } else {
                            VoxelShape shape = blockShape.move(x, y, z);
                            if (!shape.isEmpty() && Shapes.joinIsNotEmpty(shape, this.entityShape, BooleanOp.AND)) {
                                return this.resultProvider.apply(this.pos, shape);
                            }
                        }
                    }
                }
            }
        }

        return this.endOfData();
    }

    protected VoxelShape getCollisionShape(BlockState blockState) {
        VoxelShape voxelShape = blockState.getVisualShape(this.collisionGetter, this.pos, this.context);
        if (voxelShape == Shapes.empty()) {
            return blockState.getCollisionShape(this.collisionGetter, this.pos, this.context);
        } else {
            return voxelShape;
        }
    }
}
