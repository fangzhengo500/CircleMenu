package com.loosu.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestFragment extends Fragment {
    private static final String KEY_TEXT = "key_text";
    private static final String KEY_COLOR = "key_color";

    public static TestFragment createInstance(String text, int color) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TEXT, text);
        bundle.putInt(KEY_COLOR, color);

        TestFragment fragment = new TestFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();

        if (arguments != null) {
            TextView tvText = view.findViewById(R.id.tv_text);
            tvText.setText(arguments.getString(KEY_TEXT));
            tvText.setBackgroundColor(arguments.getInt(KEY_COLOR));
        }

    }
}
