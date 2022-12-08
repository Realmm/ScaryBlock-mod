package com.eystreem.scaryblock.item.items.armor;

import com.eystreem.scaryblock.armor.ScaryBlockArmorMaterial;
import com.eystreem.scaryblock.effects.ScaryBlockEffects;
import com.eystreem.scaryblock.effects.SpeedEffect;
import com.eystreem.scaryblock.item.ScaryBlockItemGroup;
import com.eystreem.scaryblock.network.NetworkMessage;
import com.eystreem.scaryblock.network.packets.DoubleJumpC2SPacket;
import com.eystreem.scaryblock.sound.ScaryBlockSounds;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.*;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.system.CallbackI;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HuggyWuggyFeetItem extends GeoArmorItem implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private boolean jumped, canJump;

    public HuggyWuggyFeetItem() {
        super(ScaryBlockArmorMaterial.HUGGYWUGGY, EquipmentSlotType.FEET,
                new Item.Properties().tab(ScaryBlockItemGroup.SCARY_BLOCK_ITEM_GROUP));
    }

    public boolean canDoubleJump() {
        return !jumped;
    }

    public void doubleJump(ServerPlayerEntity p) {
        if (!canDoubleJump()) return;
        canJump = true;

        p.level.playSound(null, p, ScaryBlockSounds.DOUBLE_JUMP.get(), SoundCategory.MASTER, 1, 1);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private void spawnParticle(World w, double x, double y, double z, double offset) {
        double xRandom = ThreadLocalRandom.current().nextDouble(-offset, offset);
        double zRandom = ThreadLocalRandom.current().nextDouble(-offset, offset);
        w.addParticle(ParticleTypes.SMOKE, xRandom + x, y, zRandom + z,
                0, 0, 0);

    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity p) {
        if (canJump && !jumped && !p.isOnGround()) {
            p.jumpFromGround();
            for (int i = 0; i < 20; i++) {
                spawnParticle(world, p.getX(), p.getY(), p.getZ(), 1);
            }
            canJump = false;
            jumped = true;
        }
        if (p.isOnGround()) jumped = false;
        if (p.inventory.armor.stream().noneMatch(i -> i.getItem() instanceof HuggyWuggyFeetItem)) return;
        p.addEffect(new EffectInstance(ScaryBlockEffects.SPEED.get()));
        p.addEffect(new EffectInstance(ScaryBlockEffects.DAMAGE_RESISTANCE.get()));
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
