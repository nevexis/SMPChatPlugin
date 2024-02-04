package de.kenseiclan.mc.smpchatplugin.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ConfigHelperImpl implements ConfigHelper {
    private final Plugin plugin;

    public ConfigHelperImpl(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<ChatGroup> loadGroups() {
        final List<ChatGroup> groups = new ArrayList<>();
        ConfigurationSection groupSection = plugin.getConfig().getConfigurationSection("groups");
        if (groupSection != null) {
            for (String key : groupSection.getKeys(false)) {
                String prefix = groupSection.getString(key + ".prefix");
                groups.add(new ChatGroup(prefix));
            }
        }
        return groups;
    }
}
