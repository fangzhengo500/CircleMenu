package com.loosu.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.loosu.floatingmenu.circlemenu.CircleMenu;
import com.loosu.floatingmenu.circlemenu.CircleMenuConfigurator;
import com.loosu.sample.utils.ViewUtil;

public class CircleMenuBuilderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_menu_builder);
        CircleMenu menu = findViewById(R.id.menu);

        Context context = this;

        int itemSize = ViewUtil.dp2px(context, 50);
        new CircleMenuConfigurator(context)
                .setActionItem(R.layout.action_view, itemSize, itemSize)
                .setAnchor(CircleMenu.Anchor.LEFT_TOP)
                .setAnchorOffsetX(400)
                .setAnchorOffsetY(200)
                .addMenuItem(R.layout.action_view, itemSize, itemSize)
                .addMenuItem(R.layout.action_view, itemSize, itemSize)
                .addMenuItem(R.layout.action_view, itemSize, itemSize)
                .addMenuItem(R.layout.action_view, itemSize, itemSize)
                .addMenuItem(R.layout.action_view, itemSize, itemSize)
                .addMenuItem(R.layout.action_view, itemSize, itemSize)
                .addMenuItem(R.layout.action_view, itemSize, itemSize)
                .addMenuItem(R.layout.action_view, itemSize, itemSize)
                .addMenuItem(R.layout.action_view, itemSize, itemSize)
                .addMenuItem(R.layout.action_view, itemSize, itemSize)
                .applyTo(menu);

    }
}
