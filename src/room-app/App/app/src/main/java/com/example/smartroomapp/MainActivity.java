package com.example.smartroomapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@SuppressLint("MissingPermission")
public class MainActivity extends AppCompatActivity {

    private OutputStream bluetoothOutputStream;
    private Button controlButton;
    private TextView textView;
    private Button ledButton;
    private SeekBar seekBar;
    private boolean ledState;
    private boolean active;
    private int alpha;
    private BluetoothClientConnectionThread connectionThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_switch);
        ledState = false;
        active = false;
        alpha = 0;
        initUI();
    }

    private void initUI() {
        textView = findViewById(R.id.textView4);
        controlButton = findViewById(R.id.remotebutton);
        controlButton.setBackgroundColor(Color.LTGRAY);
        controlButton.setEnabled(false);
        controlButton.setOnClickListener((v) -> controlMessage());
        ledButton = findViewById(R.id.remotebutton2);
        ledButton.setEnabled(false);
        ledButton.setOnClickListener((v) -> ledMessage());
        seekBar = findViewById(R.id.seekBar);
        seekBar.setEnabled(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                alpha = (i*180)/100;
                new Thread(() -> {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("androidControl", true);
                        obj.put("lightControl",ledState);
                        obj.put("alpha", alpha);

                        String message = obj.toString() + "\n";
                        bluetoothOutputStream.write(message.getBytes(StandardCharsets.UTF_8));
                        Log.i(C.TAG, "Control=" + active);
                        Log.i(C.TAG, "Led=" + ledState);
                        Log.i(C.TAG, "angle=" + alpha);
                        runOnUiThread(() ->
                            textView.setText("Value " + i + "(" + alpha + "°)")
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void controlMessage() {
        new Thread(() -> {
            try {
                JSONObject obj = new JSONObject();
                active = !active;
                obj.put("androidControl", active);
                obj.put("lightControl",ledState);
                obj.put("alpha", alpha);
                runOnUiThread(() -> {
                    seekBar.setEnabled(active);
                    ledButton.setEnabled(active);
                });
                Log.i(C.TAG, "Control=" + active);
                Log.i(C.TAG, "Led=" + ledState);
                Log.i(C.TAG, "angle=" + alpha);
                String message = obj.toString() + "\n";
                Log.i(C.TAG, message);
                bluetoothOutputStream.write(message.getBytes(StandardCharsets.UTF_8));
                runOnUiThread(() -> {
                            controlButton.setBackgroundColor(active ? Color.GREEN : Color.RED);
                            controlButton.setText(active ? "LEAVE CONTROL" : "TAKE CONTROL");
                        }
                );
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void ledMessage() {
        new Thread(() -> {
            try {
                ledState = !ledState;
                JSONObject obj = new JSONObject();
                if(!ledState){
                    obj.put("androidControl", true);
                    obj.put("lightControl",ledState);
                    obj.put("alpha", alpha);
                }else{
                    obj.put("androidControl", true);
                    obj.put("lightControl",ledState);
                    obj.put("alpha", alpha);
                }

                String message = obj.toString() + "\n";
                bluetoothOutputStream.write(message.getBytes(StandardCharsets.UTF_8));
                Log.i(C.TAG, "Control=" + active);
                Log.i(C.TAG, "Led=" + ledState);
                Log.i(C.TAG, "angle=" + alpha);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(ScanActivity.X_BLUETOOTH_DEVICE_EXTRA);
        BluetoothAdapter btAdapter = getSystemService(BluetoothManager.class).getAdapter();
        Log.i(C.TAG, "Connecting to " + bluetoothDevice.getName());
        connectionThread = new BluetoothClientConnectionThread(bluetoothDevice, btAdapter, this::manageConnectedSocket);
        connectionThread.start();
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        try {
            bluetoothOutputStream = socket.getOutputStream();
            Log.i(C.TAG, "Connection successful!");
        } catch (IOException e) {
            Log.e(C.TAG, "Error occurred when creating output stream", e);
        }
        runOnUiThread(() -> {
            controlButton.setEnabled(true);
            controlButton.setBackgroundColor(Color.RED);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        connectionThread.cancel();
    }

}