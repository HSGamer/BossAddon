package me.hsgamer.bossaddon.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Utils {
    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static ItemStack createItemStack(Material material, String name, String... lores) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(colorize("&r" + name));
        for (int i = 0; i < lores.length; i++)
            lores[i] = colorize("&7" + lores[i]);
        itemMeta.setLore(Arrays.asList(lores));

        item.setItemMeta(itemMeta);

        return item;
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
