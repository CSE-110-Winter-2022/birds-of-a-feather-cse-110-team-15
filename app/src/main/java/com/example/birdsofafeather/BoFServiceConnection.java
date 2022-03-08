package com.example.birdsofafeather;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class BoFServiceConnection implements ServiceConnection {
    private NearbyBackgroundService nearbyService;
    private boolean isBound;

    // called when connection to NearbyBackgroundService has been established
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        NearbyBackgroundService.NearbyBinder nearbyBinder = (NearbyBackgroundService.NearbyBinder)iBinder;
        nearbyService = nearbyBinder.getService();
        isBound = true;
    }

    // - called when connection to NearbyBackgroundService has been lost
    // - does not remove binding; can still receive call to onServiceConnected
    //   when service is running
    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        isBound = false;
    }

    public NearbyBackgroundService getNearbyService(){
        return nearbyService;
    }

    public boolean isBound() {
        return isBound;
    }

    public void setBound(boolean bound) {
        isBound = bound;
    }
}
