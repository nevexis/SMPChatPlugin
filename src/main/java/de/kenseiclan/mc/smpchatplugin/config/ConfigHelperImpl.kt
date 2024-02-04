package de.kenseiclan.mc.smpchatplugin.config;

import org.bukkit.plugin.Plugin

class ConfigHelperImpl(private val plugin: Plugin) : ConfigHelper {
    var chatGroups: List<ChatGroup> = loadGroups()

    private fun loadGroups(): List<ChatGroup> {
        val groups = mutableListOf<ChatGroup>()
        val groupSection = plugin.config.getConfigurationSection("chat.groups")
        groupSection?.getKeys(false)?.forEach {
            val prefix = groupSection.getString("$it.prefix")
            groups.add(ChatGroup(it, prefix ?: ""))
        }
        return groups
    }

    override fun reloadGroups() {
        chatGroups = loadGroups();
    }
}