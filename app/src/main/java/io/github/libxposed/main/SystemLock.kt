package io.github.libxposed.main

import android.annotation.SuppressLint
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam
import io.github.libxposed.api.annotations.BeforeInvocation
import io.github.libxposed.api.annotations.AfterInvocation
import io.github.libxposed.api.annotations.XposedHooker
import android.app.KeyguardManager
import android.content.Context
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import io.github.libxposed.api.XposedInterface.AfterHookCallback
import io.github.libxposed.api.XposedInterface

class SystemLock {
    companion object {
        private var module: XposedModule? = null

        @SuppressLint("PrivateApi")
        fun hook(param: PackageLoadedParam, module: MainActivity){
            this.module = module


            module.hook(
                param.classLoader.loadClass("com.android.systemui.qs.tiles.AirplaneModeTiler")
                    .getDeclaredMethod("handleClick"),
               AirplaneModeHooker::class.java

            )

        }

        //keyguardManager.isKeyguardSecure() to check for a secure lock (PIN, pattern, password).
        //keyguardManager.isKeyguardLocked() to check for any lock (secure or non-secure).
        //Return true if either check indicates a locked state, otherwise false.
        private fun isPhoneLocked(context: Context): Boolean {
            val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            return keyguardManager.isKeyguardSecure || keyguardManager.isKeyguardLocked
        }

        @XposedHooker
        private class AirplaneModeHooker() : XposedInterface.Hooker {
            companion object {
                @JvmStatic
                @BeforeInvocation
                /*Before Invocation the Method, Check if the phone is locked and return null preventing the handleClick() method to execute
                  else handleClick method continue as usual
                 */
                fun beforeInvocation(methodHookParam: MethodHookParam): Any {
                    val context= methodHookParam.thisObject as Context
                    if(isPhoneLocked(context)){
                        //Having issue in how to make the AirplaneMode class return null so that it doesn't get executed
                        return AirplaneModeHooker()
                    }
                         // Not sure if that is the way to run this
                        // Try to make the AirplaneModeHooker() run as normal
                        return methodHookParam.setResult(methodHookParam.result)

                }

                @JvmStatic
                @AfterInvocation
                fun afterInvocation(callback: AfterHookCallback, context:AirplaneModeHooker) {
                        module?.log("Airplane Mode is  toggle ${callback.args[0]}")
                    }

            }
        }


            }
        }










