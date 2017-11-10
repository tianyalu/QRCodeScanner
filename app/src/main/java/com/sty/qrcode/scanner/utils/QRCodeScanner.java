package com.sty.qrcode.scanner.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.sty.qrcode.scanner.MipcaActivityCapture;
/**
 * Created by Steven.T on 2017/10/17/0017.
 */

public class QRCodeScanner {

    public static final String SCAN_INTENT_ACTION = "android.intent.action.QRCodeScanner";
    public static final String TIMEOUT = "TIMEOUT";
    private Context context;
    private ScannerListener listener;
    private String qrCodeStr;
    private int mTimeout = 5 * 60; //默认超时时间为5分钟

    private boolean isBroadcastRegistered = false;
    public static final int SUCCESS_FLAG = 0;
    public static final int CANCEL_FLAG = 1;
    public static final int ERROR_FLAG = 2;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().endsWith(SCAN_INTENT_ACTION)) {
                int flag = intent.getIntExtra("FLAGS", ERROR_FLAG);
                switch ( flag ){
                    case SUCCESS_FLAG:
                        qrCodeStr = intent.getStringExtra("QR_CODE_STR");
                        if(listener != null) {
                            listener.onReadSuccess(qrCodeStr);
                        }
                        break;
                    case CANCEL_FLAG:
                        if(listener != null){
                            listener.onCancel();
                        }
                        break;
                    case ERROR_FLAG:
                    default:
                        if(listener != null){
                            listener.onReadError();
                        }
                        break;
                }
            }
        }
    };

    public QRCodeScanner(Context context){
        this.context = context;
    }

    public QRCodeScanner(Context context, int mTimeout){
        this.context = context;
        this.mTimeout = mTimeout;
    }

    public interface ScannerListener{
        void onReadSuccess(String result);

        void onReadError();

        void onCancel();
    }

    public void open(){
        //Intent intent = new Intent(SCAN_INTENT_ACTION);
        Intent intent = new Intent(context, MipcaActivityCapture.class);
        intent.putExtra(TIMEOUT, mTimeout);
        context.startActivity(intent);

        registerMyBroadcastReceiver();
    }

    public void start(ScannerListener listener){
        this.listener = listener;
    }

    public void close(){
        unRegisterMyBroadcastReceiver();

        Intent intent = new Intent(MipcaActivityCapture.CLOSE_SCANNER_INTENT_ACTION);
        context.sendBroadcast(intent);
    }

    private void registerMyBroadcastReceiver(){
        if(!isBroadcastRegistered) {
            isBroadcastRegistered = !isBroadcastRegistered;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(QRCodeScanner.SCAN_INTENT_ACTION);
            context.registerReceiver(receiver, intentFilter);
        }
    }

    private void unRegisterMyBroadcastReceiver(){
        if(isBroadcastRegistered) {
            isBroadcastRegistered = !isBroadcastRegistered;
            context.unregisterReceiver(receiver);
        }
    }
}
