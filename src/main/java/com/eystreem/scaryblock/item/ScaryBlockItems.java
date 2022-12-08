package com.eystreem.scaryblock.item;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.entities.ScaryBlockEntityTypes;
import com.eystreem.scaryblock.item.items.*;
import com.eystreem.scaryblock.item.items.armor.HuggyWuggyFeetItem;
import com.eystreem.scaryblock.item.items.eye.Entity303EyeItem;
import com.eystreem.scaryblock.item.items.giantalex.GiantAlexHandItem;
import com.eystreem.scaryblock.item.items.sirenhead.SirenheadPickaxeItem;
import com.eystreem.scaryblock.item.items.spawner.EntitySpawnerBlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ScaryBlockItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ScaryBlockMod.MOD_ID);

    public static final RegistryObject<Item> BLOOD_ARMOR_CHESTPLATE = ITEMS.register("blood_chestplate", BloodChestplateItem::new);
    public static final RegistryObject<Item> SIRENHEAD_PICKAXE = ITEMS.register("sirenhead_pickaxe", SirenheadPickaxeItem::new);
    public static final RegistryObject<Item> HUGGYWUGGY_FEET = ITEMS.register("huggywuggy_feet", HuggyWuggyFeetItem::new);
    public static final RegistryObject<Item> VLLR_STAFF = ITEMS.register("vllr_staff", VllrStaffItem::new);
    public static final RegistryObject<Item> GHOSTBUSTER_GUN = ITEMS.register("ghostbuster_gun", GhostBusterGunItem::new);
    public static final RegistryObject<Item> ENTITY_303_EYE = ITEMS.register("eye", Entity303EyeItem::new);
    public static final RegistryObject<Item> GIANT_ALEX_HAND = ITEMS.register("giant_alex", GiantAlexHandItem::new);
    public static final RegistryObject<Item> ENTITY_SPAWNER = ITEMS.register("entity_spawner", EntitySpawnerBlockItem::new);
    public static final RegistryObject<Item> HEROBRINE_SPAWN_EGG = ITEMS.register("herobrine_spawn_egg", () ->
            new ForgeSpawnEggItem(ScaryBlockEntityTypes.HEROBRINE_ENTITY, 0x000000, 0xff1100,
                    new Item.Properties().tab(ScaryBlockItemGroup.SCARY_BLOCK_ITEM_GROUP)));
    public static final RegistryObject<Item> BLOOD_GOLEM_SPAWN_EGG = ITEMS.register("blood_golem_spawn_egg", () ->
            new ForgeSpawnEggItem(ScaryBlockEntityTypes.BLOOD_GOLEM, 0xb0332a, 0x000000,
                    new Item.Properties().tab(ScaryBlockItemGroup.SCARY_BLOCK_ITEM_GROUP)));
    public static final RegistryObject<Item> THAT_THING_SPAWN_EGG = ITEMS.register("that_thing_spawn_egg", () ->
            new ForgeSpawnEggItem(ScaryBlockEntityTypes.THAT_THING, 0x316624, 0x000000,
                    new Item.Properties().tab(ScaryBlockItemGroup.SCARY_BLOCK_ITEM_GROUP)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
