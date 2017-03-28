package com.periphery.attendancetracker;

// should be implemented by class, which will handle messages
// (implementation by activity is possible)
public interface BeaconCallbacks {
    // triggers when appropriate message has been found
    void onSignalFound();

    // triggers when appropriate message has been lost
    void onSignalLost();

}
