package jp.maeda.shohei.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    Cursor cursor;

    TextView mTimerText;
    Timer mTimer;
    double mTimerSec = 0.0;

    Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button go = (Button) findViewById(R.id.go);
        go.setOnClickListener(this);

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);

        Button playbackstop = (Button) findViewById(R.id.playbackstop);
        playbackstop.setOnClickListener(this);


        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo();
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                }
                break;
            default:
                break;
        }
    }

    private void getContentsInfo() {

        // 画像の情報を取得する
        ContentResolver resolver = getContentResolver();
         cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目(null = 全項目)
                null, // フィルタ条件(null = フィルタなし)
                null, // フィルタ用パラメータ
                null // ソート (null ソートなし)
        );

        if (cursor.moveToFirst()) {

                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);

                Long id = cursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                imageVIew.setImageURI(imageUri);

        }
        //cursor.close();
    }




    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.go){
            if (cursor.moveToNext()) {

                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);

                Long id = cursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                imageVIew.setImageURI(imageUri);

            }else{
                cursor.moveToFirst();
                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);

                Long id = cursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                imageVIew.setImageURI(imageUri);
            }
        }else if(v.getId() == R.id.back){
            if (cursor.moveToPrevious()) {

                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);

                Long id = cursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                imageVIew.setImageURI(imageUri);

            }else{
                cursor.moveToLast();
                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);

                Long id = cursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                imageVIew.setImageURI(imageUri);
            }
        }else if(v.getId() == R.id.playbackstop) {

            Button go = (Button) findViewById(R.id.go);
            go.setEnabled(false);

            Button back = (Button) findViewById(R.id.back);
            back.setEnabled(false);

            Button playbackstop = (Button) findViewById(R.id.playbackstop);
            playbackstop.setText("停止");

            toggleTimer();

            if (v.getId() == R.id.playbackstop) {

                go.setEnabled(true);

                back.setEnabled(true);

                playbackstop.setText("再生");

                mTimer.cancel();

            }

        }
    }

    private void toggleTimer(){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // タイマーの作成
        mTimer = new Timer();
        // タイマーの始動
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mTimerSec += 0.1;

                mHandler.post(new Runnable() {
                    @Override
                    public void{
                        cursor.moveToNext();
                        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);

                        Long id = cursor.getLong(fieldIndex);
                        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                        ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                        imageVIew.setImageURI(imageUri);
                    }
                });
            }
        }, 100, 2000);    // 最初に始動させるまで 100ミリ秒、ループの間隔を 2000ミリ秒 に設定
    }
    }


