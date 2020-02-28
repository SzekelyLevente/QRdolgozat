package com.example.qrdolgozat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button btnScan,btnKiir;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator=new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("QRCode Scanning by brendon");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
        btnKiir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state;
                String szoveges_adat;
                Date datum= Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formazottDate=dateFormat.format(datum);
                szoveges_adat=text.getText().toString()+","+formazottDate;
                state= Environment.getExternalStorageState();
                if(state.equals(Environment.MEDIA_MOUNTED))
                {
                    File file=new File(Environment.getExternalStorageState(),"ScannedCodes.csv");
                    BufferedWriter writer= null;
                    try {
                        writer = new BufferedWriter(new FileWriter(file,true));
                        writer.append(szoveges_adat);
                        writer.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents()==null)
            {
                Toast.makeText(MainActivity.this,"kiléptél a scannerből!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                text.setText("QR Code eredmény: "+result.getContents());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void init()
    {
        btnScan=findViewById(R.id.btnScan);
        btnKiir=findViewById(R.id.btnKiir);
        text=findViewById(R.id.text);
    }
}
