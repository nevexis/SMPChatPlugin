package de.kenseiclan.mc.smpchatplugin.events;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Optional;

public class ChatEventHandler implements Listener {
    private final SMPChatPlugin plugin;
    private final String chatMessageSymbol;

    public ChatEventHandler(final SMPChatPlugin plugin) {
        this.plugin = plugin;
        chatMessageSymbol = Optional.ofNullable(plugin.getConfig().getString("chat.chat-symbol"))
                .orElse("»");
    }

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent chatEvent) {
        chatEvent.setCancelled(true);

        final User user = plugin.getLuckPerms()
                .getProvider()
                .getUserManager()
                .getUser(chatEvent.getPlayer().getUniqueId());
        assert user != null;
        final Group group = plugin.getLuckPerms()
                .getProvider()
                .getGroupManager()
                .getGroup(user.getPrimaryGroup());

        final String message = getFormattedChatMessage(chatEvent.getPlayer(), chatEvent.getMessage(), user, group);

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

        final User user = plugin.getLuckPerms()
                .getProvider()
                .getUserManager()
                .getUser(event.getPlayer().getUniqueId());
        assert user != null;
        final Group group = plugin.getLuckPerms()
                .getProvider()
                .getGroupManager()
                .getGroup(user.getPrimaryGroup());

        message = message.replace("/me ", "");
        message = getFormattedChatMessage(event.getPlayer(), message, user, group);
        message = String.format("%s %s", chatMessageSymbol, message);

        plugin.getServer()
                .broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private String getFormattedChatMessage(final Player player, final String message, final User user, final Group group) {
        String groupPrefix = plugin.getChatGroupProvider().getPlayerChatGroup(user, group).prefix();
        if (!groupPrefix.equals("")) groupPrefix = groupPrefix + " ";
        final String userPrefix = Optional.ofNullable(user.getCachedData().getMetaData().getPrefix()).orElse("&7");
        final String playerName = player.getName();
        return String.format("%s&r%s%s &8%s &r%s", groupPrefix, userPrefix, playerName, chatMessageSymbol, message);
    }
}
