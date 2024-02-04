package de.kenseiclan.mc.smpchatplugin.events;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin;
import de.kenseiclan.mc.smpchatplugin.config.ChatGroup;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

public class ChatEventHandler implements Listener {
    private final SMPChatPlugin plugin;
    private List<ChatGroup> groupList;
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

        final User user = plugin.getLuckPerms()
                .getProvider()
                .getUserManager()
                .getUser(chatEvent.getPlayer().getUniqueId());
        assert user != null;

        final String message = getFormattedChatMessage(chatEvent.getPlayer(), chatEvent.getMessage(), user);

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

        message = message.replace("/me ", "");
        message = getFormattedChatMessage(event.getPlayer(), message, user);
        message = String.format("%s %s", chatMessageSymbol, message);

        plugin.getServer()
                .broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private ChatGroup getPlayerChatGroup(final Player player, final User user) {
        return groupList.stream()
                .filter(chatGroup -> {
                    final String permission = String.format("chatgroup.%s", chatGroup.name());
                    return user.getNodes()
                            .contains(Node.builder(permission).build());
                })
                .findFirst()
                .orElse(new ChatGroup("", ""));
    }

    private String getFormattedChatMessage(final Player player, final String message, final User user) {
        String groupPrefix = getPlayerChatGroup(player, user).prefix();
        if(!groupPrefix.equals("")) groupPrefix = groupPrefix + " ";
        final String userPrefix = Optional.ofNullable(user.getCachedData().getMetaData().getPrefix()).orElse("&7");
        final String playerName = player.getName();
        return String.format("%s&r%s%s &8%s &r%s", groupPrefix, userPrefix, playerName, chatMessageSymbol, message);
    }

    public void reloadGroups() {
        groupList = plugin.getConfigHelper()
                .loadGroups();
    }
}
