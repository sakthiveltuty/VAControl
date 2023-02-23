package com.example.vacontrol;

import static android.app.Service.START_STICKY;

import android.app.Service;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.IBinder;
import android.speech.RecognizerIntent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private TextView textView,textView1;

    String s1;
    Toast toast;
    CameraManager cameraManager;
    String cameraId;
    Boolean state=false;

    @Override
    protected void onResume() {
        super.onResume();
        promptSpeechInput();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView1);
        textView.setVisibility(View.INVISIBLE);
    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something");
        startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textView.setText(result.get(0));
                    s1=result.get(0);


                    if(state==false && s1.equals("light on"))
                    {
                        try {
                            cameraManager=(CameraManager)getSystemService(CAMERA_SERVICE);
                            cameraId=cameraManager.getCameraIdList()[0];
                            cameraManager.setTorchMode(cameraId,!state);
                            state=true;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if(state==true && s1.equals("light off"))
                    {
                        try {
                            cameraManager=(CameraManager)getSystemService(CAMERA_SERVICE);
                            cameraId=cameraManager.getCameraIdList()[0];
                            cameraManager.setTorchMode(cameraId,!state);
                            state=false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if(s1.equals("close")||s1.equals("close the app"))
                    {
                        finish();
                    }
                }
                break;
            }
        }
    }

}
