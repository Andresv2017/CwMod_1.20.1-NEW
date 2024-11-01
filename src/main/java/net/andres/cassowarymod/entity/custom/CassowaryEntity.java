package net.andres.cassowarymod.entity.custom;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import java.util.Random;




public class CassowaryEntity extends TamableAnimal implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    //Tamea
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(CassowaryEntity.class, EntityDataSerializers.BOOLEAN);

    public static AttributeSupplier setAttributes(){
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.4f)
                .build();
    }


    public CassowaryEntity(EntityType<? extends CassowaryEntity> entityType, Level level) {
        super(entityType, level);
        this.setTame(false);
        this.goalSelector.addGoal(1, new FloatGoal((this)));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1D));//Que las entidades se acerquen para procrear
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.MELON_SLICE), false)); //Siga cuando tienes el obejto en tu mano
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0)); // Se mueve por el mundo
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new LeapAtTargetGoal(this, 0.4F));//Saltar al atacar
        this.goalSelector.addGoal(7, new FollowOwnerGoal(this, 1.0,10.0F,2.0F, false));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }


    private static final RawAnimation CS_SPRINT = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation CS_WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation CS_SWIM = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation CS_IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation CS_SIT = RawAnimation.begin().thenPlayAndHold("sit");


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController[] { new AnimationController((GeoAnimatable)this, "normal", 5, this::movementPredicate) });
        controllers.add(new AnimationController[] { new AnimationController((GeoAnimatable)this, "attackcontroller", 5, this::attackPredicate) });
    }

    private final Random random = new Random();

    private PlayState attackPredicate(AnimationState event) {
       if(this.swinging && event.getController().getAnimationState().equals(AnimationController.State.STOPPED)){
            event.getController().forceAnimationReset();

            int AttackCount = random.nextInt(2);

            if(AttackCount == 0) {
                event.getController().setAnimation(RawAnimation.begin().then("beak_attack", Animation.LoopType.PLAY_ONCE));
            } else {
                event.getController().setAnimation(RawAnimation.begin().then("kick",Animation.LoopType.PLAY_ONCE));
            }
            this.swinging = false;
       }
        return PlayState.CONTINUE;
    }


    protected PlayState movementPredicate(AnimationState event) {
        if(isSitting() && isTame()) {
            event.setAnimation(CS_SIT);
            event.getController().setAnimationSpeed(16D);
            return PlayState.CONTINUE;
        }
        if (this.getDeltaMovement().horizontalDistance() > 1.0E-6D && !this.isInWater()) {
            if (isSprinting()) {
                //System.out.println("Ejecutando animación: SPRINT");
                event.setAndContinue(CS_SPRINT);
                event.getController().setAnimationSpeed(2.0D);
                return PlayState.CONTINUE;
            }
            if (event.isMoving()) {
                //System.out.println("Ejecutando animación: WALK");
                event.setAndContinue(CS_WALK);
                event.getController().setAnimationSpeed(1.0D);
                return PlayState.CONTINUE;
            }
        }
        if (isInWater()) {
            //System.out.println("Ejecutando animación: SWIM");
            event.setAndContinue(CS_SWIM);
            event.getController().setAnimationSpeed(1.0D);
            return PlayState.CONTINUE;
        }

        if (!isInWater()) {
            //System.out.println("Ejecutando animación: IDLE");
            event.setAndContinue(CS_IDLE);
            event.getController().setAnimationSpeed(1.0D);
        }
        return PlayState.CONTINUE;
    }

    //Interacciones Mob con Jugador
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        Item item = itemstack.getItem();

        Item itemForTaming = Items.SWEET_BERRIES;

        if(isFood(itemstack)){
            return super.mobInteract(player, interactionHand);
        }

        if (item == itemForTaming && !this.isTame()) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            if (!this.level().isClientSide()) {
                if (this.random.nextInt(10) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                    setSitting(true);
                }
            }
            return InteractionResult.SUCCESS;
        }

        if(isTame() && !this.level().isClientSide && interactionHand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        if (itemstack.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, interactionHand);

    }


    //Guardar Estado de Tameado en el juego, para cuando vuelvas que el animal siga tameado


    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
    }
    //Implementar en el Mundo el tameado
    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
    }

    public void setSitting(boolean sitting){
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting(){
        return this.entityData.get(SITTING);
    }

    @Override
    public Team getTeam(){
        return super.getTeam();
    }

    public boolean canBeLeashed(Player player){
        return false;
    }

    @Override
    public void setTame(boolean pTamed) {
        super.setTame(pTamed);
        if(pTamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(25D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0f);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(1.0f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.5f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(16D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(1.0f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.4f);
        }
    }
    //Fin de Metodos Para Tamear

    //Bebes
    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        return ModEntities.CASSUWARY.get().create(serverLevel);
    }

    //Con que se pueden reproducir
    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.MELON_SLICE);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}

