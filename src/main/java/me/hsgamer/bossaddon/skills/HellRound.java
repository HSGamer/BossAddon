package me.hsgamer.bossaddon.skills;

import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.boss.api.BossSkill;
import org.mineacademy.boss.api.BossSkillDelay;
import org.mineacademy.boss.api.SpawnedBoss;
import org.mineacademy.designer.model.ItemCreator;
import org.mineacademy.remain.model.CompMaterial;

import java.util.HashMap;
import java.util.Map;

public class HellRound extends BossSkill {
    private int radius;
    private int damage;
    private int height;

    @Override
    public String getName() {
        return "Hell Round";
    }

    @Override
    public ItemStack getIcon() {
        return ItemCreator.of(
                CompMaterial.BLAZE_ROD,
                "Hell Round",
                "",
                "Create a square zone that",
                "damages the entity in it"
        ).build().make();
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
    }

    @Override
    public Map<String, Object> writeSettings() {
        final Map<String, Object> map = new HashMap<>();

        map.put("Radius", radius);
        map.put("Damage", damage);
        map.put("Height", height);

        return map;
    }

    @Override
    public String[] getDefaultHeader() {
        return new String[] {
                "  Height - The height of the damage zone",
                "  Radius - The radius of the damage zone",
                "  Damage - The damage the players take when they're in the damage zone",
        };
    }

    @Override
    public boolean execute(SpawnedBoss spawnedBoss) {
        Entity entity = spawnedBoss.getEntity();
        entity.getWorld().spawnParticle(Particle.FLAME, entity.getLocation(), 100 * radius, radius, height, radius, 0.05);
        entity.getWorld().spawnParticle(Particle.SMOKE_LARGE, entity.getLocation(), 50 * radius, radius, height, radius, 0.1);
        for (Entity e : entity.getNearbyEntities(radius, height, radius)) {
            if (!(e instanceof Player)) continue;
            ((Damageable) e).damage(damage, entity);
        }
        return true;
    }
}
