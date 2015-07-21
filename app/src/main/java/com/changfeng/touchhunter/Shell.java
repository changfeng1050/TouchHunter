package com.changfeng.touchhunter;

import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shell {


    private Runnable mRunnableShutdown = new Runnable() {
        @Override
        public void run() {
            shutdown();

        }
    };


    private static final String RTC_DEV = "/sys/class/rtc/rtc0/wakealarm";

    private static boolean delayPowerOn(long delayMillis) {
        if (delayMillis <= 0)
            return false;

        Log.d("#DEBUG#", "###########: Next power on in " +
                (delayMillis / (24 * 60 * 60 * 1000)) + " day," +
                ((delayMillis % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000)) + " hour," +
                ((delayMillis % (60 * 60 * 1000)) / (60 * 1000)) + " min," +
                ((delayMillis % (60 * 1000)) / 1000) + " sec");
        String commands = "echo +" + (delayMillis / 1000) + " > " + RTC_DEV;
        return shellExecCommand(commands, "su");
    }

    private static void _shutdown() {
        Log.d("#DEBUG#", "!!!!!!!!!! shutdown now !!!!!!!!!!");
        String command = "reboot -p";
        shellExecCommand(command, "su");
    }

    public static void shutdown() {
        delayPowerOn(365 * 24 * 3600 * 1000);
        _shutdown();
    }


    public static boolean shellExecCommand(String command, String shell) {
        return shellExecCommand(new String[]{command}, null, shell);
    }

    public static boolean shellExecCommand(String command, File workingDirectory, String shell) {
        return shellExecCommand(new String[]{command}, workingDirectory, shell);
    }

    public static boolean shellExecCommand(String command[], String shell) {
        return shellExecCommand(command, null, shell);
    }

    public static boolean shellExecCommand(String[] command, File workingDirectory, String shell) {
        if (command == null || command.length == 0)
            return false;

        OutputStream out = null;
        InputStream in = null;
        InputStream err = null;

        try {
            if (shell == null || (shell = shell.trim()).length() == 0)
                return false;
            String exit = "exit\n";

            if (workingDirectory == null)
                workingDirectory = new File("/");

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(shell, null, workingDirectory);

            // ProcessBuilder builder = new ProcessBuilder(command);
            // builder.directory(workingDirectory);
            // builder.redirectErrorStream(true);
            // Process process = builder.start();

            final int INTERVAL = 200; // 200ms
            final int WAIT_TIME = 20 * 60 * 1000; // 20min

            out = process.getOutputStream();
            for (String cmd : command) {
                if (cmd != null && cmd.length() > 0)
                    out.write(cmd.endsWith("\n") ? cmd.getBytes() : (cmd + "\n").getBytes());
            }
            out.write(exit.getBytes());

            StringBuffer inString = new StringBuffer();
            StringBuffer errString = new StringBuffer();

            in = process.getInputStream();
            err = process.getErrorStream();

            int exitValue = -1;

            int pass = 0;
            while (pass <= WAIT_TIME) {
                try {
                    while (in.available() > 0)
                        inString.append((char) in.read());
                    while (err.available() > 0)
                        errString.append((char) err.read());

                    exitValue = -1;
                    exitValue = process.exitValue();
                    break;
                } catch (IllegalThreadStateException itex) {
                    try {
                        Thread.sleep(INTERVAL);
                        pass += INTERVAL;
                    } catch (InterruptedException e) {
                        Log.e("#ERROR#", "execute command error: " + command, e);
                    }
                }
            }

            if (pass > WAIT_TIME)
                process.destroy();

            return (exitValue == 0);
        } catch (IOException e) {
            Log.e("#ERROR#", "execute command failed: " + command + e.getMessage(), e);
        } finally {
            closeStream(out);
            closeStream(in);
            closeStream(err);
        }

        return false;
    }

    private static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }
}
