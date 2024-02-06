package de.kenseiclan.mc.smpchatplugin.config

class ChatStrings {
    companion object {
        private const val PLUGIN_CHAT_PREFIX = "<gradient:#41c9b0:#287c6d><bold>SMP-CHAT</bold></gradient>"
        const val CHAT_MESSAGE = "$PLUGIN_CHAT_PREFIX <dark_gray>|<dark_gray> <gray><message>"
        const val NO_PERMISSION = "$PLUGIN_CHAT_PREFIX <dark_gray>|<dark_gray> <red>You are unauthorized to do this."
        const val MENU_HEADER =
            "$PLUGIN_CHAT_PREFIX <dark_gray>|<dark_gray> <gradient:#41c9b0:#287c6d><bold><strikethrough>-------------</strikethrough> <title> <strikethrough>-------------</strikethrough>"
    }
}