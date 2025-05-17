package com.lankaice.project.Validation;

import java.io.IOException;

public class AdbReverseHelper {

    public static void setupAdbReverse() {
        try {
            Process process = new ProcessBuilder("adb", "reverse", "tcp:8081", "tcp:8081")
                    .redirectErrorStream(true)
                    .start();

            // Optionally read the output (for logging/debugging)
            new Thread(() -> {
                try (var reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[ADB] " + line);
                    }
                } catch (Exception e) {
                    System.out.println("[ADB] Error reading output: " + e.getMessage());
                }
            }).start();

        } catch (IOException e) {
            System.out.println("⚠️ Failed to run adb reverse: " + e.getMessage());
        }
    }
}

