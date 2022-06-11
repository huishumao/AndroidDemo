package per.hsm.widgethost

import android.Manifest
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textview.MaterialTextView
import java.util.ArrayDeque

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    val APPWIDGET_HOST_ID = 1024

    private lateinit var mAppWidgetManager: AppWidgetManager
    private lateinit var mAppWidgetHost: LauncherAppWidgetHost

    lateinit var rootView:FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(2131427441, null, false)
        Log.d(TAG, "onCreate: app layout is ${view}")
        if (view is MaterialTextView) {
            view.text = "远程aaaa"
        }
        setContentView(view)
//        setContentView(R.layout.activity_main)

//        rootView = findViewById<FrameLayout>(R.id.app_widget_root)


        mAppWidgetManager = AppWidgetManager.getInstance(this)
        mAppWidgetHost = LauncherAppWidgetHost(this, APPWIDGET_HOST_ID)
        mAppWidgetHost.startListening()


//        checkPremission()

    }


    fun getAppWidgetHost(): LauncherAppWidgetHost {
        return mAppWidgetHost
    }


    private fun checkPremission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BIND_APPWIDGET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BIND_APPWIDGET), 1001)
        }

    }


    private fun loadAppWidgetsInternal() {
        val widgets: List<AppWidgetProviderInfo> = mAppWidgetManager.getInstalledProviders()
        for (widget in widgets) {
            val widgetName = widget.provider.flattenToString()
            Log.d(TAG, "loadAppWidgetsInternal: widget name = $widgetName")
        }


        val widgetIDArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getAppWidgetHost().appWidgetIds
        } else {
           intArrayOf()
        }
        Log.d(TAG, "loadAppWidgetsInternal: widgetIDArray size = ${widgetIDArray.size}")

        if ((widgets.size > 0)) {
            makeAppWidgetId(widgets[1])
        }else {
            Log.d(TAG, "loadAppWidgetsInternal: getInstalledProviders size 0")
        }

    }


    private fun makeAppWidgetId(appWidgetInfo: AppWidgetProviderInfo) {

        val appWidgetId = getAppWidgetHost().allocateAppWidgetId()
        val success: Boolean = mAppWidgetManager.bindAppWidgetIdIfAllowed(appWidgetId, appWidgetInfo.profile, appWidgetInfo.provider, null)
        Log.d(TAG, "makeAppWidgetId success = $success")
        if (success) {
//            getAppWidgetHost().deleteAppWidgetId(appWidgetId)
            val appWidgetView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo)
            Log.d(TAG, "makeAppWidgetId: appWidgetView == ${appWidgetView?.toString()}")
            appWidgetView?.let {
                if (appWidgetView.layoutParams == null) {
                    appWidgetView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT)
                }
                rootView.addView(appWidgetView)
            }

        }else {
//            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND).apply {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, appWidgetInfo.provider)
                // This is the options bundle discussed above
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_OPTIONS, appWidgetInfo.profile)
            }

            startActivityForResult(intent, 2000)

        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            loadAppWidgetsInternal()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         if(requestCode == 2000) {
             val appWidgetId = data?.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1) ?: -1

             addAppWidgetImpl(appWidgetId, mAppWidgetManager.getAppWidgetInfo(appWidgetId))
        }
    }

    fun addAppWidgetImpl(appWidgetId: Int, appWidgetInfo: AppWidgetProviderInfo) {
        val appWidgetView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo)
        Log.d(TAG, "makeAppWidgetId: appWidgetView == ${appWidgetView?.toString()}")
        appWidgetView?.let {
            if (appWidgetView.layoutParams == null) {
                appWidgetView.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
            rootView.addView(appWidgetView)
        }

    }
}