package com.sty.qrcode.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sty.qrcode.scanner.utils.QRCodeScanner;

/**
 * Created by Steven.T on 2017/10/17/0017.
 */

public class SecondActivity extends AppCompatActivity {

    public static final String CLOSE_SCANNER_INTENT_ACTION = "android.intent.action.CloseQRCodeScanner";
    private Button btnSendBroadcast;
    private EditText etQRCode;

    private BroadcastReceiver mDestroyActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(CLOSE_SCANNER_INTENT_ACTION)){
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        registerDestroyActivityReceiver();

        initViews();
        setListeners();
    }

    private void initViews(){
        btnSendBroadcast = (Button) findViewById(R.id.btn_send_broadcast);
        etQRCode = (EditText) findViewById(R.id.et_qrcode);
    }

    private void setListeners(){
        btnSendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qrcode = etQRCode.getText().toString().trim();
                if(!TextUtils.isEmpty(qrcode)){
                    sendSuccessBroadcast(qrcode);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDestroyActivityReceiver);
    }

    private void registerDestroyActivityReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CLOSE_SCANNER_INTENT_ACTION);
        registerReceiver(mDestroyActivityReceiver, intentFilter);
    }

    private void sendSuccessBroadcast(String qrcodeStr){
        Intent intent = new Intent(QRCodeScanner.SCAN_INTENT_ACTION);
        intent.putExtra("FLAGS", QRCodeScanner.SUCCESS_FLAG);
        intent.putExtra("QR_CODE_STR", qrcodeStr);
        sendBroadcast(intent);
    }

}
