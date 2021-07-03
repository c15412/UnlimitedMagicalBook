package org.c15412.Plugin3.扩展类;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.c15412.Plugin3.获取;

import java.util.Arrays;
import java.util.List;

public class 无尽卷轴类 extends 物品 {
    int 储存的数量 = 0;
    Material 储存的材料类型;
    boolean TakeEffect;
    ItemStack 格子物品 = new ItemStack(Material.AIR);

    public 无尽卷轴类(Material 材料类型, int 数量) {
        super(Material.ENCHANTED_BOOK, 1);
        this.设置储存材料(材料类型);
        this.设置存储数量(数量);
        super.设置名称("§b§l无尽卷轴");
        super.设置注释(卷轴注释(材料类型, 数量));
        //super.设置特殊值(8888);
        super.设置无法破坏();
    }

    public 无尽卷轴类(ItemStack 卷轴) {
        super(卷轴);
        this.格子物品 = 卷轴;

        if (this.getItemMeta().hasLore()) {
            List<String> 注释 = super.getItemMeta().getLore();
            if (注释.size() == 4) {
                if (注释.get(0).equals("§e§l当前存储物品为：") && 注释.get(2).equals("§b§l物品数量：")) {
                    this.设置存储(Material.getMaterial(注释.get(1).replace("§a§l", "")), Integer.parseInt(注释.get(3).replace("§a§l", "")));
                    if (this.getItemMeta().getDisplayName().equals("§b§l无尽卷轴"))
                        TakeEffect = true;
                    else if (this.getItemMeta().getDisplayName().equals("§7§l无尽卷轴（已关闭收纳功能）"))
                        TakeEffect = false;
                }
            }/* else {
                this.设置储存材料(Material.AIR);
                this.设置存储数量(0);
            }*/
        }/* else {
            this.设置储存材料(Material.AIR);
            this.设置存储数量(0);
        }*/

    }

    public 无尽卷轴类(Material 材料类型) {
        this(材料类型, 0);
    }

    public 无尽卷轴类() {
        this(Material.AIR, 0);
    }

    public static 无尽卷轴类 fromItemStack(ItemStack 物品) {
        //格子物品=物品;
        return new 无尽卷轴类(物品.clone());
    }

    public boolean isUMB() {
        if (获取.物品名称(this).equals("§b§l无尽卷轴") || 获取.物品名称(this).equals("§7§l无尽卷轴（已关闭收纳功能）"))
            if (super.getItemMeta().hasLore()) {
                List<String> 注释 = super.getItemMeta().getLore();
                if (注释.size() == 4)
                    return 注释.get(0).equals("§e§l当前存储物品为：") && 注释.get(2).equals("§b§l物品数量：");
            }
        return false;
    }

    List<String> 卷轴注释(Material 材料类型, int 数量) {
        if (材料类型 == null) return Arrays.asList("§e§l当前存储物品为：", "§a§lAIR", "§b§l物品数量：", "§a§l" + 数量);
        return Arrays.asList("§e§l当前存储物品为：", "§a§l" + 材料类型.name(), "§b§l物品数量：", "§a§l" + 数量);
    }

    public int 获取存储数量() {
        return 储存的数量;
    }

    public int getStoredNumber() {
        return 储存的数量;
    }

    public void setStoredNumber(int 数量) {
        this.设置存储数量(数量);
    }

    public void 设置存储数量(int 数量) {
        储存的数量 = 数量;
        this.设置注释(卷轴注释(储存的材料类型, 储存的数量));
        this.格子物品.setItemMeta(this.getItemMeta());
    }

    public void 设置存储(Material 材料类型, int 数量) {
        储存的数量 = 数量;
        储存的材料类型 = 材料类型;
        this.设置注释(卷轴注释(储存的材料类型, 储存的数量));
        this.格子物品.setItemMeta(this.getItemMeta());
    }

    public Material 获取存储材料种类() {
        return 储存的材料类型;
    }

    public Material 获取存储的材料类型() {
        return 储存的材料类型;
    }

    public Material getStoredMaterial() {
        return 储存的材料类型;
    }

    public void setStoredMaterial(Material 类型) {
        this.设置储存材料(类型);
    }

    public void setStoredMaterial(String 名称) {
        this.设置储存材料(Material.getMaterial(名称));
    }

    public void 设置储存材料(Material 类型) {
        储存的材料类型 = 类型;
        this.设置注释(卷轴注释(储存的材料类型, 储存的数量));
        this.格子物品.setItemMeta(this.getItemMeta());
    }

    public void 设置储存材料(String 名称) {
        this.设置储存材料(Material.getMaterial(名称));
    }

    public void 设置材料类型(Material 类型) {
        this.设置储存材料(类型);
    }

    public void 关闭自动收纳() {
        TakeEffect = false;
        this.设置名称("§7§l无尽卷轴（已关闭收纳功能）");
    }

    public void 开启自动收纳() {
        TakeEffect = true;
        this.设置名称("§b§l无尽卷轴");
    }

    public boolean 自动收纳状态() {
        return TakeEffect;
    }

    public boolean isTakeEffect() {
        return TakeEffect;
    }
}