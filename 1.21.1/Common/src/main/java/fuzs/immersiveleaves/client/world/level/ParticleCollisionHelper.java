package fuzs.immersiveleaves.client.world.level;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @see CollisionGetter
 */
public class ParticleCollisionHelper {

    public static Vec3 collideBoundingBox(@Nullable Entity entity, Vec3 vec, AABB collisionBox, Level level, List<VoxelShape> potentialHits) {
        List<VoxelShape> list = collectColliders(entity, level, potentialHits, collisionBox.expandTowards(vec));
        return collideWithShapes(vec, collisionBox, list);
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

        builder.addAll(getBlockCollisions(level, entity, boundingBox));
        return builder.build();
    }

    static Iterable<VoxelShape> getBlockCollisions(CollisionGetter collisionGetter, @Nullable Entity entity, AABB collisionBox) {
        return () -> new VisualBlockCollisions<>(collisionGetter,
                entity,
                collisionBox,
                false,
                (mutableBlockPos, voxelShape) -> voxelShape);
    }

    private static Vec3 collideWithShapes(Vec3 deltaMovement, AABB entityBB, List<VoxelShape> shapes) {
        if (shapes.isEmpty()) {
            return deltaMovement;
        } else {
            double d = deltaMovement.x;
            double e = deltaMovement.y;
            double f = deltaMovement.z;
            if (e != 0.0) {
                e = Shapes.collide(Direction.Axis.Y, entityBB, shapes, e);
                if (e != 0.0) {
                    entityBB = entityBB.move(0.0, e, 0.0);
                }
            }

            boolean bl = Math.abs(d) < Math.abs(f);
            if (bl && f != 0.0) {
                f = Shapes.collide(Direction.Axis.Z, entityBB, shapes, f);
                if (f != 0.0) {
                    entityBB = entityBB.move(0.0, 0.0, f);
                }
            }

            if (d != 0.0) {
                d = Shapes.collide(Direction.Axis.X, entityBB, shapes, d);
                if (!bl && d != 0.0) {
                    entityBB = entityBB.move(d, 0.0, 0.0);
                }
            }

            if (!bl && f != 0.0) {
                f = Shapes.collide(Direction.Axis.Z, entityBB, shapes, f);
            }

            return new Vec3(d, e, f);
        }
    }
}
