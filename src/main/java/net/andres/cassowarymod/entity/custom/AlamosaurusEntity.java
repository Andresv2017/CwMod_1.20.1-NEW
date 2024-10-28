package net.andres.cassowarymod.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;


import java.util.Random;

public class AlamosaurusEntity extends TamableAnimal implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    public AlamosaurusEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setTame(false);
        this.goalSelector.addGoal(1, new FloatGoal((this)));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1D));//Que las entidades se acerquen para procrear
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.MELON_SLICE), false)); //Siga cuando tienes el obejto en tu mano
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0)); // Se mueve por el mundo
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new FollowOwnerGoal(this, 1.0,10.0F,2.0F, false));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier setAttributes(){
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .build();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController[] { new AnimationController((GeoAnimatable)this, "normal", 5, this::movementPredicate) });
        controllers.add(new AnimationController[] { new AnimationController((GeoAnimatable)this, "attackcontroller", 5, this::attackPredicate) });
        controllers.add(genericDeathController(this));
    }

    public static final RawAnimation DIE = RawAnimation.begin().thenPlay("animation.alamo_dt.sleeping");

    public static <T extends LivingEntity & GeoAnimatable> AnimationController<T> genericDeathController(T animatable) {
        return new AnimationController<>(animatable, "Death", 0, state -> state.getAnimatable().isDeadOrDying() ? state.setAndContinue(DIE) : PlayState.STOP);
    }


    public final Random random = new Random();

    public PlayState attackPredicate(AnimationState event) {
        if(this.swinging && event.getController().getAnimationState().equals(AnimationController.State.STOPPED)){
            event.getController().forceAnimationReset();

            int AttackCount = random.nextInt(2);

            if(AttackCount == 0) {
                event.getController().setAnimation(RawAnimation.begin().then("animation.alamo_dt.stomp_attacking", Animation.LoopType.PLAY_ONCE));
            } else {
                event.getController().setAnimation(RawAnimation.begin().then("animation.alamo_dt.tail_attacking",Animation.LoopType.PLAY_ONCE));
            }

            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.alamo_dt.running");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.alamo_dt.walking");
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.alamo_dt.default");


    public PlayState movementPredicate(AnimationState event) {
        if (this.getDeltaMovement().horizontalDistance() > 1.0E-6D && !this.isInWater()) {
            if (isSprinting()) {
                //System.out.println("Ejecutando animación: SPRINT");
                event.setAndContinue(RUN);
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
            event.setAndContinue(RUN);
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

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
