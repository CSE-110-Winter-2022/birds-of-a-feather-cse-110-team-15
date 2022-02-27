package com.example.birdsofafeather;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

public class UUIDManager {
    String userUUID = null;
    private final String filename = "uuidStr";


    // Create a new UUID for the current user if none has been created
    // Fetch the previous UUID if one has been created
    public UUIDManager(Context context)  {
        // "file" is a file at ./uuidStr
        File file = new File(context.getFilesDir(), filename);

        // Attempt to create a file at ./uuidStr
        try {
            if(file.exists()) {
                // If the file already existed, read the UUID from the file and store it as a string
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextLine()) {
                    userUUID = scanner.nextLine();
                    scanner.close();
                } else {
                    scanner.close();

                    userUUID =  UUID.randomUUID().toString();

                    try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
                        fos.write(userUUID.getBytes());
                    }
                }
            } else {
                // If the new file was successfully created, generate a uuid, then write it to the file
                file.createNewFile();
                userUUID =  UUID.randomUUID().toString();

                try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
                    fos.write(userUUID.getBytes());
                }

            }
        } catch (IOException e) {
            System.err.println("An error occurred reading or writing a UUID to or from a file.");
            e.printStackTrace();
        }
    }

    // Should return true if inputUUID equals currentUUID
    public boolean match(String inputUUID) {
        if (userUUID != null) {
            return inputUUID.equals(userUUID);
        }
        return false;
    }

    public String getUserUUID () {
        return userUUID;
    }

    // Might be unnecessary, might be useful for mocking
    // Will be deleted if it's ever not the case that we need it
    public void setUserUUID (String userUUID) {
        this.userUUID = userUUID;
    }

}
