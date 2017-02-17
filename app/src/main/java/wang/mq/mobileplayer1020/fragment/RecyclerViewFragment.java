package wang.mq.mobileplayer1020.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wang.mq.mobileplayer1020.R;
import wang.mq.mobileplayer1020.adapter.RecyclerViewAdapter;
import wang.mq.mobileplayer1020.base.BaseFragment;
import wang.mq.mobileplayer1020.bean.NetAudioBean;
import wang.mq.mobileplayer1020.utils.CacheUtils;
import wang.mq.mobileplayer1020.utils.Constant;

/**
 * Created by wangmingqiang on 2017/1/8.
 */

public class RecyclerViewFragment extends BaseFragment {


    private static final String TAG = RecyclerViewFragment.class.getSimpleName();
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.tv_nomedia)
    TextView tvNomedia;
    private RecyclerViewAdapter myAdapter;
    private List<NetAudioBean.ListBean> datas;


    @Override
    public View initView() {
        Log.e(TAG, "网络音频UI被初始化了");
        View view = View.inflate(mContext, R.layout.fragment_recyclerview, null);
        ButterKnife.bind(this, view);

        //设置点击事件
//        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                NetAudioBean.ListBean listEntity = datas.get(position);
//                if(listEntity !=null ){
//                    //3.传递视频列表
//                    Intent intent = new Intent(mContext,ShowImageAndGifActivity.class);
//                    if(listEntity.getType().equals("gif")){
//                        String url = listEntity.getGif().getImages().get(0);
//                        intent.putExtra("url",url);
//                        mContext.startActivity(intent);
//                    }else if(listEntity.getType().equals("image")){
//                        String url = listEntity.getImage().getBig().get(0);
//                        intent.putExtra("url",url);
//                        mContext.startActivity(intent);
//                    }
//                }
//
//
//            }
//        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG", "网络视频数据初始化了...");

        String saveJson = CacheUtils.getString(mContext, Constant.NET_AUDIO_URL);
        if(!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }

        getDataFromNet();
    }



    private void getDataFromNet() {
        RequestParams reques = new RequestParams(Constant.NET_AUDIO_URL);
        x.http().get(reques, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                CacheUtils.putString(mContext,Constant.NET_AUDIO_URL,result);
                LogUtil.e("onSuccess==" + result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });

    }

    private void processData(String json) {
        NetAudioBean netAudioBean=paraseJons(json);
        //LogUtil.e(netAudioBean.getList().get(0).getText()+"--------------------------------------");

        datas = netAudioBean.getList();
        if(datas != null && datas.size() >0){
            //有视频
            tvNomedia.setVisibility(View.GONE);
            //设置适配器
            myAdapter = new RecyclerViewAdapter(mContext,datas);
            recyclerview.setAdapter(myAdapter);

            //设置布局管理器
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        }else{
            //没有视频
            tvNomedia.setVisibility(View.VISIBLE);
        }

        progressbar.setVisibility(View.GONE);

    }





    //json解析数据
    private NetAudioBean paraseJons(String json) {
        NetAudioBean netAudioBean = new Gson().fromJson(json,NetAudioBean.class);
        return netAudioBean;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
