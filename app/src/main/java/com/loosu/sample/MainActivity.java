package com.loosu.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.loosu.floatingmenu.CircleMenu;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private CircleMenu mMenu;
    private SeekBar mSeekMinRadius;
    private SeekBar mSeekMaxRadius;
    private SeekBar mSeekStartAngle;
    private SeekBar mSeekSweepAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView(savedInstanceState);
        initView(savedInstanceState);
        initListener(savedInstanceState);

    }


    private void findView(Bundle savedInstanceState) {
        mMenu = findViewById(R.id.menu);
        mSeekMinRadius = findViewById(R.id.seek_min_radius);
        mSeekMaxRadius = findViewById(R.id.seek_max_radius);

        mSeekStartAngle = findViewById(R.id.seek_start_angle);
        mSeekSweepAngle = findViewById(R.id.seek_sweep_angle);
    }

    private void initView(Bundle savedInstanceState) {
        mSeekMinRadius.setProgress((int) mMenu.getRadiusMin());
        mSeekMaxRadius.setProgress((int) mMenu.getRadiusMax());

        mSeekStartAngle.setProgress((int) mMenu.getStartAngle());
        mSeekSweepAngle.setProgress((int) (mMenu.getSweepAngle() + 360f) * mSeekSweepAngle.getMax() / 720);
    }

    private void initListener(Bundle savedInstanceState) {
        mMenu.setOnClickListener(this);
        mSeekMinRadius.setOnSeekBarChangeListener(this);
        mSeekMaxRadius.setOnSeekBarChangeListener(this);

        mSeekStartAngle.setOnSeekBarChangeListener(this);
        mSeekSweepAngle.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mMenu.isOpen()) {
            mMenu.close(true);
        } else {
            mMenu.open(true);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seek_min_radius:
                mMenu.setRadiusMin(progress);
                break;
            case R.id.seek_max_radius:
                mMenu.setRadiusMax(progress);
                break;
            case R.id.seek_start_angle:
                mMenu.setStartAngle(360f * progress / seekBar.getMax());
                break;
            case R.id.seek_sweep_angle:
                mMenu.setSweepAngle(720f * progress / seekBar.getMax() - 360);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
