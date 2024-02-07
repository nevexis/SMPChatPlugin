package de.kenseiclan.mc.smpchatplugin.providers;

import de.kenseiclan.mc.smpchatplugin.SMPChatPlugin;
import de.kenseiclan.mc.smpchatplugin.config.ChatGroup;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

class PlayerChatGroupProvider(private val plugin: SMPChatPlugin) {

    fun getPlayerChatGroup(user: User, group: Group?): ChatGroup {
        return plugin.configHelper.chatGroups
            .firstOrNull { chatGroup ->
                val permission = "chatgroup.${chatGroup.name}"
                user.nodes.contains(Node.builder(permission).build()) ||
                        group?.nodes?.contains(Node.builder(permission).build()) == true
            } ?: ChatGroup("", "")
    }
}