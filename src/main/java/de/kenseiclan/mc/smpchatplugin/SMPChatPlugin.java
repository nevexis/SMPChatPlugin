package de.kenseiclan.mc.smpchatplugin;

import de.kenseiclan.mc.smpchatplugin.config.ConfigHelper;
import de.kenseiclan.mc.smpchatplugin.config.ConfigHelperImpl;
import de.kenseiclan.mc.smpchatplugin.events.ChatEventHandler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class SMPChatPlugin extends JavaPlugin {

    @Getter
    private ConfigHelper configHelper;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        configHelper = new ConfigHelperImpl(this);

        getServer().getPluginManager().registerEvents(new ChatEventHandler(this), this);
    }
}
