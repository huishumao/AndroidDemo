package per.hsm.binder.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * (Read the Fucking source code)
 *
 * @ProjectName: AndroidDemo
 * @Package: per.hsm.binder
 * @ClassName: Book
 * @Description: java类作用描述
 * @Author: Roid/hsm
 * @CreateDate: 2022/4/23 13:31
 */
public class Book implements Parcelable {

    private String name;

    private int price;

    public Book(String name, int price) {
        this.name = name;
        this.price = price;
    }

    //    private List<String> chapter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.price);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.price = source.readInt();
    }

    public Book() {
    }

    protected Book(Parcel in) {
        this.name = in.readString();
        this.price = in.readInt();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
