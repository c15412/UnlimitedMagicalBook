package org.c15412.Plugin3.扩展类;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.c15412.Plugin3.获取;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class 物品 extends ItemStack {
    public 物品(final Material 类型) {super(类型, 1);}
    public 物品(final Material 类型, final int 数量) {super(类型, 数量);}
    public 物品(final 物品 物品){super(物品);}

    public static ItemMeta 设置无法破坏属性(ItemMeta 物品数据) {物品数据.setUnbreakable(true);return 物品数据;}
    public static void 设置无法破坏(ItemMeta 物品数据) {物品数据.setUnbreakable(true);}
    public static void 设置无法破坏(ItemStack 物品){ItemMeta 物品数据=物品.getItemMeta();设置无法破坏(物品数据);物品.setItemMeta(物品数据);}
    public static void 设置物品数据(ItemStack 物品, ItemMeta 物品数据){物品.setItemMeta(物品数据);}
    public static void 设置注释(ItemStack 物品, List<String> 注释){ItemMeta 数据=物品.getItemMeta();设置注释(数据,注释);物品.setItemMeta(数据); }
    public static void 设置注释(ItemMeta 物品数据, List<String> 注释){物品数据.setLore(注释);}
    public static void 设置注释(ItemMeta 物品数据, String... 注释){设置注释(物品数据,Arrays.asList(注释));}
    public static void 设置注释(ItemStack 物品, String... 注释){设置注释(物品,Arrays.asList(注释));}
    public static void 设置注释(ItemStack 物品, int 行数,String 修改的注释){ ItemMeta 数据=物品.getItemMeta();设置注释(数据,行数,修改的注释);物品.setItemMeta(数据); }
    public static void 设置注释(ItemMeta 物品数据, int 行数, String 修改的注释) { List<String> 物品注释 = 物品数据.getLore();if (行数 < 物品注释.size()) 物品注释.set(行数, 修改的注释);else 获取.控制台信息("§c§l此行为错误!");物品数据.setLore(物品注释); }
    public static void 设置名称(ItemStack 物品, String 名称){ItemMeta temp=物品.getItemMeta();temp.setDisplayName(名称);物品.setItemMeta(temp);}

    public void 设置无法破坏(){ItemMeta 物品数据=this.获取数据();设置无法破坏(物品数据);this.设置数据(物品数据);}
    public void 设置可以破坏(){ItemMeta 物品数据=this.获取数据();物品数据.setUnbreakable(false);this.设置数据(物品数据);}
    public void 设置物品数据(ItemMeta 物品数据){this.设置数据(物品数据);}
    public void 设置数据(ItemMeta 物品数据){this.setItemMeta(物品数据);}
    public void 设置物品名称(String 物品名称){设置名称(物品名称);}
    public void 设置名称(String 物品名称){ItemMeta 临时数据=this.获取数据();临时数据.setDisplayName(物品名称);this.设置数据(临时数据);}
    public ItemMeta 获取数据(){return this.getItemMeta();}
    public void 设置注释(List<String> 注释){ItemMeta 临时数据=this.获取数据();临时数据.setLore(注释);this.设置数据(临时数据);}
    public void 设置注释(String... 注释){this.设置注释(Arrays.asList(注释));}
    public void 设置注释(int 行数,String 修改的注释){ List<String> 注释=this.获取数据().getLore();注释.set(行数,修改的注释);this.设置注释(注释); }
    public void 添加注释(String 增加的注释) { List<String> 注释=new ArrayList<>();if (!this.获取数据().getLore().isEmpty()) 注释 = this.获取数据().getLore();注释.add(增加的注释);this.设置注释(注释); }
    public void 添加注释(String... 注释){ this.添加注释(Arrays.asList(注释)); }
    public void 添加注释(List<String> 注释){ List<String> 全部注释=new ArrayList<>();if (!this.获取数据().getLore().isEmpty())全部注释=this.获取数据().getLore();全部注释.addAll(注释);this.设置注释(全部注释); }
    public 物品 复制(){物品 temp=(物品) this.clone();return temp;}
    public void 设置特殊值(int 特殊值){ItemMeta 物品数据=this.获取数据();((Damageable)物品数据).setDamage(特殊值);this.设置数据(物品数据);}
    public void 设置耐久损耗(int 损耗值){this.设置特殊值(损耗值);}
    public int 获取特殊值(){return ((Damageable)(this.获取数据())).getDamage();}

}
