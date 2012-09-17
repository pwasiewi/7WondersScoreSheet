package com.aceanuu.swss;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.Toast;

public class AceListView extends ListView {

    public AceListView(Context context) {
        super(context);
    }

    public AceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AceListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        Toast.makeText(super.getContext(), "onSizeChanged", Toast.LENGTH_SHORT).show();
//        post(new Runnable() {
//            public void run() {
//                setSelection(2);
//            }
//        });

    }
    
    @Override
    public void onTouchModeChanged(boolean isInTouchMode) {
        Toast.makeText(super.getContext(), "onTouchModeChanged", Toast.LENGTH_SHORT).show();
        // leave it empty here. It looks that when you use hard keyboard,
        // this method will be called and the focus will be token.
    }
}