package io.github.libxposed.main

import android.annotation.SuppressLint
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedInterface.AfterHookCallback
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam
import io.github.libxposed.api.annotations.AfterInvocation
import io.github.libxposed.api.annotations.XposedHooker


class SystemLock {
    companion object {
        var module: XposedModule? = null
        private var isLocked = false

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
                @AfterInvocation
                fun afterInvocation(callback: AfterHookCallback) {
                    module?.log("started afterInvocation")
                    if (!isLocked) {
                        try {
                            val isLocked = callback.result as Boolean
                            if (isLocked) {
                                module?.log("Phone locked, disabling Airplane Mode toggle (implementation needed)")
                            }
                        } catch (e: Throwable) {
                            module?.log("Error disabling Airplane Mode toggle")
                        }
                    }
                }


            }

            }
        }
    }





