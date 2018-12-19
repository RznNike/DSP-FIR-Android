package com.dsp.fir.global;

import android.content.res.Resources;

import com.dsp.fir.R;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorHandler {
    private final Resources res;

    @Inject
    public ErrorHandler(Resources res) {
        this.res = res;
    }

    public void proceed(Throwable error, MessageCallback callback) {
        Preconditions.checkNotNull(error);
        log.error(error.getMessage(), error);
        callback.on(getUserMessage(error));
    }

    private String getUserMessage(Throwable error) {
        if (error instanceof IOException) {
            return getIOErrorMessage(((IOException) error));
        } else {
            return res.getString(R.string.unknown_error);
        }
    }

    private String getIOErrorMessage(IOException error) {
        if (error instanceof NoRouteToHostException || error instanceof SocketTimeoutException) {
            return res.getString(R.string.server_not_available);
        }

        return res.getString(R.string.network_error);
    }

    public interface MessageCallback {
        void on(String message);
    }
}
