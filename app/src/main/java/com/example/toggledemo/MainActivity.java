package com.example.toggledemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.toggledemo.ui.ToggleView;

public class MainActivity extends AppCompatActivity {

    ToggleView toggleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleView= findViewById(R.id.toggle_view);
//        toggleView.setSwitchBackgroundResource(R.mipmap.switch_background);
//        toggleView.setSlideButtonResource(R.mipmap.slide_button);
//        toggleView.setSwitchState(false);

        //设置开关更新监听
        toggleView.setOnSwitchStateUpdateListener(new ToggleView.OnSwitchStateUpdateListener() {
            @Override
            public void onStateUpdate(boolean state) {
                Toast.makeText(getApplicationContext(),"state："+state,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
