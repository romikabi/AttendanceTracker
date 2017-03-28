package com.periphery.attendancetracker.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import com.google.android.gms.nearby.messages.IBeaconId;
import com.periphery.attendancetracker.BeaconTracker;
import com.periphery.attendancetracker.R;
import com.periphery.attendancetracker.BeaconCallbacks;
import com.periphery.attendancetracker.model.nearMessagesApiClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.periphery.attendancetracker.R.id.mainButton;

public class MainActivity extends AppCompatActivity implements BeaconCallbacks {

    private BeaconTracker api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        URL url;
        String content = "";

        /*try {
            url = new URL("http://stattendance.somee.com/api/Student/History");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            content = reader.readLine();
        } catch (Exception ex) {
            Log.i("romik", ex.getMessage());
        }
        Log.i("romik", content);*/
        content = "{\"Uuid\":\"6BC75D33-6EA3-4F0E-B23A-C93EFE4C8704\",\"Major\":16808,\"Minor\":19400}";
        Pattern id = Pattern.compile("\"Uuid\":\"" +
                "(?<Uuid>[0-9a-zA-Z\\-]+)" +
                "\",\"Major\":" +
                "(?<Major>[0-9]+)" +
                ",\"Minor\":" +
                "(?<Minor>[0-9]+)");

        Matcher m = id.matcher(content);
        m.find();

        nearMessagesApiClass api = new nearMessagesApiClass(this, this,
                new IBeaconId(UUID.fromString(m.group(1)),
                        Short.parseShort(m.group(2)),
                        Short.parseShort(m.group(3))));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 500) return;
        api.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSignalFound() {
        Log.i("romikabi_Logs", "Signal found");
        Button b = (Button) this.findViewById(mainButton);
        b.setText("huj");
    }

    @Override
    public void onSignalLost() {
        Log.i("romikabi_Logs", "Signal lost");
        Button b = (Button) this.findViewById(mainButton);
        b.setText("pizda");
    }
}
