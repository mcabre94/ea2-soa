package com.example.ea2soa.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RefreshTokenReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        RefreshTokenService refreshTokenService = new RefreshTokenService(context.getApplicationContext());
        refreshTokenService.refreshToken();
    }
}
