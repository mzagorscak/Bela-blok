package com.example.zagorscak.belablok;

import android.os.Parcel;
import android.os.Parcelable;

public class BelaRecord implements Parcelable {
    private Integer points_We;
    private Integer points_They;
    private Integer call_Points_We;
    private Integer call_Points_They;
    private Integer points_We_Sum;
    private Integer points_They_Sum;

    public BelaRecord() {
        this.points_We = 0;
        this.points_They = 0;
        this.call_Points_We = 0;
        this.call_Points_They = 0;
        this.points_We_Sum = 0;
        this.points_They_Sum = 0;
    }

    protected BelaRecord(Parcel in) {
        if (in.readByte() == 0) {
            points_We = null;
        } else {
            points_We = in.readInt();
        }
        if (in.readByte() == 0) {
            points_They = null;
        } else {
            points_They = in.readInt();
        }
        if (in.readByte() == 0) {
            call_Points_We = null;
        } else {
            call_Points_We = in.readInt();
        }
        if (in.readByte() == 0) {
            call_Points_They = null;
        } else {
            call_Points_They = in.readInt();
        }
        if (in.readByte() == 0) {
            points_We_Sum = null;
        } else {
            points_We_Sum = in.readInt();
        }
        if (in.readByte() == 0) {
            points_They_Sum = null;
        } else {
            points_They_Sum = in.readInt();
        }
    }

    public static final Creator<BelaRecord> CREATOR = new Creator<BelaRecord>() {
        @Override
        public BelaRecord createFromParcel(Parcel in) {
            return new BelaRecord(in);
        }

        @Override
        public BelaRecord[] newArray(int size) {
            return new BelaRecord[size];
        }
    };

    public Integer getPoints_We() {
        return points_We;
    }

    public void setPoints_We(Integer points_We) {
        this.points_We = points_We;
    }

    public Integer getPoints_They() {
        return points_They;
    }

    public void setPoints_They(Integer points_They) {
        this.points_They = points_They;
    }

    public Integer getCall_Points_We() {
        return call_Points_We;
    }

    public void setCall_Points_We(Integer call_Points_We) {
        this.call_Points_We = call_Points_We;
    }

    public Integer getCall_Points_They() {
        return call_Points_They;
    }

    public void setCall_Points_They(Integer call_Points_They) {
        this.call_Points_They = call_Points_They;
    }

    public Integer getPoints_We_Sum() {
        return points_We_Sum;
    }

    public void setPoints_We_Sum(Integer points_We_Sum) {
        this.points_We_Sum = points_We_Sum;
    }

    public Integer getPoints_They_Sum() {
        return points_They_Sum;
    }

    public void setPoints_They_Sum(Integer points_They_Sum) {
        this.points_They_Sum = points_They_Sum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(points_We_Sum);
        dest.writeInt(points_They_Sum);
        dest.writeInt(points_We);
        dest.writeInt(points_They);
        dest.writeInt(call_Points_We);
        dest.writeInt(call_Points_They);
    }
}
