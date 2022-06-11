package per.hsm.binder

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import android.util.TimeUtils
import per.hsm.binder.bean.Book
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

private const val TAG = "BookService"
class BookService : Service() {


    private val mListenerList = RemoteCallbackList<INewBookArrivedListener>()

    val mBookList: CopyOnWriteArrayList<Book> by lazy {
        val list = CopyOnWriteArrayList<Book>()
        list.add(Book("三国演义", 20))
        list.add(Book("红楼梦", 29))
        list.add(Book("西游记",30))
        list.add(Book("水浒传",35))
        list.add(Book("钢铁是怎样炼成的", 15))
        list.add(Book("阿甘正传", 23))
        list
    }



    val binder = object: IBookStore.Stub() {

        override fun getBookList(): List<Book> {
            //这里是一个耗时操作
            TimeUnit.SECONDS.sleep(5)
            return mBookList
        }

        override fun buykBook(name: String?): Book? {
            mBookList.forEach {
                if (it.name == name){
                    mBookList.remove(it)
                    return it
                }
            }
            return null
        }

        override fun backBook(book: Book?) {
            book?.let {
                mBookList.add(it)
            }

        }

        override fun registerListener(listener: INewBookArrivedListener?) {
            Log.d(TAG, "registerListener: mListenerList size ")
            listener?.let {
                mListenerList.register(listener)
                bFinish = false
                addMoreBook()
            }


        }

        override fun unRegisterListener(listener: INewBookArrivedListener?) {
            mListenerList.unregister(listener)
            Log.d(TAG, "unRegisterListener: mListenerList ")
            bFinish = true
        }

    }

    override fun onBind(intent: Intent): IBinder{

        Log.d(TAG, "onBind: ")
        return binder
    }

    private var bFinish = false

    private fun addMoreBook(){
        Thread{
            while (!bFinish) {
                val newBook = Book("测试书${mBookList.size+1}", 10)
                Log.d(TAG, "addMoreBook: ${mBookList.hashCode()} and book size ${mBookList.size}")
                mBookList.add(newBook)
                boradcast(newBook)
                TimeUnit.SECONDS.sleep(3)
            }
        }.start()
    }

    private fun boradcast(newBook:  Book){
        val size = mListenerList.beginBroadcast()
        for(i in 0 until size){
            mListenerList.getBroadcastItem(i).onNewBookArrived(newBook)
        }
        mListenerList.finishBroadcast()
    }


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: server start")

    }




}