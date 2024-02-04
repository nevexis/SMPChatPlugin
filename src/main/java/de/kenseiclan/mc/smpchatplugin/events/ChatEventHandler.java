package de.kenseiclan.mc.smpchatplugin.events;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin;
import de.kenseiclan.mc.smpchatplugin.config.ChatGroup;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

public class ChatEventHandler implements Listener {
    private final List<ChatGroup> groupList;
    private final String chatMessageSymbol;

    public ChatEventHandler(final SMPChatPlugin plugin) {
        groupList = plugin.getConfigHelper()
                .loadGroups();
        chatMessageSymbol = Optional.ofNullable(plugin.getConfig().getString("chat.chat-symbol"))
                .orElse("»");
    }

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent chatEvent) {
        chatEvent.setCancelled(true);
        final String groupPrefix = groupList.stream()
                .filter(chatGroup ->
                        chatEvent.getPlayer()
                                .hasPermission(String.format("chatgroup.%s", chatGroup.prefix()))
                )
                .findFirst()
                .orElse(new ChatGroup("", ""))
                .prefix();
        final String playerName = chatEvent.getPlayer().getName();
        final String message = String.format("%s &r&7%s &8%s &r%s", groupPrefix, playerName, chatMessageSymbol, chatEvent.getMessage());

        chatEvent.getPlayer()
                .hasPermission("");
        chatEvent.getRecipients().forEach(player -> {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        });
    }
}
