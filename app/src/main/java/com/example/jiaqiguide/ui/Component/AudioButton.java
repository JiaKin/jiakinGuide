package com.example.jiaqiguide.ui.Component;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;

import com.example.jiaqiguide.Class.DefaultPath;
import com.example.jiaqiguide.R;

import java.io.IOException;

public class AudioButton extends View implements  SeekBar.OnSeekBarChangeListener,Runnable, View.OnClickListener {
    MediaPlayer mp;
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    final DefaultPath.FileType fileType = DefaultPath.FileType.Audio;
    String AudioName = "unknown music";
    LinearLayout AudioDisplay;
    final Handler upDateLoop = new Handler(Looper.getMainLooper());

    SeekBar seekBar;
    Button playButton;
    TextView textView;

    public AudioButton(Context context) {
        super(context);
        playButton = new Button(context);
        playButton.setLayoutParams(new LinearLayout.LayoutParams(250,250));
        playButton.setTextColor(Color.parseColor("#F39921"));
        playButton.setText("Play");
        playButton.setOnClickListener(this);
        seekBar = new SeekBar(context);
        seekBar.setMax(100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(1);
        }
        seekBar.setOnSeekBarChangeListener(this);


        textView = new TextView(context);
        textView.setText(FormatString(getAudioInfo()));
        LinearLayout.LayoutParams tl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        tl.setMargins(40,40,40,40);
        textView.setLayoutParams(tl);

        AudioDisplay = new LinearLayout(context);
        AudioDisplay.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        AudioDisplay.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout SundPrograssBar= new LinearLayout(context);
        SundPrograssBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        SundPrograssBar.setOrientation(LinearLayout.VERTICAL);
        AudioDisplay.addView(playButton);

        SundPrograssBar.addView(textView);
        SundPrograssBar.addView(seekBar);

        AudioDisplay.addView(SundPrograssBar);

        setAudio("default.mp3");


        setClickable(true);
        this.setLayoutParams(new LinearLayout.LayoutParams(1000,200));
        this.setClickable(true);
    }
    public String FormatString(String vd){
        String mess = vd;
        mess = mess.length()>25?mess.substring(0,24):mess;
        return String.format("%-30s   %s/%s",mess,getAudioCurrentPlayTime(),getAudioPlayTime());
    }
    public String getAudioPlayTime(){
        try {
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration = Long.parseLong(durationStr); // 媒体文件总时长，毫秒为单位
            return String.format("%02d:%02d:%02d",(duration/1000/3600)%24,((duration/1000)/60)%60,((duration/1000)%60));
        }catch (Exception e){
            return "00:00:00";
        }


    }
    public String getAudioCurrentPlayTime(){
        try {
            int currentDuration = mp.getCurrentPosition();
            return String.format("%02d:%02d:%02d",(currentDuration/1000/3600)%24,((currentDuration/1000/60)%60),((currentDuration/1000)%60));
        }catch (Exception e){
            return "00:00:00";
        }
    }
    public View getView(){
        return AudioDisplay;
    }
    public void setAudio(String Name){
        String path = DefaultPath.getLegalFilePath(fileType,Name);
        if(mp!=null)mp.release();
        mp = new MediaPlayer();
        try{
            mp.setDataSource(path);
            mp.prepare();
            mmr.setDataSource(path);
            mp.prepare();
            AudioName = Name;
        } catch (IOException e) {
            mp = MediaPlayer.create(this.getContext(),R.raw.music);
            AudioName = "music.mp3";
            Uri uri = new Uri.Builder()
                    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                    .authority(this.getContext().getPackageName())
                    .appendPath("raw")
                    .appendPath(this.getContext().getResources().getResourceEntryName(R.raw.music).toLowerCase())
                    .build();
            mmr.setDataSource(this.getContext(),uri);
            //mp.prepare();
            Toast.makeText(getContext(), "Error: Loading Audio File Failed."+mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION), Toast.LENGTH_SHORT).show();
        }
    }
    public String getAudioInfo(){
        String Infor = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        if(Infor==null)
            return AudioName;
        return Infor;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    public void updateSeekBar(double currentPosition,double Max){
        seekBar.setProgress((int)(currentPosition/Max*seekBar.getMax()));
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        try{
            if(seekBar.getProgress()<1)seekBar.setProgress(1);
            double duration = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            double position = (double)seekBar.getProgress()/(double)seekBar.getMax()*duration;
            mp.seekTo((int)position);
            play();
        }catch (Exception e){
            Toast.makeText(this.getContext(),"No audio source found.",Toast.LENGTH_SHORT).show();
            seekBar.setProgress(0);
        }
    }
    @Override
    public void run() {
        if (mp != null && mp.isPlaying()) {
            updateSeekBar(mp.getCurrentPosition(),(int)Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
            textView.setText(FormatString(getAudioInfo()));
            upDateLoop.postDelayed(this, 100); // 每秒更新一次
        }
    }
    public void play(){
        mp.start();
        upDateLoop.postDelayed(this, 100);
    }
    @Override
    public void onClick(View v) {
        if(mp.isPlaying()){
            mp.pause();
            playButton.setTextSize(15);
            playButton.setText("Play");
        }
        else{
            play();
            playButton.setTextSize(25);
            playButton.setText("| |");

        }
    }
}
