package com.sty.qrcode.scanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sty.qrcode.scanner.utils.QRCodeScanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvQRCode;
    private EditText etQRCode;
    private Button btnStartSecondActivity;
    private Button btnStartScanner;

    private QRCodeScanner qrCodeStartSecondActivity;
    private QRCodeScanner qrCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setListeners();
    }

    private void initViews(){
        tvQRCode = (TextView) findViewById(R.id.tv_qrcode);
        etQRCode = (EditText) findViewById(R.id.et_qrcode);
        btnStartSecondActivity = (Button) findViewById(R.id.btn_start_second_activity);
        btnStartScanner = (Button) findViewById(R.id.btn_start_scanner);

        qrCodeScanner = new QRCodeScanner(this);

    }

    private void setListeners(){
        btnStartSecondActivity.setOnClickListener(this);
        btnStartScanner.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start_second_activity:
                //qrCodeStartSecondActivity.start();
                break;
            case R.id.btn_start_scanner:
                startScanner();
                break;
            default:
                break;
        }
    }

    private void startScanner(){
        qrCodeScanner.open();
        qrCodeScanner.start(new QRCodeScanner.ScannerListener() {

            @Override
            public void onReadSuccess(String result) {
                qrCodeScanner.close();

                tvQRCode.setText(result);
                etQRCode.setText(result);
            }

            @Override
            public void onReadError() {
                qrCodeScanner.close();
            }

            @Override
            public void onCancel() {
                qrCodeScanner.close();
            }
        });
    }
}
