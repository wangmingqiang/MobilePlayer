package wang.mq.mobileplayer1020.app;

import android.app.Application;

import org.xutils.x;

/**
 * Created by wangmingqiang on 2017/1/11.
 */
//代表整个文件
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志..
    }
}


