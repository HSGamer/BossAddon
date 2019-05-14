package me.hsgamer.bossaddon.skills;

import me.hsgamer.bossaddon.utils.UMaterial;
import me.hsgamer.bossaddon.utils.Utils;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.boss.api.BossAPI;
import org.mineacademy.boss.api.BossSkill;
import org.mineacademy.boss.api.BossSkillDelay;
import org.mineacademy.boss.api.SpawnedBoss;

import java.util.HashMap;
import java.util.Map;

public class HellRound extends BossSkill {
    private int radius;
    private int damage;
    private int height;
    private boolean fireEntity;
    private int fireticks;

    @Override
    public String getName() {
        return "Hell Round";
    }

    @Override
    public ItemStack getIcon() {
        return Utils.createItemStack(
                UMaterial.BLAZE_ROD.getMaterial(),
                "Hell Round",
                "",
                "Create a square zone that",
                "damages the entity in it"
        );
    }

    @Override
    public BossSkillDelay getDefaultDelay() {
        return new BossSkillDelay("45 seconds", "2 minutes");
    }

    @Override
    public String[] getDefaultMessage() {
        return new String[] {
                "{boss} has executed Hell Round"
        };
    }


    @Override
    public void readSettings(Map<String, Object> map) {
        radius = (int) map.getOrDefault("Radius", 5);
        damage = (int) map.getOrDefault("Damage", 5);
        height = (int) map.getOrDefault("Height", 2);
        fireticks = (int) map.getOrDefault("Fire-Ticks", 100);
        fireEntity = (boolean) map.getOrDefault("Fire", false);
    }

    @Override
    public Map<String, Object> writeSettings() {
        final Map<String, Object> map = new HashMap<>();

        map.put("Radius", radius);
        map.put("Damage", damage);
        map.put("Height", height);
        map.put("Fire-Ticks", fireticks);
        map.put("Fire", fireEntity);

        return map;
    }

    @Override
    public String[] getDefaultHeader() {
        return new String[] {
                "  Height - The height of the damage zone",
                "  Radius - The radius of the damage zone",
                "  Damage - The damage the players take when they're in the damage zone",
                "  Fire-Ticks - How long do the entities keep on fire ?",
                "  Fire - Whether or not the skill makes the entities on fire",
        };
    }

    @Override
    public boolean execute(SpawnedBoss spawnedBoss) {
        Entity entity = spawnedBoss.getEntity();
        entity.getWorld().spawnParticle(Particle.FLAME, entity.getLocation(), 100 * radius, radius, height, radius, 0.05);
        entity.getWorld().spawnParticle(Particle.SMOKE_LARGE, entity.getLocation(), 50 * radius, radius, height, radius, 0.1);
        for (Entity e : entity.getNearbyEntities(radius, height, radius)) {
            if (!(e instanceof LivingEntity) || BossAPI.isBoss(e)) continue;
            ((Damageable) e).damage(damage, entity);
            if (fireEntity) {
                e.setFireTicks(fireticks);
            }
        }
        return true;
    }
}
