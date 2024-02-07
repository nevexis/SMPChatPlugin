package de.kenseiclan.mc.smpchatplugin;

import de.kenseiclan.mc.smpchatplugin.commands.SMPChatCommand;
import de.kenseiclan.mc.smpchatplugin.config.ConfigHelperImpl;
import de.kenseiclan.mc.smpchatplugin.events.ChatEventHandler;
import de.kenseiclan.mc.smpchatplugin.events.JoinEventHandler;
import de.kenseiclan.mc.smpchatplugin.providers.PlayerChatGroupProvider;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SMPChatPlugin extends JavaPlugin {

    @Getter
    private ConfigHelperImpl configHelper;

    @Getter
    private PlayerChatGroupProvider chatGroupProvider;

    @Getter
    private RegisteredServiceProvider<LuckPerms> luckPerms;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        configHelper = new ConfigHelperImpl(this);
        this.luckPerms = this.getServer().getServicesManager().getRegistration(LuckPerms.class);
        this.chatGroupProvider = new PlayerChatGroupProvider(this);

        getServer().getPluginManager().registerEvents(new ChatEventHandler(this), this);
        getServer().getPluginManager().registerEvents(new JoinEventHandler(this), this);

        Objects.requireNonNull(getCommand("smpchat")).setExecutor(new SMPChatCommand(this));
    }

    public void reloadChatGroups() {
        this.reloadConfig();
        configHelper.reloadGroups();
    }
}
