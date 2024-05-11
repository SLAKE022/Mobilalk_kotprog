package com.example.italdiszkont;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler{
    private static final String CHANNEL_ID = "shop_notification_channel";
    private final int NOTIFICATION_ID = 0;
    private NotificationManager manager;
    private Context context;

    public NotificationHandler(Context context) {
        this.context = context;
        this.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Shop Notification", NotificationManager.IMPORTANCE_DEFAULT);

        channel.setDescription("Kosárba raktad a terméket!");
        this.manager.createNotificationChannel(channel);
    }

    public void send(String message){
        Intent intent = new Intent(context, ShopActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Shop Application")
                .setContentText(message)
                .setSmallIcon(R.drawable.shopping_cart)
                .setContentIntent(pendingIntent);


        this.manager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel(){
        this.manager.cancel(NOTIFICATION_ID);
    }
}