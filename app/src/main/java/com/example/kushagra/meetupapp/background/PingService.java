package com.example.kushagra.meetupapp.background;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Himanshu Sagar on 28-11-2016.
 */

public class PingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {





        try
        {


            Thread.sleep(101);
        }
        catch(InterruptedException ie)
        {

        }

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
