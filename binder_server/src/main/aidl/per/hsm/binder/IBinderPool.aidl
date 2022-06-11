// IBinderPool.aidl
package per.hsm.binder;

// Declare any non-default types here with import statements

interface IBinderPool {

    IBinder getBinder(int type);



}