package info.doufm.android.Play;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with Android Studio.
 * Date 2014-04-26r
 * 封装播放音乐的功能
 *
 * @author Qichao Chen
 * @version 1.0
 */
public class PlayMusic implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private static final String LOG_TAG = "PlayMusic";

    private MediaPlayer mediaPlay;          //媒体播放器
    private Timer mTimer = new Timer();     //计时器

    private OnPlayListener onPlayListener;  //播放状态监听器
    private ProgressDialog progressDialog;
    private Context context;
    //自定义消息常量
    private static final int STOP = 1;

    //定义Handler对象
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //处理消息
            if (msg.what == STOP) {
                progressDialog.dismiss();
            }
        }
    };

    public PlayMusic(OnPlayListener onPlayListener) {

        this.onPlayListener = onPlayListener;

        try {
            mediaPlay = new MediaPlayer(); //创建媒体播放器
            mediaPlay.setAudioStreamType(AudioManager.STREAM_MUSIC); //设置媒体流类型
            mediaPlay.setOnBufferingUpdateListener(this);
            mediaPlay.setOnCompletionListener(this);
            mediaPlay.setOnPreparedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTimer.schedule(timerTask,0,1000);
    }

    public PlayMusic(Context context,OnPlayListener onPlayListener,ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
        this.context = context;
        this.onPlayListener = onPlayListener;

        try {
            mediaPlay = new MediaPlayer(); //创建媒体播放器
            mediaPlay.setAudioStreamType(AudioManager.STREAM_MUSIC); //设置媒体流类型
            mediaPlay.setOnBufferingUpdateListener(this);
            mediaPlay.setOnCompletionListener(this);
            mediaPlay.setOnPreparedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTimer.schedule(timerTask,0,1000);
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlay == null){
                return;
            }
            if (mediaPlay.isPlaying()){
                //处理播放
            }
        }
    };

    /**
     * 播放在线音乐
     * @param url
     */
    public void PlayOnline(String url){
        progressDialog = ProgressDialog.show(context,"提示","音乐加载中...",true,false);
        try{
            mediaPlay.reset();
            mediaPlay.setDataSource(url); //这种url路径
            mediaPlay.prepareAsync(); //prepare自动播放
            Message msg = new Message();
            msg.what = STOP;
            handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放
     */
    public void play(){
        mediaPlay.start();
    }

    /**
     * 暂停
     */
    public void pause(){
        mediaPlay.pause();
    }

    /**
     * 停止
     */
    public void stop(){
        if (mediaPlay != null){
            mediaPlay.stop();
            mediaPlay.release();
            mediaPlay = null;
        }
    }

    /**
     * 缓冲更新
     * @param mediaPlayer
     * @param i
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        this.onPlayListener.EndOfMusic();
        Log.i(LOG_TAG,"onCompletion");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Log.i(LOG_TAG,"onPrepared");
    }
}
