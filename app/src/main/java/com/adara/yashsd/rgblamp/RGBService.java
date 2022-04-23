package com.adara.yashsd.rgblamp;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.UUID;

public class RGBService extends Service {

    String address = null;

    boolean isConnected = false;
    boolean connectSuccess = false;
    BluetoothAdapter bluetoothAdapter = null;
    BluetoothSocket bluetoothSocket = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private IntentFilter intentFilter;

    @Override
    public void onCreate() {
        super.onCreate();

        intentFilter = new IntentFilter();
        intentFilter.addAction(MainActivity.MOOD);

        registerReceiver(receiver,intentFilter);
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MainActivity.MOOD)) {
                Toast.makeText(context, "MOOD", Toast.LENGTH_SHORT).show();
                try {
                    bluetoothSocket.getOutputStream().write("*2:".toString().getBytes());
                } catch (Exception e) {
                }
            }
            else if (intent.getAction().equals(MainActivity.MulCMode)) {
                Toast.makeText(context, "MulCMode", Toast.LENGTH_SHORT).show();
                try {
                    bluetoothSocket.getOutputStream().write("*0:".toString().getBytes());
                } catch (Exception e) {
                }
            }
            else if (intent.getAction().equals(MainActivity.MonCMode)) {
                Toast.makeText(context, "MonCMode", Toast.LENGTH_SHORT).show();
                int red = intent.getExtras().getInt("red");
                int green = intent.getExtras().getInt("green");
                int blue = intent.getExtras().getInt("blue");
                try {
                    bluetoothSocket.getOutputStream().write(("*1"+"R"+String.format("%03d",red)+"G"+String.format("%03d",green)+"B"+String.format("%03d",blue)).toString().getBytes());
                } catch (Exception e) {
                }
            }
            else if (intent.getAction().equals(MainActivity.ManMode)) {
                Toast.makeText(context, "ManMode", Toast.LENGTH_SHORT).show();
                int red = intent.getExtras().getInt("red");
                int green = intent.getExtras().getInt("green");
                int blue = intent.getExtras().getInt("blue");
                try {
                    bluetoothSocket.getOutputStream().write(("*3"+"R"+String.format("%03d",red)+"G"+String.format("%03d",green)+"B"+String.format("%03d",blue)).toString().getBytes());
                } catch (Exception e) {
                }
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        address = intent.getExtras().getString("address");
        new connectBT().execute();
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class connectBT extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            try{
                if(bluetoothSocket == null || !isConnected ){
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice BTdevice = bluetoothAdapter.getRemoteDevice(address);
                    bluetoothSocket  = BTdevice.createInsecureRfcommSocketToServiceRecord(myUUID);
                    bluetoothAdapter.cancelDiscovery();
                    bluetoothSocket.connect();

                    connectSuccess = true;
                }
            }catch (Exception e){
                connectSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(RGBService.this, "Connecting", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(connectSuccess){
                Toast.makeText(RGBService.this, "Connected", Toast.LENGTH_SHORT).show();
                isConnected = true;
            }
            else if(!connectSuccess)
            {
                Toast.makeText(RGBService.this, "Connection failed try again", Toast.LENGTH_SHORT).show();
                isConnected = false;
                stopSelf();
            }

        }
    }

}
