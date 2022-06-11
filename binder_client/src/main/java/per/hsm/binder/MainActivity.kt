package per.hsm.binder

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import per.hsm.binder.bean.Book
import per.hsm.binder.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {


    private lateinit var viewBinding: ActivityMainBinding

    private var mRemoteService: IBookStore? = null


    private val list:MutableList<Book> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initListener()

    }

    private fun initListener() {
        viewBinding.selectListBtn.setOnClickListener {

            mRemoteService?.let {
                Log.d(TAG, "initListener: ${it.bookList}")
                runOnUiThread {
                    viewBinding.bookInfo.text = "查询到有这么几本书${it.bookList}"
                }

            }

        }
        viewBinding.buyBookBtn.setOnClickListener {

                mRemoteService?.let {
                    val book = it.buykBook("西游记")
                    Log.d(TAG, "initListener: $book")
                    if (book != null) {
                        list.add(book)
                    }
                    Log.d(TAG, "initListener:book size == ${list.size}")
                }


        }

        viewBinding.backBookBtn.setOnClickListener {
            mRemoteService?.let {
                if (list.isNotEmpty()) {
                    it.backBook(list[0])
                }

            }
        }


        viewBinding.connectBtn.setOnClickListener {
            TimeUnit.SECONDS.sleep(6)
            bindIPCService()
        }

    }


    private val mDeathRecipient = object: IBinder.DeathRecipient{

        override fun binderDied() {
            Log.d(TAG, "binderDied: 远端服务挂掉了")
             mRemoteService?.let {
                 it.asBinder().unlinkToDeath(this, 0)
//                 it.unRegisterListener(mINewBookArrivedListener)
                 mRemoteService = null
             }
        }

    }

    private val serviceConnection = object: ServiceConnection{

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected: ")
            mRemoteService = IBookStore.Stub.asInterface(service)
            service?.linkToDeath(mDeathRecipient, 0)
            mRemoteService?.registerListener(mINewBookArrivedListener)

            Toast.makeText(this@MainActivity, "连接成功", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected: ")
        }
    }

    private val mINewBookArrivedListener = object: INewBookArrivedListener.Stub() {

        override fun onNewBookArrived(newBook: Book?) {
            Log.d(TAG, "onNewBookArrived: $newBook")

        }


    }

    private fun bindIPCService() {
        if (mRemoteService == null) {
            val intent = Intent("action.bookstore.service")
//            intent.setClassName(this, "per.hsm.binder.BookService")
            intent.setPackage("per.hsm.binder_server")
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }else{
            Log.d(TAG, "bindIPCService: binded")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (mRemoteService!=null && mRemoteService!!.asBinder().isBinderAlive){
            mRemoteService?.unRegisterListener(mINewBookArrivedListener)
        }
        unbindService(serviceConnection)

    }

}