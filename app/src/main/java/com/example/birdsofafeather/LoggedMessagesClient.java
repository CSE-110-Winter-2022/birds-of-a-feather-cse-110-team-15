package com.example.birdsofafeather;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.StatusCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.android.gms.tasks.Task;

public class LoggedMessagesClient implements MessagesClient {
    private final MessagesClient messagesClient;
    private final String TAG = "MESSAGES CLIENT";

    public LoggedMessagesClient(MessagesClient messagesClient) {
        this.messagesClient = messagesClient;
    }

    @Override
    @NonNull
    public Task<Void> publish(@NonNull Message message) {
        Log.d(TAG, "Publishing message: \n" + new String(message.getContent()));
        return messagesClient.publish(message);
    }

    @Override
    @NonNull
    public Task<Void> publish(@NonNull Message message, @NonNull PublishOptions publishOptions) {
        Log.d(TAG, "Publishing message: \n" + new String(message.getContent()));
        return messagesClient.publish(message, publishOptions);
    }

    @Override
    @NonNull
    public Task<Void> unpublish(@NonNull Message message) {
        Log.d(TAG, "Unpublishing message: \n" + new String(message.getContent()));
        return messagesClient.unpublish(message);
    }

    @Override
    @NonNull
    public Task<Void> subscribe(@NonNull MessageListener messageListener) {
        Log.d(TAG, "Subscribing to " + messagesClient.getClass().getSimpleName());
        return messagesClient.subscribe(messageListener);
    }

    @Override
    @NonNull
    public Task<Void> subscribe(@NonNull MessageListener messageListener, @NonNull SubscribeOptions subscribeOptions) {
        Log.d(TAG, "Subscribing to " + messagesClient.getClass().getSimpleName());
        return messagesClient.subscribe(messageListener, subscribeOptions);
    }

    @Override
    @NonNull
    public Task<Void> subscribe(@NonNull PendingIntent pendingIntent, @NonNull SubscribeOptions subscribeOptions) {
        return messagesClient.subscribe(pendingIntent, subscribeOptions);
    }

    @Override
    @NonNull
    public Task<Void> subscribe(@NonNull PendingIntent pendingIntent) {
        return messagesClient.subscribe(pendingIntent);
    }

    @Override
    @NonNull
    public Task<Void> unsubscribe(@NonNull MessageListener messageListener) {
        Log.d(TAG, "Unsubscribing from " + messagesClient.getClass().getSimpleName());
        return messagesClient.unsubscribe(messageListener);
    }

    @Override
    @NonNull
    public Task<Void> unsubscribe(@NonNull PendingIntent pendingIntent) {
        return messagesClient.unsubscribe(pendingIntent);
    }

    @Override
    @NonNull
    public Task<Void> registerStatusCallback(@NonNull StatusCallback statusCallback) {
        return messagesClient.registerStatusCallback(statusCallback);
    }

    @Override
    @NonNull
    public Task<Void> unregisterStatusCallback(@NonNull StatusCallback statusCallback) {
        return messagesClient.unregisterStatusCallback(statusCallback);
    }

    @Override
    public void handleIntent(@NonNull Intent intent, @NonNull MessageListener messageListener) {
        messagesClient.handleIntent(intent, messageListener);
    }

    @Override
    @NonNull
    @KeepForSdk
    public ApiKey<MessagesOptions> getApiKey() {
        return messagesClient.getApiKey();
    }

}
