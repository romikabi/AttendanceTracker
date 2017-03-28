package com.periphery.attendancetracker;

import android.support.annotation.NonNull;

public interface BeaconTracker {
    // should be called from activity.onRequestPermissionsResult
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
