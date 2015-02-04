package com.example.xu.myapplication;

import android.app.Application;

import com.llmofang.android.agent.LLMoFang;

/**
 * Created by xu on 2015/2/3.
 */
public class myapplicationApplication extends Application{
    @Override
    public void onCreate() {
        LLMoFang.initialize(this,"tc6nvipyaigwb0ukjuhcj3kz8n8xmig5r2jf0oa4gg25ou61","vdax1anbo1v1y5iga4n9jdrdkvmo43zeu4tvlx1ahxk4ftxm");
        super.onCreate();
    }
}
