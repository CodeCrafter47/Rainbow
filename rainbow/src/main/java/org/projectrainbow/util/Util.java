package org.projectrainbow.util;

public class Util {

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void sneakyThrow0(Throwable th) throws T {
        throw (T) th;
    }

    public static void sneakyThrow(Throwable th) {
        Util.<RuntimeException>sneakyThrow0(th);
    }
}
