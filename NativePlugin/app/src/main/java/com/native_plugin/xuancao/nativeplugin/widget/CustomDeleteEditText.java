package com.native_plugin.xuancao.nativeplugin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.native_plugin.xuancao.nativeplugin.R;


public class CustomDeleteEditText extends android.support.v7.widget.AppCompatEditText {

    private Drawable mDeleteImage;// 删除的按钮
    private int deleteIcon;
    public CustomDeleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomDeleteEditText, 0, 0);
        deleteIcon = a.getResourceId(R.styleable.CustomDeleteEditText_deleteIcon, 0);
        a.recycle();
        init();
    }

    public CustomDeleteEditText(Context context) {
        this(context, null);
    }

    public CustomDeleteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomDeleteEditText, defStyle, 0);
        deleteIcon = a.getResourceId(R.styleable.CustomDeleteEditText_deleteIcon, 0);
        a.recycle();
        init();
    }

    private void init() {
        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(deleteIcon>0){
                    mDeleteImage = TextUtils.isEmpty(s) ? null : getContext().getResources().getDrawable(deleteIcon);
                    setCompoundDrawablesWithIntrinsicBounds(null, null, mDeleteImage, null);//添加drawable ， position = right
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mDeleteImage != null && !TextUtils.isEmpty(getText())) {//如果删除图片显示，并且输入框有内容
                    if (event.getX() > (getWidth() - getTotalPaddingRight()) && event.getX() < (getWidth() - getPaddingRight()))
                        //只有在这区域能触发清除内容的效果
                        getText().clear();
                }
                break;

        }


        return super.onTouchEvent(event);
    }



}