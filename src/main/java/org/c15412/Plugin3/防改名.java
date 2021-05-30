package org.c15412.Plugin3;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class 防改名 implements Listener {
    @EventHandler
    public void 防改名(PrepareAnvilEvent 改名) {
        try {
            ItemStack 被改名物品 = 改名.getInventory().getItem(0);
            if (被改名物品.getType().equals(Material.ENCHANTED_BOOK) && 获取.特殊值(被改名物品) == 8888) {
                改名.setResult(null);
            }
        } catch (Exception ignored) {
        }
    }
}
