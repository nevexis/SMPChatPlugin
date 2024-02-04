package de.kenseiclan.mc.smpchatplugin.config;

import org.bukkit.plugin.Plugin

class ConfigHelperImpl(private val plugin: Plugin) : ConfigHelper {
    var chatGroups: List<ChatGroup> = loadGroups()

    private fun loadGroups(): List<ChatGroup> {
        val groups = mutableListOf<ChatGroup>()
        val groupSection = plugin.config.getConfigurationSection("chat.groups")
        groupSection?.getKeys(false)?.forEach { key ->
            val prefix = groupSection.getString("$key.prefix")
            groups.add(ChatGroup(key, prefix ?: ""))
        }
        return groups
    }

    override fun reloadGroups() {
        chatGroups = loadGroups();
    }
}