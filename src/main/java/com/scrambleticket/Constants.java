
package com.scrambleticket;

public interface Constants {

    String HOST = "kyfw.12306.cn";
    int PORT = 443;
    int TIMEOUT = 10_000;

    String key_CLeftTicketUrl = "CLeftTicketUrl";

    String popup_passport_appId = "otn";
    String popup_qr_appId = "otn";

    String key_uamtk = "uamtk";
    String key_tk = "tk";

    default String getPurposeCodes() {
        boolean isStudent = false;
        String login_isDisable = "N";
        if (isStudent) {
            if ("Y".equals(login_isDisable)) {
                return "0X1C";
            } else {
                return "0X00";
            }
        } else {
            if ("Y".equals(login_isDisable)) {
                return "1C";
            } else {
                return "ADULT";
            }
        }
    }
}
