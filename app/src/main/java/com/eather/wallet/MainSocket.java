package com.eather.wallet;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URI;
import java.net.URISyntaxException;
import dev.gustavoavila.websocketclient.WebSocketClient;

public class MainSocket {
    private WebSocketClient webSocketClient;
    private final MainActivity mainActivity;
    private boolean userInfoParsed = false;

    public MainSocket(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @SuppressLint("SetTextI18n")
    public boolean openConnection() {
        URI uri;
        try {
            uri = new URI("wss://wallet.eather.ru/wss/");
        }
        catch (URISyntaxException e) {
            return false;
        }

        MainActivity mainActivity_ = this.mainActivity;

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                if (!userInfoParsed) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", "user_info");
                        webSocketClient.send(jsonObject.toString());
                    } catch (JSONException ignored) {
                    }
                }
            }

            @Override
            public void onTextReceived(String message) {
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    if (jsonObject.has("response")) {
                        JSONObject response = jsonObject.getJSONObject("response");
                        int response_code = response.getInt("code");
                        JSONObject message_ = new JSONObject(response.getString("message"));
                        handleResponse(response_code, message_);
                    } else if (jsonObject.has("error")) {
                        JSONObject error = jsonObject.getJSONObject("error");
                        int error_code = error.getInt("code");
                        JSONObject message_ = new JSONObject(error.getString("message"));
                        handleError(error_code, message_);
                    }
                } catch (JSONException ignored) {
                }
            }

            @Override
            public void onBinaryReceived(byte[] data) {
            }

            @Override
            public void onPingReceived(byte[] data) {
            }

            @Override
            public void onPongReceived(byte[] data) {
            }

            @Override
            public void onException(Exception e) {
            }

            @Override
            public void onCloseReceived(int reason, String description) {
                openConnection();
            }
        };

        webSocketClient.setConnectTimeout(90000);
        webSocketClient.setReadTimeout(90000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();

        return true;
    }

    public void handleResponse(int code, JSONObject message) throws JSONException {
        switch (code) {
            case 13:
                this.mainActivity.runOnUiThread(() -> {
                    try {
                        String username = message.getString("username") + ",";
                        TextView username_view = mainActivity.findViewById(R.id.username_text);
                        username_view.setText(username);
                        username_view.invalidate();

                        double balance = message.getDouble("balance");
                        mainActivity.balanceManager.setBalance(balance);

                        String avatar_url = message.getString("avatar");
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(40));
                        synchronized (Glide.with(mainActivity)
                                .load(avatar_url)
                                .apply(requestOptions)
                                .into((ImageView) mainActivity.findViewById(R.id.user_avatar_img))) {
                            ImageView preloader = mainActivity.findViewById(R.id.preloader_image);
                            preloader.clearAnimation();
                            preloader.setVisibility(View.INVISIBLE);
                            mainActivity.findViewById(R.id.preloader_image_logo).setVisibility(View.INVISIBLE);
                            mainActivity.findViewById(R.id.balance_group).setVisibility(View.VISIBLE);
                            mainActivity.findViewById(R.id.news_title_text).setVisibility(View.VISIBLE);
                            mainActivity.findViewById(R.id.navigation_container).setVisibility(View.VISIBLE);
                            mainActivity.findViewById(R.id.news_container).setVisibility(View.VISIBLE);
                            userInfoParsed = true;
                        }
                    }catch(JSONException ignored) {}
                });
                break;
        }
    }

    public void handleError(int code, JSONObject message) {

    }
}
