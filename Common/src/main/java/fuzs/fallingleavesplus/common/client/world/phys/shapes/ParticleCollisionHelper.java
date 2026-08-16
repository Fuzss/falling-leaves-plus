package fuzs.fallingleavesplus.common.client.world.phys.shapes;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Custom collisions for particles that take visual block shape into account.
 */
public final class ParticleCollisionHelper {
    /**
     * @see EntityCollisionContext.Empty#WITHOUT_FLUID_COLLISIONS
     */
    private static final CollisionContext CONTEXT = new EntityCollisionContext.Empty(false) {
        @Override
        public VoxelShape getCollisionShape(BlockState blockState, CollisionGetter collisionGetter, BlockPos pos) {
            VoxelShape voxelShape = blockState.getVisualShape(collisionGetter, pos, this);
            if (voxelShape == Shapes.empty()) {
                return super.getCollisionShape(blockState, collisionGetter, pos);
            } else {
                return voxelShape;
            }
        }
    };

    private ParticleCollisionHelper() {
        // NO-OP
    }

    /**
     * @see Entity#collideBoundingBox(Entity, Vec3, AABB, Level, List)
     */
    public static Vec3 collideBoundingBox(@Nullable Entity entity, Vec3 vec, AABB collisionBox, Level level, List<VoxelShape> potentialHits) {
        List<VoxelShape> colliders = collectCollidersIgnoringWorldBorder(entity,
                level,
                potentialHits,
                collisionBox.expandTowards(vec));
        return Entity.collideWithShapes(vec, collisionBox, colliders);
    }

    /**
     * @see Entity#collectCollidersIgnoringWorldBorder(Entity, Level, List, AABB)
     */
    private static List<VoxelShape> collectCollidersIgnoringWorldBorder(@Nullable Entity source, Level level, List<VoxelShape> entityColliders, AABB boundingBox) {
        ImmutableList.Builder<VoxelShape> colliders = ImmutableList.builderWithExpectedSize(entityColliders.size() + 1);
        if (!entityColliders.isEmpty()) {
            colliders.addAll(entityColliders);
        }

        WorldBorder worldBorder = level.getWorldBorder();
        boolean isEntityInsideCloseToBorder = source != null && worldBorder.isInsideCloseToBorder(source, boundingBox);
        if (isEntityInsideCloseToBorder) {
            colliders.add(worldBorder.getCollisionShape());
        }

        colliders.addAll(getBlockCollisions(level, boundingBox));
        return colliders.build();
    }

    /**
     * @see CollisionGetter#getBlockCollisions(Entity, AABB)
     */
    public static Iterable<VoxelShape> getBlockCollisions(CollisionGetter collisionGetter, AABB collisionBox) {
        return () -> new BlockCollisions<>(collisionGetter,
                CONTEXT,
                collisionBox,
                false,
                (BlockPos.MutableBlockPos blockPos, VoxelShape voxelShape) -> {
                    return voxelShape;
                });
    }
}
