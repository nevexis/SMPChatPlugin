package de.kenseiclan.mc.smpchatplugin.commands

class ChatStrings {
    companion object {
        private const val PLUGIN_CHAT_PREFIX = "<gradient:#41c9b0:#287c6d><bold>SMP-CHAT</bold></gradient>"
        const val CHAT_MESSAGE = "$PLUGIN_CHAT_PREFIX <dark_gray>|<dark_gray> <gray><message>"
        const val NO_PERMISSION = "$PLUGIN_CHAT_PREFIX <dark_gray>|<dark_gray> <gray>You are unauthorized to do this."
    }
}