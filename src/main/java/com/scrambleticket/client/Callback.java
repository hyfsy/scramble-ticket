
package com.scrambleticket.client;

public interface Callback<T> {

    void onSuccess(T response);

    void onError(Throwable t);

    void onComplete(T response, Throwable t);

}
