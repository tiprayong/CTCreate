package com.comtip.tip.ctcreate;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

/**
 * Created by TipRayong on 12/10/2560 12:38
 * CTCreate
 */

public class ChromeCustomTab {
    Context context;
    private CustomTabsClient customTabsClient;
    private CustomTabsSession customTabsSession;
    private CustomTabsServiceConnection customTabsServiceConnection;
    private CustomTabsIntent customTabsIntent;

    public ChromeCustomTab(Context context) {
        this.context = context;
    }

    public void setupChromeCustomTab() {
        customTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                customTabsClient = client;
                customTabsClient.warmup(0);
                customTabsSession = customTabsClient.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                customTabsClient = null;
            }
        };

        customTabsClient.bindCustomTabsService(context, "com.android.chrome", customTabsServiceConnection);
        customTabsIntent = new CustomTabsIntent.Builder(customTabsSession)
                .setShowTitle(true)
                .setToolbarColor(Color.BLACK)
                .setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .build();
    }

    public void openChromeCustom(String url) {
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }

    // Check Internet Available
    public boolean isInternetConnectionAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


}
