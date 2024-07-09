
package com.scrambleticket.model;

import com.scrambleticket.exception.ScrambleTicketException;

import com.scrambleticket.util.StringUtil;
import lombok.Data;

import java.util.UUID;

@Data
public class LoginUser implements Checkable {

    private String username = "xxx"; // trim
    private String password = "xxx"; // trim
    // 手机短信登录
    private String idCardNumber = "xxxx"; // trim
    private String smsCode = ""; // trim

    private String id = UUID.randomUUID().toString();
    private String realUserName;

    // 后四位
    public String getIdCardNumberLast4() {
        return idCardNumber.substring(idCardNumber.length() - 4);
    }

    // 扫码登录
    // 无扩展字段

    @Override
    public boolean check() {

        if (StringUtil.isBlank(username)) {
            throw new ScrambleTicketException("username invalid");
        }
        if (StringUtil.isBlank(password) || password.length() < 6) {
            throw new ScrambleTicketException("password invalid");
        }
        if (StringUtil.isBlank(idCardNumber) || idCardNumber.length() < 4) {
            throw new ScrambleTicketException("idCardNumber invalid");
        }

        return true;
    }

    public boolean checkSmsCode() {
        if (StringUtil.isBlank(smsCode) || smsCode.length() != 6) {
            throw new ScrambleTicketException("smsCode invalid");
        }
        return true;
    }
}
