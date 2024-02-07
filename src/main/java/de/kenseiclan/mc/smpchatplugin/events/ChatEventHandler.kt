package de.kenseiclan.mc.smpchatplugin.events;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
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

        var message = getFormattedChatMessage(chatEvent.player, chatEvent.message, user, group)

        chatEvent.recipients.forEach { _ ->
            val audience = SMPChatPlugin.adventure.players()
            val adventureFormattedMessage = SMPChatPlugin.miniMessage.deserialize(message)
            audience.sendMessage(adventureFormattedMessage)
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

        val audience = SMPChatPlugin.adventure.players()
        val adventureFormattedMessage = SMPChatPlugin.miniMessage.deserialize(formattedMessage)
        audience.sendMessage(adventureFormattedMessage)
    }

    private fun getFormattedChatMessage(player: Player, message: String, user: User, group: Group?): String {
        var groupPrefix = plugin.chatGroupProvider.getPlayerChatGroup(user, group).prefix
        if (groupPrefix.isNotEmpty()) groupPrefix += " "
        val userPrefix = user.cachedData.metaData.prefix ?: "<gray>"
        val playerName = player.name

        return "$groupPrefix$userPrefix$playerName<reset> <dark_gray>$chatMessageSymbol</dark_gray> <gray>$message"
    }
}