package io.github.libxposed.example

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings


import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedInterface.BeforeHookCallback

import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam
import io.github.libxposed.api.annotations.BeforeInvocation



import io.github.libxposed.api.annotations.XposedHooker

class SystemUIHooker {
    companion object {
        var module: XposedModule? = null
        // Hypothetical setting name (replace with actual name)
         private var isLocked= true
        @SuppressLint("PrivateApi")
        fun hook(param: PackageLoadedParam, module: XposedModule) {
            this.module = module
            
            module.hook(
                param.classLoader.loadClass("android.app.KeyguardManager")
                    .getDeclaredMethod("isKeyguardLocked"),
                        LockScreenAirplaneHooker::class.java
            )
        }

        @XposedHooker
        private class LockScreenAirplaneHooker : XposedInterface.Hooker {
            companion object {
                @JvmStatic
                 @BeforeInvocation
                fun beforeInvocation(callback: BeforeHookCallback): LockScreenAirplaneHooker {
                    if (isLocked) {

                        val airplaneModeOn = Settings.Global.getInt(module?.context?.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0)
                        if(airplaneModeOn)

                            module?.log("Phone unlocked, disabling Airplane Mode toggle (implementation needed)")
                        //} catch (e: Throwable) {
                           // module?.log("Error disabling Airplane Mode toggle")
                       // }
                    }
                    return LockScreenAirplaneHooker()

                }
            }
        }
    }
}


