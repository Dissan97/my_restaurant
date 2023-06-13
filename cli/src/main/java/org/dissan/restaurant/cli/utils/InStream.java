package org.dissan.restaurant.cli.utils;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class InStream {
    private InStream() {}
    private static BufferedReader reader;
    public static final Logger LOGGER = Logger.getLogger(InStream.class.getSimpleName());
    public static @Nullable String getLine(){
        try {
            return reader.readLine();
        } catch (IOException e) {
            InStream.LOGGER.warning("EXCEPTION CAUGHT: " + e.getMessage());
            return null;
        }catch (NullPointerException e){
            setInput();
            return getLine();
        }
    }

    public static void setInput() {
        if (InStream.reader == null) {
            InStream.reader = new BufferedReader(
                    new InputStreamReader(System.in)
            );
        }
    }

}
