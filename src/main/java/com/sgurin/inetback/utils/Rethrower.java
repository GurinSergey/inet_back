package com.sgurin.inetback.utils;

public class Rethrower {
    public static void rethrow(final Throwable exception) {
        class CheckedExceptionRethrower<T extends Throwable> {
            @SuppressWarnings("unchecked")
            private void rethrow(Throwable exception) throws T {
                throw (T) exception;
            }
        }
        new CheckedExceptionRethrower<RuntimeException>().rethrow(exception);
    }
}
