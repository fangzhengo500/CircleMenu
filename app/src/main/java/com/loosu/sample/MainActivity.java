package com.loosu.sample;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
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
    private TextView mTvAnchorOffsetValueX;
    private TextView mTvAnchorOffsetValueY;

    // radius
    private SeekBar mSeekMinRadius;
    private SeekBar mSeekMaxRadius;
    private SeekBar mSeekItemRadius;
    private TextView mTvMenuMinRadiusValue;
    private TextView mTvMenuMaxRadiusValue;
    private TextView mTvItemRadiusValue;

    // angle
    private SeekBar mSeekStartAngle;
    private SeekBar mSeekSweepAngle;
    private TextView mTvStartAngleValue;
    private TextView mTvSweepAngleValue;

    // menu color
    private ImageView mBtnMenuColor;
    private ImageView mBtnMenuShadowColor;
    private SeekBar mSeekMenuShadowRadius;

    private ViewPager mViewPager;
    private TextView mTvMenuShadowRadiusValue;

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
        mTvAnchorOffsetValueX = findViewById(R.id.tv_anchor_offset_x_value);
        mTvAnchorOffsetValueY = findViewById(R.id.tv_anchor_offset_y_value);

        // radius
        mSeekMinRadius = findViewById(R.id.seek_min_radius);
        mSeekMaxRadius = findViewById(R.id.seek_max_radius);
        mSeekItemRadius = findViewById(R.id.seek_item_radius);
        mTvMenuMinRadiusValue = findViewById(R.id.tv_min_menu_radius_value);
        mTvMenuMaxRadiusValue = findViewById(R.id.tv_max_menu_radius_value);
        mTvItemRadiusValue = findViewById(R.id.tv_item_radius_value);

        // angle
        mSeekStartAngle = findViewById(R.id.seek_start_angle);
        mSeekSweepAngle = findViewById(R.id.seek_sweep_angle);
        mTvStartAngleValue = findViewById(R.id.tv_start_angle_value);
        mTvSweepAngleValue = findViewById(R.id.tv_sweep_angle_value);

        // menu color
        mBtnMenuColor = findViewById(R.id.btn_menu_color);
        mBtnMenuShadowColor = findViewById(R.id.btn_menu_shadow_color);
        mSeekMenuShadowRadius = findViewById(R.id.seek_menu_shadow_radius);
        mTvMenuShadowRadiusValue = findViewById(R.id.tv_menu_shadow_radius_value);

        mViewPager = findViewById(R.id.view_pager);
    }

    private void initView(Bundle savedInstanceState) {
        // menu
        LayoutInflater inflater = LayoutInflater.from(this);
        View actionView = inflater.inflate(R.layout.action_view, null, false);
        mMenu.setActionItem(new CircleMenu.Item(actionView, 80, 80));

        List<IMenu.IItem> items = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            TextView item = (TextView) inflater.inflate(R.layout.menu_item, null, false);
            final int finalI = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "click item " + finalI, Toast.LENGTH_SHORT).show();
                    mMenu.close(true);
                }
            });
            item.setText(String.valueOf(i));
            items.add(new CircleMenu.Item(item, 70, 70));
        }
        mMenu.setItems(items);

        mMenu.post(new Runnable() {
            @Override
            public void run() {
                // anchor
                mSeekAnchorOffsetX.setProgress(mMenu.getAnchorOffsetX() + mSeekAnchorOffsetX.getMax() / 2);
                mSeekAnchorOffsetY.setProgress(mMenu.getAnchorOffsetY() + mSeekAnchorOffsetY.getMax() / 2);
                mTvAnchorOffsetValueX.setText(String.valueOf(mMenu.getAnchorOffsetX()));
                mTvAnchorOffsetValueY.setText(String.valueOf(mMenu.getAnchorOffsetY()));

                // radius
                mSeekMinRadius.setProgress((int) mMenu.getMenuRadiusMin());
                mSeekMaxRadius.setProgress((int) mMenu.getMenuRadiusMax());
                mSeekItemRadius.setProgress((int) mMenu.getItemRadius());
                mTvMenuMinRadiusValue.setText(String.valueOf(mMenu.getMenuRadiusMin()));
                mTvMenuMaxRadiusValue.setText(String.valueOf(mMenu.getMenuRadiusMax()));
                mTvItemRadiusValue.setText(String.valueOf(mMenu.getItemRadius()));

                // angle
                mSeekStartAngle.setProgress((int) mMenu.getStartAngle());
                mSeekSweepAngle.setProgress((int) (mMenu.getSweepAngle() + 360f) * mSeekSweepAngle.getMax() / 720);
                mTvStartAngleValue.setText(String.valueOf(mMenu.getStartAngle()));
                mTvSweepAngleValue.setText(String.valueOf(mMenu.getSweepAngle()));

                // menu color
                mBtnMenuColor.setImageDrawable(new ColorDrawable(mMenu.getMenuColor()));
                mBtnMenuShadowColor.setImageDrawable(new ColorDrawable(mMenu.getMenuShadowColor()));
                mTvMenuShadowRadiusValue.setText(String.valueOf(mMenu.getMenuShadowRadius()));
            }
        });

        mViewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
    }

    private void initListener(Bundle savedInstanceState) {
        // menu
        //mMenu.setOnClickListener(this);
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

        // menu color
        mBtnMenuColor.setOnClickListener(this);
        mBtnMenuShadowColor.setOnClickListener(this);
        mSeekMenuShadowRadius.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.btn_menu_color:
                onClickBtnMenuColor(v);
                break;
            case R.id.btn_menu_shadow_color:
                onClickBtnMenuShadowColor(v);
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

    private void onClickBtnMenuColor(View v) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setTitle(getString(R.string.menu_color))
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        mMenu.setMenuColor(selectedColor);
                        mBtnMenuColor.setImageDrawable(new ColorDrawable(selectedColor));
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void onClickBtnMenuShadowColor(View v) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setTitle(getString(R.string.menu_shadow_radius))
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        mMenu.setMenuShadowColor(selectedColor);
                        mBtnMenuShadowColor.setImageDrawable(new ColorDrawable(selectedColor));
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seek_anchor_offset_x:
                mMenu.setAnchorOffsetX(progress - seekBar.getMax() / 2);
                mTvAnchorOffsetValueX.setText(String.valueOf(mMenu.getAnchorOffsetX()));
                break;
            case R.id.seek_anchor_offset_y:
                mMenu.setAnchorOffsetY(progress - seekBar.getMax() / 2);
                mTvAnchorOffsetValueY.setText(String.valueOf(mMenu.getAnchorOffsetY()));
                break;
            case R.id.seek_min_radius:
                mMenu.setMenuRadiusMin(progress);
                mTvMenuMinRadiusValue.setText(String.valueOf(mMenu.getMenuRadiusMin()));
                break;
            case R.id.seek_max_radius:
                mMenu.setMenuRadiusMax(progress);
                mTvMenuMaxRadiusValue.setText(String.valueOf(mMenu.getMenuRadiusMax()));
                break;
            case R.id.seek_item_radius:
                mMenu.setItemRadius(progress);
                mTvItemRadiusValue.setText(String.valueOf(mMenu.getItemRadius()));
                break;
            case R.id.seek_start_angle:
                mMenu.setStartAngle(360f * progress / seekBar.getMax());
                mTvStartAngleValue.setText(String.valueOf(mMenu.getStartAngle()));
                break;
            case R.id.seek_sweep_angle:
                mMenu.setSweepAngle(720f * progress / seekBar.getMax() - 360);
                mTvSweepAngleValue.setText(String.valueOf(mMenu.getSweepAngle()));
                break;
            case R.id.seek_menu_shadow_radius:
                mMenu.setMenuShadowRadius(progress);
                mTvMenuShadowRadiusValue.setText(String.valueOf(mMenu.getMenuShadowRadius()));
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
