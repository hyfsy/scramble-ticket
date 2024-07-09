
package com.scrambleticket.handler;

import java.util.Base64;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.Logger;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.model.LoginUser;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;
import com.scrambleticket.util.SM4Util;
import com.scrambleticket.util.UrlUtil;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class LoginHandler implements FlowHandler {

    private static final String SM4_key = "tiekeyuankp12306"; // 从 login_new.js 获取

    private static final String paste = "N";
    private static final boolean isOpen = false;

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        LoginUser loginUser = context.getInteractionService().getLoginUser();
        loginUser.checkSmsCode();
        String encryptedPassword = getEncryptedPassword(loginUser.getPassword(), SM4_key);
        ByteBuf body = ByteBufUtil.create("sessionId=&sig=&if_check_slide_passcode_token=&scene=&checkMode=0&randCode="
            + loginUser.getSmsCode() + "&username=" + loginUser.getUsername() + "&password=" + encryptedPassword
            + "&appid=" + popup_passport_appId);
        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/passport/web/login", body);
        HttpUtils.setFormUrlEncodedContentType(request);
        request.headers().add("isPasswordCopy", paste);
        request.headers().add("appFlag", (isOpen ? "otnLove" : ""));

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());
                Integer result_code = (Integer)json.get("result_code");
                String result_message = (String)json.get("result_message");
                // String uamtk = (String)json.get("uamtk");
                boolean control = false;
                switch (result_code) {
                    case 0:
                        break;
                    case 91:
                    case 92:
                    case 94:
                    case 95:
                    case 97:
                        control = true;
                        break;
                    case 101:
                        throw new ScrambleTicketException(
                            "您的密码很久没有修改了，为降低安全风险，请您重新设置密码后再登录，地址：https://kyfw.12306.cn/otn/forgetPassword/initforgetMyPassword");
                    default:
                        throw new ScrambleTicketException(result_message);
                }
                if (control) {
                    throw new UnsupportedOperationException("control: " + result_code + "_" + result_message);
                }
                Logger.debug(result_message);

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("login", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {

            }
        });
    }

    private String getEncryptedPassword(String password, String key) {
        byte[] passwordBytes;
        try {
            passwordBytes = SM4Util.encryptEcb(password, key);
        } catch (Exception e) {
            throw new ScrambleTicketException("password encrypt failed", e);
        }
        String encryptedPassword = "@" + Base64.getEncoder().encodeToString(passwordBytes);
        return UrlUtil.encode(encryptedPassword);
    }
}
