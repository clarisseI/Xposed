package io.Clarisse.libxposed.Main

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedInterface.BeforeHookCallback
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam
import io.github.libxposed.api.annotations.BeforeInvocation
import io.github.libxposed.api.annotations.XposedHooker


class HandleAirplaneMode{
    companion object {
        var module: XposedModule? = null

        @SuppressLint("PrivateApi")
        fun hook(param: PackageLoadedParam, module: XposedModule) {
            this.module = module

            module.hook(
                param.classLoader.loadClass("com.android.settings.AirplaneModeEnabler")
                    .getDeclaredMethod("setAirplaneModeOn", Boolean::class.java),
                AirplaneModeHooker::class.java

            )
        }

        @XposedHooker
        private class AirplaneModeHooker : XposedInterface.Hooker {
            companion object {
                @JvmStatic
                @BeforeInvocation
                fun beforeInvocation(callback: BeforeHookCallback): Any {
                    callback.args[0] = false // Set isEnabled argument to false
                    return true
                    }

                }
            }
        }
    }
