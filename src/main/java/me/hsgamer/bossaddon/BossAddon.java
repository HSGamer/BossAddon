package me.hsgamer.bossaddon;

import me.hsgamer.bossaddon.skills.FreezeTime;
import me.hsgamer.bossaddon.skills.HellRound;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineacademy.boss.api.BossSkillRegistry;

public final class BossAddon extends JavaPlugin {
    private static BossAddon instance;

    public static BossAddon getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        registerSkill();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "boss reload");
    }

    private void registerSkill() {
        BossSkillRegistry.register(new HellRound());
        BossSkillRegistry.register(new FreezeTime());
    }
}
