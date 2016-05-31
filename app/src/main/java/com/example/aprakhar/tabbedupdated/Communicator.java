package com.example.aprakhar.tabbedupdated;

/**
 * Created by aprakhar on 14-Feb-16.
 */
public interface Communicator {
    public void respond(String Number,String Date,String Time,String id,String enabled);
    public void signalToAForDateBeingAValidEntryOrNot(boolean flag);
    public void updateDateTimeListAfterDelete();
}
