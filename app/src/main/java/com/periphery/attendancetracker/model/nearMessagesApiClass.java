package com.periphery.attendancetracker.model;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.*;
import com.periphery.attendancetracker.BeaconCallbacks;
import com.periphery.attendancetracker.BeaconTracker;

public class nearMessagesApiClass implements BeaconTracker {

    // should be called from activity<

    // if your activity implements BeaconCallbacks, should be called like
    // nearMessagesApiClass(this, this, ID)
    public nearMessagesApiClass(AppCompatActivity activity, BeaconCallbacks callbacks, IBeaconId id) {
        mActivity = activity;
        mBeaconCallbacks = callbacks;
        mMessageFilter = new MessageFilter.Builder()
                .includeIBeaconIds(id.getProximityUuid(), id.getMajor(), id.getMinor())
                .build();

        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i(TAG, "Signal found");
                mBeaconCallbacks.onSignalFound();
            }

            @Override
            public void onLost(Message message) {
                Log.i(TAG, "Signal lost");
                mBeaconCallbacks.onSignalLost();
            }
        };
        if (!havePermissions())
            requestPermissions();
        else
            buildGoogleApiClient();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != PERMISSIONS_REQUEST_CODE) {
            return;
        }
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted");
                buildGoogleApiClient();
            }
        }
    }
    // should be called from activity>

    private AppCompatActivity mActivity;

    private BeaconCallbacks mBeaconCallbacks;

    private final String TAG = "romikabi_Logs";

    private GoogleApiClient mGoogleApiClient;

    private MessageListener mMessageListener;

    private MessageFilter mMessageFilter;

    private final int PERMISSIONS_REQUEST_CODE = 1111;

    private synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                    .addApi(Nearby.MESSAGES_API, new MessagesOptions.Builder()
                            .setPermissions(NearbyPermissions.BLE)
                            .build())
                    .addConnectionCallbacks(new mConnectionCallbacks())
                    .enableAutoManage(mActivity, new mOnConnectionFailedListener())
                    .build();
        }
    }

    private boolean havePermissions() {
        return ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Log.i(TAG, "Requesting permissions");
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
    }

    private void subscribe() {
        Log.i(TAG, "Subscribing");
        if (!mGoogleApiClient.isConnected()) {
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else {
            SubscribeOptions options = new SubscribeOptions.Builder()
                    .setStrategy(Strategy.BLE_ONLY)
                    .setFilter(mMessageFilter)
                    .build();
            Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener, options);
            Log.i(TAG, "Subscribed successfully");
        }
    }

    private class mConnectionCallbacks implements GoogleApiClient.ConnectionCallbacks {
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Log.i(TAG, "Connected");
            subscribe();
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.i(TAG, "Connection suspended");
        }
    }

    private class mOnConnectionFailedListener implements GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.i(TAG, "Connection failed");
        }
    }
}
