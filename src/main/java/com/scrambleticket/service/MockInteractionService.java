
package com.scrambleticket.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Scanner;

import com.scrambleticket.Logger;
import com.scrambleticket.client.Callback;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.handler.qrcode.QrCodeRequest;
import com.scrambleticket.handler.qrcode.QrCodeResponse;
import com.scrambleticket.handler.qrcode.QrCodeTimer;
import com.scrambleticket.model.LoginUser;
import com.scrambleticket.config.SystemConfig;

public class MockInteractionService implements InteractionService {

    private final String username = SystemConfig.getStr("username");
    private final String password = SystemConfig.getStr("password");
    private final String idCardNumber = SystemConfig.getStr("idCardNumber");
    private final String qrFilePath = SystemConfig.getStr("qrFilePath");

    private final LoginUser loginUser = new LoginUser();
    {
        loginUser.setUsername(username);
        loginUser.setPassword(password);
        loginUser.setIdCardNumber(idCardNumber);
    }

    UserSessionService userSessionService;
    int sessionId;

    public MockInteractionService(UserSessionService userSessionService, int sessionId) {
        this.userSessionService = userSessionService;
        this.sessionId = sessionId;
    }

    @Override
    public void sendData(byte[] data) {
        sendMessage(new String(data));
    }

    @Override
    public void sendMessage(String message) {
        Logger.info(message);
    }

    @Override
    public LoginUser getLoginUser() {
        return loginUser;
    }

    @Override
    public void asyncForSmsCode(FlowContext context, Callback<String> callback) {

        Logger.info("please enter the message code: ");
        Scanner scanner = new Scanner(System.in);
        String smsCode_input = scanner.nextLine();

        Throwable e = null;
        try {
            callback.onSuccess(smsCode_input);
        } catch (Throwable t) {
            e = t;
            callback.onError(t);
        }
        callback.onComplete(smsCode_input, e);

    }

    @Override
    public void asyncForQrCode(FlowContext context, QrCodeRequest request, Callback<QrCodeResponse> callback) {

        byte[] bytes = Base64.getDecoder().decode(request.getImage().getBytes(StandardCharsets.UTF_8));

        Throwable t = null;
        try {
            Files.write(Paths.get(qrFilePath), bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            t = e;
            callback.onError(new ScrambleTicketException("二维码下载失败", e));
        } finally {
            if (t != null) {
                callback.onComplete(null, t);
            }
        }

        if (t != null) {
            return;
        }

        // TODO 刷新二维码的功能
        QrCodeTimer qrCodeTimer = new QrCodeTimer();
        qrCodeTimer.asyncForConfirm(context, request, new Callback<QrCodeResponse>() {
            @Override
            public void onSuccess(QrCodeResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }

            @Override
            public void onComplete(QrCodeResponse response, Throwable t) {
                try {
                    Files.delete(Paths.get(qrFilePath));
                } catch (IOException e) {
                    Logger.error("delete qrcode file failed", e);
                }
                callback.onComplete(response, t);
            }
        });
    }
}
