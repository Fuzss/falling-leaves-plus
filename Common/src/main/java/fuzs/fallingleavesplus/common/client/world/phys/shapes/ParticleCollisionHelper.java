package fuzs.fallingleavesplus.common.client.world.phys.shapes;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Custom collisions for particles that take visual block shape into account.
 */
public final class ParticleCollisionHelper {

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

    private ParticleCollisionHelper() {
        // NO-OP
    }

    /**
     * @see Entity#collectColliders(Entity, Level, List, AABB)
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

        colliders.addAll(getBlockCollisions(level, source, boundingBox));
        return colliders.build();
    }

    /**
     * @see CollisionGetter#getBlockCollisions(Entity, AABB)
     */
    public static Iterable<VoxelShape> getBlockCollisions(CollisionGetter collisionGetter, @Nullable Entity source, AABB collisionBox) {
        return () -> new VisualBlockCollisions<>(collisionGetter,
                source,
                collisionBox,
                false,
                (BlockPos.MutableBlockPos blockPos, VoxelShape voxelShape) -> {
                    return voxelShape;
                });
    }
}
