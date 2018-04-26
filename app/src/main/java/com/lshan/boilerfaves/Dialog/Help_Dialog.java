package com.lshan.boilerfaves.Dialog;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.app.Dialog;
import com.lshan.boilerfaves.R;

public class Help_Dialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Button ok;

    public Help_Dialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.help_dialog);
        ok = (Button) findViewById(R.id.btn_ok);
        ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
