package per.hsm.androiddemo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TimeUtils
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_app)

        //Debug.startMethodTracing()

        Log.d(TAG, "onCreate: layout id is" + R.layout.activity_main_app)


//        application.startActivity(Intent())

//        okhttpMethod()
//        glideMethod()

    }

    private fun glideMethod() {
//        Glide.with(application).load("").into()

    }

    private fun okhttpMethod() {
         val client = OkHttpClient.Builder()
             .build()
        val request = Request.Builder().addHeader("head", "css/txt")
            .url("baseurl")
            .build()
        val call = client.newCall(request)

        call.enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                TODO("Not yet implemented")
            }
        })

        val response = call.execute()


    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }
}