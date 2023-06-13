package org.dissan.restaurant.cli.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public final class OutStream {

    private static final Logger LOGGER = Logger.getLogger(OutStream.class.getSimpleName());
    private static final int OUT_SIZE = 512;
    private static final String IO_EXCEPTION = "CATCH IO EXCEPTION: ";
    private static BufferedWriter printer;

    private OutStream(){}

    public static void print(String msg){
        try {
            OutStream.printer.write(msg);
            OutStream.printer.flush();
        } catch (IOException e) {
            LOGGER.warning(OutStream.IO_EXCEPTION + e.getMessage());
        }catch (NullPointerException e){
            setOutput();
            OutStream.print(msg);
        }
    }

    public static void println(String msg){
        OutStream.print(msg + '\n');
    }

    public static void close(){
        try {
            OutStream.printer.close();
            printer = null;
        } catch (IOException e) {
            LOGGER.warning(OutStream.IO_EXCEPTION + e.getMessage());
        }
    }


    public static void setOutput(){
        if (OutStream.printer == null) {
            OutStream.printer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(FileDescriptor.out), StandardCharsets.UTF_8
                    ), OutStream.OUT_SIZE);
        }
    }


}
