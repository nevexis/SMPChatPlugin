package de.kenseiclan.mc.smpchatplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class SMPChatPlugin extends JavaPlugin {

    @Getter
    private ConfigHelper configHelper;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        configHelper = new ConfigHelperImpl(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
