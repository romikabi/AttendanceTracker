package com.periphery.beacontransmitter;

import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.nearby.messages.IBeaconId;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

public class MBeaconTransmitter {

    private AppCompatActivity mActivity;

    public void startAdvertising(IBeaconId id){
        Beacon beacon = new Beacon.Builder()
                .setId1(id.getProximityUuid().toString())
                .setId2(((Short)(id.getMajor())).toString())
                .setId3(((Short)(id.getMinor())).toString())
                .build();
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24");
        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(mActivity.getApplicationContext(), beaconParser);
        beaconTransmitter.startAdvertising(beacon);
    }
}
