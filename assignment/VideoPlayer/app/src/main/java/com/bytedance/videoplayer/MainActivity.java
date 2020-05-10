package com.bytedance.videoplayer;


import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;


public class MainActivity extends AppCompatActivity {
    private Button buttonPlay;
    private Button buttonPause;
    private VideoView videoView;
    private SeekBar seekBar;
    private TextView video_now_time,video_total_time;
    public  static final int UPDATE_UI = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
            buttonPause = findViewById(R.id.buttonPause);
            findViewById(R.id.buttonPause).setVisibility(View.VISIBLE);
            buttonPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoView.pause();
                }
            });

            buttonPlay = findViewById(R.id.buttonPlay);
            findViewById(R.id.buttonPlay).setVisibility(View.VISIBLE);
            buttonPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoView.start();
                }
            });

            video_now_time = findViewById(R.id.video_now_time);
            findViewById(R.id.video_now_time).setVisibility(View.VISIBLE);

            video_total_time = findViewById(R.id.video_total_time);
            findViewById(R.id.video_total_time).setVisibility(View.VISIBLE);

            seekBar = findViewById(R.id.seekbar);
            findViewById(R.id.seekbar).setVisibility(View.VISIBLE);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int process = seekBar.getProgress();
                    if(videoView !=null&& videoView.isPlaying()){
                        videoView.seekTo(process);

                    }

                }
            });

            videoView = findViewById(R.id.videoView);
            findViewById(R.id.videoView).setVisibility(View.VISIBLE);
            videoView.setVideoPath(getVideoPath(R.raw.bytedance));
        }

        else if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){
            getSupportActionBar().hide();//去除标题栏
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;//去除状态栏

            buttonPause = findViewById(R.id.buttonPause);
            findViewById(R.id.buttonPause).setVisibility(View.GONE);
            buttonPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoView.pause();
                }
            });

            buttonPlay = findViewById(R.id.buttonPlay);
            findViewById(R.id.buttonPlay).setVisibility(View.GONE);
            buttonPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoView.start();
                }
            });

            video_now_time = findViewById(R.id.video_now_time);
            findViewById(R.id.video_now_time).setVisibility(View.VISIBLE);

            video_total_time = findViewById(R.id.video_total_time);
            findViewById(R.id.video_total_time).setVisibility(View.VISIBLE);

            seekBar = findViewById(R.id.seekbar);
            findViewById(R.id.seekbar).setVisibility(View.VISIBLE);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int process = seekBar.getProgress();
                    if(videoView !=null&& videoView.isPlaying()){
                        videoView.seekTo(process);

                    }

                }
            });

            videoView = findViewById(R.id.videoView);
            findViewById(R.id.videoView).setVisibility(View.VISIBLE);
            videoView.setVideoPath(getVideoPath(R.raw.bytedance));

            videoView.setSystemUiVisibility(option);

        }




        if (savedInstanceState != null) {
            // 得到进度
            int ss = savedInstanceState.getInt("aa");
            // 接着播放


                videoView.seekTo(ss);
                videoView.start();

        }


        UIhandle.sendEmptyMessage(UPDATE_UI);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        // 记录当前播放进度
            outState.putInt("aa", videoView.getCurrentPosition());

    }


    public void onConfigurationChanged(Configuration newConfig)
    {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            videoView.setLayoutParams(layoutParams);
        }
        super.onConfigurationChanged(newConfig);
    }











    private void updateTime(TextView textView,int millisecond){
        int second = millisecond/1000;
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;

        String str = null;
        if(hh!=0){
            str = String.format("%02d:%02d:%02d",hh,mm,ss);
        }else {
            str = String.format("%02d:%02d",mm,ss);
        }

        textView.setText(str);

    }

    private Handler UIhandle = new Handler(){

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what==UPDATE_UI) {
                int position = videoView.getCurrentPosition();
                int totalduration = videoView.getDuration();

                seekBar.setMax(totalduration);
                seekBar.setProgress(position);

                updateTime(video_now_time,position);
                updateTime(video_total_time,totalduration);

                UIhandle.sendEmptyMessageDelayed(UPDATE_UI, 500);
            }
        }
    };




    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }
}
