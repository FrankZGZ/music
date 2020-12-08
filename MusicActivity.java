package com.example.musicplayer;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MusicActivity extends AppCompatActivity {
    ArrayList<String> songpathList = new ArrayList<String>();
    String path;
    String songname;
    String presongPath;
    String nextsongPath;
    int songsize;
//    String path1;
    MediaPlayer mediaPlayer = new MediaPlayer();
    private static final String TAG = "111";

    //进度条
    private boolean seekbarchange = false;
    private SeekBar seekBar  ;
    private  String currenttime;
    private Timer timer = new Timer();;
    private TextView time;
//    private String parseTime(int oldTime) {
//        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");// 时间格式
//        String newTime = sdf.format(new Date(oldTime));
//        return newTime;
//    }



        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        Serializable s = bundle.getSerializable("songpathList");
        songpathList = (ArrayList<String>) s;
        songsize = intent.getIntExtra("songsize",songsize);
        path = intent.getStringExtra("path");
        presongPath = intent.getStringExtra("presongpath");
        nextsongPath = intent.getStringExtra("nextsongpath");
        songname = intent.getStringExtra("songName");
        TextView text = (TextView)findViewById(R.id.name);
        text.setText(songname);
//        System.out.print(path);

        Button startbutton = (Button) findViewById(R.id.start);
        Button stopbutton = (Button) findViewById(R.id.stop);
        final Button pausebutton = (Button) findViewById(R.id.pause);
        Button prebutton = (Button) findViewById(R.id.pre);
        Button nextbutton = (Button) findViewById(R.id.next);
//         time = (TextView) findViewById(R.id.time);
//        time.setText(parseTime(mediaPlayer.getDuration()));
        Log.v("MusicService", "00000");
        seekBar=(SeekBar)findViewById(R.id.bar);
        seekBar.setOnSeekBarChangeListener(new MySeekBar());
        //播放
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(!mediaPlayer.isPlaying())
//                {
//                    mediaPlayer.start();
//
//
//                }else {
//                    mediaPlayer.start();
//
//                }
                play(path);
//                if(mediaPlayer.seekTo(mediaPlayer.getCurrentPosition())== mediaPlayer.getDuration())
//                {
//                    play(nextsongPath);
//                }


            }
        });

        //停止
        stopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
//                mediaPlayer.stop();
//                seekBar.setProgress(0);
//                mediaPlayer.seekTo(mediaPlayer.getDuration());
//                seekBar.setProgress(0);
            }
        });

//            timer2 = new Timer();
//            timer2.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    if(! seekbarchange){
////                        Log.d(TAG, "run: "+mediaPlayer.getCurrentPosition());
//                        seekBar.setProgress(mediaPlayer.getDuration());
////                        time = (TextView) findViewById(R.id.time);
////                        time.setText(mediaPlayer.getDuration()+"");
//
//                    }
//
//                }
//
//            },0,50);

        //暂停
        pausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            }
        });


        //播放下一曲
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("MusicActivite", "nextSong");
                play(nextsongPath);

            }
        });

        //播放上一曲
        prebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(presongPath);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music, menu);
        return true;
    }

    //单曲循环
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.loop:
                boolean loop1 = mediaPlayer.isLooping();
                if(loop1){
                    mediaPlayer.setLooping(!loop1);
                }else{
                    mediaPlayer.setLooping(!loop1);
                }
                break;
            case R.id.loop_item:
                int i;
//                Iterator it = songpathList.iterator();
                for( i =1;i<songsize;i++){
                    final String p = songpathList.get(i);
//                    path = nextsongPath;
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPl) {
                            play(p);

                        }
                    });

                }


//                   int i=1;
//                   String path1 = path + i;
//                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mediaPl) {
//                            play(path1);
//
//                        }
//                    });

                break;
                default:

        }
        return true;
    }

    //播放
    public void play(final String path) {
        try {
            mediaPlayer.reset();
//                String datapath = arrayList.get(songid);
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaP) {
                    mediaP.start();
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                    seekBar.setMax(mediaPlayer.getDuration());
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPl) {
                            play(nextsongPath);

                        }
                    });

                }
            });
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(! seekbarchange){
                        Log.d(TAG, "run: "+mediaPlayer.getCurrentPosition());
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
//                        time = (TextView) findViewById(R.id.time);
//                        time.setText(mediaPlayer.getDuration()+"");

                }

                }

            },0,50);


        } catch (Exception e) {
//            Log.v("MusicService", e.getMessage());
        }

    }


    protected void onDestroy() {
        mediaPlayer.release();
        timer.cancel();
        timer = null;
        mediaPlayer = null;
        super.onDestroy();
    }



    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            seekbarchange = false;
           // mediaPlayer.seekTo((int) (seekBar.getProgress()));
        }

        //滚动时,应当暂停后台定时
        public void onStartTrackingTouch(SeekBar seekBar) {
            seekbarchange = true;
            mediaPlayer.seekTo((int) (seekBar.getProgress()));
        }

        //滑动结束后，重新设置值
        public void onStopTrackingTouch(SeekBar seekBar) {
            seekbarchange = false;
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }

}

