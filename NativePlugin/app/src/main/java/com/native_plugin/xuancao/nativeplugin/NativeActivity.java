package com.native_plugin.xuancao.nativeplugin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NativeActivity extends AppCompatActivity {

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        tv_title = (TextView) findViewById(R.id.tv_title);

        CharSequence charSequence =  getIntent().getCharSequenceExtra("name");
        if (charSequence !=null){
            tv_title.setText(charSequence);
        }

    }
}
