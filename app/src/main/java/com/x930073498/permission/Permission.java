package com.x930073498.permission;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by x930073498 on 2019/1/24 0024.
 */
@SuppressWarnings("ALL")
public class Permission implements Parcelable {
    public final String name;
    public final boolean granted;
    public final boolean shouldShowRequestPermissionRationale;
    public final int index;
    final int groupLength;


    Permission(String name, int index, int groupLength, boolean granted, boolean shouldShowRequestPermissionRationale) {
        this.name = name;
        this.index = index;
        this.groupLength = groupLength;
        this.granted = granted;
        this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
    }

    public boolean isLast() {
        return index == groupLength - 1;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "name='" + name + '\'' +
                ", granted=" + granted +
                ", shouldShowRequestPermissionRationale=" + shouldShowRequestPermissionRationale +
                '}';
    }

    protected Permission(Parcel in) {
        name = in.readString();
        index = in.readInt();
        groupLength = in.readInt();
        granted = in.readByte() != 0;
        shouldShowRequestPermissionRationale = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(index);
        dest.writeInt(groupLength);
        dest.writeByte((byte) (granted ? 1 : 0));
        dest.writeByte((byte) (shouldShowRequestPermissionRationale ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Permission> CREATOR = new Creator<Permission>() {
        @Override
        public Permission createFromParcel(Parcel in) {
            return new Permission(in);
        }

        @Override
        public Permission[] newArray(int size) {
            return new Permission[size];
        }
    };

}
