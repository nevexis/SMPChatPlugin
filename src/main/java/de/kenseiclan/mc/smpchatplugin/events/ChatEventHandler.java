package de.kenseiclan.mc.smpchatplugin.events;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin;
import de.kenseiclan.mc.smpchatplugin.config.ChatGroup;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

public class ChatEventHandler implements Listener {
    private final Plugin plugin;
    private final List<ChatGroup> groupList;
    private final String chatMessageSymbol;

    public ChatEventHandler(final SMPChatPlugin plugin) {
        this.plugin = plugin;
        groupList = plugin.getConfigHelper()
                .loadGroups();
        chatMessageSymbol = Optional.ofNullable(plugin.getConfig().getString("chat.chat-symbol"))
                .orElse("»");
    }

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent chatEvent) {
        chatEvent.setCancelled(true);
        final String groupPrefix = getPlayerChatGroup(chatEvent.getPlayer()).prefix();
        final String playerName = chatEvent.getPlayer().getName();
        final String message = String.format("%s &r&7%s &8%s &r%s", groupPrefix, playerName, chatMessageSymbol, chatEvent.getMessage());

        chatEvent.getPlayer()
                .hasPermission("");
        chatEvent.getRecipients().forEach(player -> {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        });
    }

    @EventHandler
    public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        if (!message.toLowerCase().startsWith("/me ")) return;
        event.setCancelled(true);

        message = message.replace("/me ", "");
        final String groupPrefix = getPlayerChatGroup(event.getPlayer()).prefix();
        final String playerName = event.getPlayer().getName();
        message = String.format("%s %s &r&7%s &8%s &r%s", chatMessageSymbol, groupPrefix, playerName, chatMessageSymbol, message);

        plugin.getServer()
                .broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private ChatGroup getPlayerChatGroup(final Player player) {
        return groupList.stream()
                .filter(chatGroup ->
                        player.hasPermission(String.format("chatgroup.%s", chatGroup.name()))
                )
                .findFirst()
                .orElse(new ChatGroup("", ""));
    }
}
