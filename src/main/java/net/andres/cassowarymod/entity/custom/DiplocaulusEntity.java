package net.andres.cassowarymod.entity.custom;

import net.andres.cassowarymod.entity.custom.util.AnimatedTCreature;
import net.andres.cassowarymod.entity.goals.MeleeStrikeGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class DiplocaulusEntity extends AnimatedTCreature {

    public static AttributeSupplier setAttributes(){
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .build();
    }

    private final int baseAttackDuration = 10;
    private final int baseAttackActionPoint = 4;

    protected DiplocaulusEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setTextureId(this.random.nextInt(2));
        this.goalSelector.addGoal(1, new FloatGoal((this)));
        this.goalSelector.addGoal(2, new MeleeStrikeGoal(this, baseAttackDuration, baseAttackActionPoint, 2, 1.0, true));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));//Que las entidades se acerquen para procrear
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25D, Ingredient.of(Items.TROPICAL_FISH), false)); //Siga cuando tienes el obejto en tu mano
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0)); // Se mueve por el mundo
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public boolean isFood(ItemStack pStack) {return pStack.is(Items.TROPICAL_FISH); }

    @Override
    public boolean isTame() {
        return false;
    }

}
