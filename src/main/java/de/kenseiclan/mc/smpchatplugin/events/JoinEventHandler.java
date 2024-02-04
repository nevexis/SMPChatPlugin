package de.kenseiclan.mc.smpchatplugin.events;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class JoinEventHandler implements Listener {
    private final SMPChatPlugin plugin;

    public JoinEventHandler(SMPChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoinEvent(final PlayerJoinEvent joinEvent) {
        final User user = plugin.getLuckPerms()
                .getProvider()
                .getUserManager()
                .getUser(joinEvent.getPlayer().getUniqueId());
        assert user != null;
        final Group group = plugin.getLuckPerms()
                .getProvider()
                .getGroupManager()
                .getGroup(user.getPrimaryGroup());

        final String playerTabListName = ChatColor.translateAlternateColorCodes('&', getFormattedPlayerName(user, group, joinEvent.getPlayer().getName()));
        joinEvent.getPlayer().setPlayerListName(playerTabListName);
    }

    private String getFormattedPlayerName(final User user, final Group group, final String playerName) {
        String groupPrefix = plugin.getChatGroupProvider().getPlayerChatGroup(user, group).prefix();
        if (!groupPrefix.equals("")) groupPrefix = groupPrefix + " ";
        final String userPrefix = Optional.ofNullable(user.getCachedData().getMetaData().getPrefix()).orElse("&7");
        return String.format("%s&r%s%s", groupPrefix, userPrefix, playerName);
    }
}
