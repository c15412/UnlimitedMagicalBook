package org.c15412.Plugin3;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Hopper;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.c15412.Plugin3.扩展类.无尽卷轴类;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static org.bukkit.Material.*;

public class 无尽卷轴 implements Listener {

    public static 无尽卷轴类 无尽卷轴(int 数量) {
        return new 无尽卷轴类();
    }

    public static ShapedRecipe 合成无尽卷轴() {
        ShapedRecipe 配方 = new ShapedRecipe(获取.key("Unlimited-BOOK"), 无尽卷轴(1));
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
                if (拾取物品.getEntity() instanceof Player) {
                    Player 玩家 = (Player) 拾取物品.getEntity();
                    if (玩家.hasPermission("Get-M.UMB-USE"))
                        卷轴收纳物品(玩家.getInventory()).runTaskAsynchronously(获取.插件);
                }
                获取.终止进程(this.getTaskId());
            }
        }.runTaskAsynchronously(获取.插件);
    }

    @EventHandler
    public void 背包物品移动(InventoryMoveItemEvent 背包物品移动事件) {
        卷轴收纳物品(背包物品移动事件.getDestination()).runTaskAsynchronously(获取.插件);
    }

    @EventHandler
    public void 背包打开(InventoryOpenEvent 背包打开事件) {
        卷轴收纳物品(背包打开事件.getInventory()).runTaskAsynchronously(获取.插件);
    }

    @EventHandler
    public void 背包关闭(InventoryCloseEvent 背包关闭事件) {
        卷轴收纳物品(背包关闭事件.getInventory()).runTaskAsynchronously(获取.插件);
    }

    @EventHandler
    public void 卷轴右键方块(PlayerInteractEvent 右键卷轴) {
        new BukkitRunnable() {
            Player 玩家 = 右键卷轴.getPlayer();

            @Override
            public void run() {
                运行();
                获取.终止进程(this.getTaskId());
            }

            void 运行() {
                if (右键卷轴.getAction().equals(Action.RIGHT_CLICK_BLOCK) || 右键卷轴.getAction().equals(Action.RIGHT_CLICK_AIR))
                    if (玩家.hasPermission("Get-M.UMB-USE"))
                        if (!右键卷轴.useItemInHand().equals(Event.Result.DENY))
                            if (右键卷轴.hasItem()) {
                                //ItemStack 手中卷轴 = 右键卷轴.getItem();
                                if (无尽卷轴类.fromItemStack(右键卷轴.getItem()).isUMB()) {
                                    无尽卷轴类 手中卷轴 = new 无尽卷轴类(右键卷轴.getItem());
                                    if (右键卷轴.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                                        if (!右键卷轴.getClickedBlock().getType().isInteractable()) {
                                            if (右键卷轴.getClickedBlock().getType().isBlock()) {
                                                使用判定(手中卷轴);
                                            }
                                        } else if (右键卷轴.getPlayer().isSneaking()) {
                                            使用判定(手中卷轴);
                                        }
                                    } else if (右键卷轴.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                                        Player 玩家 = 右键卷轴.getPlayer();
                                        if (玩家.isSneaking()) {
                                            if (手中卷轴.isTakeEffect()) {
                                                if (!手中卷轴.getStoredMaterial().equals(AIR)) {
                                                    //设置名称(右键卷轴.getItem(), "§7§l无尽卷轴（已关闭收纳功能）");
                                                    手中卷轴.关闭自动收纳();
                                                }
                                            } else if (!手中卷轴.isTakeEffect()) {
                                                //设置名称(右键卷轴.getItem(), "§b§l无尽卷轴");
                                                手中卷轴.开启自动收纳();
                                            }
                                        } else if (!手中卷轴.isTakeEffect()) {
                                            if (!(手中卷轴.getStoredNumber() == 0 || 手中卷轴.getStoredMaterial().equals(AIR)
                                                    || 手中卷轴.getStoredMaterial() == null)) {
                                                Material 物品种类 = 手中卷轴.getStoredMaterial();
                                                int 数量 = 手中卷轴.getStoredNumber();
                                                int 最大堆叠数 = 物品种类.getMaxStackSize();
                                                int i = Integer.min(最大堆叠数, 数量);
                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        玩家.getWorld().dropItem(玩家.getLocation(), new ItemStack(物品种类, i));
                                                        this.cancel();
                                                    }
                                                }.runTask(获取.插件);
                                                数量 -= i;

                                                手中卷轴.setStoredNumber(数量);
                                                右键卷轴.getItem().setItemMeta(手中卷轴.getItemMeta());
                                            }
                                        }
                                    }
                                }
                            }
            }

            void 使用判定(无尽卷轴类 手中卷轴) {
                //List<String> 注释 = 获取.注释(手中卷轴);
                Block 点击的方块 = 右键卷轴.getClickedBlock();
                Material 物品类型 = 点击的方块.getType();
                if (手中卷轴.getStoredMaterial() == AIR || 手中卷轴.getStoredNumber() == 0) {
                    手中卷轴.设置储存材料(物品种类(物品类型));
                    右键卷轴.getItem().setItemMeta(手中卷轴.getItemMeta());
                } else {
                    Location 位置 = 点击的方块.getLocation().add(右键卷轴.getBlockFace().getDirection()).add(0.5, 0.5, 0.5);
                    if (位置.getBlockY() > 255) return;
                    Block 方块 = 位置.getBlock();
                    位置.add(0, -1, 0);
                    Block 底部方块 = 位置.getBlock();
                    位置.add(0, 1, 0);
                    Material 方块种类 = 材料种类转换为方块种类(手中卷轴.getStoredMaterial());
                    int[] 存储的数量 = {手中卷轴.getStoredNumber()};

                    if (方块种类.name().contains("SIGN")) return;
                    if (!方块种类.isBlock()) {
                        Material final方块种类 = 方块种类;
                        if (方块种类.name().contains("BOAT")) {
                            存储的数量[0]--;
                            String Species = final方块种类.name();
                            if (Species.equals("OAK_BOAT")) Species = "GENERIC";
                            else Species = Species.replace("_BOAT", "");
                            String finalSpecies = Species;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Boat 船 = (Boat) 位置.getWorld().spawnEntity(位置, EntityType.BOAT);
                                    船.setWoodType(TreeSpecies.valueOf(finalSpecies));
                                    手中卷轴.设置存储数量(存储的数量[0]);
                                    //右键卷轴.getItem().setItemMeta(手中卷轴.getItemMeta());
                                    this.cancel();
                                }
                            }.runTask(获取.插件);
                        }
                        if (方块种类.name().contains("MINECART")) {
                            存储的数量[0]--;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    位置.getWorld().spawnEntity(位置, EntityType.fromName(final方块种类.name()));
                                    手中卷轴.设置存储数量(存储的数量[0]);
                                    //右键卷轴.getItem().setItemMeta(手中卷轴.getItemMeta());
                                    this.cancel();
                                }
                            }.runTask(获取.插件);
                        }
                        return;
                    }
                    存储的数量[0] = 存储的数量[0] - 1;
                    boolean[] flag = {false};
                    //if (!是否实体堆放(方块种类)) {
                    //String 类型 = 底部方块.getType().name();
                    Material 底部方块类型 = 底部方块.getType();


                    switch (方块种类) {
                        case COBWEB:
                        case BLACK_CARPET:
                        case RED_CARPET:
                        case YELLOW_CARPET:
                        case BLUE_CARPET:
                        case CYAN_CARPET:
                        case GRAY_CARPET:
                        case LIME_CARPET:
                        case PINK_CARPET:
                        case BROWN_CARPET:
                        case GREEN_CARPET:
                        case WHITE_CARPET:
                        case ORANGE_CARPET:
                        case PURPLE_CARPET:
                        case LIGHT_GRAY_CARPET:
                        case LIGHT_BLUE_CARPET:
                        case MAGENTA_CARPET:
                        case LEGACY_CARPET:
                        case OAK_BUTTON:
                        case BIRCH_BUTTON:
                        case STONE_BUTTON:
                        case ACACIA_BUTTON:
                        case JUNGLE_BUTTON:
                        case SPRUCE_BUTTON:
                        case WARPED_BUTTON:
                        case CRIMSON_BUTTON:
                        case DARK_OAK_BUTTON:
                        case POLISHED_BLACKSTONE_BUTTON:
                        case LEGACY_WOOD_BUTTON:
                        case LEGACY_STONE_BUTTON:
                        case LEVER:
                        case VINE:
                        case LADDER: {
                            flag[0] = true;
                            break;
                        }
                        case WEEPING_VINES: {
                            if (位置.add(0, 1, 0).getBlock().getType().isBlock()) {
                                flag[0] = true;
                                break;
                            }
                        }
                        case TORCH: {
                            if (右键卷轴.getBlockFace().getModY() == 0) 方块种类 = WALL_TORCH;
                            flag[0] = true;
                            break;
                        }
                        case REDSTONE_TORCH: {
                            if (右键卷轴.getBlockFace().getModY() == 0) 方块种类 = REDSTONE_WALL_TORCH;
                            flag[0] = true;
                            break;
                        }
                        default: {
                            if (方块种类.name().contains("BANNER")) return;
                            if (方块种类.isSolid()) {
                                flag[0] = true;
                                break;
                            } else {
                                if (!底部方块类型.isSolid()) {
                                    switch (底部方块类型) {
                                        case COBWEB: {
                                            flag[0] = true;
                                            break;
                                        }
                                        case WEEPING_VINES:
                                        case WEEPING_VINES_PLANT: {
                                            方块种类 = WEEPING_VINES_PLANT;
                                            flag[0] = true;
                                            break;
                                        }
                                        case SUGAR_CANE:
                                        case KELP_PLANT:
                                        case KELP:
                                        case LADDER:
                                        case TWISTING_VINES:
                                        case TWISTING_VINES_PLANT: {
                                            flag[0] = 方块种类 == 底部方块类型;
                                            break;
                                        }
                                        default: {
                                        }
                                    }
                                    if (!flag[0]) return;
                                }
                            }
                        }
                    }
                    String 方块名 = 方块.getType().name();
                    if (方块.getType().isAir() || 方块.isLiquid() || 方块.isEmpty()
                            || 方块.getType().equals(FIRE) || 方块.getType().equals(VINE)
                            || (方块名.contains("GRASS") && !方块名.contains("BLOCK"))
                            || 方块名.contains("FERN") || 方块名.contains("_SPROUTS")
                            || 方块名.contains("_ROOTS")) {
                        更改方块(位置, 手中卷轴, 存储的数量[0], 方块种类, flag);
                    }
                }
            }

            void 更改方块(Location 位置, 无尽卷轴类 手中卷轴, int 存储的数量, Material 方块种类, boolean flag[]) {
                ;
                Block 方块 = 位置.getBlock();
                BlockData 方块数据 = 方块种类.createBlockData();
                BlockFace 点击面 = 右键卷轴.getBlockFace();
                if (方块数据 instanceof MultipleFacing) {
                    if (!(方块数据 instanceof Fence)) {
                        if (方块.getType().equals(方块种类)) 方块数据 = 方块.getBlockData();
                        if (((MultipleFacing) 方块数据).getAllowedFaces().contains(点击面.getOppositeFace()))
                            ((MultipleFacing) 方块数据).setFace(点击面.getOppositeFace(), true);
                        else return;
                    }
                }
                if (方块数据 instanceof FaceAttachable) {
                    if (点击面.getModY() < 0)
                        ((FaceAttachable) 方块数据).setAttachedFace(FaceAttachable.AttachedFace.CEILING);
                    else if (点击面.getModY() > 0)
                        ((FaceAttachable) 方块数据).setAttachedFace(FaceAttachable.AttachedFace.FLOOR);
                    else ((FaceAttachable) 方块数据).setAttachedFace(FaceAttachable.AttachedFace.WALL);
                }
                if (方块数据 instanceof Directional) {
                    if (((Directional) 方块数据).getFaces().contains(点击面)) {
                        if (方块数据 instanceof Hopper) ((Directional) 方块数据).setFacing(点击面.getOppositeFace());
                        if (方块数据 instanceof Ladder)
                            ((Directional) 方块数据).setFacing(方块.getFace(右键卷轴.getClickedBlock()).getOppositeFace());
                        if (((Directional) 方块数据).getFaces().contains(点击面)) ((Directional) 方块数据).setFacing(点击面);
                    } else {
                        BlockFace 玩家朝向 = 右键卷轴.getPlayer().getFacing();
                        if (方块数据 instanceof Ladder)
                            if (位置.add(玩家朝向.getDirection()).getBlock().getBoundingBox().contains(1, 1, 1))
                                ((Directional) 方块数据).setFacing(玩家朝向.getOppositeFace());
                            else return;
                        else if (((Directional) 方块数据).getFaces().contains(右键卷轴.getPlayer().getFacing().getOppositeFace()))
                            ((Directional) 方块数据).setFacing(玩家朝向.getOppositeFace());
                        else return;
                    }
                }

                BlockData final方块数据 = 方块数据.clone();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        run2();
                        this.cancel();
                    }

                    void run2() {
                        if (final方块数据.getMaterial().isSolid()) {
                            Collection<Entity> 附近的实体 = 方块.getWorld().getNearbyEntities(位置, 0.5, 0.5, 0.5);
                            附近的实体.removeIf(实体 -> !实体.getType().isAlive());
                            if (!(附近的实体.size() < 1)) return;
                        }
                        方块.setBlockData(final方块数据, true);
                        手中卷轴.setStoredNumber(存储的数量);
                        //右键卷轴.getItem().setItemMeta(手中卷轴.getItemMeta());
                    }
                }.runTask(获取.插件);
            }

        }.runTaskAsynchronously(获取.插件);
    }

    @EventHandler
    public void 卷轴右键实体(PlayerInteractEntityEvent 右键实体) {
        new BukkitRunnable() {
            @Override
            public void run() {
                运行();
                获取.终止进程(this.getTaskId());
            }

            void 运行() {
                if (右键实体.getRightClicked() instanceof ItemFrame) {

                    if (右键实体.getPlayer().hasPermission("Get-M.UMB-USE"))
                        if (无尽卷轴类.fromItemStack(右键实体.getPlayer().getEquipment().getItemInMainHand()).isUMB()) {
                            无尽卷轴类 手中卷轴 = new 无尽卷轴类(右键实体.getPlayer().getEquipment().getItemInMainHand());

                            Material 物品类型 = ((ItemFrame) 右键实体.getRightClicked()).getItem().getType();
                            if (!物品类型.isAir()) {
                                if (手中卷轴.getStoredMaterial().equals(AIR) || 手中卷轴.getStoredMaterial() == null
                                        || 手中卷轴.getStoredNumber() == 0) {
                                    手中卷轴.setStoredMaterial(物品类型.name().replace("_VINES_PLANT", "_VINES"));
                                }
                            }
                        }
                }
            }
        }.runTaskAsynchronously(获取.插件);
    }

    private void 设置注释(ItemStack 物品, List<String> 注释) {
        ItemMeta 数据 = 物品.getItemMeta().clone();
        数据.setLore(注释);
        数据.setUnbreakable(true);
        ((Damageable) 数据).setDamage(8888);
        物品.setItemMeta(数据);
    }

    private void 设置名称(ItemStack 物品, String 名称) {
        ItemMeta 数据 = 物品.getItemMeta().clone();
        数据.setDisplayName(名称);
        数据.setUnbreakable(true);
        ((Damageable) 数据).setDamage(8888);
        物品.setItemMeta(数据);
    }

    private Material 物品种类(Material 方块种类) {
        String name = 方块种类.name();
        if (name.contains("WALL_")) return Material.getMaterial(name.replace("WALL_", ""));
        else switch (方块种类) {
            case WEEPING_VINES_PLANT:
                return WEEPING_VINES;
            case TWISTING_VINES_PLANT:
                return TWISTING_VINES;
            case MELON_STEM:
            case ATTACHED_MELON_STEM:
                return MELON_SEEDS;
            case PUMPKIN_STEM:
            case ATTACHED_PUMPKIN_STEM:
                return PUMPKIN_SEEDS;
            case CARROTS:
                return CARROT;
            case POTATOES:
                return POTATO;
            case WHEAT:
                return WHEAT_SEEDS;
            case BEETROOTS:
                return BEETROOT_SEEDS;
            case SWEET_BERRY_BUSH:
                return SWEET_BERRIES;
            case KELP_PLANT:
                return KELP;
            case REDSTONE_WALL_TORCH:
                return REDSTONE_TORCH;
            case WALL_TORCH:
                return TORCH;
            case BAMBOO_SAPLING:
                return BAMBOO;
            default:
                return 方块种类;
        }
    }

    private Material 材料种类转换为方块种类(Material 物品种类) {
        switch (物品种类) {
            case CARROT:
                return CARROTS;
            case POTATO:
                return POTATOES;
            case WHEAT_SEEDS:
                return WHEAT;
            case BEETROOT_SEEDS:
                return BEETROOTS;
            case MELON_SEEDS:
                return MELON_STEM;
            case PUMPKIN_SEEDS:
                return PUMPKIN_STEM;
            case SWEET_BERRIES:
                return SWEET_BERRY_BUSH;
            default:
                return 物品种类;
        }
    }

    public BukkitRunnable 卷轴收纳物品(Inventory 背包) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                List<Integer> 卷轴的序号 = new ArrayList<>();
                if (背包.contains(Material.ENCHANTED_BOOK)) {

                    背包.all(ENCHANTED_BOOK).keySet().forEach(i -> {
                        if (无尽卷轴类.fromItemStack(背包.getItem(i)).isUMB()) {
                            无尽卷轴类 无尽卷轴 = new 无尽卷轴类(背包.getItem(i));
                            if (!(无尽卷轴.getStoredMaterial().equals(AIR) || 无尽卷轴.getStoredMaterial() == null))
                                if (无尽卷轴.isTakeEffect()) {
                                    卷轴的序号.add(i);
                                }
                        }
                    });

                    if (卷轴的序号.size() > 0)
                        卷轴的序号.forEach(序号 -> {
                            无尽卷轴类 卷轴 = new 无尽卷轴类(背包.getItem(序号));
                            //List<String> 注释 = 卷轴.getItemMeta().getLore();
                            int 数量 = 卷轴.getStoredNumber();
                            Material 材料种类 = 卷轴.getStoredMaterial();
                            final int[] 背包内总数量 = {0};

                            IntStream.range(0, 背包.getSize()).forEach(i -> {
                                //if (!卷轴的序号.contains(i)) {
                                ItemStack 格子物品 = 背包.getItem(i);
                                if (格子物品 != null)
                                    if (格子物品.getType().equals(材料种类))
                                        if (格子物品.equals(new ItemStack(材料种类, 格子物品.getAmount()))) {
                                            背包内总数量[0] = 背包内总数量[0] + 格子物品.getAmount();
                                            背包.clear(i);
                                        }
                                //}
                            });

                            数量 = 数量 + 背包内总数量[0];
                            卷轴.setStoredNumber(数量);
                            //背包.setItem(序号, 卷轴);
                        });
                }
                获取.终止进程(this.getTaskId());
            }
        };
    }

}
