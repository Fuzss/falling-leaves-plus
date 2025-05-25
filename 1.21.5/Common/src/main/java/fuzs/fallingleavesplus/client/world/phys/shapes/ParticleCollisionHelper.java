package fuzs.fallingleavesplus.client.world.phys.shapes;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ParticleCollisionHelper {
    private static final CollisionContext COLLISION_CONTEXT = new EntityCollisionContext(false,
            false,
            -Double.MAX_VALUE,
            ItemStack.EMPTY,
            (FluidState fluidState) -> false,
            null) {
        @Override
        public VoxelShape getCollisionShape(BlockState blockState, CollisionGetter collisionGetter, BlockPos pos) {
            VoxelShape voxelShape = blockState.getVisualShape(collisionGetter, pos, this);
            if (voxelShape == Shapes.empty()) {
                return super.getCollisionShape(blockState, collisionGetter, pos);
            } else {
                return voxelShape;
            }
        }

        @Override
        public boolean isAbove(VoxelShape shape, BlockPos pos, boolean canAscend) {
            return canAscend;
        }
    };

    public static Vec3 collideBoundingBox(@Nullable Entity entity, Vec3 vec, AABB collisionBox, Level level, List<VoxelShape> potentialHits) {
        List<VoxelShape> list = collectColliders(entity, level, potentialHits, collisionBox.expandTowards(vec));
        return Entity.collideWithShapes(vec, collisionBox, list);
    }

    private static List<VoxelShape> collectColliders(@Nullable Entity entity, Level level, List<VoxelShape> collisions, AABB boundingBox) {
        ImmutableList.Builder<VoxelShape> builder = ImmutableList.builderWithExpectedSize(collisions.size() + 1);
        if (!collisions.isEmpty()) {
            builder.addAll(collisions);
        }

        WorldBorder worldBorder = level.getWorldBorder();
        boolean bl = entity != null && worldBorder.isInsideCloseToBorder(entity, boundingBox);
        if (bl) {
            builder.add(worldBorder.getCollisionShape());
        }

        builder.addAll(getBlockCollisions(level, boundingBox));
        return builder.build();
    }

    public static Iterable<VoxelShape> getBlockCollisions(CollisionGetter collisionGetter, AABB collisionBox) {
        return () -> new BlockCollisions<>(collisionGetter,
                COLLISION_CONTEXT,
                collisionBox,
                false,
                (BlockPos.MutableBlockPos blockPos, VoxelShape voxelShape) -> {
                    return voxelShape;
                });
    }
}
