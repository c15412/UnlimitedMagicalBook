package org.c15412.Plugin3;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
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
                                                if (获取.物品名称(格子物品).equals("§b§l无尽卷轴"))
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
                                            序号.add(i);
                                        }
                                }
                                序号.forEach(背包::clear);
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
    public void 设置存储物品(PlayerInteractEvent 右键方块) {
        new BukkitRunnable() {
            @Override
            public void run() {
                运行();
                获取.终止进程(this.getTaskId());
            }

            void 运行() {
                if (!(右键方块.useItemInHand().equals(Event.Result.DENY) || 右键方块.useInteractedBlock().equals(Event.Result.DENY)))
                    if (右键方块.getPlayer().hasPermission("UMB.USE"))
                        if (右键方块.hasBlock())
                            if (右键方块.hasItem())
                                if (右键方块.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                                    ItemStack 手中物品 = 右键方块.getItem();
                                    Block 点击的方块 = 右键方块.getClickedBlock();
                                    Material 方块类型 = 点击的方块.getType();
                                    if (手中物品.hasItemMeta())
                                        if (手中物品.getType().equals(Material.ENCHANTED_BOOK))
                                            if (获取.特殊值(手中物品) == 8888)
                                                if (获取.物品名称(手中物品).equals("§b§l无尽卷轴"))
                                                    if (手中物品.getItemMeta().hasLore()) {
                                                        List<String> 注释 = 获取.注释(手中物品);
                                                        if (注释.get(1).equals("§a§l") || 注释.get(3).equals("§a§l0")) {
                                                            注释.set(1, "§a§l" + 方块类型.name());
                                                            物品.设置注释(手中物品, 注释);
                                                        } else {
                                                            Location 位置 = 点击的方块.getLocation().add(右键方块.getBlockFace().getDirection()).add(0.5, 0.5, 0.5);
                                                            Block 方块 = 右键方块.getClickedBlock().getWorld().getBlockAt(位置);
                                                            Material 物品种类 = Material.getMaterial(注释.get(1).replace("§a§l", ""));
                                                            int 数量 = 获取.数值(注释.get(3).replace("§a§l", ""));
                                                            if (右键方块.getClickedBlock().getType().isInteractable()) {
                                                                if (右键方块.getPlayer().isSneaking()) {
                                                                    BlockState 方块实体 = 点击的方块.getState();
                                                                    if (方块实体 instanceof Container) {
                                                                        Container 容器 = (Container) 方块实体;
                                                                        Inventory 存储清单 = 容器.getSnapshotInventory();
                                                                        if (存储清单.getStorageContents().length < 存储清单.getSize())
                                                                            if (数量 < 64) {
                                                                                数量 = 0;
                                                                                容器.getInventory().all(new ItemStack(物品种类, 数量));
                                                                            } else {
                                                                                数量 = 数量 - 64;
                                                                                容器.getInventory().all(new ItemStack(物品种类, 64));
                                                                            }
                                                                        注释.set(3, "§a§l" + 数量);
                                                                        设置注释(手中物品, 注释);
                                                                    }
                                                                }
                                                                右键方块.setCancelled(true);
                                                            } else {
                                                                数量 = 数量 - 1;
                                                                注释.set(3, "§a§l" + 数量);
                                                                new BukkitRunnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Collection<Entity> 附近的实体 = 方块.getWorld().getNearbyEntities(位置, 0.5, 0.5, 0.5);

                                                                        附近的实体.removeIf(实体 -> !实体.getType().isAlive());
                                                                        if (附近的实体.size() < 1 || !物品种类.isSolid())
                                                                            if (方块.getType().isAir() || 方块.isLiquid() || 方块.isEmpty()
                                                                                    || 方块.getType().equals(Material.FIRE)
                                                                                    || (方块.getType().name().toLowerCase().contains("grass") && !方块.getType().name().toLowerCase().contains("block"))
                                                                                    || 方块.getType().name().toLowerCase().contains("fern")) {
                                                                                方块.setType(物品种类);
                                                                                设置注释(手中物品, 注释);
                                                                                this.cancel();
                                                                            }
                                                                    }
                                                                }.runTask(获取.插件);
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
}
