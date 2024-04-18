package io.github.libxposed.main


import android.annotation.SuppressLint
import android.app.AndroidAppHelper
import android.app.KeyguardManager
import android.content.Context
import android.view.View
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam
import io.github.libxposed.api.XposedModuleInterface.SystemServerLoadedParam
import io.github.libxposed.api.annotations.BeforeInvocation
import io.github.libxposed.api.annotations.XposedHooker


private lateinit var module: MainActivity


class MainActivity(base: XposedInterface, param: ModuleLoadedParam) : XposedModule(base, param){
    init {
        module = this

    }

    override fun onSystemServerLoaded(param: SystemServerLoadedParam) {

        super.onSystemServerLoaded(param)
        module.log("SERVER LOADED!")

    }

    @SuppressLint("PrivateApi")
    override fun onPackageLoaded(param: PackageLoadedParam) {

        super.onPackageLoaded(param)

        if(param.packageName != "com.android.systemui"){
            return
        }
                try {
                    // hook the handleClick method of AirplaneModeTiler class
                    hook(
                        param.classLoader.loadClass("com.android.systemui.qs.tiles.AirplaneModeTile")
                            .getDeclaredMethod("handleClick", View::class.java),
                        AirplaneModeHooker::class.java
                    )
                    module.log("[MainActivity] Hooked $param.packageName")
                } catch (e: Exception) {
                    module.log("[MainActivity] ERROR:$e")
                }
            }
        }

     @XposedHooker
     // **AirplaneModeHooker:** Class to handle hooking the AirplaneModeTiler method
       class AirplaneModeHooker: XposedInterface.Hooker {
           companion object{
               @JvmStatic
               @BeforeInvocation
               @Suppress("unused")
               fun beforeInvocation(callback: XposedInterface.BeforeHookCallback) {
                   val context= AndroidAppHelper.currentApplication().applicationContext
                   //keyguardManager.isKeyguardSecure() to check for a secure lock (PIN, pattern, password).
                   //keyguardManager.isKeyguardLocked() to check for any lock (secure or non-secure).
                   val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                   val isPhoneLocked= keyguardManager.isKeyguardLocked || keyguardManager.isKeyguardSecure
                   if(isPhoneLocked){
                       // prevent AirplaneMode execution by returning null
                       callback.returnAndSkip(null)
                       module.log("phone locked can't Turn Airplane Mode on $AirplaneModeHooker ")
                   }
                     module.log("Phone unlocked $isPhoneLocked")


               }



           }

        }





