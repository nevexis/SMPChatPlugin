package de.kenseiclan.mc.smpchatplugin.events;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinEventHandler(private val plugin: SMPChatPlugin) : Listener {

    @EventHandler
    fun onPlayerJoinEvent(joinEvent: PlayerJoinEvent) {
        val user = plugin.luckPerms.provider.userManager.getUser(joinEvent.player.uniqueId)
        requireNotNull(user)
        val group = plugin.luckPerms.provider.groupManager.getGroup(user.primaryGroup)

        val playerTabListName =
            ChatColor.translateAlternateColorCodes('&', getFormattedPlayerName(user, group, joinEvent.player.name))
        joinEvent.player.setPlayerListName(playerTabListName)
    }

    private fun getFormattedPlayerName(user: User, group: Group?, playerName: String): String {
        var groupPrefix = plugin.chatGroupProvider.getPlayerChatGroup(user, group).prefix
        if (groupPrefix.isNotEmpty()) groupPrefix += " "
        val userPrefix = user.cachedData.metaData.prefix ?: "&7"
        return "$groupPrefix&r$userPrefix$playerName"
    }
}