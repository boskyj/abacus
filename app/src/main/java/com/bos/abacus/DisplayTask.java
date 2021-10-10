package com.bos.abacus;

import android.widget.TextView;

public class DisplayTask implements  Runnable{
    private  String val;

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    private TextView textView;

    public void setVal(String val) {
        this.val = val;
    }


    @Override
    public void run() {
        textView.setText(val);
    }
}
