package de.kenseiclan.mc.smpchatplugin.providers;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin;
import de.kenseiclan.mc.smpchatplugin.config.ChatGroup;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class PlayerChatGroupProvider {
    private final SMPChatPlugin plugin;

    public PlayerChatGroupProvider(SMPChatPlugin plugin) {
        this.plugin = plugin;
    }

    public ChatGroup getPlayerChatGroup(final User user, final Group group) {
        return plugin.getConfigHelper().getChatGroups().stream()
                .filter(chatGroup -> {
                    final String permission = String.format("chatgroup.%s", chatGroup.name());
                    return user.getNodes()
                            .contains(Node.builder(permission).build())
                            || group.getNodes()
                            .contains(Node.builder(permission).build());
                })
                .findFirst()
                .orElse(new ChatGroup("", ""));
    }
}
