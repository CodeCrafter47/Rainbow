package org.projectrainbow.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.io.PrintStream;

public class LoggingPrintStream extends PrintStream {
    private static final Logger LOGGER = LogManager.getLogger();
    private final String domain;

    public LoggingPrintStream(String var1, OutputStream var2) {
        super(var2);
        this.domain = var1;
    }

    public void println(String var1) {
        this.logString(var1);
    }

    public void println(Object var1) {
        this.logString(String.valueOf(var1));
    }

    private void logString(String var1) {
        LOGGER.info(var1);
    }
}