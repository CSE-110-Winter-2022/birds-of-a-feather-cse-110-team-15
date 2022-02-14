package com.example.birdsofafeather;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FakedMessageListener extends MessageListener {
    private final MessageListener messageListener;
    private final ScheduledExecutorService executor;

    /**
     * Constructor
     * @param realMessageListener uses a real message listener
     *                            so we can call its onFound / onLost
     *                            for simulating receiving messages
     */
    public FakedMessageListener(MessageListener realMessageListener) {
        this.messageListener = realMessageListener;
        // use an executor to call the onFound / onLost functions, and add a delay
        this.executor = Executors.newSingleThreadScheduledExecutor();

    }

    // mock receiving data
    // Delay 1 second -> Create Message -> call onFound(message) -> call onLost(message)
    public void receive(String messageStr) {
        executor.schedule(() -> {
            Message message = new Message(messageStr.getBytes(StandardCharsets.UTF_8));
            this.messageListener.onFound(message);
            this.messageListener.onLost(message);
        }, 1, TimeUnit.SECONDS);
    }

}