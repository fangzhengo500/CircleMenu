package com.loosu.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loosu.floatingmenu.circlemenu.BaseItem;
import com.loosu.floatingmenu.circlemenu.CircleMenu;
import com.loosu.floatingmenu.IMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, IMenu.OnStateChangeListener {
    private static final String TAG = "MainActivity";

    // menu
    private CircleMenu mMenu;

    // anchor
    private List<RadioButton> mAnchorRadioBtn = new ArrayList<>();
    private RadioButton mRbAnchorCenter;
    private RadioButton mRbAnchorCenterLeft;
    private RadioButton mRbAnchorCenterTop;
    private RadioButton mRbAnchorCenterRight;
    private RadioButton mRbAnchorCenterBottom;
    private RadioButton mRbAnchorLeftTop;
    private RadioButton mRbAnchorLeftBottom;
    private RadioButton mRbAnchorRightTop;
    private RadioButton mRbAnchorRightBottom;
    private SeekBar mSeekAnchorOffsetX;
    private SeekBar mSeekAnchorOffsetY;

    // radius
    private SeekBar mSeekMinRadius;
    private SeekBar mSeekMaxRadius;
    private SeekBar mSeekItemRadius;

    // angle
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
        // menu
        mMenu = findViewById(R.id.menu);

        // anchor
        mRbAnchorCenter = findViewById(R.id.rb_anchor_center);
        mRbAnchorCenterLeft = findViewById(R.id.rb_anchor_center_left);
        mRbAnchorCenterTop = findViewById(R.id.rb_anchor_center_top);
        mRbAnchorCenterRight = findViewById(R.id.rb_anchor_center_right);
        mRbAnchorCenterBottom = findViewById(R.id.rb_anchor_center_bottom);
        mRbAnchorLeftTop = findViewById(R.id.rb_anchor_left_top);
        mRbAnchorLeftBottom = findViewById(R.id.rb_anchor_left_bottom);
        mRbAnchorRightTop = findViewById(R.id.rb_anchor_right_top);
        mRbAnchorRightBottom = findViewById(R.id.rb_anchor_right_bottom);

        mAnchorRadioBtn.add(mRbAnchorCenter);
        mAnchorRadioBtn.add(mRbAnchorCenterLeft);
        mAnchorRadioBtn.add(mRbAnchorCenterTop);
        mAnchorRadioBtn.add(mRbAnchorCenterRight);
        mAnchorRadioBtn.add(mRbAnchorCenterBottom);
        mAnchorRadioBtn.add(mRbAnchorLeftTop);
        mAnchorRadioBtn.add(mRbAnchorLeftBottom);
        mAnchorRadioBtn.add(mRbAnchorRightTop);
        mAnchorRadioBtn.add(mRbAnchorRightBottom);

        mSeekAnchorOffsetX = findViewById(R.id.seek_anchor_offset_x);
        mSeekAnchorOffsetY = findViewById(R.id.seek_anchor_offset_y);

        // radius
        mSeekMinRadius = findViewById(R.id.seek_min_radius);
        mSeekMaxRadius = findViewById(R.id.seek_max_radius);
        mSeekItemRadius = findViewById(R.id.seek_item_radius);

        // angle
        mSeekStartAngle = findViewById(R.id.seek_start_angle);
        mSeekSweepAngle = findViewById(R.id.seek_sweep_angle);
    }

    private void initView(Bundle savedInstanceState) {
        // menu
        LayoutInflater inflater = LayoutInflater.from(this);
        View actionView = inflater.inflate(R.layout.action_view, null, false);
        mMenu.setActionItem(new BaseItem(actionView, 80, 80));

        TextView item1 = (TextView) inflater.inflate(R.layout.menu_item, null, false);
        TextView item2 = (TextView) inflater.inflate(R.layout.menu_item, null, false);
        TextView item3 = (TextView) inflater.inflate(R.layout.menu_item, null, false);
        TextView item4 = (TextView) inflater.inflate(R.layout.menu_item, null, false);

        item1.setText(String.valueOf(1));
        item2.setText(String.valueOf(2));
        item3.setText(String.valueOf(3));
        item4.setText(String.valueOf(4));

        List<IMenu.IItem> items = new ArrayList<>();
        items.add(new BaseItem(item1, 20, 20));
        items.add(new BaseItem(item2, 40, 40));
        items.add(new BaseItem(item3, 80, 80));
        items.add(new BaseItem(item4, 100, 100));
        mMenu.setItems(items);

        // anchor
        mSeekAnchorOffsetX.setProgress(mMenu.getAnchorOffsetX() + mSeekAnchorOffsetX.getMax() / 2);
        mSeekAnchorOffsetY.setProgress(mMenu.getAnchorOffsetY() + mSeekAnchorOffsetY.getMax() / 2);

        // radius
        mSeekMinRadius.setProgress((int) mMenu.getMenuRadiusMin());
        mSeekMaxRadius.setProgress((int) mMenu.getMenuRadiusMax());

        // angle
        mSeekStartAngle.setProgress((int) mMenu.getStartAngle());
        mSeekSweepAngle.setProgress((int) (mMenu.getSweepAngle() + 360f) * mSeekSweepAngle.getMax() / 720);
    }

    private void initListener(Bundle savedInstanceState) {
        // menu
        mMenu.setOnClickListener(this);
        mMenu.setStateChangeListener(this);

        // anchor
        for (RadioButton radioButton : mAnchorRadioBtn) {
            radioButton.setOnClickListener(this);
        }
        mSeekAnchorOffsetX.setOnSeekBarChangeListener(this);
        mSeekAnchorOffsetY.setOnSeekBarChangeListener(this);

        // radius
        mSeekMinRadius.setOnSeekBarChangeListener(this);
        mSeekMaxRadius.setOnSeekBarChangeListener(this);
        mSeekItemRadius.setOnSeekBarChangeListener(this);

        // angle
        mSeekStartAngle.setOnSeekBarChangeListener(this);
        mSeekSweepAngle.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                switch (mMenu.getState()) {
                    case OPENED:
                        mMenu.close(true);
                        break;
                    case CLOSED:
                        mMenu.open(true);
                        break;
                }
                break;
            case R.id.rb_anchor_center:
            case R.id.rb_anchor_center_left:
            case R.id.rb_anchor_center_top:
            case R.id.rb_anchor_center_right:
            case R.id.rb_anchor_center_bottom:
            case R.id.rb_anchor_left_top:
            case R.id.rb_anchor_left_bottom:
            case R.id.rb_anchor_right_top:
            case R.id.rb_anchor_right_bottom:
                onClickAnchorBtn(v);
                break;
        }

    }

    private void onClickAnchorBtn(View v) {
        switch (v.getId()) {
            case R.id.rb_anchor_center:
                mMenu.setAnchor(CircleMenu.Anchor.CENTER);
                break;
            case R.id.rb_anchor_center_left:
                mMenu.setAnchor(CircleMenu.Anchor.CENTER_LEFT);
                break;
            case R.id.rb_anchor_center_top:
                mMenu.setAnchor(CircleMenu.Anchor.CENTER_TOP);
                break;
            case R.id.rb_anchor_center_right:
                mMenu.setAnchor(CircleMenu.Anchor.CENTER_RIGHT);
                break;
            case R.id.rb_anchor_center_bottom:
                mMenu.setAnchor(CircleMenu.Anchor.CENTER_BOTTOM);
                break;
            case R.id.rb_anchor_left_top:
                mMenu.setAnchor(CircleMenu.Anchor.LEFT_TOP);
                break;
            case R.id.rb_anchor_left_bottom:
                mMenu.setAnchor(CircleMenu.Anchor.LEFT_BOTTOM);
                break;
            case R.id.rb_anchor_right_top:
                mMenu.setAnchor(CircleMenu.Anchor.RIGHT_TOP);
                break;
            case R.id.rb_anchor_right_bottom:
                mMenu.setAnchor(CircleMenu.Anchor.RIGHT_BOTTOM);
                break;
        }

        for (RadioButton radioButton : mAnchorRadioBtn) {
            radioButton.setChecked(false);
        }
        if (v instanceof CompoundButton) {
            ((CompoundButton) v).setChecked(true);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seek_anchor_offset_x:
                mMenu.setAnchorOffsetX(progress - seekBar.getMax() / 2);
                break;
            case R.id.seek_anchor_offset_y:
                mMenu.setAnchorOffsetY(progress - seekBar.getMax() / 2);
                break;
            case R.id.seek_min_radius:
                mMenu.setMenuRadiusMin(progress);
                break;
            case R.id.seek_max_radius:
                mMenu.setMenuRadiusMax(progress);
                break;
            case R.id.seek_item_radius:
                mMenu.setItemRadius(progress);
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

    @Override
    public void onMenuStateChange(IMenu menu, IMenu.State state) {
        Log.i(TAG, "onMenuStateChange: " + state.toString());
        Toast.makeText(this, "onMenuStateChange: " + state.toString(), Toast.LENGTH_SHORT).show();
    }

}
