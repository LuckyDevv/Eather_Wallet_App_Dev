package com.eather.wallet.managers;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eather.wallet.MainActivity;
import com.eather.wallet.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class BalanceManager {
    private double User_Balance = 0;
    private int default_margin = 243;
    private MainActivity mainActivity;

    public BalanceManager (MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void addBalance(double amount) {
        this.User_Balance += Math.round(amount * 100.0) / 100.0;
        this.updateBalanceText();
    }
    public void reduceBalance(double amount) {
        this.User_Balance -= Math.round(amount * 100.0) / 100.0;
        this.updateBalanceText();
    }
    public void setBalance(double user_Balance) {
        this.User_Balance = Math.round(user_Balance * 100.0) / 100.0;
        this.updateBalanceText();
    }
    public double getBalance() {
        return this.User_Balance;
    }

    @SuppressLint("SetTextI18n")
    public void updateBalanceText() {
        String balance = String.valueOf(this.User_Balance);
        int int_balance = Integer.parseInt(balance.split("\\.")[0]);
        int float_balance = Integer.parseInt(balance.split("\\.")[1]);

        Locale l = Locale.getDefault();
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(l);

        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' '); // setting the thousand separator

        formatter.setDecimalFormatSymbols(symbols);
        formatter.setMaximumFractionDigits(0);
        TextView int_balance_text = this.mainActivity.findViewById(R.id.int_balance_text);
        TextView float_balance_text = this.mainActivity.findViewById(R.id.float_balance_text);
        this.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int_balance_text.setText(formatter.format(int_balance));
                float_balance_text.setText("," + float_balance);
                int_balance_text.requestLayout();
                int_balance_text.invalidate();
                float_balance_text.invalidate();
                int_balance_text.post(() -> {
                    int width = int_balance_text.getWidth();
                    int left = int_balance_text.getLeft();
                    float_balance_text.setLeft(left + width);
                });
            }
        });
    }
}
