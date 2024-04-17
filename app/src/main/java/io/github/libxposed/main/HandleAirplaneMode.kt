package io.github.libxposed.main

import android.annotation.SuppressLint

import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedInterface.BeforeHookCallback
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam
import io.github.libxposed.api.annotations.BeforeInvocation
import io.github.libxposed.api.annotations.XposedHooker
import io.github.libxposed.api.annotations.AfterInvocation
import io.github.libxposed.api.XposedInterface.AfterHookCallback


class HandleAirplaneMode{
    companion object {
        var module: XposedModule? = null

        @SuppressLint("PrivateApi")
        fun hook(param: PackageLoadedParam, module: MainActivity) {
            this.module = module
                //Method to check if the phone is locked or not
            module.hook(
                param.classLoader.loadClass("android.app.KeyguardManager")
                    .getDeclaredMethod("isKeyguardLocked"),
                LockScreenAirplaneHooker::class.java
            )
            //Method that handles the CLick on the AirPlane mode
            module.hook(
                param.classLoader.loadClass("com.android.systemui.qs.tiles.AirplaneModeTiler")
                    .getDeclaredMethod("handleClick"),
                AirplaneModeHooker::class.java

            )
        }

        @XposedHooker
        private class AirplaneModeHooker : XposedInterface.Hooker {
            companion object {
                @JvmStatic
                @BeforeInvocation
                // Will set the callBack to false so that it doesn't get executed
                fun beforeInvocation(callback: BeforeHookCallback): Boolean {
                    callback.args[0] = false // Set isEnabled argument to false
                    return false
                    }

                }
            }

         @XposedHooker
         private class LockScreenAirplaneHooker : XposedInterface.Hooker {
             companion object {
                 @JvmStatic
                 @AfterInvocation
                 //I used afterInvocation to let this method works as usual and when isLocked is true
                 //call the AirplaneModeHooker() else continue as usual
                 fun afterInvocation(callback: AfterHookCallback) {
                         try {
                             val isLocked = callback.result as Boolean
                             if (isLocked) {
                                 AirplaneModeHooker()
                                 module?.log("Phone locked, AirplaneModeHooker won't execute)")


                             }
                     } catch (e: Throwable) {
                             // Handle exception (e.g., log the error and return default value)
                             module?.log("SystemLock $e")


                         }

                     }

                 }
             }

    }
}
