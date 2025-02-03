package jp.reitou_mugicha.scarynight;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ScaryNightEvent implements Listener
{
    private final LanguageManager languageManager;
    private final ScaryNightController controller;

    private int healthMultiplier;
    private int dropItemMultiplier;

    public ScaryNightEvent(ScaryNight plugin)
    {
        this.languageManager = plugin.getLanguageManager();
        this.controller = plugin.getController();

        this.healthMultiplier = plugin.getConfig().getInt("health_multiplier");
        this.dropItemMultiplier = plugin.getConfig().getInt("dropitem_multiplier");
    }

    public void reloadSettings(ScaryNight plugin) {
        healthMultiplier = plugin.getConfig().getInt("health_multiplier");
        dropItemMultiplier = plugin.getConfig().getInt("dropitem_multiplier");
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event)
    {
        if (controller.isScaryNightActive())
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(languageManager.getMessage("scarynight.bed_enter"));
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event)
    {
        if (controller.isScaryNightActive())
        {
            LivingEntity entity = event.getEntity();
            entity.setMaxHealth(entity.getMaxHealth() * healthMultiplier);
            entity.setHealth(entity.getMaxHealth());

            if (entity.getEquipment() != null)
            {
                EntityEquipment equipment = entity.getEquipment();
                setDropChanceZero(equipment);

                switch (entity.getType())
                {
                    case SKELETON:
                    case STRAY:
                    case BOGGED:
                        equipment.setHelmet(ItemCreator.of(CompMaterial.LEATHER_HELMET).enchant(Enchantment.PROTECTION, getRandomLevel(1, 3)).make());
                        equipment.setChestplate(ItemCreator.of(CompMaterial.LEATHER_CHESTPLATE).enchant(Enchantment.PROTECTION, getRandomLevel(1, 3)).make());
                        equipment.setLeggings(ItemCreator.of(CompMaterial.LEATHER_LEGGINGS).enchant(Enchantment.PROTECTION, getRandomLevel(1, 3)).make());
                        equipment.setBoots(ItemCreator.of(CompMaterial.LEATHER_BOOTS).enchant(Enchantment.PROTECTION, getRandomLevel(1, 3)).make());
                        equipment.setItemInMainHand(ItemCreator.of(CompMaterial.BOW).enchant(Enchantment.POWER, getRandomLevel(1, 8)).enchant(Enchantment.INFINITY, 1).make());
                        break;
                    case ZOMBIE:
                    case ZOMBIE_VILLAGER:
                    case HUSK:
                        equipment.setHelmet(ItemCreator.of(CompMaterial.IRON_HELMET).enchant(Enchantment.PROTECTION, getRandomLevel(1, 8)).make());
                        equipment.setChestplate(ItemCreator.of(CompMaterial.IRON_CHESTPLATE).enchant(Enchantment.PROTECTION, getRandomLevel(1, 8)).enchant(Enchantment.FIRE_PROTECTION, getRandomLevel(1, 5)).make());
                        equipment.setLeggings(ItemCreator.of(CompMaterial.IRON_LEGGINGS).enchant(Enchantment.PROTECTION, getRandomLevel(1, 8)).make());
                        equipment.setBoots(ItemCreator.of(CompMaterial.IRON_BOOTS).enchant(Enchantment.PROTECTION, getRandomLevel(1, 8)).make());
                        equipment.setItemInMainHand(ItemCreator.of(CompMaterial.DIAMOND_SWORD).enchant(Enchantment.SHARPNESS, getRandomLevel(3, 5)).enchant(Enchantment.FIRE_ASPECT, getRandomLevel(1, 5)).make());
                        equipment.setItemInOffHand(new ItemStack(Material.TOTEM_OF_UNDYING));
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        if (controller.isScaryNightActive())
        {
            event.getDrops().forEach(drop -> drop.setAmount(drop.getAmount() * dropItemMultiplier));
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event)
    {
        if (controller.isScaryNightActive())
        {
            if (event.getEntity().getType() == EntityType.SKELETON || event.getEntity().getType() == EntityType.STRAY || event.getEntity().getType() == EntityType.BOGGED)
            {
                if (new Random().nextInt(10) == 0)
                {
                    event.getProjectile().setFireTicks(100);
                }
            }
        }
    }

    private void setDropChanceZero(EntityEquipment equipment)
    {
        equipment.setHelmetDropChance(0.0f);
        equipment.setChestplateDropChance(0.0f);
        equipment.setLeggingsDropChance(0.0f);
        equipment.setBootsDropChance(0.0f);
        equipment.setItemInMainHandDropChance(0.0f);
        equipment.setItemInOffHandDropChance(0.0f);
    }

    private int getRandomLevel(int min, int max)
    {
        return new Random().nextInt(max - min + 1) + min;
    }
}
