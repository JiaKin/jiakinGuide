package com.example.jiaqiguide.ui.Component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiaqiguide.Class.EditObject;
import com.example.jiaqiguide.Class.ZoneManager;
import com.example.jiaqiguide.R;

public class EditTextButton extends androidx.appcompat.widget.AppCompatButton implements EditObject {

    ZoneManager zoneManager;
    public EditTextButton(Context context, Activity activity) {
        super(context);
        this.setVisibility(Button.INVISIBLE);
        FrameLayout.LayoutParams lp =   new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM | Gravity.START;
        lp.bottomMargin = 250;
        lp.leftMargin = 10;
        this.setLayoutParams(lp);
        this.setText("DESC");
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.text_edit_pop_window, null);
                PopupWindow popupWindow = new PopupWindow(popupView,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 必须设置背景来响应点击事件
// 显示在视图正中心
                View rootView = activity.getWindow().getDecorView().getRootView(); // 获取根视图
// 其他事件
                String txt =zoneManager.getDescription();
                if(txt!=null){
                    ((EditText)popupView.findViewById(R.id.edit_text_input)).setText(txt.toString());
                }
                popupWindow.showAtLocation(rootView, Gravity.CENTER,0,0);

                popupView.findViewById(R.id.button_zoneText_submit).setOnClickListener(View->{
                    if(zoneManager!=null){
                        zoneManager.setTempDescription( ((EditText)popupView.findViewById(R.id.edit_text_input)).getText().toString());
                        popupWindow.dismiss();
                    }else{
                        Toast.makeText(context,"区域还未创建 请重新选择。",Toast.LENGTH_SHORT).show();
                    }
                });
                popupView.findViewById(R.id.button_zoneText_exit).setOnClickListener(View->{
                    ((EditText)popupView.findViewById(R.id.edit_text_input)).setText("");
                    popupWindow.dismiss();
                });
            }
        });
    }

    @Override
    public void TurnOnEditMode(Object ojb) {
        zoneManager = (ZoneManager)ojb;
        this.setVisibility(Button.VISIBLE);
    }

    @Override
    public void TurnOffEditMode(Object ojb) {
        zoneManager = null;
        this.setVisibility(Button.INVISIBLE);
    }
}
