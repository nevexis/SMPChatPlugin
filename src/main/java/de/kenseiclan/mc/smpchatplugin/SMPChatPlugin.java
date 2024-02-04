package de.kenseiclan.mc.smpchatplugin;

import de.kenseiclan.mc.smpchatplugin.commands.SMPChatCommand;
import de.kenseiclan.mc.smpchatplugin.config.ConfigHelper;
import de.kenseiclan.mc.smpchatplugin.config.ConfigHelperImpl;
import de.kenseiclan.mc.smpchatplugin.events.ChatEventHandler;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SMPChatPlugin extends JavaPlugin {

    @Getter
    private ConfigHelper configHelper;

    @Getter
    private RegisteredServiceProvider<LuckPerms> luckPerms;

    private ChatEventHandler chatEventHandler;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        configHelper = new ConfigHelperImpl(this);
        this.luckPerms = this.getServer().getServicesManager().getRegistration(LuckPerms.class);

        chatEventHandler = new ChatEventHandler(this);
        getServer().getPluginManager().registerEvents(chatEventHandler, this);

        Objects.requireNonNull(getCommand("smpchat")).setExecutor(new SMPChatCommand(this));
    }

    public void reloadChatGroups(){
        this.reloadConfig();
        configHelper.loadGroups();
        chatEventHandler.reloadGroups();
    }
}
