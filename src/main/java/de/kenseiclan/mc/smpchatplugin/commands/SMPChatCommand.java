package de.kenseiclan.mc.smpchatplugin.commands;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SMPChatCommand implements CommandExecutor {
    private final SMPChatPlugin plugin;

    public SMPChatCommand(final SMPChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) return false;

        final String subCommand = args[0];

        if(subCommand.equals("reload")) {
            reloadCommand(sender);
        }else {
            helpCommand(sender);
        }
        return true;
    }

    private void reloadCommand(final CommandSender sender) {
        if(!sender.hasPermission("smpchat.reload")) {
            sender.sendMessage("[SMPChat] No permissions!");
            return;
        }

        plugin.reloadChatGroups();
        sender.sendMessage("[SMPChat] Reload complete!");
    }

    private void helpCommand(final CommandSender sender){
        sender.sendMessage("---- SMPChat Help ----");
        sender.sendMessage("- /smpchat reload");
    }
}
