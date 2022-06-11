// IBookStore.aidl
package per.hsm.binder;

import per.hsm.binder.bean.Book;
import per.hsm.binder.INewBookArrivedListener;



interface IBookStore {

    List<Book> getBookList();

    Book buykBook(String name);

    void backBook(in Book book);

    void registerListener(INewBookArrivedListener listener);

    void unRegisterListener(INewBookArrivedListener listener);
}