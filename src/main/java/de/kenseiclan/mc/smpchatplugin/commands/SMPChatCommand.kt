package de.kenseiclan.mc.smpchatplugin.commands;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
        if (!sender.hasPermission("smpchat.reload")) {
            sender.sendMessage("[SMPChat] No permissions!")
            return
        }

        plugin.reloadChatGroups()
        sender.sendMessage("[SMPChat] Reload complete!")
    }

    private fun helpCommand(sender: CommandSender) {
        sender.sendMessage("---- SMPChat Help ----")
        sender.sendMessage("- /smpchat reload")
    }
}