package wang.mq.mobileplayer1020.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import wang.mq.mobileplayer1020.IMusicPlayerService;
import wang.mq.mobileplayer1020.R;
import wang.mq.mobileplayer1020.bean.MediaItem;
import wang.mq.mobileplayer1020.service.MusicPlayerService;
import wang.mq.mobileplayer1020.utils.LyricParaser;
import wang.mq.mobileplayer1020.utils.Utils;
import wang.mq.mobileplayer1020.view.BaseVisualizerView;
import wang.mq.mobileplayer1020.view.LyricShowView;

public class SystemAudioPlayerActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView ivIcon;
    private TextView tvArtist;
    private TextView tvName;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnSwichLyric;
    private int position;
    private LyricShowView lyric_show_view;
    private BaseVisualizerView baseVisualizerView;

    private MyReceiver receiver;
    /**
     * 进度更新
     */
    private static final int PROGRESS = 1;
    private static final int SHOW_LYRIC = 2;
    private Utils utils;
    private boolean notification;
    private Visualizer mVisualizer;


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-01-11 15:19:08 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_system_audio_player);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTime = (TextView) findViewById(R.id.tv_time);
        seekbarAudio = (SeekBar) findViewById(R.id.seekbar_audio);
        btnAudioPlaymode = (Button) findViewById(R.id.btn_audio_playmode);
        btnAudioPre = (Button) findViewById(R.id.btn_audio_pre);
        btnAudioStartPause = (Button) findViewById(R.id.btn_audio_start_pause);
        btnAudioNext = (Button) findViewById(R.id.btn_audio_next);
        btnSwichLyric = (Button) findViewById(R.id.btn_swich_lyric);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        lyric_show_view = (LyricShowView) findViewById(R.id.lyric_show_view);
        baseVisualizerView = (BaseVisualizerView) findViewById(R.id.baseVisualizerView);


        ivIcon.setBackgroundResource(R.drawable.animation_list);
        AnimationDrawable drawable = (AnimationDrawable) ivIcon.getBackground();
        drawable.start();

        btnAudioPlaymode.setOnClickListener(this);
        btnAudioPre.setOnClickListener(this);
        btnAudioStartPause.setOnClickListener(this);
        btnAudioNext.setOnClickListener(this);
        btnSwichLyric.setOnClickListener(this);

        //设置拖拽监听
        seekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                try {
                    service.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnAudioPlaymode) {
            // Handle clicks for btnAudioPlaymode
            changePlaymode();
        } else if (v == btnAudioPre) {
            // Handle clicks for btnAudioPre
            try {
                service.pre();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (v == btnAudioStartPause) {
            // Handle clicks for btnAudioStartPause

            try {
                if (service.isPlaying()) {
                    //暂停
                    service.pause();
                    //按钮状态-设置播放
                    btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                } else {
                    //播放
                    service.start();
                    //按钮状态-设置暂停
                    btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }


        } else if (v == btnAudioNext) {
            // Handle clicks for btnAudioNext
            try {
                service.next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (v == btnSwichLyric) {
            // Handle clicks for btnSwichLyric
        }
    }

    private void changePlaymode() {
        try {
            int playmode = service.getPlayMode();

            if (playmode == MusicPlayerService.REPEATE_NOMAL) {
                playmode = MusicPlayerService.REPEATE_SINGLE;
            } else if (playmode == MusicPlayerService.REPEATE_SINGLE) {
                playmode = MusicPlayerService.REPEATE_ALL;
            } else if (playmode == MusicPlayerService.REPEATE_ALL) {
                playmode = MusicPlayerService.REPEATE_NOMAL;
            } else {
                playmode = MusicPlayerService.REPEATE_NOMAL;
            }
            //保存到服务中
            service.setPlayMode(playmode);

            checkButtonStatu();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void checkButtonStatu() {
        int playmode = 0;
        try {
            playmode = service.getPlayMode();

            if (playmode == MusicPlayerService.REPEATE_NOMAL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
            } else if (playmode == MusicPlayerService.REPEATE_SINGLE) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
            } else if (playmode == MusicPlayerService.REPEATE_ALL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
            } else {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    private IMusicPlayerService service;

    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 当连接服务成功后回调
         * @param name
         * @param iBdinder
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBdinder) {
            service = IMusicPlayerService.Stub.asInterface(iBdinder);

            if (service != null) {
                //从列表进入
                if (!notification) {
                    try {
                        //开始播放
                        service.openAudio(position);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {

                    //再次显示
                    showViewData(null);

                }

            }
        }

        /**
         * 当断开的时候回调
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_LYRIC://显示歌词
                    try {
                        int currentPosition = service.getCurrentPosition();

                        lyric_show_view.setNextShowLyric(currentPosition);


                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    removeMessages(SHOW_LYRIC);
                    sendEmptyMessage(SHOW_LYRIC);

                    break;
                case PROGRESS:

                    try {
                        int currentPosition = service.getCurrentPosition();
                        tvTime.setText(utils.stringForTime(currentPosition) + "/" + utils.stringForTime(service.getDuration()));


                        //SeekBar进度更新
                        seekbarAudio.setProgress(currentPosition);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, 1000);

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findViews();
        getData();
        //绑定方式启动服务
        startAndBindServide();


    }

    /**
     * 接收广播
     */
    private void initData() {
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.OPEN_COMPLETE);
        registerReceiver(receiver, intentFilter);

        utils = new Utils();

        //1.注册
        EventBus.getDefault().register(this);

    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (MusicPlayerService.OPEN_COMPLETE.equals(intent.getAction())) {

                showViewData(null);
            }

        }
    }

    /**
     * 显示视图的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showViewData(MediaItem mediaItem) {
        setupVisualizerFxAndUi();
        try {
            tvArtist.setText(service.getArtistName());
            tvName.setText(service.getAudioName());

            //得到总时长
            int duration = service.getDuration();
            seekbarAudio.setMax(duration);

            //更新进度
            handler.sendEmptyMessage(PROGRESS);

            checkButtonStatu();

            String path = service.getAudioPath();//mnt/sdcard/audio/beij.mp3

            path = path.substring(0,path.lastIndexOf("."));

            File file = new File(path+".lrc");
            if(!file.exists()){
                file = new File(path+".txt");
            }

            LyricParaser lyricParaser = new LyricParaser();
            try {
                lyricParaser.readFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(lyricParaser.isExistsLyric()){
                lyric_show_view.setLyric(lyricParaser.getLyricBeens());

            }else{
                lyric_show_view.setLyric(lyricParaser.getLyricBeens());
            }//歌词同步
            handler.sendEmptyMessage(SHOW_LYRIC);


        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成一个VisualizerView对象，使音频频谱的波段能够反映到 VisualizerView上
     */
    private void setupVisualizerFxAndUi()
    {

        int audioSessionid = 0;
        try {
            audioSessionid = service.getAudioSessionId();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("audioSessionid=="+audioSessionid);
        mVisualizer = new Visualizer(audioSessionid);
        // 参数内必须是2的位数
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        // 设置允许波形表示，并且捕获它
        baseVisualizerView.setVisualizer(mVisualizer);
        mVisualizer.setEnabled(true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing())
        {
            mVisualizer.release();
        }
    }

    @Override
    protected void onDestroy() {
        if (receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }

        if (conn != null) {
            unbindService(conn);
            conn = null;
        }
        handler.removeCallbacksAndMessages(null);
        //2.取消注册
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void startAndBindServide() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        //启动服务
        startService(intent);//防止服务多次创建
        //绑定服务
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

    }

    private void getData() {
        //true:从状态栏进入
        //false：从ListView中进入
        notification = getIntent().getBooleanExtra("notification", false);

        if (!notification) {
            /**
             * 得到播放位置
             */
            position = getIntent().getIntExtra("position", 0);
        }

    }
}