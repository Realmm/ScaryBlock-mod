package com.eystreem.scaryblock.item.items.sirenhead;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.item.ScaryBlockItemGroup;
import com.eystreem.scaryblock.sound.ScaryBlockSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

@Mod.EventBusSubscriber(modid = ScaryBlockMod.MOD_ID)
public class SirenheadPickaxeItem extends PickaxeItem implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public SirenheadPickaxeItem() {
        super(ItemTier.DIAMOND, 1, -2.8F, new Properties().tab(ScaryBlockItemGroup.SCARY_BLOCK_ITEM_GROUP).setISTER(() -> SirenheadPickaxeRenderer::new));
    }

    @SubscribeEvent(receiveCanceled = true)
    public static void onUse(BlockEvent.BreakEvent e) {
        ItemStack itemStack = e.getPlayer().getMainHandItem();
        if (!(itemStack.getItem() instanceof SirenheadPickaxeItem)) return;
        if (e.getWorld().isClientSide() || !(e.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity p = (ServerPlayerEntity) e.getPlayer();
        p.getLevel().playSound(null, p, ScaryBlockSounds.ALARM.get(), SoundCategory.MASTER, 1, 1);
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

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        return 100;
    }
}
