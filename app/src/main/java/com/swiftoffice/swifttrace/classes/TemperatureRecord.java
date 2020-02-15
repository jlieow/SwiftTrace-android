package com.swiftoffice.swifttrace.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class TemperatureRecord implements Parcelable {
    String Date;
    String Time;
    String Temperature;
    String DocID;

    public TemperatureRecord(String Date, String Time, String Temperature, String DocID) {
        this.Date = Date;
        this.Time = Time;
        this.Temperature = Temperature;
        this.DocID = DocID;
    }

    public void insertDate(String Date) {
        this.Date = Date;
    }

    public void insertTime(String Time) {
        this.Date = Time;
    }

    public void insertTemperature(String Temperature) {
        this.Date = Temperature;
    }

    public void insertDocID(String DocID) {
        this.Date = DocID;
    }

    protected TemperatureRecord(Parcel in) {
        Date = in.readString();
        Time = in.readString();
        Temperature = in.readString();
        DocID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Date);
        dest.writeString(Time);
        dest.writeString(Temperature);
        dest.writeString(DocID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TemperatureRecord> CREATOR = new Creator<TemperatureRecord>() {
        @Override
        public TemperatureRecord createFromParcel(Parcel in) {
            return new TemperatureRecord(in);
        }

        @Override
        public TemperatureRecord[] newArray(int size) {
            return new TemperatureRecord[size];
        }
    };

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getTemperature() {
        return Temperature;
    }

    public String getDocID() {
        return DocID;
    }



}
