
package com.scrambleticket.service;

import com.scrambleticket.client.Callback;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.handler.qrcode.QrCodeRequest;
import com.scrambleticket.handler.qrcode.QrCodeResponse;
import com.scrambleticket.model.LoginUser;

// TODO 通知：https://segmentfault.com/a/1190000041982599?sort=newest
public interface InteractionService {

    void sendData(byte[] data);

    void sendMessage(String message);

    LoginUser getLoginUser();

    void asyncForSmsCode(FlowContext context, Callback<String> callback);

    void asyncForQrCode(FlowContext context,  QrCodeRequest request, Callback<QrCodeResponse> callback);

}
