package org.c15412.Plugin3;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Hopper;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
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
import org.c15412.Plugin3.扩展类.物品;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.bukkit.Material.*;

public class 无尽卷轴 implements Listener {

    public static ItemStack 无尽卷轴(int 数量) {
        物品 无尽卷轴 = new 物品(Material.ENCHANTED_BOOK, 数量);
        无尽卷轴.设置名称("§b§l无尽卷轴");
        无尽卷轴.设置注释("§e§l当前存储物品为：", "§a§l", "§b§l物品数量：", "§a§l0");
        无尽卷轴.设置特殊值(8888);
        return 无尽卷轴;
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
                    if (玩家.hasPermission("UMB.USE"))
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
                if (右键卷轴.getAction().name().toUpperCase().contains("RIGHT_CLICK"))
                    if (玩家.hasPermission("UMB.USE"))
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
                                                        if (获取.物品名称(手中物品).equals("§b§l无尽卷轴") || 获取.物品名称(手中物品).equals("§7§l无尽卷轴（已关闭收纳功能）")) {
                                                            if (!右键卷轴.getClickedBlock().getType().isInteractable()) {
                                                                if (右键卷轴.getClickedBlock().getType().isBlock()) {
                                                                    使用判定(手中物品);
                                                                }
                                                            } else if (右键卷轴.getPlayer().isSneaking()) {
                                                                使用判定(手中物品);
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

                                                                注释.set(3, "§a§l" + 数量);
                                                                设置注释(手中物品, 注释);
                                                            }
                                                        }
                                                    }
                                            }
                            }
            }

            void 使用判定(ItemStack 手中物品) {
                List<String> 注释 = 获取.注释(手中物品);
                Block 点击的方块 = 右键卷轴.getClickedBlock();
                Material 物品类型 = 点击的方块.getType();
                if (注释.get(1).equals("§a§l") || 注释.get(3).equals("§a§l0")) {
                    注释.set(1, "§a§l" + 物品种类(物品类型).name());
                    物品.设置注释(手中物品, 注释);
                } else {
                    Location 位置 = 点击的方块.getLocation().add(右键卷轴.getBlockFace().getDirection()).add(0.5, 0.5, 0.5);
                    if (位置.getBlockY() > 255) return;
                    Block 方块 = 位置.getBlock();
                    位置.add(0, -1, 0);
                    Block 底部方块 = 位置.getBlock();
                    位置.add(0, 1, 0);
                    Material 方块种类 = 材料种类转换为方块种类(Material.getMaterial(注释.get(1).replace("§a§l", "")));
                    if (方块种类.name().contains("SIGN")) return;
                    else if (!方块种类.isBlock()) return;
                    int 数量 = 获取.数值(注释.get(3).replace("§a§l", ""));
                    数量 = 数量 - 1;
                    注释.set(3, "§a§l" + 数量);
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
                        }
                        case WEEPING_VINES: {
                            if (位置.add(0, 1, 0).getBlock().getType().isBlock()) {
                                flag[0] = true;
                            }
                        }
                        case TORCH: {
                            if (右键卷轴.getBlockFace().getModY() == 0) 方块种类 = WALL_TORCH;
                            flag[0] = true;
                        }
                        case REDSTONE_TORCH: {
                            if (右键卷轴.getBlockFace().getModY() == 0) 方块种类 = REDSTONE_WALL_TORCH;
                            flag[0] = true;
                        }
                        default: {
                            if (方块种类.name().contains("BANNER")) return;
                            if (方块种类.isSolid()) {
                                flag[0] = true;
                            } else {
                                if (!底部方块类型.isSolid()) {
                                    switch (底部方块类型) {
                                        case COBWEB:
                                            flag[0] = true;
                                        case WEEPING_VINES:
                                        case WEEPING_VINES_PLANT: {
                                            方块种类 = WEEPING_VINES_PLANT;
                                            flag[0] = true;
                                        }
                                        case SUGAR_CANE:
                                        case KELP_PLANT:
                                        case KELP:
                                        case LADDER:
                                        case TWISTING_VINES:
                                        case TWISTING_VINES_PLANT: {
                                            flag[0] = 方块种类 == 底部方块类型;
                                        }
                                        default: {
                                        }
                                    }
                                    if (!flag[0]) return;
                            /*
                            if (类型.equals("SUGAR_CANE") || 类型.contains("VINE")||类型.contains("KELP")) {
                                if (类型.contains("WEEPING_VINES"))方块种类=Material.getMaterial("WEEPING_VINES_PLANT");
                                flag[0] = true;
                            }
                            else if (右键卷轴.getBlockFace().getModY()==-1) {
                                类型=点击的方块.getType().name();
                                if (类型.contains("VINE")||类型.contains("KELP"))flag[0]=true;
                            }
                            else return;*/
                                }
                                ;
                            }
                        }
                    }
                    String 方块名 = 方块.getType().name();
                    if (方块.getType().isAir() || 方块.isLiquid() || 方块.isEmpty()
                            || 方块.getType().equals(FIRE) || 方块.getType().equals(VINE)
                            || (方块名.contains("GRASS") && !方块名.contains("BLOCK"))
                            || 方块名.contains("FERN") || 方块名.contains("_SPROUTS")
                            || 方块名.contains("_ROOTS")) {
                        更改方块(位置, 手中物品, 注释, 方块种类, flag);
                    }
                }
            }

            void 更改方块(Location 位置, ItemStack 手中物品, List<String> 注释, Material 方块种类, boolean flag[]) {
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
                        设置注释(手中物品, 注释);
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

                    if (右键实体.getPlayer().hasPermission("UMB.USE"))
                        if (右键实体.getPlayer().getEquipment().getItemInMainHand().getType().equals(ENCHANTED_BOOK)) {
                            ItemStack 手中物品 = 右键实体.getPlayer().getEquipment().getItemInMainHand();
                            if (手中物品.hasItemMeta())
                                if (获取.特殊值(手中物品) == 8888)
                                    if (手中物品.getItemMeta().hasLore())
                                        if (获取.物品名称(手中物品).equals("§b§l无尽卷轴") || 获取.物品名称(手中物品).equals("§7§l无尽卷轴（已关闭收纳功能）")) {
                                            List<String> 注释 = 获取.注释(手中物品);
                                            if (注释.size() == 4) {
                                                Material 物品类型 = ((ItemFrame) 右键实体.getRightClicked()).getItem().getType();
                                                if (!物品类型.isAir()) {
                                                    if (注释.get(1).equals("§a§l") || 注释.get(3).equals("§a§l0")) {
                                                        注释.set(1, "§a§l" + 物品类型.name().replace("_VINES_PLANT", "_VINES"));
                                                        设置注释(手中物品, 注释);
                                                    }
                                                }
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
        ((Damageable) 数据).setDamage(8888);
        物品.setItemMeta(数据);
    }

    private void 设置名称(ItemStack 物品, String 名称) {
        ItemMeta 数据 = 物品.getItemMeta().clone();
        数据.setDisplayName(名称);
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
                if (背包.contains(ENCHANTED_BOOK)) {

                    List<Integer> 卷轴的序号 = new ArrayList<>();
                    if (背包.contains(Material.ENCHANTED_BOOK)) {

                        背包.all(ENCHANTED_BOOK).keySet().forEach(i -> {
                            ItemStack 格子物品 = 背包.getItem(i);
                            if (格子物品 != null)
                                if (格子物品.getType().equals(ENCHANTED_BOOK))
                                    if (格子物品.hasItemMeta()) {
                                        if (获取.特殊值(格子物品) == 8888)
                                            if (获取.物品名称(格子物品).equals("§b§l无尽卷轴")) {
                                                if (格子物品.getItemMeta().hasLore()) {
                                                    List<String> 注释 = 获取.注释(格子物品);
                                                    if (!注释.get(1).replace("§a§l", "").equals(""))
                                                        if (getMaterial(注释.get(1).replace("§a§l", "")) != null) {
                                                            if (背包.contains(getMaterial(注释.get(1).replace("§a§l", ""))))
                                                                卷轴的序号.add(i);
                                                        }
                                                }
                                            }
                                    }
                        });

                        if (卷轴的序号.size() > 0)
                            卷轴的序号.forEach(序号 -> {
                                ItemStack 卷轴 = 背包.getItem(序号);
                                List<String> 注释 = 获取.注释(卷轴);
                                int 数量 = 获取.数值(注释.get(3).replace("§a§l", ""));
                                Material 材料种类 = Material.getMaterial(注释.get(1).replace("§a§l", ""));
                                final int[] 背包内总数量 = {0};

                                背包.all(材料种类).keySet().forEach(i -> {
                                    ItemStack 格子物品 = 背包.getItem(i);
                                    if (格子物品 != null)
                                        if (格子物品.equals(new ItemStack(材料种类, 格子物品.getAmount()))) {
                                            背包内总数量[0] = 背包内总数量[0] + 格子物品.getAmount();
                                            背包.clear(i);
                                        }
                                });

                                数量 = 数量 + 背包内总数量[0];
                                注释.set(3, "§a§l" + 数量);
                                物品.设置注释(卷轴, 注释);
                            });
                    }
                }
                获取.终止进程(this.getTaskId());
            }
        };
    }

}
