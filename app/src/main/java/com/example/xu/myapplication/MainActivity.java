package com.example.xu.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.xu.myapplication.service.ServiceRulesException;
import com.example.xu.myapplication.service.UserService;
import com.example.xu.myapplication.service.UserServiceImpl;

import java.lang.ref.WeakReference;


public class MainActivity extends Activity {

    private Button btnHttp;
   private static ProgressDialog prcsDialog;
    private UserService userService = new UserServiceImpl();
    private static class IHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public IHandler(MainActivity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }


        @Override
        public void handleMessage(Message msg) {

            if (prcsDialog != null) {
                prcsDialog.dismiss();
            }
            int flag = msg.what;
            switch (flag) {
                case 0:
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    ((MainActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case 1:
                    ((MainActivity) mActivity.get()).showTip("Success!!");
                    break;
                default:
                    break;

            }
            // super.handleMessage(msg);
        }
    }
    private void showTip(String str) {
        Toast.makeText(this, str,Toast.LENGTH_SHORT).show();
    }
    private IHandler handler = new IHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btnHttp = (Button) findViewById(R.id.btn_http);
        btnHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            userService.userLogin("", "");
                            handler.sendEmptyMessage(1);
                        } catch (ServiceRulesException e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ErrorMsg", e.getMessage());
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ErrorMsg", "Something error!");
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                });
                thread.start();
            }
        });
    }



}
