package org.c15412.Plugin3;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.Bukkit.getServer;

public class 获取 {

    public static final Plugin 插件 = getServer().getPluginManager().getPlugin("UnlimitedMagicalBook");
    public static final ConsoleCommandSender 控制台 = getServer().getConsoleSender();

    public static int 数值(String 字符串) {
        String 数字 = 提取数字(字符串);
        if (数字.length() > 0) return Integer.parseInt(数字);
        else return 0;
    }

    public static String 提取字符(String 字符串, String 需要被提取的字符) {
        String 正则表达式 = String.format("[^%s]", 需要被提取的字符.trim());
        Pattern p = Pattern.compile(正则表达式);
        Matcher 标记器 = p.matcher(字符串);
        return 标记器.replaceAll("").trim();
    }

    public static String 提取数字(String 字符串) {
        return 提取字符(字符串, "0-9");
    }

    public static String 物品名称(ItemStack 物品) {
        if (物品.getItemMeta().hasDisplayName()) return 物品.getItemMeta().getDisplayName();
        else return 物品.getType().name();
    }

    public static int 特殊值(ItemStack 物品) {
        return 特殊值(物品.getItemMeta());
    }

    public static int 特殊值(ItemMeta 物品数据) {
        return ((Damageable) (物品数据)).getDamage();
    }

    public static List<String> 注释(ItemMeta 物品数据) {
        if (物品数据.hasLore()) return 物品数据.getLore();
        else return new ArrayList<>();
    }

    public static List<String> 注释(ItemStack 物品) {
        return 注释(物品.getItemMeta());
    }

    public static NamespacedKey key(String 字符串) {
        return new NamespacedKey(插件, 字符串);
    }

    public static NamespacedKey key(Recipe 合成表) {
        return ((Keyed) 合成表).getKey();
    }

    public static void 控制台信息(String 信息) {
        控制台.sendMessage("§e「无尽卷轴类」 " + 信息);
    }

    public static void 终止进程(int 进程ID) {
        new BukkitRunnable() {
            @Override
            public void run() {
                getScheduler().cancelTask(进程ID);
                this.cancel();
            }
        }.runTask(插件);
    }

    public static RecipeChoice.ExactChoice 准确原料(Material 物品) {
        return (new RecipeChoice.ExactChoice(new ItemStack(物品, 1)));
    }

}
