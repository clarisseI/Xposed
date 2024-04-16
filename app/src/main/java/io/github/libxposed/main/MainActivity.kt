package io.github.libxposed.main


import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam


private lateinit var module: MainActivity


class MainActivity(base: XposedInterface, param: ModuleLoadedParam) : XposedModule(base, param){

    init {
        module = this

    }


//    fun setMyService(myService: MyService) {
//        this.myService = myService
//    }
    override fun onSystemServerLoaded(param: XposedModuleInterface.SystemServerLoadedParam) {

        super.onSystemServerLoaded(param)
        module.log("SERVER LOADED!")

    }

    override fun onPackageLoaded(param: PackageLoadedParam) {

        super.onPackageLoaded(param)

        module.log("Loaded ${param.packageName}")

        when (param.packageName) {
            "com.android.systemui" -> {
                module.log("started ")
                try{
                    module.log("[MainActivity hooking to")
                    SystemLock.hook(param, this)

                }catch (e:Exception){
                    module.log("[MainActivity] ERROR:$e")
                }
            }
        }
    }


}

