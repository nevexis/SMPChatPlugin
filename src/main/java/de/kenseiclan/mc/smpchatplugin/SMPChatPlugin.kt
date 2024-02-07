package de.kenseiclan.mc.smpchatplugin;

import de.kenseiclan.mc.smpchatplugin.commands.SMPChatCommand
import de.kenseiclan.mc.smpchatplugin.config.ConfigHelperImpl
import de.kenseiclan.mc.smpchatplugin.events.ChatEventHandler
import de.kenseiclan.mc.smpchatplugin.events.JoinEventHandler
import de.kenseiclan.mc.smpchatplugin.providers.PlayerChatGroupProvider
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPerms
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin

class SMPChatPlugin : JavaPlugin() {
    companion object {
        lateinit var adventure: BukkitAudiences
        lateinit var miniMessage: MiniMessage
    }

    val configHelper: ConfigHelperImpl by lazy { ConfigHelperImpl(this) }
    val chatGroupProvider: PlayerChatGroupProvider by lazy { PlayerChatGroupProvider(this) }
    val luckPerms: RegisteredServiceProvider<LuckPerms> by lazy {
        server.servicesManager.getRegistration(LuckPerms::class.java)
            ?: throw IllegalStateException("LuckPerms not found")
    }

    override fun onEnable() {
        saveDefaultConfig()
        adventure = BukkitAudiences.create(this)
        miniMessage = MiniMessage.miniMessage()

        server.pluginManager.registerEvents(ChatEventHandler(this), this)
        server.pluginManager.registerEvents(JoinEventHandler(this), this)
        server.getPluginCommand("smpchat")?.setExecutor(SMPChatCommand(this))
    }

    fun reloadChatGroups() {
        reloadConfig()
        configHelper.reloadGroups()
    }
}
