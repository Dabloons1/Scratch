package com.soulstrikefloatingmenu

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HookEntry : IXposedHookLoadPackage {
    private var floatingWindow: FloatingWindow? = null

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.com2usholdings.soulstrike.android.google.global.normal") {
            return
        }

        XposedBridge.log("SoulStrikeFloatingMenu: Hook loaded for ${lpparam.packageName}")

        // Hook Activity creation to show floating window
        XposedHelpers.findAndHookMethod(
            Activity::class.java,
            "onCreate",
            Bundle::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val activity = param.thisObject as Activity
                    if (activity.javaClass.name.contains("MainActivity") || 
                        activity.javaClass.name.contains("GameActivity")) {
                        
                        XposedBridge.log("SoulStrikeFloatingMenu: Activity created, showing floating window")
                        showFloatingWindow(activity)
                    }
                }
            }
        )

        // Hook Application onCreate to ensure we can show the window
        XposedHelpers.findAndHookMethod(
            "android.app.Application",
            lpparam.classLoader,
            "onCreate",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    XposedBridge.log("SoulStrikeFloatingMenu: Application onCreate hooked")
                }
            }
        )
    }

    private fun showFloatingWindow(context: Context) {
        try {
            if (floatingWindow == null) {
                floatingWindow = FloatingWindow(context)
                floatingWindow?.show()
                XposedBridge.log("SoulStrikeFloatingMenu: Floating window created and shown")
            }
        } catch (e: Exception) {
            XposedBridge.log("SoulStrikeFloatingMenu: Error showing floating window: ${e.message}")
        }
    }
}
