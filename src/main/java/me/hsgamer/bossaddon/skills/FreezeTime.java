package me.hsgamer.bossaddon.skills;

import me.hsgamer.bossaddon.BossAddon;
import me.hsgamer.bossaddon.utils.Sound;
import me.hsgamer.bossaddon.utils.UMaterial;
import me.hsgamer.bossaddon.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.mineacademy.boss.api.BossSkill;
import org.mineacademy.boss.api.BossSkillDelay;
import org.mineacademy.boss.api.SpawnedBoss;

import java.util.*;

public class FreezeTime extends BossSkill {
    private int radius;
    private int time;
    private int frequency;
    private static HashMap<Entity, Vector> vector = new HashMap<>();
    private static HashMap<UUID, BukkitTask> tasks = new HashMap<>();

    @Override
    public String getName() {
        return "Freeze Time";
    }

    @Override
    public ItemStack getIcon() {
        return Utils.createItemStack(
                UMaterial.CLOCK.getMaterial(),
                "Freeze Time",
                "",
                "Make all the projectiles around the boss freeze"
        );
    }

    @Override
    public BossSkillDelay getDefaultDelay() {
        return new BossSkillDelay("1 minutes", "2 minutes");
    }

    @Override
    public String[] getDefaultMessage() {
        return new String[] {
                "{boss} has executed Freeze Time"
        };
    }


    @Override
    public void readSettings(Map<String, Object> map) {
        radius = (int) map.getOrDefault("Radius", 5);
        time = (int) map.getOrDefault("Time", 200);
        frequency = (int) map.getOrDefault("Frequency", 10);
    }

    @Override
    public Map<String, Object> writeSettings() {
        final Map<String, Object> map = new HashMap<>();

        map.put("Radius", radius);
        map.put("Time", time);
        map.put("Frequency", frequency);

        return map;
    }

    @Override
    public String[] getDefaultHeader() {
        return new String[] {
                "  Time - How long are the projectiles freezing (in tick) ?",
                "  Radius - The radius of the zone",
                "  Frequency - How frequently does the skill check for projectiles (in tick) ?",
        };
    }

    @Override
    public boolean execute(SpawnedBoss spawnedBoss) {
        Entity entity = spawnedBoss.getEntity();
        Location loc = entity.getLocation();
        List<Entity> entities = new ArrayList<>();
        tasks.put(entity.getUniqueId(), (new BukkitRunnable() {

            @Override
            public void run() {
                loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, (radius * 150), radius, radius, radius, 0.01);
                loc.getWorld().playSound(loc, Sound.NOTE_PLING.bukkitSound(), 10, 2);
                for (Entity entity : loc.getWorld().getNearbyEntities(loc, radius, radius, radius)) {
                    if (entity instanceof Projectile
                            || entity.getType().equals(EntityType.SMALL_FIREBALL) || entity.getType().equals(EntityType.FIREBALL) || entity.getType().equals(EntityType.DRAGON_FIREBALL)) {
                        if (vector.containsKey(entity)) continue;
                        entities.add(entity);
                        vector.put(entity, entity.getVelocity().multiply(entity.getVelocity().length()));
                        entity.setGravity(false);
                        entity.setVelocity(new Vector(0, 0, 0));
                    }
                }
            }
        }).runTaskTimer(BossAddon.getInstance(), frequency, frequency));
        new BukkitRunnable() {

            @Override
            public void run() {
                tasks.remove(entity.getUniqueId()).cancel();
                for (Entity entity : entities) {
                    entity.setGravity(true);
                    entity.setVelocity(vector.get(entity));
                    vector.remove(entity);
                }
            }
        }.runTaskLater(BossAddon.getInstance(), time);
        return true;
    }
}
