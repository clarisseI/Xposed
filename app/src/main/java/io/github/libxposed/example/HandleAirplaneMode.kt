import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

class AirplaneModeService : Service() {

    private val airplaneModeReceiver = object : BroadcastReceiver() {
        fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                // Handle Airplane Mode change here (e.g., logging)
                XposedBridge.log("Airplane Mode changed in service.")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        registerReceiver(airplaneModeReceiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airplaneModeReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
