package com.adara.yashsd.rgblamp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class Main2Activity extends AppCompatActivity {
    TextView LLN,LLA;
    Button bConnect;
    ListView lvDevices;

    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> pairdevices;

    String LLNF = "LLNF";
    String LLAF = "LLAF";
    String LLNFtv = null;
    String LLAFtv = null;

    ArrayList<String> LampName ;
    ArrayList<String> LampAddress ;

    customListviewAdapter CLVA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LampName = new ArrayList<>();
        LampAddress = new ArrayList<>();

        LLN = (TextView)findViewById(R.id.LCN);
        LLA = (TextView)findViewById(R.id.LCA);
        bConnect = (Button)findViewById(R.id.bConnect);
        lvDevices = (ListView)findViewById(R.id.lvdevices);

        bConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this,RGBService.class);
                i.putExtra("address",LLAFtv);
                startService(i);
            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null){
            Toast.makeText(this, "Device does not have Bluetooth capabilities", Toast.LENGTH_SHORT).show();
        }
        else if(!bluetoothAdapter.isEnabled()){
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try{
            FileInputStream fis = openFileInput(LLNF);
            String temp = "";
            int c;
            while((c = fis.read())!= -1)
            {
                temp = temp + Character.toString((char)c);
            }
            LLNFtv = temp;
        }catch (IOException e)
        { e.printStackTrace();}
        finally {
            if(LLNFtv == null || LLNFtv.equals("")){
                LLN.setText("~No Last Lamp Name Available~");
            }
            else
            {
                LLN.setText(LLNFtv);
            }
        }

        try{
            FileInputStream fis = openFileInput(LLAF);
            String temp = "";
            int c;
            while((c = fis.read())!= -1)
            {
                temp = temp + Character.toString((char)c);
            }
            LLAFtv = temp;
        }catch (IOException e)
        {e.printStackTrace();}
        finally {
            if(LLAFtv == null || LLAFtv.equals("")){
                LLA.setText("~No Last Lamp Address Available~");
            }
            else
            {
                LLA.setText(LLAFtv);
            }
        }

        pairdevices = bluetoothAdapter.getBondedDevices();

        if(pairdevices.size()>0){
            for(BluetoothDevice bd : pairdevices){
                LampName.add(bd.getName());
                LampAddress.add(bd.getAddress());
            }

            String[] LampNameArr = new String[LampName.size()];
            LampNameArr = LampName.toArray(LampNameArr);

            String[] LampAddressArr = new String[LampAddress.size()];
            LampAddressArr = LampAddress.toArray(LampAddressArr);

            CLVA = new customListviewAdapter(this,LampNameArr,LampAddressArr);
            lvDevices.setAdapter(CLVA);

            lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView cubeN = (TextView)view.findViewById(R.id.patternname);
                    TextView cubeA = (TextView)view.findViewById(R.id.pattern);

                    try{
                        FileOutputStream fos = openFileOutput(LLNF,MODE_PRIVATE);
                        fos.write(cubeN.getText().toString().getBytes());
                        fos.close();
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    try{
                        FileOutputStream fos = openFileOutput(LLAF,MODE_PRIVATE);
                        fos.write(cubeA.getText().toString().getBytes());
                        fos.close();
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    LLN.setText(cubeN.getText().toString());
                    LLA.setText(cubeA.getText().toString());

                    Intent i = new Intent(Main2Activity.this,RGBService.class);
                    i.putExtra("address",cubeA.getText().toString());
                    startService(i);
                }
            });
        }

    }
}