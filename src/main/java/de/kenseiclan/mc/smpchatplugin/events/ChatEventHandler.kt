package de.kenseiclan.mc.smpchatplugin.events;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class ChatEventHandler(private val plugin: SMPChatPlugin) : Listener {
    private val chatMessageSymbol: String = plugin.config.getString("chat.chat-symbol") ?: "»"

    @EventHandler
    fun onChat(chatEvent: AsyncPlayerChatEvent) {
        chatEvent.isCancelled = true

        val user = plugin.luckPerms.provider.userManager.getUser(chatEvent.player.uniqueId)
        requireNotNull(user)
        val group = plugin.luckPerms.provider.groupManager.getGroup(user.primaryGroup)

        val message = getFormattedChatMessage(chatEvent.player, chatEvent.message, user, group)

        chatEvent.recipients.forEach { player ->
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
        }
    }

    @EventHandler
    fun onPlayerCommandPreprocess(event: PlayerCommandPreprocessEvent) {
        val message = event.message
        if (!message.lowercase().startsWith("/me ")) return
        event.isCancelled = true

        val user = plugin.luckPerms.provider.userManager.getUser(event.player.uniqueId)
        requireNotNull(user)
        val group = plugin.luckPerms.provider.groupManager.getGroup(user.primaryGroup)

        var formattedMessage = message.replace("/me ", "")
        formattedMessage = getFormattedChatMessage(event.player, formattedMessage, user, group)
        formattedMessage = "$chatMessageSymbol $formattedMessage"

        plugin.server.broadcastMessage(ChatColor.translateAlternateColorCodes('&', formattedMessage))
    }

    private fun getFormattedChatMessage(player: Player, message: String, user: User, group: Group?): String {
        var groupPrefix = plugin.chatGroupProvider.getPlayerChatGroup(user, group).prefix
        if (groupPrefix.isNotEmpty()) groupPrefix += " "
        val userPrefix = user.cachedData.metaData.prefix ?: "&7"
        val playerName = player.name
        return "$groupPrefix&r$userPrefix$playerName &8$chatMessageSymbol &r$message"
    }
}