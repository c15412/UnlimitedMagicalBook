package org.c15412.Plugin3;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.c15412.Plugin3.扩展类.物品;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.bukkit.Material.DIAMOND;
import static org.bukkit.Material.NETHER_STAR;

public class 无尽卷轴 implements Listener {

    public static ItemStack 无尽卷轴(int 数量) {
        物品 无尽卷轴 = new 物品(Material.ENCHANTED_BOOK, 数量);
        无尽卷轴.设置名称("§b§l无尽卷轴");
        无尽卷轴.设置注释("§e§l当前存储物品为：", "§a§l", "§b§l物品数量：", "§a§l0");
        无尽卷轴.设置特殊值(8888);
        return 无尽卷轴;
    }

    public static ShapedRecipe 合成无尽卷轴() {
        ShapedRecipe 配方 = new ShapedRecipe(获取.key("Unlimited-Chest"), 无尽卷轴(1));
        配方.shape("aaa", "aba", "aaa");
        配方.setIngredient('b', DIAMOND);
        配方.setIngredient('a', 获取.准确原料(NETHER_STAR));
        return 配方;
    }

    @EventHandler
    public void 拾取物品(EntityPickupItemEvent 拾取物品) {
        new BukkitRunnable() {
            @Override
            public void run() {
                运行();
                获取.终止进程(this.getTaskId());
            }

            void 运行() {
                if (拾取物品.getEntity() instanceof Player) {
                    Player 玩家 = (Player) 拾取物品.getEntity();
                    if (玩家.hasPermission("UMB.USE")) {

                        Inventory 背包 = 玩家.getInventory();
                        final int[] 格子编号 = {0};
                        ItemStack 捡起的物品 = 拾取物品.getItem().getItemStack();
                        final ItemStack[] 无尽卷轴 = {无尽卷轴(1)};
                        final Material[] 物品种类 = {拾取物品.getItem().getItemStack().getType()};
                        final Material[] 材料种类 = {Material.AIR};
                        final boolean[] flag = {false};
                        if (背包.contains(Material.ENCHANTED_BOOK)) {
                            for (ItemStack 格子物品 : 背包) {
                                if (格子物品 != null)
                                    if (格子物品.hasItemMeta()) {
                                        if (格子物品.getType().equals(Material.ENCHANTED_BOOK))
                                            if (获取.特殊值(格子物品) == 8888)
                                                if (获取.物品名称(格子物品).equals("§b§l无尽卷轴")) {
                                                    if (格子物品.getItemMeta().hasLore()) {
                                                        List<String> 注释 = 获取.注释(格子物品);
                                                        if (!注释.get(1).replace("§a§l", "").equals("")) {
                                                            if (物品种类[0].name().equals(注释.get(1).replace("§a§l", ""))) {
                                                                材料种类[0] = 物品种类[0];
                                                                if (捡起的物品.equals(new ItemStack(材料种类[0], 捡起的物品.getAmount()))) {
                                                                    无尽卷轴[0] = 格子物品;
                                                                    格子编号[0] = 背包.first(格子物品);
                                                                    flag[0] = true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                    }
                            }
                            if (flag[0]) {
                                List<String> 注释 = 获取.注释(无尽卷轴[0]);
                                int 数量 = 获取.数值(注释.get(3).replace("§a§l", ""));
                                final int[] 背包内总数量 = {0};
                                List<Integer> 序号 = new ArrayList<>();
                                for (int i = 0; i < 背包.getSize(); i++) {
                                    ItemStack 格子物品 = 背包.getItem(i);
                                    if (格子物品 != null)
                                        if (格子物品.equals(new ItemStack(材料种类[0], 格子物品.getAmount()))) {
                                            背包内总数量[0] = 背包内总数量[0] + 格子物品.getAmount();
                                            背包.clear(i);
                                        }
                                }
                                数量 = 数量 + 背包内总数量[0];
                                注释.set(3, "§a§l" + 数量);
                                物品.设置注释(无尽卷轴[0], 注释);
                            }
                        }
                    }
                }
            }
        }.runTaskAsynchronously(获取.插件);
    }

    @EventHandler
    public void 设置存储物品(PlayerInteractEvent 右键卷轴) {
        new BukkitRunnable() {
            @Override
            public void run() {
                运行();
                获取.终止进程(this.getTaskId());
            }

            void 运行() {
                if (右键卷轴.getAction().name().toUpperCase().contains("RIGHT_CLICK"))
                    if (右键卷轴.getPlayer().hasPermission("UMB.USE"))
                        if (!右键卷轴.useItemInHand().equals(Event.Result.DENY))
                            if (右键卷轴.hasItem()) {
                                ItemStack 手中物品 = 右键卷轴.getItem();
                                if (手中物品.getType().equals(Material.ENCHANTED_BOOK))
                                    if (手中物品.hasItemMeta())
                                        if (获取.特殊值(手中物品) == 8888)
                                            if (手中物品.getItemMeta().hasLore()) {
                                                List<String> 注释 = 获取.注释(手中物品);
                                                if (注释.size() == 4)
                                                    if (右键卷轴.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                                                        if (!右键卷轴.getClickedBlock().getType().isInteractable()) {
                                                            Block 点击的方块 = 右键卷轴.getClickedBlock();
                                                            Material 方块类型 = 点击的方块.getType();
                                                            if (获取.物品名称(手中物品).equals("§b§l无尽卷轴") || 获取.物品名称(手中物品).equals("§7§l无尽卷轴（已关闭收纳功能）")) {

                                                                if (注释.get(1).equals("§a§l") || 注释.get(3).equals("§a§l0")) {
                                                                    注释.set(1, "§a§l" + 方块类型.name().replace("_VINES_PLANT", "_VINES"));
                                                                    物品.设置注释(手中物品, 注释);
                                                                } else {
                                                                    Location 位置 = 点击的方块.getLocation().add(右键卷轴.getBlockFace().getDirection()).add(0.5, 0.5, 0.5);
                                                                    if (位置.getBlockY() > 255) return;
                                                                    Block 方块 = 位置.getBlock();
                                                                    Material 物品种类 = Material.getMaterial(注释.get(1).replace("§a§l", ""));
                                                                    int 数量 = 获取.数值(注释.get(3).replace("§a§l", ""));
                                                                    数量 = 数量 - 1;
                                                                    注释.set(3, "§a§l" + 数量);
                                                                    final boolean[] flag = {false};
                                                                    if (!物品种类.isSolid()) {
                                                                        flag[0] = true;
                                                                    }
                                                                    String 方块名 = 方块.getType().name();
                                                                    if (方块.getType().isAir() || 方块.isLiquid() || 方块.isEmpty()
                                                                            || 方块.getType().equals(Material.FIRE)
                                                                            || (方块名.contains("GRASS") && !方块名.contains("BLOCK"))
                                                                            || 方块名.contains("FERN") || 方块名.contains("_SPROUTS")
                                                                            || 方块名.contains("_ROOTS")) {
                                                                        new BukkitRunnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                Collection<Entity> 附近的实体 = 方块.getWorld().getNearbyEntities(位置, 0.5, 0.5, 0.5);
                                                                                附近的实体.removeIf(实体 -> !实体.getType().isAlive());

                                                                                if (附近的实体.size() < 1) {
                                                                                    flag[0] = true;
                                                                                }
                                                                                if (flag[0]) {
                                                                                    方块.setType(物品种类);
                                                                                    设置注释(手中物品, 注释);
                                                                                }
                                                                                this.cancel();
                                                                            }
                                                                        }.runTask(获取.插件);
                                                                    }
                                                                }

                                                            }
                                                        }
                                                    } else if (右键卷轴.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                                                        Player 玩家 = 右键卷轴.getPlayer();
                                                        if (玩家.isSneaking()) {
                                                            if (获取.物品名称(手中物品).equals("§b§l无尽卷轴")) {
                                                                if (!注释.get(1).equals("§a§l"))
                                                                    设置名称(手中物品, "§7§l无尽卷轴（已关闭收纳功能）");
                                                            } else if (获取.物品名称(手中物品).equals("§7§l无尽卷轴（已关闭收纳功能）"))
                                                                设置名称(手中物品, "§b§l无尽卷轴");
                                                        } else if (获取.物品名称(手中物品).equals("§7§l无尽卷轴（已关闭收纳功能）")) {
                                                            if (!注释.get(1).equals("§a§l") && !注释.get(3).equals("§a§l0")) {
                                                                Material 物品种类 = Material.getMaterial(注释.get(1).replace("§a§l", ""));
                                                                int 数量 = 获取.数值(注释.get(3).replace("§a§l", ""));
                                                                if (数量 < 64) {
                                                                    int final数量 = 数量;
                                                                    new BukkitRunnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            玩家.getWorld().dropItem(玩家.getLocation(), new ItemStack(物品种类, final数量));
                                                                            this.cancel();
                                                                        }
                                                                    }.runTask(获取.插件);
                                                                    数量 = 0;
                                                                } else {
                                                                    new BukkitRunnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            玩家.getWorld().dropItem(玩家.getLocation(), new ItemStack(物品种类, 64));
                                                                            this.cancel();
                                                                        }
                                                                    }.runTask(获取.插件);
                                                                    数量 = 数量 - 64;
                                                                }
                                                                注释.set(3, "§a§l" + 数量);
                                                                设置注释(手中物品, 注释);
                                                            }
                                                        }
                                                    }
                                            }
                            }
            }

        }.runTaskAsynchronously(获取.插件);
    }

    void 设置注释(ItemStack 物品, List<String> 注释) {
        ItemMeta 数据 = 物品.getItemMeta();
        数据.setLore(注释);
        物品.setItemMeta(数据);
    }

    void 设置名称(ItemStack 物品, String 名称) {
        ItemMeta 数据 = 物品.getItemMeta().clone();
        数据.setDisplayName(名称);
        物品.setItemMeta(数据);
    }
}
