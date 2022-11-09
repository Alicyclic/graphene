package com.github

import com.github.lua.LuaAPI
import com.github.lua.events.EventRegistry
import com.github.lua.objects.LuaGlobals
import com.github.lua.objects.Script
import com.github.lua.objects.Script.Companion.disablePlugins
import com.github.lua.objects.Script.Companion.enablePlugins
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import org.luaj.vm2.Globals
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.jse.JsePlatform


class Graphene : JavaPlugin() {
    override fun onEnable() = reload()
    override fun onDisable() = this.disablePlugins()
    private var pluginEnabled = false

    private fun reload() {
        System.setProperty(
            "org.litote.mongo.test.mapping.service",
            "org.litote.kmongo.jackson.JacksonClassMappingTypeService"
        )
        Script.getOrDefault("test")
        LuaC.install(globals)
        globals.compiler = LuaC.instance
        if (pluginEnabled) this.disablePlugins()
        HandlerList.unregisterAll(this)
        EventRegistry.registerEvents(this)
        LuaAPI.register()
        LuaGlobals.register()
        this.enablePlugins()
        pluginEnabled = !pluginEnabled
    }

    companion object {
        val classLoader: ClassLoader = Companion::class.java.classLoader
        val globals: Globals = JsePlatform.standardGlobals()
    }
}