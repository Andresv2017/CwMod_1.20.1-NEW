package net.andres.cassowarymod.entity.custom.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
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

public class AnimatedTCreature extends TamableAnimal implements GeoEntity {

    protected AnimatedTCreature(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        if(this.isAlive()){
            return (AnimatedTCreature) this.getType().create(this.level());
        }
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController[] { new AnimationController((GeoAnimatable)this, "movecontroller", 5, this::movementPredicate) });
        controllers.add(new AnimationController<>(this, "Attack", 4, this::attackingPredicate));
    }

    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation SPRINT = RawAnimation.begin().thenLoop("sprint");
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("swim");
    public static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("attack");

    protected PlayState movementPredicate(AnimationState event) {
        if (this.getDeltaMovement().horizontalDistance() > 1.0E-6D && !this.isInWater()) {
            if (isSprinting()) {
                //System.out.println("Ejecutando animación: SPRINT");
                event.setAndContinue(SPRINT);
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
            event.setAndContinue(SWIM);
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

    public <E extends GeoAnimatable> PlayState attackingPredicate(AnimationState<E> state) {
        if (getAttackAnimation() == BASE_ATTACK) {
            return state.setAndContinue(ATTACK_ANIM);
        }
        state.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if(isFood(itemstack)){
            return super.mobInteract(player, hand);
        }

        return InteractionResult.SUCCESS;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private static final EntityDataAccessor<Integer> TEXTUREID = SynchedEntityData.defineId(AnimatedTCreature.class, EntityDataSerializers.INT);
    public void setTextureId(int i){
        this.getEntityData().set(TEXTUREID, i);
    }
    public int getTextureID(){
        return this.getEntityData().get(TEXTUREID);
    }

    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(TEXTUREID, 0);
        this.entityData.define(ATTACK_ANIMATION, NO_ANIMATION);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("TEXTUREID", this.getTextureID());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("TEXTUREID")) {
            this.setTextureId(pCompound.getInt("TEXTUREID"));
        }
    }
    private static final EntityDataAccessor<Integer> ATTACK_ANIMATION = SynchedEntityData.defineId(AnimatedTCreature.class, EntityDataSerializers.INT);

    public int getAttackAnimation() {
        return this.entityData.get(ATTACK_ANIMATION);
    }

    public void setAttackAnimation(int animation) {
        this.entityData.set(ATTACK_ANIMATION, animation);
    }

    public static final int NO_ANIMATION = 0;
    public static final int BASE_ATTACK = 1;


}
