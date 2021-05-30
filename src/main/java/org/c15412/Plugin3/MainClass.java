package org.c15412.Plugin3;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import static org.c15412.Plugin3.无尽卷轴.合成无尽卷轴;

public class MainClass extends JavaPlugin {
    @Override
    public void onLoad() {
        super.onLoad();
        控制台信息("§e「无尽卷轴」§b插件已经开始加载。");
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new 无尽卷轴(),this);
        getServer().getPluginManager().registerEvents(new 防改名(), this);
        添加合成表(合成无尽卷轴());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        移除合成表(合成无尽卷轴());
    }

    private void 控制台信息(String 信息) {
        获取.控制台.sendMessage("[Get-M] " + 信息);
    }

    private void 添加合成表(Recipe 合成表) {
        移除合成表(合成表);
        getServer().addRecipe(合成表);
    }

    private void 移除合成表(Recipe 合成表) {
        移除合成表(获取.key(合成表));
    }

    private void 移除合成表(NamespacedKey key) {
        getServer().removeRecipe(key);
    }

}

