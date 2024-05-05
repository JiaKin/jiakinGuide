package com.example.jiaqiguide.ui.Component;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.jiaqiguide.Class.EditObject;
import com.example.jiaqiguide.Class.ZoneManager;
import com.example.jiaqiguide.R;

public class TextBox extends androidx.appcompat.widget.AppCompatEditText implements EditObject {
    FrameLayout fl;
    public TextBox(@NonNull Context context) {
        super(context);
        fl = new FrameLayout(context);
        fl.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.TOP | Gravity.START;
        lp.topMargin=280;
        lp.leftMargin =20;
        lp.rightMargin = 30;
        setPadding(10,10,10,10);
        setLayoutParams(lp);
        setHint("Insert Zone Name");
        setBackground(ContextCompat.getDrawable(this.getContext(), R.drawable.textboxbackground));
        fl.addView(this);
        fl.setVisibility(INVISIBLE);

    }
    public View getView(){
        return fl;
    }
    public String getEditText(){
        return this.getText().toString();
    }
    @Override
    public void TurnOnEditMode(Object pbj) {
        fl.setVisibility(VISIBLE);
    }

    @Override
    public void TurnOffEditMode(Object obj) {
        if(this.getText().toString()==""||this.getText().toString()==null){
            Toast.makeText(getContext(), "Input Zone Name Is Empty.", Toast.LENGTH_SHORT).show();
        }else{
            ((ZoneManager)obj).setTempName(this.getText().toString());
            this.setText("");
        }
        fl.setVisibility(INVISIBLE);
    }

}
