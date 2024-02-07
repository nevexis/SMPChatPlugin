package de.kenseiclan.mc.smpchatplugin.config;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ConfigHelperImpl implements ConfigHelper {
    private final Plugin plugin;

    @Getter
    private List<ChatGroup> chatGroups;

    public ConfigHelperImpl(final Plugin plugin) {
        this.plugin = plugin;
        this.chatGroups = loadGroups();
    }

    private List<ChatGroup> loadGroups() {
        final List<ChatGroup> groups = new ArrayList<>();
        ConfigurationSection groupSection = plugin.getConfig().getConfigurationSection("chat.groups");
        if (groupSection != null) {
            for (String key : groupSection.getKeys(false)) {
                String prefix = groupSection.getString(key + ".prefix");
                groups.add(new ChatGroup(key, prefix));
            }
        }
        return groups;
    }

    @Override
    public void reloadGroups() {
        this.chatGroups = loadGroups();
    }
}
