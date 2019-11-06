package com.vfaraday.stepapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

import com.vfaraday.stepapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding layout;
    private int step = 0;
    private List<String> mIntegerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mIntegerList = new ArrayList<>();
        layout.step.setImageAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.altha));
        for (int i = 0; i < 6; i++) {
            mIntegerList.add("Step " + (i + 1));
        }

        layout.step.setProgressText(mIntegerList);

        layout.btnNext.setOnClickListener(view -> {
            step++;
            if (step <= 6) {
                layout.step.setProgress(step);
            }
        });

        layout.btnBack.setOnClickListener(view -> {
            step--;
            layout.step.setProgress(step);
        });

        layout.btnClear.setOnClickListener(view -> {
            step = 0;
            layout.step.setProgress(0);
        });
    }
}
