package com.eather.wallet;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.eather.wallet.managers.BalanceManager;

public class MainActivity extends AppCompatActivity {

    private ImageButton current_filtered_btn = null;
    private TextView current_filtered_text = null;
    public BalanceManager balanceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView preloader = this.findViewById(R.id.preloader_image);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(4000);
        rotate.setRepeatCount(Animation.INFINITE);
        preloader.startAnimation(rotate);

        Window window = this.getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        // In Activity's onCreate() for instance
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(this.getColor(R.color.my_green));
        this.setNavFilter(
                this.findViewById(R.id.home_nav_button),
                this.findViewById(R.id.home_nav_text)
        );
        this.balanceManager = new BalanceManager(this);
        this.balanceManager.setBalance(0);
        MainSocket mainSocket = new MainSocket(this);
        mainSocket.openConnection();
    }

    public void setMainNav(View view) {
        this.setNavFilter(
                this.findViewById(R.id.home_nav_button),
                this.findViewById(R.id.home_nav_text)
        );
    }
    public void setTransferNav(View view) {
        this.setNavFilter(
                this.findViewById(R.id.transfer_nav_button),
                this.findViewById(R.id.transfer_nav_text)
        );
    }
    public void setNotifyNav(View view) {
        this.setNavFilter(
                this.findViewById(R.id.notifications_nav_button),
                this.findViewById(R.id.notifications_nav_text)
        );
    }
    public void setHistoryNav(View view) {
        this.setNavFilter(
                this.findViewById(R.id.history_nav_button),
                this.findViewById(R.id.history_nav_text)
        );
    }

    private void setNavFilter(ImageButton imageButton, TextView textView) {
        if (current_filtered_btn != null) {
            current_filtered_btn.getBackground().setColorFilter(this.getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }
        if (current_filtered_text != null) {
            current_filtered_text.setTextColor(this.getColor(R.color.white));
        }
        imageButton.getBackground().setColorFilter(this.getColor(R.color.my_green), PorterDuff.Mode.SRC_IN);
        current_filtered_btn = imageButton;
        textView.setTextColor(this.getColor(R.color.my_green));
        current_filtered_text = textView;
    }
}