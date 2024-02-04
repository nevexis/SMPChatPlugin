package de.kenseiclan.mc.smpchatplugin;

import de.kenseiclan.mc.smpchatplugin.config.ConfigHelper;
import de.kenseiclan.mc.smpchatplugin.config.ConfigHelperImpl;
import de.kenseiclan.mc.smpchatplugin.events.ChatEventHandler;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SMPChatPlugin extends JavaPlugin {

    @Getter
    private ConfigHelper configHelper;

    @Getter
    private RegisteredServiceProvider<LuckPerms> luckPerms;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        configHelper = new ConfigHelperImpl(this);
        this.luckPerms = this.getServer().getServicesManager().getRegistration(LuckPerms.class);

        getServer().getPluginManager().registerEvents(new ChatEventHandler(this), this);
    }
}
