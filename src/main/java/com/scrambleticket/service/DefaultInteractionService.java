
package com.scrambleticket.service;

import com.scrambleticket.client.Callback;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.handler.qrcode.QrCodeRequest;
import com.scrambleticket.handler.qrcode.QrCodeResponse;
import com.scrambleticket.model.LoginUser;

public class DefaultInteractionService implements InteractionService {

    UserSessionService userSessionService;
    int sessionId;

    public DefaultInteractionService(UserSessionService userSessionService, int sessionId) {
        this.userSessionService = userSessionService;
        this.sessionId = sessionId;
    }

    @Override
    public void sendData(byte[] data) {

    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public LoginUser getLoginUser() {
        return null;
    }

    @Override
    public void asyncForSmsCode(FlowContext context, Callback<String> callback) {

    }

    @Override
    public void asyncForQrCode(FlowContext context, QrCodeRequest request, Callback<QrCodeResponse> callback) {

        // QrCodeTimer

    }
}
