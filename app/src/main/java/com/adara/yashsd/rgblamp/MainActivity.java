package com.adara.yashsd.rgblamp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    SeekBar sbr,sbg,sbb;
    Button b1,b2,b3,b4,b5;
    TextView tv;

    final static public String MOOD = "MOOD";
    final static public String MulCMode = "MulCMode";
    final static public String MonCMode = "MonCMode";
    final static public String ManMode = "ManMode";

    int red,green,blue;
    String redColorSave = "redColorSave";
    String greenColorSave = "greenColorSave";
    String blueColorSave = "blueColorSave";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        red = 0;
        green = 0;
        blue = 0;

        b1 = (Button)findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(MulCMode);
                sendBroadcast(broadcastIntent);
            }
        });
        b2 = (Button)findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(MonCMode);
                sendBroadcast(broadcastIntent);
            }
        });
        b3 = (Button)findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(ManMode);
                sendBroadcast(broadcastIntent);
            }
        });
        b4 = (Button)findViewById(R.id.b4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(MOOD);
                sendBroadcast(broadcastIntent);
            }
        });
        b5 = (Button)findViewById(R.id.b5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(i);
            }
        });

        sbr = (SeekBar)findViewById(R.id.sbr);
        sbg = (SeekBar)findViewById(R.id.sbg);
        sbb = (SeekBar)findViewById(R.id.sbb);

        tv = (TextView)findViewById(R.id.tv);

        try{
            FileInputStream fis = openFileInput(redColorSave);
            String temp= "";
            int c;
            while((c = fis.read())!=-1)
            {
                temp = temp + Character.toString((char)c);
            }

            if(temp.equals("") || temp == null ){}
            else{
                red = Integer.parseInt(temp);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        try{
            FileInputStream fis = openFileInput(greenColorSave);
            String temp= "";
            int c;
            while((c = fis.read())!=-1)
            {
                temp = temp + Character.toString((char)c);
            }

            if(temp.equals("") || temp == null ){}
            else{
                green = Integer.parseInt(temp);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        try{
            FileInputStream fis = openFileInput(blueColorSave);
            String temp= "";
            int c;
            while((c = fis.read())!=-1)
            {
                temp = temp + Character.toString((char)c);
            }

            if(temp.equals("") || temp == null ){}
            else{
                blue = Integer.parseInt(temp);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        sbr = (SeekBar)findViewById(R.id.sbr);
        sbr.setMax(255);
        sbr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red = progress;
                try{
                    FileOutputStream fos = openFileOutput(redColorSave,MODE_PRIVATE);
                    fos.write(Integer.toString(red).getBytes());
                    fos.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                sendColors(red,green,blue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbg = (SeekBar)findViewById(R.id.sbg);
        sbg.setMax(255);
        sbg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                green = progress;
                try{
                    FileOutputStream fos = openFileOutput(greenColorSave,MODE_PRIVATE);
                    fos.write(Integer.toString(green).getBytes());
                    fos.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                sendColors(red,green,blue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbb = (SeekBar)findViewById(R.id.sbb);
        sbb.setMax(255);
        sbb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blue = progress;
                try{
                    FileOutputStream fos = openFileOutput(blueColorSave,MODE_PRIVATE);
                    fos.write(Integer.toString(blue).getBytes());
                    fos.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                sendColors(red,green,blue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbr.setProgress(red);
        sbg.setProgress(green);
        sbb.setProgress(blue);

        if (checkPermission(Manifest.permission.BLUETOOTH) && checkPermission(Manifest.permission.BLUETOOTH_ADMIN)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_ADMIN},
                    007);
        }
    }

    private boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return (checkPermission == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 007: {
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    void sendColors(int r,int g,int b){
        tv.setBackgroundColor(Color.rgb(r,g,b));
        /*Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ManMode);
        broadcastIntent.putExtra("red",red);
        broadcastIntent.putExtra("green",green);
        broadcastIntent.putExtra("blue",blue);
        sendBroadcast(broadcastIntent);
        int temp = 000+blue;
        Toast.makeText(this,"R"+String.format("%03d",red)+"G"+String.format("%03d",green)+"B"+String.format("%03d",blue), Toast.LENGTH_SHORT).show();*/
    }

}
