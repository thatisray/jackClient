package com.jack.jackclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jack.jack_aidl.Book;
import com.jack.jack_aidl.BookController;

import java.util.List;

public class MainActivity extends Activity {
    private final String TAG ="Client";

    private BookController mBookController;

    private  boolean connected;

    private List<Book> mBookList;


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected( ComponentName componentName, IBinder iBinder ) {
            mBookController = BookController.Stub.asInterface(iBinder);
            connected = true;
            System.out.println("执行了"+mBookController+"链接状态+"+connected);
        }

        @Override
        public void onServiceDisconnected( ComponentName componentName ) {
           connected = false;
            System.out.println("没执行了");
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick( View view ) {
           switch (view.getId()){
               case R.id.btn_getBookList:
                   if (connected){
                       try {
                           mBookList = mBookController.getBookList();
                       } catch (RemoteException e) {
                           e.printStackTrace();
                       }
                      log();
                   }
                   break;
               case R.id.btn_addBook_inOut:
                   if (connected){
                       Book book = new Book("这是一本新书 InOut");
                       try {
                           mBookController.addBookInOut(book);
                           Log.e(TAG,"向服务器以Inout方式添加了一本新书");
                           Log.e(TAG,"新书名：" + book.getName());

                       } catch (RemoteException e) {
                           e.printStackTrace();
                       }
                   }
                   break;


           }
        }
    };

    private void log() {
        for (Book book:mBookList){
            Log.e(TAG,book.toString());
        }
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_addBook_inOut).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_getBookList).setOnClickListener(mOnClickListener);
        bindService();
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setPackage("com.jack.jack_aidl");
        intent.setAction("com.jack.jack_aidl.action");
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connected){
            unbindService(mServiceConnection);
        }
    }


}
