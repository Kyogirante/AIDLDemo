package com.demo.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KyoWang on 2016/12/20 .
 */
public class ParcelableData implements Parcelable {
    public int num;

    public static final Creator<ParcelableData> CREATOR = new Creator<ParcelableData>() {
        @Override
        public ParcelableData createFromParcel(Parcel in) {
            return new ParcelableData(in);
        }

        @Override
        public ParcelableData[] newArray(int size) {
            return new ParcelableData[size];
        }
    };

    public ParcelableData(int num) {
        this.num = num;
    }

    public ParcelableData(Parcel in) {
        num = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(num);
    }
}
