package com.eystreem.scaryblock.item.items.giantalex;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.armor.ScaryBlockArmorMaterial;
import com.eystreem.scaryblock.item.ScaryBlockItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

@Mod.EventBusSubscriber(modid = ScaryBlockMod.MOD_ID)
public class GiantAlexHandItem extends Item implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public GiantAlexHandItem() {
        super(new Properties().tab(ScaryBlockItemGroup.SCARY_BLOCK_ITEM_GROUP)
                .setISTER(() -> GiantAlexHandRenderer::new));
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        if (context.getLevel().isClientSide) return super.onItemUseFirst(stack, context);
        BlockPos pos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if (state.isAir()) return super.onItemUseFirst(stack, context);
        context.getLevel().explode(context.getPlayer(), DamageSource.explosion(context.getPlayer()), null, pos.getX(), pos.getY(), pos.getZ(), 3, true, Explosion.Mode.DESTROY);
        return super.onItemUseFirst(stack, context);
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent e) {
        if (e.getSource().isExplosion() && e.getSource().getEntity() instanceof PlayerEntity
                && e.getEntity() instanceof PlayerEntity) e.setCanceled(true);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity p, LivingEntity entity, Hand hand) {
        entity.hurt(DamageSource.playerAttack(p), 10);
        return super.interactLivingEntity(stack, p, entity, hand);
    }

    @Override
    public void registerControllers(AnimationData data) {
//        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
//        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

}
