// INewBookArrivedListener.aidl
package per.hsm.binder;
import per.hsm.binder.bean.Book;
// Declare any non-default types here with import statements

interface INewBookArrivedListener {

    void onNewBookArrived(in Book newBook);
}