package net.andres.cassowarymod.entity.custom;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.function.Predicate;

public class SmilodonEntity extends TamableAnimal implements GeoEntity {

    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_289448_) -> {
        EntityType<?> entitytype = p_289448_.getType();
        return entitytype == EntityType.SHEEP || entitytype == EntityType.GOAT || entitytype == EntityType.PLAYER;
    };


    public static AttributeSupplier setAttributes(){
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.5f)
                .build();
    }

    protected SmilodonEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.goalSelector.addGoal(1, new FloatGoal((this)));
        this.goalSelector.addGoal(2, new BaseMeleeAttackGoalS(this, 11, 5, 2, 1.0, true));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0)); // Se mueve por el mundo
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Attack", 4, this::attackingPredicate));
        controllers.add(new AnimationController[] { new AnimationController((GeoAnimatable)this, "movecontroller", 5, this::movementPredicate) });
    }
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("attack");

    public <E extends GeoAnimatable> PlayState attackingPredicate(AnimationState<E> state) {
        if (getAttackAnimation() == BASE_ATTACK) {
            return state.setAndContinue(ATTACK_ANIM);
        }

        state.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    protected PlayState movementPredicate(AnimationState event) {
        if (this.getDeltaMovement().horizontalDistance() > 1.0E-6D && !this.isInWater()) {
            if (isSprinting()) {
                //System.out.println("Ejecutando animación: SPRINT");
                event.setAndContinue(WALK);
                event.getController().setAnimationSpeed(2.0D);
                return PlayState.CONTINUE;
            }
            if (event.isMoving()) {
                //System.out.println("Ejecutando animación: WALK");
                event.setAndContinue(WALK);
                event.getController().setAnimationSpeed(1.0D);
                return PlayState.CONTINUE;
            }
        }
        if (isInWater()) {
            //System.out.println("Ejecutando animación: SWIM");
            event.setAndContinue(WALK);
            event.getController().setAnimationSpeed(1.0D);
            return PlayState.CONTINUE;
        }

        if (!isInWater()) {
            //System.out.println("Ejecutando animación: IDLE");
            event.setAndContinue(IDLE);
            event.getController().setAnimationSpeed(1.0D);
        }
        return PlayState.CONTINUE;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean stalking;

    public boolean isStalking() {
        return stalking;
    }

    public void setStalking(boolean stalking) {
        this.stalking = stalking;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }


    public abstract static class MoveToTargetGoalS extends Goal {

        protected final SmilodonEntity mob;
        protected final double speedModifier;
        protected final boolean followingEvenIfNotSeen;
        protected int ticksUntilNextPathRecalculation;

        protected MoveToTargetGoalS(SmilodonEntity mob, double speedModifier, boolean followingEvenIfNotSeen) {
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.followingEvenIfNotSeen = followingEvenIfNotSeen;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else {
                Path path = this.mob.getNavigation().createPath(target, 0);
                if (path != null) {
                    return true;
                } else {
                    // If we can't find a path to the target, but can reach it and see it
                    return this.getAttackReachSqr(target) >= this.mob.distanceToSqr(target) && this.mob.getSensing().hasLineOfSight(target);
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else if (!this.followingEvenIfNotSeen) {
                return !this.mob.getNavigation().isDone();
            }
            return true;
        }

        @Override
        public void start() {
            this.ticksUntilNextPathRecalculation = 0;
        }

        @Override
        public void stop() {
            mob.getNavigation().stop();
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                // Move to target
                double distToTargetSqr = this.mob.distanceToSqr(target);
                if ((this.followingEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(target))) {
                    if (--ticksUntilNextPathRecalculation <= 0) {
                        this.ticksUntilNextPathRecalculation = 2 + this.mob.getRandom().nextInt(4);
                        if (!this.mob.getNavigation().moveTo(target, this.speedModifier)) {
                            this.ticksUntilNextPathRecalculation += 15;
                        }

                        if (distToTargetSqr > 1024.0D) {
                            this.ticksUntilNextPathRecalculation += 10;
                        } else if (distToTargetSqr > 256.0D) {
                            this.ticksUntilNextPathRecalculation += 5;
                        }
                    }
                }
            }
        }

        abstract protected double getAttackReachSqr(LivingEntity entity);
    }

    private static final EntityDataAccessor<Integer> ATTACK_ANIMATION = SynchedEntityData.defineId(SmilodonEntity.class, EntityDataSerializers.INT);

    public int getAttackAnimation() {
        return this.entityData.get(ATTACK_ANIMATION);
    }

    public void setAttackAnimation(int animation) {
        this.entityData.set(ATTACK_ANIMATION, animation);
    }

    public static final int NO_ANIMATION = 0;
    public static final int BASE_ATTACK = 1;

    public static class BaseMeleeAttackGoalS extends MoveToTargetGoalS {
        private final int attackDuration;
        private final int actionPoint;
        private final int attackCoolDown;
        int attackAnimationTick;
        private long lastUseTime;
        private int ticksUntilNextAttack;

        public BaseMeleeAttackGoalS(SmilodonEntity mob, int attackDuration, int hurtTick, int attackCoolDown, double speedModifier, boolean followingEvenIfNotSeen ) {
            super(mob, speedModifier, followingEvenIfNotSeen);
            this.attackDuration = (int) Math.ceil(attackDuration / 2.0);
            this.actionPoint = hurtTick / 2;
            this.attackCoolDown = (int) Math.ceil(attackCoolDown / 2.0);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        public boolean isAttacking() {
            return this.mob.getAttackAnimation() == SmilodonEntity.BASE_ATTACK;
        }

        public boolean canUse() {
            long time = this.mob.level().getGameTime();
            if (time - this.lastUseTime < 20 || isAttacking()) {
                return false;
            } else {
                return super.canUse();
            }
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        public void start() {
            super.start();
            this.mob.setAggressive(true);
            this.attackAnimationTick = 0;
            this.ticksUntilNextAttack = 0;
        }

        public void stop() {
            super.stop();
            this.lastUseTime = this.mob.level().getGameTime();

            if (isAttacking())
                this.stopAttack();

            this.mob.setAggressive(false);
        }

        public void tick() {
            if (this.attackAnimationTick > 0)
                this.attackAnimationTick -= 1;
            if (this.ticksUntilNextAttack > 0)
                this.ticksUntilNextAttack -= 1;

            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                double distToTargetSqr = this.mob.distanceToSqr(target);

                // Move to target
                super.tick();

                // Attack target
                if (this.ticksUntilNextAttack == 0 && getAttackReachSqr(target) >= distToTargetSqr && !isAttacking()) {
                    this.mob.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
                    this.startAttack();
                }

                if (this.attackAnimationTick == 0 && isAttacking()) {
                    this.stopAttack();
                }

                if (isActionPoint())
                    this.executeAttack(target);
            }
        }

        protected void startAttack() {
            this.mob.setAttackAnimation(SmilodonEntity.BASE_ATTACK);
            this.attackAnimationTick = this.attackDuration;
        }

        protected void stopAttack() {
            this.mob.setAttackAnimation(SmilodonEntity.NO_ANIMATION);
            this.ticksUntilNextAttack = this.attackCoolDown;
        }

        protected boolean executeAttack(LivingEntity target) {
            return mob.doHurtTarget(target);
        }

        protected  boolean isActionPoint() {
            return this.attackAnimationTick == (this.attackDuration - this.actionPoint);
        }

        protected double getAttackReachSqr(LivingEntity entity) {
            return (double) (this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + entity.getBbWidth());
        }
    }

    public static class StalkAndAttackGoal extends SmilodonEntity.BaseMeleeAttackGoalS {
        private final int maxStalkTime;
        private int stalkTime; // Declarar el campo para el tiempo de acecho

        public StalkAndAttackGoal(SmilodonEntity mob, double speedModifier, int maxStalkTime, int attackDuration, int hurtTick, int attackCoolDown) {
            super(mob, attackDuration, hurtTick, attackCoolDown, speedModifier, true);
            this.maxStalkTime = maxStalkTime;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.mob.getTarget();
            if (target == null || !target.isAlive() || !SmilodonEntity.PREY_SELECTOR.test(target)) {
                return false;
            }
            return super.canUse();
        }

        @Override
        public void start() {
            super.start();
            this.mob.setStalking(true); // Activa el estado de acecho
            this.stalkTime = this.maxStalkTime; // Establece el tiempo de acecho inicial
        }

        @Override
        public void stop() {
            super.stop();
            this.mob.setStalking(false); // Desactiva el estado de acecho
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                this.stop();
                return;
            }

            double distToTargetSqr = this.mob.distanceToSqr(target);

            // Fase de acecho
            if (this.stalkTime > 0) {
                this.stalkTime--;
                this.mob.getNavigation().moveTo(target, this.speedModifier * 0.5); // Movimiento más lento
                this.mob.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());

                // Si está dentro del rango de ataque, interrumpe el acecho
                if (distToTargetSqr <= this.getAttackReachSqr(target)) {
                    this.stalkTime = 0;
                }
            } else {
                // Fase de ataque
                super.tick(); // Delega el comportamiento de ataque en la lógica de `BaseMeleeAttackGoalS`
            }
        }

        @Override
        protected boolean executeAttack(LivingEntity target) {
            // Causa daño en el tick 20 (1 segundo) desde que la animación empezó
            if (this.attackAnimationTick == 20) { // (27 ticks - 20 = 7 ticks restantes)
                return mob.doHurtTarget(target);
            }
            return false;
        }
    }



    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(ATTACK_ANIMATION, NO_ANIMATION);
    }

}
