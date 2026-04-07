package com.leclowndu93150.cosycritters.particle;

import com.leclowndu93150.cosycritters.Config;
import com.leclowndu93150.cosycritters.util.SpriteHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BirdParticle extends BaseParticle {

    Vector3f target;
    Behaviour behaviour;
    BlockState perch;
    public static final Collection<BirdParticle> birds = new ArrayList<>();
    int behaviourTime;

    private enum Behaviour {
        FLYING,
        LANDING,
        PERCHED,
        CHECKING
    }

    public BirdParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z, SpriteHelper.getSprite("crow_left"));
        this.quadSize = 0.5F;
        this.setSize(0.5F, 0.5F);
        this.lifetime = 6000;
        this.target = new Vector3f((this.random.nextFloat() - 0.5F), this.random.nextFloat(), (this.random.nextFloat() - 0.5F)).normalize().mul(0.5F);
        setBehaviour(Behaviour.FLYING);
        birds.add(this);
        this.xd = target.x;
        this.yd = target.y;
        this.zd = target.z;
    }

    private void setBehaviour(Behaviour behaviour) {
        setBehaviour(behaviour, true);
    }

    private void setBehaviour(Behaviour behaviour, boolean resetBehaviourTime) {
        this.behaviour = behaviour;
        if (resetBehaviourTime) behaviourTime = 0;
        switch (behaviour) {
            case LANDING -> {
                BlockPos.MutableBlockPos highest = new BlockPos.MutableBlockPos(this.x, Integer.MIN_VALUE, this.z);
                for (int i = 0; i < 3; i++) {
                    int bx = random.nextIntBetweenInclusive((int) this.x - 16, (int) this.x + 16);
                    int bz = random.nextIntBetweenInclusive((int) this.z - 16, (int) this.z + 16);
                    int by = level.getHeight(Heightmap.Types.MOTION_BLOCKING, bx, bz) - 1;
                    if (by > highest.getY()) highest.set(bx, by, bz);
                }
                if (level.getFluidState(highest).isEmpty()) {
                    this.target = highest.getCenter().toVector3f();
                } else {
                    setBehaviour(Behaviour.FLYING);
                }
            }
            case PERCHED -> {
                this.setParticleSpeed(0, 0, 0);
                this.perch = level.getBlockState(BlockPos.containing(x, y - 0.5, z));
            }
        }
    }

    private String getRelativeDirection(float facingX, float facingZ) {
        Vector2f facing = new Vector2f(facingX, facingZ).normalize();
        Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().position();
        Vector2f relativeDirection = new Vector2f((float) (x - camPos.x()), (float) (z - camPos.z())).normalize();
        float a = (float) Math.atan2(relativeDirection.y, relativeDirection.x);
        float b = (float) Math.atan2(facing.y, facing.x);
        float c = a - b;
        if (c > Mth.PI) {
            c -= Mth.TWO_PI;
        } else if (c < -Mth.PI) {
            c += Mth.TWO_PI;
        }
        return c < 0 ? "right" : "left";
    }

    private void reactToDisturbances() {
        int reactionSpeed = Config.birdReactionSpeed.get();
        if (perch != null && !perch.equals(level.getBlockState(BlockPos.containing(x, y - 0.5, z)))) {
            setBehaviour(Behaviour.FLYING);
        } else if (this.age % reactionSpeed == 0) {
            Vec3 birdPos = new Vec3(this.x, this.y, this.z);
            if (entitiesNearby(birdPos)) {
                setBehaviour(Behaviour.FLYING);
            }
        }
    }

    private boolean entitiesNearby(Vec3 pos) {
        int dist = Config.birdReactionDistance.get();
        List<Entity> nearbyEntities = level.getEntitiesOfClass(Entity.class, AABB.ofSize(pos, dist, dist, dist));
        return !nearbyEntities.isEmpty();
    }

    private void setFlyingSprite(double speed, double oldSpeed) {
        if (speed < oldSpeed & y < yo) {
            this.setSprite(SpriteHelper.getSprite("crow_flying_%s_1".formatted(getRelativeDirection((float) xd, (float) zd))));
        } else {
            this.setSprite(SpriteHelper.getSprite("crow_flying_%s_%d".formatted(getRelativeDirection((float) xd, (float) zd), (age / 2) % 2)));
        }
    }

    @Override
    public void remove() {
        birds.remove(this);
        super.remove();
    }

    @Override
    public void tick() {
        super.tick();
        if (Double.isNaN(y)) {
            this.remove();
        }
        Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().position();
        int despawnDist = Config.birdDespawnDistance.get();
        if (camPos.distanceToSqr(x, y, z) > Mth.square(despawnDist)) {
            this.remove();
        }
        behaviourTime++;

        int maxBehaviourTime = Config.birdMaxBehaviourTime.get();
        int blockAvoidDist = Config.birdBlockAvoidanceDistance.get();
        float blockAvoidFactor = Config.birdBlockAvoidanceFactor.get().floatValue();
        float maxSpeed = Config.birdMaxSpeed.get().floatValue();
        float minSpeed = Config.birdMinSpeed.get().floatValue();
        int flockRange = Config.birdFlockRange.get();
        int separationDist = Config.birdSeparationDistance.get();
        float alignment = Config.birdAlignment.get().floatValue();
        float cohesion = Config.birdCohesion.get().floatValue();
        float separation = Config.birdSeparation.get().floatValue();
        int flightHeightLimit = Config.birdFlightHeightLimit.get();
        float flightHeightLimitFactor = Config.birdFlightHeightLimitFactor.get().floatValue();
        float flightRandomness = Config.birdFlightRandomness.get().floatValue();
        float landingResponsiveness = Config.birdLandingResponsiveness.get().floatValue();

        switch (behaviour) {
            case FLYING -> {
                double oldSpeed2 = Vector3d.length(xd, yd, zd);

                double avgVelX = 0, avgVelY = 0, avgVelZ = 0;
                double avgPosX = 0, avgPosY = 0, avgPosZ = 0;
                double closeXd = 0, closeYd = 0, closeZd = 0;

                int neighbors = 0;
                for (BirdParticle other : birds) {
                    if (other == this) continue;
                    double distance = Vector3d.distance(x, y, z, other.x, other.y, other.z);
                    if (distance < flockRange) {
                        avgVelX += other.xd; avgVelY += other.yd; avgVelZ += other.zd;
                        avgPosX += other.x;  avgPosY += other.y;  avgPosZ += other.z;
                        neighbors += 1;
                        if (distance < separationDist) {
                            closeXd += this.x - other.x;
                            closeYd += this.y - other.y;
                            closeZd += this.z - other.z;
                        }
                    }
                }
                if (neighbors > 0) {
                    avgVelX /= neighbors; avgVelY /= neighbors; avgVelZ /= neighbors;
                    avgPosX /= neighbors; avgPosY /= neighbors; avgPosZ /= neighbors;
                    this.xd += ((avgVelX - this.xd) * alignment)
                            + ((avgPosX - this.x) * cohesion)
                            + (closeXd * separation)
                            + random.triangle(0, flightRandomness);
                    this.yd += ((avgVelY - this.yd) * alignment)
                            + ((avgPosY - this.y) * cohesion)
                            + (closeYd * separation)
                            + random.triangle(0, flightRandomness);
                    this.zd += ((avgVelZ - this.zd) * alignment)
                            + ((avgPosZ - this.z) * cohesion)
                            + (closeZd * separation)
                            + random.triangle(0, flightRandomness);
                }

                if (y - level.getHeight(Heightmap.Types.MOTION_BLOCKING, (int) x, (int) z) > flightHeightLimit)
                    this.yd -= flightHeightLimitFactor;

                Vec3 lineToCast = new Vec3(xd, yd, zd).normalize().multiply(blockAvoidDist, blockAvoidDist, blockAvoidDist);
                Vec3 start = new Vec3(x, y, z);
                Vec3 end = new Vec3(x, y, z).add(lineToCast);
                BlockHitResult hitResult = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, CollisionContext.empty()));
                if (hitResult.getType() != HitResult.Type.MISS) {
                    float factor = blockAvoidFactor;
                    if (hitResult.getLocation().closerThan(start, 1)) {
                        if (hitResult.getDirection() == Direction.UP
                                && behaviourTime > 60
                                && level.getFluidState(hitResult.getBlockPos()).isEmpty()) {
                            Vec3 loc = hitResult.getLocation();
                            this.setPos(loc.x, loc.y + 0.5, loc.z);
                            setBehaviour(Behaviour.PERCHED);
                            return;
                        }
                        factor = 0.4F;
                    }
                    xd -= Math.signum(xd) * factor;
                    yd -= Math.signum(yd) * factor;
                    zd -= Math.signum(zd) * factor;
                }

                double speed2 = Vector3d.length(xd, yd, zd);
                if (speed2 > maxSpeed) {
                    this.xd = (this.xd / speed2) * maxSpeed;
                    this.yd = (this.yd / speed2) * maxSpeed;
                    this.zd = (this.zd / speed2) * maxSpeed;
                } else if (speed2 < minSpeed) {
                    this.xd = (this.xd / speed2) * minSpeed;
                    this.yd = (this.yd / speed2) * minSpeed;
                    this.zd = (this.zd / speed2) * minSpeed;
                }

                if (behaviourTime > maxBehaviourTime) {
                    setBehaviour(Behaviour.LANDING);
                }

                setFlyingSprite(speed2, oldSpeed2);
            }
            case LANDING -> {
                double oldSpeed2 = Vector3d.length(xd, yd, zd);
                Vec3 targetOffset = new Vec3(x - target.x, y - target.y, z - target.z);
                float responsiveness = (float) Math.min(landingResponsiveness, targetOffset.length());

                if (this.x > target.x) this.xd -= responsiveness;
                if (this.x < target.x) this.xd += responsiveness;
                if (this.y > target.y) this.yd -= responsiveness;
                if (this.y < target.y) this.yd += responsiveness;
                if (this.z > target.z) this.zd -= responsiveness;
                if (this.z < target.z) this.zd += responsiveness;
                BlockPos pos = BlockPos.containing(x, y - 0.7, z);
                BlockState state = level.getBlockState(pos);
                if (!state.getCollisionShape(level, pos).isEmpty()) {
                    setBehaviour(Behaviour.PERCHED);
                }
                double speed2 = Vector3d.length(xd, yd, zd);
                setFlyingSprite(speed2, oldSpeed2);
            }
            case PERCHED -> {
                if (random.nextFloat() < 0.01) setBehaviour(Behaviour.CHECKING, false);
                this.setSprite(SpriteHelper.getSprite("crow_" + getRelativeDirection(target.x, target.z)));
                reactToDisturbances();
                if (behaviourTime > maxBehaviourTime) {
                    setBehaviour(Behaviour.FLYING);
                }
            }
            case CHECKING -> {
                if (random.nextFloat() < 0.02) setBehaviour(Behaviour.PERCHED, false);
                this.setSprite(SpriteHelper.getSprite("crow_checking_" + getRelativeDirection(target.x, target.z)));
                reactToDisturbances();
            }
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Provider(SpriteSet spriteSet) {}

        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ, RandomSource random) {
            return new BirdParticle(level, x, y, z);
        }
    }
}
