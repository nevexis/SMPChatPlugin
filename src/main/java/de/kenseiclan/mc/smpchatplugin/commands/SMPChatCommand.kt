package de.kenseiclan.mc.smpchatplugin.commands;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class SMPChatCommand(private val plugin: SMPChatPlugin) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) return false

        when (args[0]) {
            "reload" -> reloadCommand(sender)
            else -> helpCommand(sender)
        }
        return true
    }

    private fun reloadCommand(sender: CommandSender) {
        val audience = SMPChatPlugin.adventure.sender(sender)
        if (!sender.hasPermission("smpchat.reload")) {
            val parsed = SMPChatPlugin.miniMessage.deserialize(ChatStrings.NO_PERMISSION)
            audience.sendMessage(parsed)
            return
        }

        plugin.reloadChatGroups()
        val parsed = SMPChatPlugin.miniMessage.deserialize(
            ChatStrings.CHAT_MESSAGE,
            Placeholder.component("message", Component.text("Reload complete!"))
        )
        audience.sendMessage(parsed)
    }

    private fun helpCommand(sender: CommandSender) {
        val audience = SMPChatPlugin.adventure.sender(sender)
        var parsed = SMPChatPlugin.miniMessage.deserialize(
            ChatStrings.MENU_HEADER,
            Placeholder.component("title", Component.text("Help (Page 1/1)"))
        )
        audience.sendMessage(parsed)
        parsed = SMPChatPlugin.miniMessage.deserialize(
            ChatStrings.CHAT_MESSAGE,
            Placeholder.component("message", Component.text("/smpchat reload - Reloads the configuration."))
        )
        audience.sendMessage(parsed)
        parsed = SMPChatPlugin.miniMessage.deserialize(
            ChatStrings.CHAT_MESSAGE,
            Placeholder.component("message", Component.text("/smpchat help - Shows this message."))
        )
        audience.sendMessage(parsed)
    }
}