package de.kenseiclan.mc.smpchatplugin.events;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
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
            SMPChatPlugin.miniMessage.deserialize(getFormattedPlayerName(user, group, joinEvent.player.name))

        val legacyText = LegacyComponentSerializer.legacySection().serialize(playerTabListName)
        joinEvent.player.setPlayerListName(legacyText)
    }

    private fun getFormattedPlayerName(user: User, group: Group?, playerName: String): String {
        var groupPrefix = plugin.chatGroupProvider.getPlayerChatGroup(user, group).prefix
        if (groupPrefix.isNotEmpty()) groupPrefix += " "
        val userPrefix = user.cachedData.metaData.prefix ?: "<gray>"
        return "$groupPrefix<reset>$userPrefix$playerName<reset>"
    }
}