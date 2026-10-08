package com.ladokgi.apps.data;

/** Small callback contracts shared by the repositories. */
public final class ApiCallback {

    private ApiCallback() {
    }

    public interface Success {
        void onSuccess();
    }

    public interface Error {
        void onFailure(Exception e);
    }

    public interface SuccessError extends Success, Error {
    }

    public interface Value<T> {
        void onValue(T value);
    }
}
