package wang.mq.mobileplayer1020.utils;

import android.content.Context;
import android.content.SharedPreferences;

import wang.mq.mobileplayer1020.service.MusicPlayerService;


/**得到缓存的文本数据
 * Created by wangmingqiang on 2017/1/11.
 */

public class CacheUtils {
    public static String getString(Context mContext, String key) {
        SharedPreferences sp=mContext.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    //保存数据
    public static void putString(Context mContext, String key, String value) {
        SharedPreferences sp=mContext.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    //保存播放模式
    public static void setPlaymode(Context context, String key, int value) {
        SharedPreferences sp=context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }

    //得到保存的播放模式
    public static int getPlaymode(Context context, String key) {
        SharedPreferences sp=context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getInt(key, MusicPlayerService.REPEATE_NOMAL);
    }
}
