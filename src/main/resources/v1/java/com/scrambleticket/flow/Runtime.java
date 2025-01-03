
package com.scrambleticket.flow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.scrambleticket.handler.scramble.common.OrderQueueWaitTimer;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scrambleticket.v1.HttpClient;
import com.scrambleticket.util.EscapeUtil;
import com.scrambleticket.util.SM4Util;
import com.scrambleticket.util.UrlUtil;

// 通知：https://segmentfault.com/a/1190000041982599?sort=newest
public class Runtime {

    String popup_uam_dataType = "json";
    String popup_uam_type = "POST";

    String popup_passport_appId = "otn";
    String popup_passport_baseUrl = "https://kyfw.12306.cn/passport/";
    String popup_baseUrl = "https://kyfw.12306.cn";
    String popup_publicName = "/otn";

    String popup_passport_apptk_static = popup_passport_baseUrl + "web/auth/uamtk-static";
    String popup_passport_login = popup_passport_baseUrl + "web/login";
    // String popup_passport_captcha = popup_passport_baseUrl +
    // "captcha/captcha-image64?login_site=E&module=login&rand=sjrand&";
    // String popup_passport_captcha_check = popup_passport_baseUrl + "captcha/captcha-check";
    String popup_passport_uamtk = popup_passport_baseUrl + "web/auth/uamtk";
    String sendMobileMsg = popup_baseUrl + popup_publicName + "/login/sendMobileMsg"; // 国际手机号发送下行短信
    // 是否开启滑块验证
    String slide_passport_url = popup_passport_baseUrl + "web/slide-passcode";
    String check_login_passport_url = popup_passport_baseUrl + "web/checkLoginVerify";// 获取登陆前校验

    String popup_is_uam_login = "Y"; // 是否统一认证登录
    String popup_is_login_passCode = "Y"; // 是否启用验证码校验登录（仅本地登录）
    String popup_is_sweep_login = "Y"; // 统一认证登录情况下是否开启扫码登录
    String popup_is_login = "N"; // 是否已登录

    // TODO    "queryUrl": "leftTicket/queryG",
    //         "psr_qr_code_result": "N",
    //         "now": 1715137062648,
    //         "login_url": "resources/login.html",

    String base_uamauthclient_url = popup_baseUrl + popup_publicName + "/uamauthclient";
    String base_checkUpMeg = popup_baseUrl + popup_publicName + "/login/checkUpMeg";

    String apptk_tmp_control = "";
    String SM4_key = "tiekeyuankp12306"; // 从 login_new.js 获取

    // 滑动验证码相关
    boolean isPasscode = false;
    boolean isOpen = false;// 工具条是否开启
    String appkey = "FFFF0N000000000085DE";
    String nc = null;
    String nc_token = "";
    String csessionid = "";
    String sig = "";
    String scene = "nc_login";

    // 获取短信验证码
    String popup_sms_verification = popup_passport_baseUrl + "web/getMessageCode";
    int modalType = 1;
    String popup_is_message_passCode = "Y";// 是否显示短信验证
    int modalTime = 60;
    String set_time;
    String is_key_up = "";// 判断是否是键盘触发
    int time = 60;
    boolean flag = true;
    String timer;
    String code = "";

    String popup_qr_appId = "otn";
    // String popup_url = {
    // 'loginConf': popup_baseUrl + popup_publicName + '/login/conf',
    // // 本地登录
    // 'getPassCodeNew': popup_baseUrl + popup_publicName + '/passcodeNew/getPassCodeNew?module=login&rand=sjrand&',
    // 'checkRandCodeAnsyn': popup_baseUrl + popup_publicName + '/passcodeNew/checkRandCodeAnsyn',
    // 'login': popup_baseUrl + popup_publicName + '/login/loginAysnSuggest',
    // 'getBanners': popup_baseUrl + popup_publicName + '/index12306/getLoginBanner',
    // 'loginSlide': popup_baseUrl + popup_publicName + '/slide/loginSlide',//本地登录获取滑块验证码
    // // 扫码登录
    // 'qr': popup_baseUrl + '/passport/web/create-qr',
    // 'qr64': popup_baseUrl + '/passport/web/create-qr64',
    // 'checkqr': popup_baseUrl + '/passport/web/checkqr'
    // }

    int timeout = 10_000; // ms

    // TODO 不支持学生

    // TODO 配置
    String username = "hyfsya"; // trim
    String password = "h13971864253"; // trim
    String idCardNumber = "4815"; // trim
    String smsCode = ""; // trim
    String qrCodeFilePath = "C:\\Users\\user\\Desktop\\qrcode_12306.jpg";

    String fromStation = "上海";
    String toStation = "常州";
    Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-05-18", new ParsePosition(0)); // validate
    List<String> selectedTrainCodes = new ArrayList<>(); // 非用户看的no
    List<String> selectedPassengerKeys = new ArrayList<>(); // format: name_allEncStr

    // 有些儿童没有一等座的票，需通过 ticketInfoForPassengerForm.limitBuySeatTicketDTO 判断
    /**
     * @see seatTypeCodeForName
     */
    String default_seat_type = "O"; // ou o 非 零 0 TODO 针对每个乘客可配置自定义，失败后可fallback到其他票种类（多个按顺序）
    /**
     * @see ticketTypeCodeForName
     */
    String default_ticket_type = "1"; // 成人票 TODO 针对每个乘客可配置自定义

    // TODO

    {
        selectedTrainCodes.add("G7032");
        selectedPassengerKeys.add("何映峰_2dcb3f1fd997c1ac3c1384d5e9425af93acd655663abe115d07f0afb26ac8ef550cac39d64fca2c8541c1e7265c9d0af8f41855fba4a0dd7f0218ce69d046a8c4a76746eb2a05700c60e204a01a89ea8443bbd37894cc28911a60e93cf6ba535");
    }

    public static void main(String[] args) throws Exception {
        new com.scrambleticket.v1.Runtime().flow();
    }

    private void ex() {
        if (true) {
            throw new RuntimeException("stop");
        }
    }

    static String url = null;
    static String header = null;
    static String body = null;
    static ResponseEntity<String> response_type_string = null;
    static ResponseEntity<Map> response = null;
    static Map<String, String> cookies = null;
    static Integer result_code = null;
    static String result_message = null;
    static String route = null;
    static String JSESSIONID = null;
    static String BIGipServerotn = null;
    static String _passport_session = null;
    static String BIGipServerpassport = null;
    static String cookieString = null;
    static String defaultCookie = "; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off";
    static Map data = null;
    static Boolean status = null;

    static String uamtk = null;
    static String tk = null;
    static String uKey = null;

    public void flow() throws Exception {
        check();

        Limiter.disable();
        // TODO _uab_collina ?
        String s =
                "Cookie: _uab_collina=171524823772866925881772; JSESSIONID=4B30C73041F787C1549C23BE477A583F; tk=VMdFju6KUbBD6XLC0VePfeHjmhNwoPUnxhh1h0; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_toDate=2024-05-09; _jc_save_wfdc_flag=dc; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=1675165962.50210.0000; BIGipServerpassport=820510986.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; uKey=4aa75764e3ab09a99d4a16519989d87115640354f9696fe8f1c9c477b6c84224; _jc_save_fromDate=2024-05-18"
                ;
        login_mock(s);
        login();
        // loginQr();

        // String train_date = new SimpleDateFormat("yyyy-MM-dd").format(date);
        // Map<String, Map<String, String>> stationMap = queryG(train_date);
        // // TODO 并发处理
        // // 用code就够了，无需no
        // String selectedTrainCode = selectedTrainCodes.get(0);
        // Map<String, String> trainInfo = stationMap.get(selectedTrainCode);
        // checkTrainNo(trainInfo);
        // // 这边开始，接口都要判断用户登录情况
        // checkUser();
        // Limiter.enable();
        // String train_tour_flag = "other"; // ?
        // String tour_flag = "dc"; // 需结合 $("#dc").is(":checked") 和 train_tour_flag 判断
        // submitOrderRequest(trainInfo, train_date, train_tour_flag, tour_flag);
        // // 这边开始安全检查
        // String initDC_html = initDC(train_tour_flag, tour_flag);
        // String REPEAT_SUBMIT_TOKEN = get_REPEAT_SUBMIT_TOKEN(initDC_html);
        // Map<String, Map<String, String>> normal_passengers_map = getPassengerDTOs(REPEAT_SUBMIT_TOKEN);
        // check_passengers_and_tickets(normal_passengers_map, initDC_html);
        // JSONObject ticketInfoForPassengerForm = get_ticketInfoForPassengerForm(initDC_html);
        //
        // checkOrderInfo(normal_passengers_map, initDC_html, REPEAT_SUBMIT_TOKEN, tour_flag);
        // getQueueCount(trainInfo, ticketInfoForPassengerForm, REPEAT_SUBMIT_TOKEN);
        //
        // // TODO 小黑屋 302 https://www.12306.cn/mormhweb/logFiles/error.html 第一次半小时到一小时左右
        //
        // // TODO test
        // // while_test_check_order_and_queue();
        //
        // confirmSingleForQueue(normal_passengers_map, ticketInfoForPassengerForm, REPEAT_SUBMIT_TOKEN, tour_flag);

    }

    private void while_test_check_order_and_queue() throws Exception {
        int c = 0;
        // 1s  5c
        // 30s 20c 10?
        while(true) {
            c++;
            if (c % 5 == 0) {
                Thread.sleep(2_000);
            }
            test_check_order_and_queue();
            System.out.println(c);
        }
    }

    private void test_check_order_and_queue() {

        String data1 =
                "cancel_flag=2&bed_level_order_num=000000000000000000000000000000&passengerTicketStr=O%2C0%2C1%2C%E4%BD%95%E6%98%A0%E5%B3%B0%2C1%2C3207***********815%2C177****5166%2CN%2C2dcb3f1fd997c1ac3c1384d5e9425af93acd655663abe115d07f0afb26ac8ef550cac39d64fca2c8541c1e7265c9d0af8f41855fba4a0dd7f0218ce69d046a8c4a76746eb2a05700c60e204a01a89ea8443bbd37894cc28911a60e93cf6ba535&oldPassengerStr=%E4%BD%95%E6%98%A0%E5%B3%B0%2C1%2C3207***********815%2C1_&tour_flag=dc&whatsSelect=1&sessionId=&sig=&scene=nc_login&_json_att=&REPEAT_SUBMIT_TOKEN=9f64dfd18bae82848ea5230794246515"
                ;
        String data2 =
                "train_date=Tue+May+21+2024+00%3A00%3A00+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&train_no=55000G703271&stationTrainCode=G7032&seatType=O&fromStationTelecode=SHH&toStationTelecode=CZH&leftTicket=kFmM3fT211pvl1J9Mr5K7ogHfB%252B4rISnJBvG3lgHuvEQEFHw&purpose_codes=00&train_location=HY&_json_att=&REPEAT_SUBMIT_TOKEN=9f64dfd18bae82848ea5230794246515"
                ;

        url = "https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
        header = "Host: kyfw.12306.cn\n" +
                "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n" +
                cookieString
        ;
        body = data1;
        response = HttpClient.post(url, header, body, Map.class);
        data = (Map)response.getBody().get("data");
        if (!(Boolean) data.get("submitStatus")) {
            throw new RuntimeException("submitStatus");
        }

        // TODO 快速调用一直有问题，需要排查是否存在被关到小黑屋的可能
        url = "https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount";
        header =
                "Host: kyfw.12306.cn\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                + cookieString
        ;
        body = data2;
        response = HttpClient.post(url, header, body, Map.class); // TODO 报错提示：网络忙，请稍后再试。
        status = (Boolean) response.getBody().get("status"); // TODO messages 提示系统忙，请稍后重试   系统繁忙，请稍后重试！    api调用太快，会提示这个
        if (!status) {
            throw new RuntimeException("getQueueCount未知错误：" + response);
        }
        System.out.println("success");
    }

    private void login_mock(String s) {

        Map<String, String> map = new HashMap<>();

        // // TODO _uab_collina ?
        // String s =
        //         "Cookie: _uab_collina=171516853815587795347043; JSESSIONID=F0685001969FD6B39BAB547A4C4B81FD; tk=38J51AAixu_4FrinBKzuAqbjoQjyecji27h1h0; route=9036359bb8a8a461c164a04f8f50b252; BIGipServerotn=1641611530.50210.0000; BIGipServerpassport=786956554.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_fromDate=2024-05-10; _jc_save_toDate=2024-05-08; _jc_save_wfdc_flag=dc; uKey=1513dbb59381c53f0256f04f947584c133586b4c0f956fb8a3b955f9b4d26412"
        //         ;
        s = s.substring("Cookie: ".length());
        map = parseCookie(Arrays.asList(s));

        // String json =
        //         ""
        //         ;
        // if (StringUtil.isNotBlank(json)) {
        //     JSONObject jsonObject = JSON.parseObject(json);
        //     jsonObject.forEach((k, v) -> map.put(k, String.valueOf(v)));
        // }

        _passport_session = map.get("_passport_session");
        JSESSIONID = map.get("JSESSIONID");
        route = map.get("route");
        BIGipServerotn = map.get("BIGipServerotn");
        BIGipServerpassport = map.get("BIGipServerpassport");

        uamtk = map.get("uamtk");
        JSESSIONID = map.get("JSESSIONID");
        tk = map.get("tk");
        uKey = map.get("uKey");

        // uamtk = "6OY_HOwOdXU__x0_3NaCDmPwipVBwPtJubh1h0";
        // JSESSIONID = "01802E72310736147CF6BBE3C541866F"; // /otn
        // tk = "4AC3rZPWFQOFlgi6f6Qls1ReRfaN_geBeth1h0";
        // uKey = "";

        cookieString = "Cookie: JSESSIONID=" + JSESSIONID + "; tk=" + tk + "; route=" + route
                + "; BIGipServerotn=" + BIGipServerotn + "; BIGipServerpassport=" + BIGipServerpassport + defaultCookie
                + "; uKey=" + uKey;
    }

    private void check() {

        if (username == null) {
            throw new RuntimeException("username invalid");
        }
        if (password == null || password.length() < 6) {
            throw new RuntimeException("password invalid");
        }
        if (idCardNumber == null || idCardNumber.length() < 4) {
            throw new RuntimeException("idCardNumber invalid");
        }
    }

    private void config() {

        url = "https://kyfw.12306.cn/otn/login/conf";
        header = "Host: kyfw.12306.cn\n";
        // + "Connection: keep-alive\n" + "Content-Length: 0\n"
        //     + "Pragma: no-cache\n" + "Cache-Control: no-cache\n"
        //     + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
        //     + "Accept: */*\n" + "X-Requested-With: XMLHttpRequest\n" + "sec-ch-ua-mobile: ?0\n"
        //     + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
        //     + "sec-ch-ua-platform: \"Windows\"\n" + "Origin: https://kyfw.12306.cn\n" + "Sec-Fetch-Site: same-origin\n"
        //     + "Sec-Fetch-Mode: cors\n" + "Sec-Fetch-Dest: empty\n"
        //     + "Referer: https://kyfw.12306.cn/otn/resources/login.html\n" + "Accept-Encoding: gzip, deflate, br, zstd\n"
        //     + "Accept-Language: zh-CN,zh;q=0.9\n";

        response = HttpClient.post(url, header, Map.class);

        System.out.println("\n\nconfig: " + response.getBody());

        data = (Map)response.getBody().get("data");
        popup_is_uam_login = (String) data.get("is_uam_login");
        popup_is_login_passCode = (String) data.get("is_login_passCode");
        popup_is_sweep_login = (String) data.get("is_sweep_login");
        popup_is_login = (String) data.get("is_login");
        String is_message_passCode = (String) data.get("is_message_passCode");
        if (is_message_passCode == null || "".equals(is_message_passCode) || "Y".equals(is_message_passCode)) {
            popup_is_message_passCode = "Y";
        } else {
            popup_is_message_passCode = "N";
        }
        if (!"Y".equals(popup_is_uam_login)) {
            throw new UnsupportedOperationException("is_uam_login: " + popup_is_uam_login);
        }

        // Set-Cookie: route=495c805987d0f5c8c84b14f60212447d; Path=/
        // Set-Cookie: JSESSIONID=D03B6646E292240404E476B5A01DDA5A; Path=/otn
        // Set-Cookie: BIGipServerotn=484966666.64545.0000; path=/


        cookies = parseCookie(response.getHeaders().get("Set-Cookie"));
        route = cookies.get("route");
        JSESSIONID = cookies.get("JSESSIONID"); // /passport
        BIGipServerotn = cookies.get("BIGipServerotn");
        if (route == null || JSESSIONID == null || BIGipServerotn == null) {
            throw new RuntimeException("route == null || JSESSIONID == null || BIGipServerotn == null" + route + "_"
                    + JSESSIONID + "_" + BIGipServerotn);
        }

    }

    private void login() {

        config();

        url = "https://kyfw.12306.cn/passport/web/auth/uamtk-static";
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n" + "Content-Length: 9\n" + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "Accept: */*\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                // + "X-Requested-With: XMLHttpRequest\n" + "sec-ch-ua-mobile: ?0\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                // + "sec-ch-ua-platform: \"Windows\"\n" + "Origin: https://kyfw.12306.cn\n" + "Sec-Fetch-Site: same-origin\n"
                // + "Sec-Fetch-Mode: cors\n" + "Sec-Fetch-Dest: empty\n"
                // + "Referer: https://kyfw.12306.cn/otn/resources/login.html\n" + "Accept-Encoding: gzip, deflate, br, zstd\n"
                // + "Accept-Language: zh-CN,zh;q=0.9\n"
                + "Cookie: route=" + route + "; BIGipServerotn=" + BIGipServerotn;
        body = "appid=" + popup_passport_appId;
        response = HttpClient.post(url, header, body, Map.class);
        // Set-Cookie: _passport_session=46162d6937c14441bbffde73af38a7ff8693; Path=/passport
        // Set-Cookie: BIGipServerpassport=937951498.50215.0000; path=/
        cookies = parseCookie(response.getHeaders().get("Set-Cookie"));
        _passport_session = cookies.get("_passport_session");
        BIGipServerpassport = cookies.get("BIGipServerpassport");
        if (_passport_session == null || BIGipServerpassport == null) {
            throw new RuntimeException("_passport_session == null || BIGipServerpassport == null" + _passport_session
                    + "_" + BIGipServerpassport);
        }

        url = "https://kyfw.12306.cn/passport/web/checkLoginVerify";
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n" + "Content-Length: 25\n" + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Google Chrome\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"\n"
                // + "Accept: */*\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                // + "X-Requested-With: XMLHttpRequest\n" + "sec-ch-ua-mobile: ?0\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36\n"
                // + "sec-ch-ua-platform: \"Windows\"\n" + "Origin: https://kyfw.12306.cn\n" + "Sec-Fetch-Site: same-origin\n"
                // + "Sec-Fetch-Mode: cors\n" + "Sec-Fetch-Dest: empty\n"
                // + "Referer: https://kyfw.12306.cn/otn/resources/login.html\n" + "Accept-Encoding: gzip, deflate, br, zstd\n"
                // + "Accept-Language: zh-CN,zh;q=0.9\n"
                + "Cookie: _passport_session=" + _passport_session + "; route="
                + route + "; BIGipServerotn=" + BIGipServerotn + "; BIGipServerpassport=" + BIGipServerpassport
                + defaultCookie;

        body = "username=" + username + "&appid=" + popup_qr_appId;

        response = HttpClient.post(url, header, body, Map.class);
        String login_check_code = (String)response.getBody().get("login_check_code");
        if (!"3".equals(login_check_code)) {
            throw new UnsupportedOperationException("login_check_code: " + login_check_code);
        }

        System.out.println("\n\nCookie: _passport_session=" + _passport_session + "; JSESSIONID=" + JSESSIONID + "; route="
                + route + "; BIGipServerotn=" + BIGipServerotn + "; BIGipServerpassport=" + BIGipServerpassport
                + defaultCookie);

        // 一天只能发十次短信
        url = "https://kyfw.12306.cn/passport/web/getMessageCode";
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n" + "Content-Length: 38\n" + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "Accept: */*\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                // + "X-Requested-With: XMLHttpRequest\n" + "sec-ch-ua-mobile: ?0\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                // + "sec-ch-ua-platform: \"Windows\"\n" + "Origin: https://kyfw.12306.cn\n" + "Sec-Fetch-Site: same-origin\n"
                // + "Sec-Fetch-Mode: cors\n" + "Sec-Fetch-Dest: empty\n"
                // + "Referer: https://kyfw.12306.cn/otn/resources/login.html\n" + "Accept-Encoding: gzip, deflate, br, zstd\n"
                // + "Accept-Language: zh-CN,zh;q=0.9\n"
                + "Cookie: _passport_session=" + _passport_session + "; route="
                + route + "; BIGipServerotn=" + BIGipServerotn + "; BIGipServerpassport=" + BIGipServerpassport
                + defaultCookie;
        String castNum = idCardNumber.substring(idCardNumber.length() - 4); // 后四位
        body = "appid=" + popup_qr_appId + "&username=" + username + "&castNum=" + castNum;
        response = HttpClient.post(url, header, body, Map.class);
        result_code = (Integer)response.getBody().get("result_code");
        result_message = (String)response.getBody().get("result_message");
        // 0/6/11
        if (0 != result_code) {
            throw new UnsupportedOperationException("发送短信失败: " + result_code + "_" + result_message);
        }
        System.out.println(result_message);
        System.out.print("请输入短信验证码：");
        Scanner scanner = new Scanner(System.in);
        String smsCode_input = scanner.nextLine();

        // FIXME 用户输入短信验证码
        smsCode = smsCode_input;

        if (smsCode == null || smsCode.length() < 6) {
            throw new RuntimeException("smsCode invalid");
        }

        if (!"Y".equals(popup_is_uam_login)) {
            throw new UnsupportedOperationException("is_uam_login: " + popup_is_uam_login);
        }

        System.out.println("\n\nsmsCode: " + smsCode);


        // smsCode = "460248";

        String paste = "N";
        url = "https://kyfw.12306.cn/passport/web/login";
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                // + "Accept: application/json, text/javascript, */*; q=0.01\n"
                // + "Origin: https://kyfw.12306.cn\n"
                //     + "Referer: https://kyfw.12306.cn/otn/resources/login.html\n"
                // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                // // + "Content-Length: 150\n"
                // + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "sec-ch-ua-mobile: ?0\n"
                // + "sec-ch-ua-platform: \"Windows\"\n"
                // + "Sec-Fetch-Site: same-origin\n" + "Sec-Fetch-Mode: cors\n"
                // + "Sec-Fetch-Dest: empty\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                + "isPasswordCopy: "
                + paste + "\n" +
                // isOpen = typeof(EsdToolbar)=='object'&&EsdToolbar.isOpen
                "appFlag: " + (isOpen ? "otnLove" : "") + "\n" + "Cookie: _passport_session=" + _passport_session
                + "; route=" + route + "; BIGipServerotn=" + BIGipServerotn + "; BIGipServerpassport=" + BIGipServerpassport
                + defaultCookie;

        byte[] pass = new byte[0];
        try {
            pass = SM4Util.encryptEcb(password, SM4_key);
        } catch (Exception e) {
            throw new RuntimeException("密码加密失败", e);
        }
        String encryptedPassword = "@" + Base64.getEncoder().encodeToString(pass);
        encryptedPassword = UrlUtil.encode(encryptedPassword);
        body = "sessionId=&sig=&if_check_slide_passcode_token=&scene=&checkMode=0&randCode=" + smsCode + "&username="
                + username + "&password=" + encryptedPassword + "&appid=" + popup_passport_appId;
        response = HttpClient.post(url, header, body, Map.class);
        result_code = (Integer)response.getBody().get("result_code");
        result_message = (String)response.getBody().get("result_message");
        uamtk = (String)response.getBody().get("uamtk");
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
                throw new RuntimeException("您的密码很久没有修改了，为降低安全风险，请您重新设置密码后再登录，地址：" + popup_baseUrl + popup_publicName
                        + "/forgetPassword/initforgetMyPassword");
            default:
                throw new RuntimeException(result_message);
        }
        if (control) {
            throw new UnsupportedOperationException("control: " + result_code + "_" + result_message);
        }
        System.out.println(result_message);

        System.out.println("\n\nuamtk: " + uamtk);

        popup_loginCallBack();
    }

    private void loginQr() {

        config();

        // 这个接口仅用来获取 _passport_session，正常流程并不需要调用
        url = "https://kyfw.12306.cn/passport/web/auth/uamtk-static";
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n" + "Content-Length: 9\n" + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "Accept: */*\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                // + "X-Requested-With: XMLHttpRequest\n" + "sec-ch-ua-mobile: ?0\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                // + "sec-ch-ua-platform: \"Windows\"\n" + "Origin: https://kyfw.12306.cn\n" + "Sec-Fetch-Site: same-origin\n"
                // + "Sec-Fetch-Mode: cors\n" + "Sec-Fetch-Dest: empty\n"
                // + "Referer: https://kyfw.12306.cn/otn/resources/login.html\n" + "Accept-Encoding: gzip, deflate, br, zstd\n"
                // + "Accept-Language: zh-CN,zh;q=0.9\n"
                + "Cookie: route=" + route + "; BIGipServerotn=" + BIGipServerotn;
        body = "appid=" + popup_passport_appId;
        response = HttpClient.post(url, header, body, Map.class);
        // Set-Cookie: _passport_session=46162d6937c14441bbffde73af38a7ff8693; Path=/passport
        // Set-Cookie: BIGipServerpassport=937951498.50215.0000; path=/
        cookies = parseCookie(response.getHeaders().get("Set-Cookie"));
        _passport_session = cookies.get("_passport_session");
        BIGipServerpassport = cookies.get("BIGipServerpassport");
        if (_passport_session == null || BIGipServerpassport == null) {
            throw new RuntimeException("_passport_session == null || BIGipServerpassport == null" + _passport_session
                    + "_" + BIGipServerpassport);
        }

        url = "https://kyfw.12306.cn/passport/web/create-qr64";
        header = "Host: kyfw.12306.cn\n"
                // "Connection: keep-alive\n" +
                // "Content-Length: 9\n" +
                // "Pragma: no-cache\n" +
                // "Cache-Control: no-cache\n" +
                // "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n" +
                // "Accept: */*\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                // "X-Requested-With: XMLHttpRequest\n" +
                // "sec-ch-ua-mobile: ?0\n" +
                // "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n" +
                // "sec-ch-ua-platform: \"Windows\"\n" +
                // "Origin: https://kyfw.12306.cn\n" +
                // "Sec-Fetch-Site: same-origin\n" +
                // "Sec-Fetch-Mode: cors\n" +
                // "Sec-Fetch-Dest: empty\n" +
                // "Referer: https://kyfw.12306.cn/otn/resources/login.html\n" +
                // "Accept-Encoding: gzip, deflate, br, zstd\n" +
                // "Accept-Language: zh-CN,zh;q=0.9"
                // // 这边本身并不需要传cookie
                // + "Cookie: _passport_session=" + _passport_session + "; route="
                // + route + "; BIGipServerotn=" + BIGipServerotn + "; BIGipServerpassport=" + BIGipServerpassport
                // + defaultCookie;
        ;
        body = "appid=" + popup_qr_appId;
        // 忽略所有错误
        response = HttpClient.post(url, header, body, Map.class);
        data = (Map) response.getBody(); // .get("data")
        String result_code_str = (String) data.get("result_code");
        String result_message = (String) data.get("result_message");
        String uuid = (String) data.get("uuid");
        String image = (String) data.get("image");
        if (data == null || !"0".equals(result_code_str) || image == null) {
            throw new RuntimeException("未知错误: " + response);
        }

        byte[] bytes = Base64.getDecoder().decode(image);
        try {
            Files.write(Paths.get(qrCodeFilePath), bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException("二维码下载失败", e);
        }

        System.out.println("\n\n请手机扫码，图片位置：" + qrCodeFilePath);

        // cookies = parseCookie(response.getHeaders().get("Set-Cookie"));
        // BIGipServerpassport = cookies.get("BIGipServerpassport");
        // _passport_session = ""; // TODO 这个没给 额外添加个 /uamtk-static 接口获取

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger popup_s = new AtomicInteger(-1);
        // setInterval 1000
        Runnable popup_t = () -> {
            if (popup_s.get() == 2 || popup_s.get() == 3) {
                // clearInterval(popup_t);
                latch.countDown();
                scheduledExecutorService.shutdown();
                return; // stop interval
            }

            // 轮询调用二维码检查接口，直至返回状态为2：登录成功，（已识别且已授权）、3：已失效
            // popup_checkQr(uuid);
            String url2 = "https://kyfw.12306.cn/passport/web/checkqr";
            String header2 = "Host: kyfw.12306.cn\n"
                    // "Connection: keep-alive\n" +
                    // "Content-Length: 94\n" +
                    // "Pragma: no-cache\n" +
                    // "Cache-Control: no-cache\n" +
                    // "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n" +
                    // "Accept: */*\n" +
                    + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                    // "X-Requested-With: XMLHttpRequest\n" +
                    // "sec-ch-ua-mobile: ?0\n" +
                    // "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n" +
                    // "sec-ch-ua-platform: \"Windows\"\n" +
                    // "Origin: https://kyfw.12306.cn\n" +
                    // "Sec-Fetch-Site: same-origin\n" +
                    // "Sec-Fetch-Mode: cors\n" +
                    // "Sec-Fetch-Dest: empty\n" +
                    // "Referer: https://kyfw.12306.cn/otn/resources/login.html\n" +
                    // "Accept-Encoding: gzip, deflate, br, zstd\n" +
                    // "Accept-Language: zh-CN,zh;q=0.9\n"
                    // "Cookie: BIGipServerpassport=870842634.50215.0000"
                    + cookieString
                    ;
            String body2 =
                    // "RAIL_DEVICEID=" + // RAIL_DEVICEID: $.cookie("RAIL_DEVICEID")
                    // "&RAIL_EXPIRATION=" + // RAIL_EXPIRATION: $.cookie("RAIL_EXPIRATION")
                    "uuid=" + uuid + "&appid=" + popup_qr_appId;
            // 忽略所有异常
            ResponseEntity<Map> response2 = HttpClient.post(url2, header2, body2, Map.class);
            Map data = (Map) response2.getBody(); // .get("data");
            int result_code = Integer.parseInt((String) data.get("result_code"));

            System.out.println(result_code);

            popup_s.set(result_code);
            // popup_tipsQrInfo(result_code);
            // 0：未识别、
            // 1：已识别，暂未授权（未点击授权或不授权）、
            // 2：登录成功，（已识别且已授权）、
            // 3：已失效、
            // 5系统异常
            int resCode = result_code;
            switch (resCode) {
                case 0:
                    break;
                case 1:
                    // codeTips.hide()
                    // codeTipsSuccess.removeClass('hide')
                    break;
                case 2:
                    cookies = parseCookie(response2.getHeaders().get("Set-Cookie"));
                    uamtk = cookies.get("uamtk"); // /passport
                    System.out.println("\n\nuamtk: " + uamtk);
                    // codeTips.hide()
                    // codeTipsSuccess.removeClass('hide')
                    // 成功回调
                    popup_loginCallBack();
                    break;
                case 3:
                    throw new RuntimeException("二维码已失效");
                // case 4:
                //     break;
                case 5:
                    throw new RuntimeException("系统异常");
                default:
                    throw new RuntimeException("二维码已失效");
            }
        };

        int delay = 1000;
        scheduledExecutorService.scheduleWithFixedDelay(popup_t, 0, delay, TimeUnit.MILLISECONDS);

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("qrcode登录失败", e);
        }
    }

    private void popup_loginCallBack() {
        if (uamtk == null) {
            throw new RuntimeException("uamtk == null");
        }

        // userLogin 拿JSESSIONID，没有就用原来的
        url = "https://kyfw.12306.cn/otn/login/userLogin";
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n" + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "sec-ch-ua-mobile: ?0\n" + "sec-ch-ua-platform: \"Windows\"\n" + "Upgrade-Insecure-Requests: 1\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                // + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n"
                // + "Sec-Fetch-Site: same-origin\n" + "Sec-Fetch-Mode: navigate\n" + "Sec-Fetch-Dest: document\n"
                // + "Referer: https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin\n"
                // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                // 这边不要带 /passport 的 JSESSIONID
                + "Cookie: "
                // +"JSESSIONID=" + JSESSIONID + "; "
                + "route=" + route + "; BIGipServerotn=" + BIGipServerotn + "; BIGipServerpassport="
                + BIGipServerpassport + defaultCookie;
        response_type_string = HttpClient.get(url, header, String.class);
        cookies = parseCookie(response_type_string.getHeaders().get("Set-Cookie"));
        // 没有cookies，表示此处无需更换JSESSIONID
        System.out.println("before JSESSIONID=" + JSESSIONID);
        if (!cookies.isEmpty()) {
            JSESSIONID = cookies.get("JSESSIONID"); // /otn
            System.out.println("\n\n/otn JSESSIONID=" + JSESSIONID);
        }

        System.out.println("now JSESSIONID=" + JSESSIONID);
        System.out.println("use before JSESSIONID=" + cookies.isEmpty());

        // TODO 任何接口调用，会返回Set-Cookie，提供新的JSESSIONID，清除uKey/tk，重定向登录
        //  然后从这开始重新走登录逻辑，通过uamtk换取tk/uKey，刷新登录状态，类似于refresh-token逻辑；如果用户登录状态过期了，会返回错误 {"result_message":"用户未登录","result_code":1}
        //  _passport_session uamtk 也换掉，更新的tk和uamtk一样
        //  流程：任意接口(get/post)302重定向，conf判断，uamtk重置session，userLogin重定向登录，uamtk换tk，uamauthclient验证tk，userLogin换uKey
        //  任何接口的错误都有可能清除用户登录状态，如cookie参数错误等
        url = "https://kyfw.12306.cn/passport/web/auth/uamtk";
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n" + "Content-Length: 9\n" + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "Accept: application/json, text/javascript, */*; q=0.01\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                //     + "X-Requested-With: XMLHttpRequest\n"
                // + "sec-ch-ua-mobile: ?0\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                // + "sec-ch-ua-platform: \"Windows\"\n" + "Origin: https://kyfw.12306.cn\n" + "Sec-Fetch-Site: same-origin\n"
                // + "Sec-Fetch-Mode: cors\n" + "Sec-Fetch-Dest: empty\n"
                // + "Referer: https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin\n"
                // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                + "Cookie: uamtk="
                + uamtk + "; _passport_session=" + _passport_session + "; route=" + route + "; BIGipServerotn="
                + BIGipServerotn + "; BIGipServerpassport=" + BIGipServerpassport + defaultCookie;
        body = "appid=" + popup_passport_appId;
        response = HttpClient.post(url, header, body, Map.class);
        result_code = (Integer)response.getBody().get("result_code");
        result_message = (String)response.getBody().get("result_message");
        String newapptk = (String)response.getBody().get("newapptk");
        tk = newapptk;
        if (result_code != 0) {
            throw new RuntimeException("未知错误: " + result_code + "_" + result_message + "，逻辑是未登录走到这");
        }

        System.out.println("\n\ntk: " + tk);

        url = "https://kyfw.12306.cn/otn/uamauthclient";
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n" + "Content-Length: 41\n" + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "Accept: */*\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                // + "X-Requested-With: XMLHttpRequest\n" + "sec-ch-ua-mobile: ?0\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                // + "sec-ch-ua-platform: \"Windows\"\n" + "Origin: https://kyfw.12306.cn\n" + "Sec-Fetch-Site: same-origin\n"
                // + "Sec-Fetch-Mode: cors\n" + "Sec-Fetch-Dest: empty\n"
                // + "Referer: https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin\n"
                // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                + "Cookie: JSESSIONID="
                + JSESSIONID + "; route=" + route + "; BIGipServerotn=" + BIGipServerotn + "; BIGipServerpassport="
                + BIGipServerpassport + defaultCookie;
        body = "tk=" + tk;
        response = HttpClient.post(url, header, body, Map.class);
        result_code = (Integer)response.getBody().get("result_code");
        result_message = (String)response.getBody().get("result_message");
        if (result_code != 0) {
            throw new RuntimeException("未知错误: " + result_code + "_" + result_message + "，逻辑是未登录走到这");
        }

        url = "https://kyfw.12306.cn/otn/login/userLogin";
        header = "Host: kyfw.12306.cn\n"
                + "Connection: keep-alive\n" + "Pragma: no-cache\n"
                + "Cache-Control: no-cache\n"
                + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                + "sec-ch-ua-mobile: ?0\n" + "sec-ch-ua-platform: \"Windows\"\n" + "Upgrade-Insecure-Requests: 1\n"
                + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n"
                + "Sec-Fetch-Site: same-origin\n" + "Sec-Fetch-Mode: navigate\n" + "Sec-Fetch-Dest: document\n"
                + "Referer: https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin\n"
                + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                + "Cookie: JSESSIONID="
                + JSESSIONID + "; tk=" + tk + "; route=" + route + "; BIGipServerotn=" + BIGipServerotn
                + "; BIGipServerpassport=" + BIGipServerpassport + defaultCookie;
        response_type_string = HttpClient.get(url, header, String.class);
        cookies = parseCookie(response_type_string.getHeaders().get("Set-Cookie"));
        uKey = cookies.get("uKey");
        if (uKey == null) {
            throw new RuntimeException("uKey == null，未知错误: " + result_code + "_" + result_message);
        }
        System.out.println("\n\nuKey: " + uKey);

        JSONObject loginInfo = new JSONObject();
        // loginInfo.put("uKey", uKey);
        loginInfo.put("_passport_session", _passport_session);
        // loginInfo.put("JSESSIONID", JSESSIONID);
        loginInfo.put("route", route);
        loginInfo.put("BIGipServerotn", BIGipServerotn);
        loginInfo.put("BIGipServerpassport", BIGipServerpassport);
        loginInfo.put("uamtk", uamtk);
        loginInfo.put("JSESSIONID", JSESSIONID);
        loginInfo.put("tk", tk);
        loginInfo.put("uKey", uKey);
        System.out.println("\n\n" + loginInfo);

        // TODO 目前无需要调用的情况
        // https://kyfw.12306.cn/otn/login/conf
        // https://kyfw.12306.cn/otn/index/initMy12306Api
    }

    private Map<String, Map<String, String>> queryG(String train_date) {

        String CLeftTicketUrl = "leftTicket/queryG"; // 页面获取
        // String train_date = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String from_station = StationUtil.getStations().get(fromStation).get("code");
        String to_station = StationUtil.getStations().get(toStation).get("code");
        String _jc_save_fromStation = EscapeUtil.escape_js(fromStation + "," + from_station); // 上海,SHH
        String _jc_save_toStation = EscapeUtil.escape_js(toStation + "," + to_station);
        String _jc_save_fromDate = train_date;
        String _jc_save_toDate = train_date;
        String _jc_save_wfdc_flag = "dc"; // 单程 dc 往返 wf

        cookieString = "Cookie: JSESSIONID=" + JSESSIONID + "; tk=" + tk + "; route=" + route
                + "; BIGipServerotn=" + BIGipServerotn + "; BIGipServerpassport=" + BIGipServerpassport + defaultCookie
                + "; uKey=" + uKey
        // + "; _jc_save_fromStation=" + _jc_save_fromStation + "; _jc_save_fromDate="
        // + _jc_save_fromDate + "; _jc_save_toDate=" + _jc_save_toDate + "; _jc_save_wfdc_flag=" + _jc_save_wfdc_flag
        // + "; _jc_save_toStation=" + _jc_save_toStation
        ;

        while (true) {
            url = "https://kyfw.12306.cn/otn/" + CLeftTicketUrl + "?leftTicketDTO.train_date=" + train_date
                    + "&leftTicketDTO.from_station=" + from_station + "&leftTicketDTO.to_station=" + to_station
                    + "&purpose_codes=" + getPurposeCodes();
            header = "Host: kyfw.12306.cn\n"
                    // 禁止缓存
                    + "If-Modified-Since: 0\n"
                    + "Cache-Control: no-cache\n"
                    //     + "Connection: keep-alive\n" + "Pragma: no-cache\n"
                    // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                    // + "sec-ch-ua-mobile: ?0\n"
                    // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                    // + "Accept: */*\n" + "X-Requested-With: XMLHttpRequest\n"
                    // + "sec-ch-ua-platform: \"Windows\"\n" + "Sec-Fetch-Site: same-origin\n" + "Sec-Fetch-Mode: cors\n"
                    // + "Sec-Fetch-Dest: empty\n" + "Referer: https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc\n"
                    // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                    + cookieString
            // + "Cookie: JSESSIONID=" + JSESSIONID + "; tk=" + tk + "; route=" + route + "; BIGipServerotn="
            // + BIGipServerotn + "; BIGipServerpassport=" + BIGipServerpassport + defaultCookie + "; uKey=" + uKey
            // + "; _jc_save_fromStation=" + _jc_save_fromStation + "; _jc_save_fromDate=" + _jc_save_fromDate
            // + "; _jc_save_toDate=" + _jc_save_toDate + "; _jc_save_wfdc_flag=" + _jc_save_wfdc_flag
            // + "; _jc_save_toStation=" + _jc_save_toStation
            ;
            response = HttpClient.get(url, header, Map.class);
            status = (Boolean)response.getBody().get("status");
            result_message = (String)response.getBody().get("messages");
            if (!status) {
                String c_url = (String)response.getBody().get("c_url");
                if (c_url == null) {
                    throw new RuntimeException("未知错误: " + status + "_" + result_message);
                }
                CLeftTicketUrl = c_url;
                // 重新调用兼容的查询接口
                continue;
            }
            data = (Map)response.getBody().get("data");
            if (data == null || data.get("result") == null || ((List<String>)data.get("result")).isEmpty()) {
                String lc_search_url = "/lcquery/queryG";
                throw new RuntimeException("出现错误，得通过其他接口查询，lc_search_url: " + lc_search_url);
            }
            break; // 结束 while
        }
        String flag = (String)data.get("flag");
        if (!"1".equals(flag)) {
            throw new RuntimeException("未知错误：flag: " + flag);
        }
        // if ("1".equals(flag)) {
        //      // data -> stationMap
        // }

        // train_no -> info
        Map<String, Map<String, String>> stationMap = new LinkedHashMap<>();
        List<String> result = (List<String>)data.get("result");
        Map<String, String> map = (Map<String, String>)data.get("map");
        for (String stationInfo : result) {
            // Oy1n3fY3wHl5K7F3QBADSZhu3Gt6nIrmTSKwbGPrW9kVv7sxrMRIsckP2yblzvsMVlZJuWXcqse0NgPhUaqki8uKa96lc8QU4XthZ3SzswRZQ7MyN35hn6RFTIk6mlpwlCAr8J3xxzKD9vS4szs/nBj5OvV37q2csNoHoekd75wLY/BqP3aWr8lJALTNXnX2cKcPKCagUK3q4Fu0eYMrBceLHYPMpEmV/TchjcvfDsU0QUK15EB+lUg7Psb1lEXBYvIW57jNrWB5ztqf0AOVhslOwNVeo/myZ8fSmkYXdwmuhQgG/YIK1PrnnO/56WQuQwdNn/vEZk8aQrmUY/Yncg==
            // |预订|5l000D301002|D3010|AOH|HKN|AOH|CZH|14:42|15:44|01:02|Y|0G8LkaB6U6b1/gJtmhT+sByCIKGR3+ESnvDmPIhFkfBqYCIk|20240422|3|H3|01|04|1|0|||||||有||||3|14|||M0O0W0|MOO|1|0||M009800014O006100003O006103076|0|||||1|0#0#0#0#z#0#z||7|CHN,CHN|||N#N#|||202404081330|
            String[] stationInfos = stationInfo.split("\\|");

            Map<String, String> stationInfoMap = new HashMap<>();
            stationInfoMap.put("secretStr", stationInfos[0]);
            // "Y" == df[dn].queryLeftNewDTO.canWebBuy 14点30分起售 canWebBuy IS_TIME_NOT_BUY
            stationInfoMap.put("buttonTextInfo", stationInfos[1]); // TODO 14点30分起售
            stationInfoMap.put("train_no", stationInfos[2]); // 5l000D301002
            stationInfoMap.put("station_train_code", stationInfos[3]); // D3010
            stationInfoMap.put("start_station_telecode", stationInfos[4]);
            stationInfoMap.put("end_station_telecode", stationInfos[5]);
            stationInfoMap.put("from_station_telecode", stationInfos[6]);
            stationInfoMap.put("to_station_telecode", stationInfos[7]);
            stationInfoMap.put("start_time", stationInfos[8]);
            stationInfoMap.put("arrive_time", stationInfos[9]);
            stationInfoMap.put("lishi", stationInfos[10]);
            stationInfoMap.put("canWebBuy", stationInfos[11]);
            stationInfoMap.put("yp_info", stationInfos[12]);
            stationInfoMap.put("start_train_date", stationInfos[13]);
            stationInfoMap.put("train_seat_feature", stationInfos[14]);
            stationInfoMap.put("location_code", stationInfos[15]);
            stationInfoMap.put("from_station_no", stationInfos[16]);
            stationInfoMap.put("to_station_no", stationInfos[17]);
            stationInfoMap.put("is_support_card", stationInfos[18]);
            stationInfoMap.put("controlled_train_flag", stationInfos[19]);
            stationInfoMap.put("gg_num",
                    stationInfos[20] == null || stationInfos[20].isEmpty() ? "--" : stationInfos[20]);
            stationInfoMap.put("gr_num",
                    stationInfos[21] == null || stationInfos[21].isEmpty() ? "--" : stationInfos[21]);
            stationInfoMap.put("qt_num",
                    stationInfos[22] == null || stationInfos[22].isEmpty() ? "--" : stationInfos[22]);
            stationInfoMap.put("rw_num",
                    stationInfos[23] == null || stationInfos[23].isEmpty() ? "--" : stationInfos[23]);
            stationInfoMap.put("rz_num",
                    stationInfos[24] == null || stationInfos[24].isEmpty() ? "--" : stationInfos[24]);
            stationInfoMap.put("tz_num",
                    stationInfos[25] == null || stationInfos[25].isEmpty() ? "--" : stationInfos[25]);
            stationInfoMap.put("wz_num",
                    stationInfos[26] == null || stationInfos[26].isEmpty() ? "--" : stationInfos[26]);
            stationInfoMap.put("yb_num",
                    stationInfos[27] == null || stationInfos[27].isEmpty() ? "--" : stationInfos[27]);
            stationInfoMap.put("yw_num",
                    stationInfos[28] == null || stationInfos[28].isEmpty() ? "--" : stationInfos[28]);
            stationInfoMap.put("yz_num",
                    stationInfos[29] == null || stationInfos[29].isEmpty() ? "--" : stationInfos[29]);
            stationInfoMap.put("ze_num",
                    stationInfos[30] == null || stationInfos[30].isEmpty() ? "--" : stationInfos[30]);
            stationInfoMap.put("zy_num",
                    stationInfos[31] == null || stationInfos[31].isEmpty() ? "--" : stationInfos[31]);
            stationInfoMap.put("swz_num",
                    stationInfos[32] == null || stationInfos[32].isEmpty() ? "--" : stationInfos[32]);
            stationInfoMap.put("srrb_num",
                    stationInfos[33] == null || stationInfos[33].isEmpty() ? "--" : stationInfos[33]);
            stationInfoMap.put("yp_ex", stationInfos[34]);
            stationInfoMap.put("seat_types", stationInfos[35]);
            stationInfoMap.put("exchange_train_flag", stationInfos[36]);
            stationInfoMap.put("houbu_train_flag", stationInfos[37]);
            stationInfoMap.put("houbu_seat_limit", stationInfos[38]);
            stationInfoMap.put("yp_info_new", stationInfos[39]);
            // 40-45 无
            stationInfoMap.put("dw_flag", stationInfos[46]);
            // 47 无
            stationInfoMap.put("stopcheckTime", stationInfos[48]);
            stationInfoMap.put("country_flag", stationInfos[49]);
            stationInfoMap.put("local_arrive_time", stationInfos[50]);
            stationInfoMap.put("local_start_time", stationInfos[51]);
            // 52 无
            stationInfoMap.put("bed_level_info", stationInfos[53]);
            stationInfoMap.put("seat_discount_info", stationInfos[54]);
            stationInfoMap.put("sale_time", stationInfos[55]);
            stationInfoMap.put("from_station_name", map.get(stationInfos[6]));
            stationInfoMap.put("to_station_name", map.get(stationInfos[7]));
            // queryLeftNewDTO -> stationInfoMap
            stationMap.put(stationInfos[3], stationInfoMap);
        }

        // Map<String, String> trainInfo = stationMap.get(selectedTrainCode);
        // String secretStr = trainInfo.get("secretStr"); // g
        // String start_time = trainInfo.get("start_time"); // d 往返情况校验时间，目前没用
        // String train_no = trainInfo.get("train_no"); // a
        // String from_station_telecode = trainInfo.get("from_station_telecode"); // b
        // String to_station_telecode = trainInfo.get("to_station_telecode"); // f
        // String bed_level_info = trainInfo.get("bed_level_info"); // e
        // String seat_discount_info = trainInfo.get("seat_discount_info"); // h
        //
        // // checkG1234('Jrj%2BKAUeO8u717u3h00g3tDb9N%2Fe05eWI%2BYno1IACKDsztSqE%2F61Rbrn81fQzsQeM7dY45%2B%2B4FLR%0AR43X8TWVdad6b%2Fdho2Rxv0VpPq2NZMeLSilyPetj7d6QmaD9RcgpLakAJ%2FW2qD5RhiFzJL94Qk4r%0AAu3LpJpgZ%2Fix2wPTECc69B6SNGyLXxQ4i%2FMecnXtsSZ17PnFVysr3WXzC2i5v9OxUHqGZ3a3uCZC%0Aa7yzY%2F2zC1q%2F8EzcYA5C1L8NDraOae0o7Drdl49C9DKplYvfCISxewebhFZUXsv%2BZEMGxKfBGfNn%0AQETrwRQTPZmMX8OWuZ5fLusSYmKPZL6Jl6kg2Q%3D%3D'
        // // ,'11:24','5l000G7126D0','AOH','CZH','','M0076O0075W0075')
        // // function checkG1234(g, d, a, b, f, e, h)
        // // submitOrderRequest(g, d, e, h)
        //
        // String c = "99999GGGGG";
        // String k = "##CCT##PPT##CPT##PXT##SBT##PBD##JOD##HPD##SHD##QTP##TSP##TJP##";
        // String j = "##CBP##DIP##JGK##ZEK##UUH##NKH##ESH##OHH##AOH##";
        // if (train_no.contains(c) && k.contains(from_station_telecode) && j.contains(to_station_telecode)) {
        //     throw new UnsupportedOperationException(
        //             "不支持的车次：" + train_no + "_" + from_station_telecode + "_" + to_station_telecode);
        // }

        return stationMap;
    }

    private void checkTrainNo(Map<String, String> trainInfo) {
        String secretStr = trainInfo.get("secretStr"); // g
        String start_time = trainInfo.get("start_time"); // d 往返情况校验时间，目前没用
        String train_no = trainInfo.get("train_no"); // a
        String from_station_telecode = trainInfo.get("from_station_telecode"); // b
        String to_station_telecode = trainInfo.get("to_station_telecode"); // f
        String bed_level_info = trainInfo.get("bed_level_info"); // e
        String seat_discount_info = trainInfo.get("seat_discount_info"); // h

        // checkG1234('Jrj%2BKAUeO8u717u3h00g3tDb9N%2Fe05eWI%2BYno1IACKDsztSqE%2F61Rbrn81fQzsQeM7dY45%2B%2B4FLR%0AR43X8TWVdad6b%2Fdho2Rxv0VpPq2NZMeLSilyPetj7d6QmaD9RcgpLakAJ%2FW2qD5RhiFzJL94Qk4r%0AAu3LpJpgZ%2Fix2wPTECc69B6SNGyLXxQ4i%2FMecnXtsSZ17PnFVysr3WXzC2i5v9OxUHqGZ3a3uCZC%0Aa7yzY%2F2zC1q%2F8EzcYA5C1L8NDraOae0o7Drdl49C9DKplYvfCISxewebhFZUXsv%2BZEMGxKfBGfNn%0AQETrwRQTPZmMX8OWuZ5fLusSYmKPZL6Jl6kg2Q%3D%3D'
        // ,'11:24','5l000G7126D0','AOH','CZH','','M0076O0075W0075')
        // function checkG1234(g, d, a, b, f, e, h)
        // submitOrderRequest(g, d, e, h)

        String c = "99999GGGGG";
        String k = "##CCT##PPT##CPT##PXT##SBT##PBD##JOD##HPD##SHD##QTP##TSP##TJP##";
        String j = "##CBP##DIP##JGK##ZEK##UUH##NKH##ESH##OHH##AOH##";
        if (train_no.contains(c) && k.contains(from_station_telecode) && j.contains(to_station_telecode)) {
            throw new UnsupportedOperationException(
                    "不支持的车次：" + train_no + "_" + from_station_telecode + "_" + to_station_telecode);
        }
    }

    private void checkUser() {

        url = "https://kyfw.12306.cn/otn/login/checkUser";
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n" + "Content-Length: 10\n" + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "sec-ch-ua-mobile: ?0\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                //     + "Accept: */*\n"
                // + "X-Requested-With: XMLHttpRequest\n" + "If-Modified-Since: 0\n" + "sec-ch-ua-platform: \"Windows\"\n"
                // + "Origin: https://kyfw.12306.cn\n" + "Sec-Fetch-Site: same-origin\n" + "Sec-Fetch-Mode: cors\n"
                // + "Sec-Fetch-Dest: empty\n" + "Referer: https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc\n"
                // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                + cookieString
        // + "Cookie: JSESSIONID="
        // + JSESSIONID + "; tk=" + tk + "; route=" + route + "; BIGipServerotn=" + BIGipServerotn
        // + "; BIGipServerpassport=" + BIGipServerpassport + defaultCookie + "; uKey=" + uKey
        // + "; _jc_save_fromStation=" + _jc_save_fromStation + "; _jc_save_fromDate=" + _jc_save_fromDate
        // + "; _jc_save_toDate=" + _jc_save_toDate + "; _jc_save_wfdc_flag=" + _jc_save_wfdc_flag
        // + "; _jc_save_toStation=" + _jc_save_toStation
        ;
        body = "_json_att=";
        response = HttpClient.post(url, header, body, Map.class);
        Boolean flag_boolean = (Boolean)((Map)response.getBody().get("data")).get("flag");
        if (!flag_boolean) {
            throw new UnsupportedOperationException("未知错误：" + response.getBody().get("data") + "，可能是登录信息过期了");
        }

        // TODO 用户会话过期的异常情况，先查询 /otn/login/checkUser ，然后查询 /otn/login/conf

        if (getPurposeCodes().equals("0X00")) {
            // String[]
            // studentComPerArr=['2020-04-01','2020-11-30','2020-12-01','2020-12-31','2021-01-01','2025-09-30'];
            throw new RuntimeException("学生票的乘车时间为每年的暑假6月1日至9月30日、寒假12月1日至3月31日，目前不办理学生票业务。");
        }
    }

    private void submitOrderRequest(Map<String, String> trainInfo, String train_date, String train_tour_flag, String tour_flag) {
        String secretStr = trainInfo.get("secretStr"); // g
        String start_time = trainInfo.get("start_time"); // d 往返情况校验时间，目前没用
        String train_no = trainInfo.get("train_no"); // a
        String from_station_telecode = trainInfo.get("from_station_telecode"); // b
        String to_station_telecode = trainInfo.get("to_station_telecode"); // f
        String bed_level_info = trainInfo.get("bed_level_info"); // e
        String seat_discount_info = trainInfo.get("seat_discount_info"); // h

        url = "https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n" + "Content-Length: 589\n" + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "Accept: */*\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                // + "X-Requested-With: XMLHttpRequest\n" + "sec-ch-ua-mobile: ?0\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                // + "sec-ch-ua-platform: \"Windows\"\n" + "Origin: https://kyfw.12306.cn\n" + "Sec-Fetch-Site: same-origin\n"
                // + "Sec-Fetch-Mode: cors\n" + "Sec-Fetch-Dest: empty\n"
                // + "Referer: https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc\n"
                // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                + cookieString
        //     + "Cookie: JSESSIONID="
        // + JSESSIONID + "; tk=" + tk + "; route=" + route + "; BIGipServerotn=" + BIGipServerotn
        // + "; BIGipServerpassport=" + BIGipServerpassport + defaultCookie + "; uKey=" + uKey
        // + "; _jc_save_fromStation=" + _jc_save_fromStation + "; _jc_save_fromDate=" + _jc_save_fromDate
        // + "; _jc_save_toDate=" + _jc_save_toDate + "; _jc_save_wfdc_flag=" + _jc_save_wfdc_flag
        // + "; _jc_save_toStation=" + _jc_save_toStation
        ;
        String back_train_date = train_date;
        // String train_tour_flag = "other"; // ?
        // String tour_flag = "dc"; // 需结合 $("#dc").is(":checked") 和 train_tour_flag 判断
        String dk = "undefined"; // checkusermdId != undefined ? "&_json_att=" + encodeURIComponent(checkusermdId) : "";
        body = "secretStr=" + secretStr + "&train_date=" + train_date + "&back_train_date=" + back_train_date
                + "&tour_flag=" + tour_flag + "&purpose_codes=" + getPurposeCodes() + "&query_from_station_name="
                + fromStation + "&query_to_station_name=" + toStation + "&bed_level_info=" + bed_level_info
                + "&seat_discount_info=" + seat_discount_info + "&" + dk;
        response = HttpClient.post(url, header, body, Map.class); // TODO 302未登录
        status = (Boolean) response.getBody().get("status");
        List<String> messages = (List<String>)response.getBody().get("messages");
        if (!status) {
            throw new RuntimeException("未知错误：" + messages);
        }
        String submitOrderRequest_response_data = (String)response.getBody().get("data");
        if ("1".equals(submitOrderRequest_response_data)) {
            System.out.println("\n\n您选择的列车距开车时间很近了，请确保有足够的时间办理安全检查、实名制验证及检票等手续，以免耽误您的旅行。");
        } else if (submitOrderRequest_response_data.startsWith("2")) {
            System.out.println("\n\n您选择的列车距开车时间很近了，进站约需" + submitOrderRequest_response_data.substring(1)
                    + "分钟，请确保有足够的时间办理安全检查、实名制验证及检票等手续，以免耽误您的旅行。");
        }
    }

    private String initDC(String train_tour_flag, String tour_flag) {

        String c9 = tour_flag;
        String c8 = train_tour_flag;

        String init_url = null;
        if (c8 != null) {
            if ("fc".equals(c8)) {
                init_url = "confirmPassenger/initFc";
            }
            if ("gc".equals(c8)) {
                init_url = "confirmPassenger/initGc";
            }
        }
        if ("dc".equals(c9)) {
            init_url = "confirmPassenger/initDc";
        } else {
            init_url = "confirmPassenger/initWc";
        }

        url = "https://kyfw.12306.cn/otn/" + init_url;
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n" + "Content-Length: 10\n" + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "sec-ch-ua-mobile: ?0\n" + "sec-ch-ua-platform: \"Windows\"\n" + "Upgrade-Insecure-Requests: 1\n"
                // + "Origin: https://kyfw.12306.cn\n"
                + "Content-Type: application/x-www-form-urlencoded\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                // + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n"
                // + "Sec-Fetch-Site: same-origin\n" + "Sec-Fetch-Mode: navigate\n" + "Sec-Fetch-User: ?1\n"
                // + "Sec-Fetch-Dest: document\n" + "Referer: https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc\n"
                // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                + cookieString
        //     + "Cookie: JSESSIONID="
        // + JSESSIONID + "; tk=" + tk + "; route=" + route + "; BIGipServerotn=" + BIGipServerotn
        // + "; BIGipServerpassport=" + BIGipServerpassport + defaultCookie + "; uKey=" + uKey
        // + "; _jc_save_fromStation=" + _jc_save_fromStation + "; _jc_save_fromDate=" + _jc_save_fromDate
        // + "; _jc_save_toDate=" + _jc_save_toDate + "; _jc_save_wfdc_flag=" + _jc_save_wfdc_flag
        // + "; _jc_save_toStation=" + _jc_save_toStation
        ;
        body = "_json_att=";
        ResponseEntity<String> response_string = HttpClient.post(url, header, body, String.class);
        String initDC_html = response_string.getBody();

        return initDC_html;
    }

    private String get_REPEAT_SUBMIT_TOKEN(String initDC_html) {

        Pattern globalRepeatSubmitTokenPattern = Pattern.compile("var globalRepeatSubmitToken = '(.*?)';");
        Matcher matcher = globalRepeatSubmitTokenPattern.matcher(initDC_html);
        if (!matcher.find()) {
            throw new RuntimeException("未知错误，找不到globalRepeatSubmitToken：" + initDC_html);
        }
        String globalRepeatSubmitToken = matcher.group(1);
        String REPEAT_SUBMIT_TOKEN = globalRepeatSubmitToken;

        System.out.println("\n\nREPEAT_SUBMIT_TOKEN: " + REPEAT_SUBMIT_TOKEN);

        return REPEAT_SUBMIT_TOKEN;
    }

    private Map<String, Map<String, String>> getPassengerDTOs(String REPEAT_SUBMIT_TOKEN) {

        // FIXME async
        url = "https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n" + "Content-Length: 63\n" + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "Accept: */*\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                // + "X-Requested-With: XMLHttpRequest\n" + "sec-ch-ua-mobile: ?0\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                // + "sec-ch-ua-platform: \"Windows\"\n" + "Origin: https://kyfw.12306.cn\n" + "Sec-Fetch-Site: same-origin\n"
                // + "Sec-Fetch-Mode: cors\n" + "Sec-Fetch-Dest: empty\n"
                // + "Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc\n"
                // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                + cookieString
        //     + "Cookie: JSESSIONID="
        // + JSESSIONID + "; tk=" + tk + "; route=" + route + "; BIGipServerotn=" + BIGipServerotn
        // + "; BIGipServerpassport=" + BIGipServerpassport + defaultCookie + "; uKey=" + uKey
        // + "; _jc_save_fromStation=" + _jc_save_fromStation + "; _jc_save_fromDate=" + _jc_save_fromDate
        // + "; _jc_save_toDate=" + _jc_save_toDate + "; _jc_save_wfdc_flag=" + _jc_save_wfdc_flag
        // + "; _jc_save_toStation=" + _jc_save_toStation
        ;
        body = "_json_att=&REPEAT_SUBMIT_TOKEN=" + REPEAT_SUBMIT_TOKEN;
        response = HttpClient.post(url, header, body, Map.class);
        status = (Boolean) response.getBody().get("status");
        Boolean isExist = (Boolean)((Map)response.getBody().get("data")).get("isExist");
        if (!status) {
            throw new RuntimeException("查询乘客错误：" + response);
        }
        if (!isExist) {
            String exNoraml = (String)((Map)response.getBody().get("data")).get("exNoraml");
            // if ("sysEx".equals(exNoraml)) {
            throw new RuntimeException("查询乘客错误（isExist）：" + response);
        }
        String exMsg = (String)((Map)response.getBody().get("data")).get("exMsg");
        if (exMsg != null && !exMsg.isEmpty()) {
            System.out.println(exMsg);
        }

        List<Map<String, String>> dj_passengers =
                (List<Map<String, String>>)((Map)response.getBody().get("data")).get("dj_passengers"); // 受让人，不考虑
        List<Map<String, String>> normal_passengers =
                (List<Map<String, String>>)((Map)response.getBody().get("data")).get("normal_passengers");

        List<String> two_isOpenClick = (List<String>)((Map)response.getBody().get("data")).get("two_isOpenClick");
        List<String> other_isOpenClick = (List<String>)((Map)response.getBody().get("data")).get("other_isOpenClick");

        // 检查乘客信息是否过期
        for (Map<String, String> normalPassenger : normal_passengers) {
            String passenger_id_type_code = normalPassenger.get("passenger_id_type_code");
            String total_times = normalPassenger.get("total_times");

            if (!"B".equals(passenger_id_type_code)) {
                boolean contains = false;
                for (String s : two_isOpenClick) {
                    if (total_times.equals(s)) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    throw new RuntimeException("乘客信息过期");
                }
            } else {
                boolean contains = false;
                for (String s : other_isOpenClick) {
                    if (total_times.equals(s)) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    throw new RuntimeException("乘客信息过期");
                }
            }
        }

        Map<String, Map<String, String>> normal_passengers_map = normal_passengers.stream()
                .collect(Collectors.toMap(p -> p.get("passenger_name") + "_" + p.get("allEncStr"), Function.identity()));

        String notify_for_gat = (String)((Map)response.getBody().get("data")).get("notify_for_gat");
        if (notify_for_gat != null && !notify_for_gat.isEmpty()) {
            System.out.println(notify_for_gat);
        }

        return normal_passengers_map;
    }

    private void check_passengers_and_tickets(Map<String, Map<String, String>> normal_passengers_map, String initDC_html) {

        Pattern init_limit_ticket_numPattern = Pattern.compile("var init_limit_ticket_num='(.*?)';");
        Matcher matcher = init_limit_ticket_numPattern.matcher(initDC_html);
        if (!matcher.find()) {
            // <div class="tit error" id="ERROR">系统忙，请稍后重试</div>
            throw new RuntimeException("未知错误，找不到init_limit_ticket_num：" + initDC_html);
        }
        String init_limit_ticket_num = matcher.group(1); // TODO 9 是否可以提升性能，此处不解析
        // String init_limit_ticket_num = "9";
        if (selectedPassengerKeys.isEmpty()) {
            throw new RuntimeException("至少选择一位乘客");
        }
        if (selectedPassengerKeys.size() > Integer.parseInt(init_limit_ticket_num)) {
            throw new RuntimeException("最多只能购买" + init_limit_ticket_num + "张票");
        }

        // 提交订单前检查
        for (String selectedPassengerKey : selectedPassengerKeys) {
            Map<String, String> normal_passenger = normal_passengers_map.get(selectedPassengerKey);
            if (normal_passenger == null) {
                throw new RuntimeException("乘客信息不存在：" + selectedPassengerKey);
            }

            String mobile_no = normal_passenger.get("mobile_no");
            String email = normal_passenger.get("email");
            String am = "请提供乘车人真实有效的联系方式。对于未成年人、老年人等重点旅客以及无手机的旅客，可提供监护人或能及时联系的亲友手机号码。";
            if ((mobile_no == null || mobile_no.isEmpty()) && (email == null || email.isEmpty())) {
                throw new RuntimeException(am + "：" + selectedPassengerKey);
            }

        }
    }

    private void checkOrderInfo(Map<String, Map<String, String>> normal_passengers_map, String initDC_html, String REPEAT_SUBMIT_TOKEN, String tour_flag) {

        // FIXME async
        //  checkOrderInfo 和 getQueueCount 得顺序调用，不能只调用单个，否则 getQueueCount 会提示系统繁忙
        url = "https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n" + "Content-Length: 1145\n" + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "Accept: application/json, text/javascript, */*; q=0.01\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                //     + "X-Requested-With: XMLHttpRequest\n"
                // + "sec-ch-ua-mobile: ?0\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                // + "sec-ch-ua-platform: \"Windows\"\n" + "Origin: https://kyfw.12306.cn\n" + "Sec-Fetch-Site: same-origin\n"
                // + "Sec-Fetch-Mode: cors\n" + "Sec-Fetch-Dest: empty\n"
                // + "Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc\n"
                // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                + cookieString
        //     + "Cookie: JSESSIONID="
        // + JSESSIONID + "; tk=" + tk + "; route=" + route + "; BIGipServerotn=" + BIGipServerotn
        // + "; BIGipServerpassport=" + BIGipServerpassport + defaultCookie + "; uKey=" + uKey
        // + "; _jc_save_fromStation=" + _jc_save_fromStation + "; _jc_save_fromDate=" + _jc_save_fromDate
        // + "; _jc_save_toDate=" + _jc_save_toDate + "; _jc_save_wfdc_flag=" + _jc_save_wfdc_flag
        // + "; _jc_save_toStation=" + _jc_save_toStation
        ;

        String passengerTicketStr = "";
        for (String selectedPassengerKey : selectedPassengerKeys) {
            Map<String, String> normal_passenger = normal_passengers_map.get(selectedPassengerKey);
            String save_status = ""; // save_status = $("#save_" + aD).prop("checked") ? "checked='checked'" : "";
            // 赋值位置：var aL = "normalPassenger_" + aK.passenger_name + "_" + aK.passenger_id_type_code + "_" +
            // aK.passenger_id_no + "_" + (aK.mobile_no == "" ? "null" : aK.mobile_no);
            passengerTicketStr +=
                    default_seat_type + ",0," + default_ticket_type + "," + normal_passenger.get("passenger_name") + ","
                            + normal_passenger.get("passenger_id_type_code") + "," + normal_passenger.get("passenger_id_no")
                            + "," + (normal_passenger.get("mobile_no") == null ? "" : normal_passenger.get("mobile_no")) + ","
                            + ("".equals(save_status) ? "N" : "Y") + "," + normal_passenger.get("allEncStr");
        }
        passengerTicketStr = UrlUtil.encode(passengerTicketStr);
        String oldPassengerStr = "";
        for (String selectedPassengerKey : selectedPassengerKeys) {
            Map<String, String> normal_passenger = normal_passengers_map.get(selectedPassengerKey);
            oldPassengerStr +=
                    normal_passenger.get("passenger_name") + "," + normal_passenger.get("passenger_id_type_code") + ","
                            + normal_passenger.get("passenger_id_no") + "," + normal_passenger.get("passenger_type") + "_";
        }
        oldPassengerStr = UrlUtil.encode(oldPassengerStr);
        String whatsSelect = "1"; // $.whatsSelect(true) ? "1" : "0";
        // whatsSelect: function(z) {
        // if (z) {
        // var x = $('#normal_passenger_id input[type="checkbox"]:checked').length;
        // if ($("#dj_passenger_id li").length == 0) {
        // return true
        // }
        // return x > 0 ? true : false
        // } else {
        // var y = $('#dj_passenger_id input[type="checkbox"]:checked').length;
        // return y > 0 ? true : false
        // }
        // }
        String csessionid = ""; // default empty
        String sig = ""; // default empty
        // randCode: $("#randCode").val(), 代码里看到有，但没有值，且url也没传递，暂时忽略
        body = "cancel_flag=2&bed_level_order_num=000000000000000000000000000000&passengerTicketStr="
                + passengerTicketStr + "&oldPassengerStr=" + oldPassengerStr + "&tour_flag=" + tour_flag + "&whatsSelect="
                + whatsSelect + "&sessionId=" + csessionid + "&sig=" + sig
                + "&scene=nc_login&_json_att=&REPEAT_SUBMIT_TOKEN=" + REPEAT_SUBMIT_TOKEN;

        System.out.println("\n\n" + url);
        System.out.println("\n\n" + header);
        System.out.println("\n\n" + body);

        response = HttpClient.post(url, header, body, Map.class); // TODO 报错提示：网络忙，请稍后再试。
        data = (Map)response.getBody().get("data");

        if (!(Boolean) data.get("submitStatus")) {
            if (data.get("isRelogin") != null && (Boolean) data.get("isRelogin")) {
                throw new RuntimeException("checkOrderInfo未知错误：" + response);
            }
            if (data.get("isNoActive") != null) {
                throw new RuntimeException("checkOrderInfo错误：" + data.get("errMsg"));
            } else {
                if (data.get("checkSeatNum") != null) {
                    throw new RuntimeException("很抱歉，无法提交您的订单，原因：" + data.get("errMsg"));
                } else {
                    throw new RuntimeException("出票失败，原因：" + data.get("errMsg"));
                }
            }
        }
        // if ("Y".equals(data.get("ifShowPassCode"))) {
        //     System.out.println("doByAlert(true): " + response); // ?
        // }
        // else {
        //     // 走了这里 // System.out.println("doByAlert(false): " + response);
        // }

        if (data.get("smokeStr") != null && !"".equals(data.get("smokeStr"))
                && ((String)data.get("smokeStr")).length() > 0) {
            throw new RuntimeException("吸烟功能不支持：" + response);
        }
        if (data.get("get608Msg") != null && !"".equals(data.get("get608Msg"))) {
            throw new RuntimeException("举报功能不支持：" + response);
        }

    }

    private JSONObject get_ticketInfoForPassengerForm(String initDC_html) {

        Pattern ticketInfoForPassengerFormPattern = Pattern.compile("var ticketInfoForPassengerForm=(.*?);");
        Matcher matcher = ticketInfoForPassengerFormPattern.matcher(initDC_html);
        if (!matcher.find()) {
            throw new RuntimeException("未知错误，找不到ticketInfoForPassengerForm：" + initDC_html);
        }
        String ticketInfoForPassengerFormString = matcher.group(1);
        ticketInfoForPassengerFormString = ticketInfoForPassengerFormString.replace("'", "\"");
        JSONObject ticketInfoForPassengerForm = JSON.parseObject(ticketInfoForPassengerFormString);

        System.out.println("\n\nticketInfoForPassengerFormString: " + ticketInfoForPassengerFormString);

        // ticket_submit_order passengerInfo_js.js 写死的
        // 满足这个条件说明是非异步的，命名有问题
        String ticket_submit_order_request_flag_isAsync = "1";
        if (!ticket_submit_order_request_flag_isAsync.equals(ticketInfoForPassengerForm.get("isAsync")) || ""
                .equals(ticketInfoForPassengerForm.getJSONObject("queryLeftTicketRequestDTO").getString("ypInfoDetail"))) {
            throw new UnsupportedOperationException(
                    "不支持非异步功能，ticketInfoForPassengerForm：" + ticketInfoForPassengerFormString);
        }

        return ticketInfoForPassengerForm;
    }

    private void getQueueCount(Map<String, String> trainInfo, JSONObject ticketInfoForPassengerForm, String REPEAT_SUBMIT_TOKEN) {

        url = "https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount"; // TODO timers 三次重试
        header = "Host: kyfw.12306.cn\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                //     + "Connection: keep-alive\n"
                //     // + "Content-Length: 412\n"
                //     + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "Accept: application/json, text/javascript, */*; q=0.01\n"
                //     + "X-Requested-With: XMLHttpRequest\n"
                // + "sec-ch-ua-mobile: ?0\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                // + "sec-ch-ua-platform: \"Windows\"\n" + "Origin: https://kyfw.12306.cn\n" + "Sec-Fetch-Site: same-origin\n"
                // + "Sec-Fetch-Mode: cors\n" + "Sec-Fetch-Dest: empty\n"
                // + "Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc\n"
                // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                + cookieString
        //     + "Cookie: JSESSIONID="
        // + JSESSIONID + "; tk=" + tk + "; route=" + route + "; BIGipServerotn=" + BIGipServerotn
        // + "; BIGipServerpassport=" + BIGipServerpassport + defaultCookie + "; uKey=" + uKey
        // + "; _jc_save_fromStation=" + _jc_save_fromStation + "; _jc_save_fromDate=" + _jc_save_fromDate
        // + "; _jc_save_toDate=" + _jc_save_toDate + "; _jc_save_wfdc_flag=" + _jc_save_wfdc_flag
        // + "; _jc_save_toStation=" + _jc_save_toStation
        ;

        String train_no = trainInfo.get("train_no"); // a
        String from_station_telecode = trainInfo.get("from_station_telecode"); // b
        String to_station_telecode = trainInfo.get("to_station_telecode"); // f

        // Fri+May+10+2024+00%3A00%3A00+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)
        // train_date = UrlUtil
        //     .encode(new SimpleDateFormat("EEE+MMM+MM+yyyy", Locale.US).format(date) + "+00:00:00+GMT+0800+(中国标准时间)"); // "Thu+Apr+25+2024+00:00:00+GMT+0800+(中国标准时间)"
        String train_date = new SimpleDateFormat("EEE+MMM+dd+yyyy", Locale.US).format(date) + "+00%3A00%3A00+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)";
        String stationTrainCode = trainInfo.get("station_train_code");
        String seatType = default_seat_type;
        String fromStationTelecode = from_station_telecode;
        String toStationTelecode = to_station_telecode;
        String leftTicket =
                UrlUtil.encode(ticketInfoForPassengerForm.getJSONObject("queryLeftTicketRequestDTO").getString("ypInfoDetail")); // ticketInfoForPassengerForm.queryLeftTicketRequestDTO.ypInfoDetail % 要转为 %25
        String purpose_codes = ticketInfoForPassengerForm.getString("purpose_codes"); // 从页面解析获取
        // ticketInfoForPassengerForm.purpose_codes
        String train_location = ticketInfoForPassengerForm.getString("train_location"); // ticketInfoForPassengerForm.train_location
        // isCheckOrderInfo=null
        body = "train_date=" + train_date + "&train_no=" + train_no + "&stationTrainCode=" + stationTrainCode
                + "&seatType=" + seatType + "&fromStationTelecode=" + fromStationTelecode + "&toStationTelecode="
                + toStationTelecode + "&leftTicket=" + leftTicket + "&purpose_codes=" + purpose_codes + "&train_location="
                + train_location + "&_json_att=&REPEAT_SUBMIT_TOKEN=" + REPEAT_SUBMIT_TOKEN;

        System.out.println("\n\n" + url);
        System.out.println("\n\n" + header);
        System.out.println("\n\n" + body);

        response = HttpClient.post(url, header, body, Map.class); // TODO 报错提示：网络忙，请稍后再试。
        status = (Boolean) response.getBody().get("status"); // TODO messages 提示系统忙，请稍后重试   系统繁忙，请稍后重试！    api调用太快，会提示这个
        if (!status) {
            throw new RuntimeException("getQueueCount未知错误：" + response);
        }
        data = (Map)response.getBody().get("data");
        String isRelogin = (String)data.get("isRelogin");
        if (isRelogin != null && "Y".equals(isRelogin)) {
            throw new RuntimeException("getQueueCount未知错误，未登录逻辑：" + response);
        }
        String ticket = (String)data.get("ticket");
        String[] ticketCounts = ticket.split(","); // 0 or 充足
        if (ticketCounts.length > 0) {
            // TODO seatType调整为中文
            System.out.println("本次列车[" + seatType + "]余票：" + ticketCounts[0]);
        }
        if (ticketCounts.length > 1) {
            System.out.println("本次列车[" + seatType + "]无座余票：" + ticketCounts[1]);
        }
        String op_2 = (String)data.get("op_2");
        if ("true".equals(op_2)) {
            System.out.println("目前排队人数已经超过余票张数，请您选择其他席别或车次。"); // TODO 转候补流程
            return;
        } else {
            String countT = (String)data.get("countT");
            if (Integer.parseInt(countT) > 0) {
                System.out.println("目前排队人数[" + countT + "]人，请确认以上信息是否正确，点击“确认”后，系统将为您随机分配席位。");
            }
        }

        String tour_flag = ticketInfoForPassengerForm.getString("tour_flag");
        if ("wc".equals(tour_flag)) { // ticket_submit_order.tour_flag.wc
            throw new UnsupportedOperationException("confirmPassenger/confirmGoForQueue");
        }
        if ("fc".equals(tour_flag)) { // ticket_submit_order.tour_flag.fc
            // param: fczk = $("#fczk").is(":checked") ? "Y" : "N"
            throw new UnsupportedOperationException("confirmPassenger/confirmBackForQueue");
        }
        if ("gc".equals(tour_flag)) { // ticket_submit_order.tour_flag.gc
            throw new UnsupportedOperationException("confirmPassenger/confirmResignForQueue");
        }
        if (!"dc".equals(tour_flag)) { // ticket_submit_order.tour_flag.dc
            throw new RuntimeException("订票失败，原因：旅程形式" + tour_flag + "为非法的旅程方式");
        }

    }

    private void confirmSingleForQueue(Map<String, Map<String, String>> normal_passengers_map, JSONObject ticketInfoForPassengerForm, String REPEAT_SUBMIT_TOKEN, String tour_flag) {

        // copy from before

        String passengerTicketStr = "";
        for (String selectedPassengerKey : selectedPassengerKeys) {
            Map<String, String> normal_passenger = normal_passengers_map.get(selectedPassengerKey);
            String save_status = ""; // save_status = $("#save_" + aD).prop("checked") ? "checked='checked'" : "";
            // 赋值位置：var aL = "normalPassenger_" + aK.passenger_name + "_" + aK.passenger_id_type_code + "_" +
            // aK.passenger_id_no + "_" + (aK.mobile_no == "" ? "null" : aK.mobile_no);
            passengerTicketStr +=
                    default_seat_type + ",0," + default_ticket_type + "," + normal_passenger.get("passenger_name") + ","
                            + normal_passenger.get("passenger_id_type_code") + "," + normal_passenger.get("passenger_id_no")
                            + "," + (normal_passenger.get("mobile_no") == null ? "" : normal_passenger.get("mobile_no")) + ","
                            + ("".equals(save_status) ? "N" : "Y") + "," + normal_passenger.get("allEncStr") + "_";
        }
        passengerTicketStr = passengerTicketStr.substring(0, passengerTicketStr.length() - 1);
        passengerTicketStr = UrlUtil.encode(passengerTicketStr);
        String oldPassengerStr = "";
        for (String selectedPassengerKey : selectedPassengerKeys) {
            Map<String, String> normal_passenger = normal_passengers_map.get(selectedPassengerKey);
            oldPassengerStr +=
                    normal_passenger.get("passenger_name") + "," + normal_passenger.get("passenger_id_type_code") + ","
                            + normal_passenger.get("passenger_id_no") + "," + normal_passenger.get("passenger_type") + "_";
        }
        oldPassengerStr = UrlUtil.encode(oldPassengerStr);
        String whatsSelect = "1"; // $.whatsSelect(true) ? "1" : "0";
        // whatsSelect: function(z) {
        // if (z) {
        // var x = $('#normal_passenger_id input[type="checkbox"]:checked').length;
        // if ($("#dj_passenger_id li").length == 0) {
        // return true
        // }
        // return x > 0 ? true : false
        // } else {
        // var y = $('#dj_passenger_id input[type="checkbox"]:checked').length;
        // return y > 0 ? true : false
        // }
        // }
        String purpose_codes = ticketInfoForPassengerForm.getString("purpose_codes"); // 从页面解析获取
        String train_location = ticketInfoForPassengerForm.getString("train_location"); // ticketInfoForPassengerForm.train_location

        url = "https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue";
        header = "Host: kyfw.12306.cn\n"
                //     + "Connection: keep-alive\n" + "Content-Length: 4453\n" + "Pragma: no-cache\n"
                // + "Cache-Control: no-cache\n"
                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                // + "Accept: application/json, text/javascript, */*; q=0.01\n"
                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                //     + "X-Requested-With: XMLHttpRequest\n"
                // + "sec-ch-ua-mobile: ?0\n"
                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                // + "sec-ch-ua-platform: \"Windows\"\n" + "Origin: https://kyfw.12306.cn\n" + "Sec-Fetch-Site: same-origin\n"
                // + "Sec-Fetch-Mode: cors\n" + "Sec-Fetch-Dest: empty\n"
                // + "Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc\n"
                // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                + cookieString
        //     + "Cookie: JSESSIONID="
        // + JSESSIONID + "; tk=" + tk + "; route=" + route + "; BIGipServerotn=" + BIGipServerotn
        // + "; BIGipServerpassport=" + BIGipServerpassport + defaultCookie + "; uKey=" + uKey
        // + "; _jc_save_fromStation=" + _jc_save_fromStation + "; _jc_save_fromDate=" + _jc_save_fromDate
        // + "; _jc_save_toDate=" + _jc_save_toDate + "; _jc_save_wfdc_flag=" + _jc_save_wfdc_flag
        // + "; _jc_save_toStation=" + _jc_save_toStation
        ;

        String dwAll = "N";
        // if ($("#chooseAllDW")[0] && $("#chooseAllDW").is(":checked")) { // TODO 正常座位没有这些元素
        // dwAll = "Y";
        // }
        String key_check_isChange = ticketInfoForPassengerForm.getString("key_check_isChange");
        //  % 要转为 %25 和上面那个ypInfoDetail一样
        String leftTicketStr = UrlUtil.encode(ticketInfoForPassengerForm.getString("leftTicketStr"));
        String choose_seats = ""; // 不支持指定座位，随意坐
        String seatDetailType = "000";
        // var x = $("#x_no").text(); // TODO 下中上铺 正常座位没有这些元素
        // var y = $("#z_no").text();
        // var z = $("#s_no").text();
        // seatDetailType = x + y + z
        String is_jy = "N"; // 静音车厢席位 $("#seat-jy").is(":checked") ? "Y" : "N";
        String is_cj = "Y"; // 残疾人专用席位 $("#seat-cj").is(":checked") ? "Y" : "N"; 默认 checked="checked"
        String encryptedData = getEncryptedData();
        String roomType = "00";
        // String isDw = "N"; // 页面默认N
        // if (isDw != null) {
        // String Z = "";
        // if ($("#nvbbf").is(":checked")) { // TODO 正常座位没有这些元素
        // Z = "1";
        // } else {
        // Z = "0";
        // }
        // if ($("#jtbf").is(":checked")) { // TODO 正常座位没有这些元素
        // Z = Z + "1";
        // } else {
        // Z = Z + "0";
        // }
        // roomType = Z;
        // }
        body = "passengerTicketStr=" + passengerTicketStr + "&oldPassengerStr=" + oldPassengerStr + "&purpose_codes="
                + purpose_codes + "&key_check_isChange=" + key_check_isChange + "&leftTicketStr=" + leftTicketStr
                + "&train_location=" + train_location + "&choose_seats=" + choose_seats + "&seatDetailType="
                + seatDetailType + "&is_jy=" + is_jy + "&is_cj=" + is_cj + "&encryptedData=" + encryptedData
                + "&whatsSelect=" + whatsSelect + "&roomType=" + roomType + "&dwAll=" + dwAll
                + "&_json_att=&REPEAT_SUBMIT_TOKEN=" + REPEAT_SUBMIT_TOKEN;
        response = HttpClient.post(url, header, body, Map.class); // TODO
        // 报错提示：
        // 订票失败，很抱歉！网络忙，请关闭窗口稍后再试。
        // 订票失败，很抱歉！请关闭窗口重新预定车票
        status = (boolean)response.getBody().get("status");
        if (!status) {
            throw new RuntimeException("12306未知错误：订票失败，很抱歉！请关闭窗口重新预定车票" + response);
        }
        data = (Map)response.getBody().get("data");
        Boolean submitStatus = (Boolean)data.get("submitStatus");
        if (!submitStatus) {
            throw new RuntimeException("出票失败，原因：" + data.get("errMsg") + "，点击修改？？？：" + response);
        }
        String isAsync = (String)data.get("isAsync");
        if (!("1".equals(isAsync))) {
            // otsRedirect("post", ctx + "payOrder/init?random=" + new Date().getTime(), {})
            System.out.println("购票成功！");
            // TODO 支付
            return;
        }

        // String cookieString = "Cookie: JSESSIONID=" + JSESSIONID + "; tk=" + tk + "; route=" + route
        //     + "; BIGipServerotn=" + BIGipServerotn + "; BIGipServerpassport=" + BIGipServerpassport + defaultCookie
        //     + "; uKey=" + uKey + "; _jc_save_fromStation=" + _jc_save_fromStation + "; _jc_save_fromDate="
        //     + _jc_save_fromDate + "; _jc_save_toDate=" + _jc_save_toDate + "; _jc_save_wfdc_flag=" + _jc_save_wfdc_flag
        //     + "; _jc_save_toStation=" + _jc_save_toStation;

        // var ae = new OrderQueueWaitTime(Y,y,T);
        // ae.start();
        String cookieString2 = cookieString;
        OrderQueueWaitTimer orderQueueWaitTimer = new OrderQueueWaitTimer(tour_flag,
                //
                (tourFlag, dispTime, waitTimeDesc) -> {
                    if (dispTime <= 5) {
                        System.out.println("订单已经提交，系统正在处理中，请稍等。");
                    } else {
                        if (dispTime > 30 * 60) {
                            System.out.println("订单已经提交，预计等待时间超过30分钟，请耐心等待。");
                        } else {
                            System.out.println("订单已经提交，最新预估等待时间" + waitTimeDesc + "，请耐心等待。");
                        }
                    }
                },
                //
                (tourFlag, dispTime, waitObj) -> {
                    if (dispTime == -1 || dispTime == -100) {

                        if ("wc".equals(tourFlag)) { // ticket_submit_order.tour_flag.wc
                            throw new UnsupportedOperationException("confirmPassenger/resultOrderForWcQueue");
                        }
                        if ("fc".equals(tourFlag)) { // ticket_submit_order.tour_flag.fc
                            // param: fczk = $("#fczk").is(":checked") ? "Y" : "N"
                            throw new UnsupportedOperationException("confirmPassenger/resultOrderForFcQueue");
                        }
                        if ("gc".equals(tourFlag)) { // ticket_submit_order.tour_flag.gc
                            throw new UnsupportedOperationException("confirmPassenger/resultOrderForGcQueue");
                        }
                        if (!"dc".equals(tourFlag)) { // ticket_submit_order.tour_flag.dc
                            throw new RuntimeException("订票失败，原因：resultOrderForGcQueue 形式" + tourFlag + "为非法的方式");
                        }

                        String url2 = "https://kyfw.12306.cn/otn/confirmPassenger/resultOrderForDcQueue";
                        String header2 = "Host: kyfw.12306.cn\n"
                                //     + "Connection: keep-alive\n" + "Content-Length: 91\n"
                                // + "Pragma: no-cache\n" + "Cache-Control: no-cache\n"
                                // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
                                // + "Accept: application/json, text/javascript, */*; q=0.01\n"
                                + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n"
                                // + "X-Requested-With: XMLHttpRequest\n" + "sec-ch-ua-mobile: ?0\n"
                                // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
                                // + "sec-ch-ua-platform: \"Windows\"\n" + "Origin: https://kyfw.12306.cn\n"
                                // + "Sec-Fetch-Site: same-origin\n" + "Sec-Fetch-Mode: cors\n" + "Sec-Fetch-Dest: empty\n"
                                // + "Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc\n"
                                // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                                + cookieString2;
                        String orderSequence_no = (String)waitObj.get("orderId");
                        String body2 = "orderSequence_no=" + orderSequence_no + "&_json_att=&REPEAT_SUBMIT_TOKEN="
                                + REPEAT_SUBMIT_TOKEN;
                        ResponseEntity<Map> response2 = HttpClient.post(url2, header2, body2, Map.class); // TODO 报错打印
                        // System.out.println("下单成功。");
                        // 报错提示：订票失败，很抱歉！网络忙，请关闭窗口稍后再试。
                        Boolean status2 = (Boolean)response2.getBody().get("status");
                        if (!status2) {
                            System.out.println("下单成功。"); // 小黑屋
                        }
                        Map data2 = (Map)response2.getBody().get("data");
                        if ((Boolean)data2.get("submitStatus")) {
                            // otsRedirect("post", ctx + "payOrder/init?random=" + new Date().getTime(), {})
                            // TODO 支付
                            System.out.println("下单成功。");
                        } else {
                            System.out.println("下单成功。"); // 小黑屋
                        }
                    } else {
                        if (waitObj.get("name") != null && waitObj.get("card") != null && waitObj.get("tel") != null) {
                            throw new RuntimeException("不支持举报功能");
                        }
                        if (dispTime == -1) {
                            // return;
                        } else {
                            if (dispTime == -2) {
                                if ((int)waitObj.get("errorcode") == 0) {
                                    throw new RuntimeException("订票失败，原因： " + waitObj.get("msg") + "，：" + waitObj);
                                } else {
                                    throw new RuntimeException("订票失败，原因： " + waitObj.get("msg") + "，：" + waitObj);
                                }
                            } else {
                                if (dispTime == -3) {
                                    throw new RuntimeException("哎呀,订票失败，订单已撤销");
                                } else {
                                    throw new RuntimeException(
                                            "未知错误：window.location.href = ctx + \"view/train_order.html?type=1&random=\" + new Date().getTime()");
                                }
                            }
                        }
                    }
                }, REPEAT_SUBMIT_TOKEN, cookieString);
        orderQueueWaitTimer.start();

        // https://kyfw.12306.cn/otn/basedata/log 这个可以不上报日志
    }

    private static String getEncryptedData() {

        // https://mobile.12306.cn/otsmobile/antcaptcha/suite1608722853171.js
        // window.json_ua.toString()

        // 目前测试下来，无需该字段

        // window[c[1708]].toString = function() {
        // return c[1721] = "",
        // c[1721] = c[1721].split(""),
        // c[1721] = c[1721].reverse(),
        // c[1721] = c[1721].join(""),
        // c[1722] = c[1721],
        // be(Wa, 5).r(),
        // c[1722] = c[1062],
        // c[1722]
        // }

        // suite.js au_nosj
        String json_ua = "";
        try {
            // window.json_ua.toString()
        } catch (Throwable ignored) {
        }
        return json_ua;
    }

    private static class A {

        //
        // var
        // init_seatTypes=[{'end_station_name':null,'end_time':null,'id':'M','start_station_name':null,'start_time':null,'value':'一等座'},{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'二等座'},{'end_station_name':null,'end_time':null,'id':'P','start_station_name':null,'start_time':null,'value':'特等座'}];
        //
        // var
        // defaultTicketTypes=[{'end_station_name':null,'end_time':null,'id':'1','start_station_name':null,'start_time':null,'value':'成人票'},{'end_station_name':null,'end_time':null,'id':'2','start_station_name':null,'start_time':null,'value':'儿童票'},{'end_station_name':null,'end_time':null,'id':'3','start_station_name':null,'start_time':null,'value':'学生票'},{'end_station_name':null,'end_time':null,'id':'4','start_station_name':null,'start_time':null,'value':'残军票'}];
        //
        // var
        // init_cardTypes=[{'end_station_name':null,'end_time':null,'id':'1','start_station_name':null,'start_time':null,'value':'中国居民身份证'},{'end_station_name':null,'end_time':null,'id':'C','start_station_name':null,'start_time':null,'value':'港澳居民来往内地通行证'},{'end_station_name':null,'end_time':null,'id':'G','start_station_name':null,'start_time':null,'value':'台湾居民来往大陆通行证'},{'end_station_name':null,'end_time':null,'id':'B','start_station_name':null,'start_time':null,'value':'护照'},{'end_station_name':null,'end_time':null,'id':'H','start_station_name':null,'start_time':null,'value':'外国人永久居留身份证'}];
        //
        // var
        // ticket_seat_codeMap={'3':[{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'二等座'}],'2':[{'end_station_name':null,'end_time':null,'id':'M','start_station_name':null,'start_time':null,'value':'一等座'},{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'二等座'},{'end_station_name':null,'end_time':null,'id':'P','start_station_name':null,'start_time':null,'value':'特等座'}],'1':[{'end_station_name':null,'end_time':null,'id':'M','start_station_name':null,'start_time':null,'value':'一等座'},{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'二等座'},{'end_station_name':null,'end_time':null,'id':'P','start_station_name':null,'start_time':null,'value':'特等座'}],'4':[{'end_station_name':null,'end_time':null,'id':'M','start_station_name':null,'start_time':null,'value':'一等座'},{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'二等座'},{'end_station_name':null,'end_time':null,'id':'P','start_station_name':null,'start_time':null,'value':'特等座'}]};
        //
        // var
        // ticketInfoForPassengerForm={'cardTypes':[{'end_station_name':null,'end_time':null,'id':'1','start_station_name':null,'start_time':null,'value':'中国居民身份证'},{'end_station_name':null,'end_time':null,'id':'C','start_station_name':null,'start_time':null,'value':'港澳居民来往内地通行证'},{'end_station_name':null,'end_time':null,'id':'G','start_station_name':null,'start_time':null,'value':'台湾居民来往大陆通行证'},{'end_station_name':null,'end_time':null,'id':'B','start_station_name':null,'start_time':null,'value':'护照'},{'end_station_name':null,'end_time':null,'id':'H','start_station_name':null,'start_time':null,'value':'外国人永久居留身份证'}],'isAsync':'1','key_check_isChange':'BB3920D28353D98A6E214BF986244B333CEC52B79F866C4DB971111A','leftDetails':['无座(
        // ¥68.0元 7.5折)无票','特等座( ¥122.0元 7.5折)6张票','一等座( ¥109.0元 7.6折)7张票','二等座( ¥68.0元
        // 7.5折)有票'],'leftTicketStr':'dJd%2F6513p7iX9JJ%2BuotPddACkT%2FSc%2BvPM7eZlXv%2FZ2LWGWIf3jybVUku%2Bx4%3D','limitBuySeatTicketDTO':{'seat_type_codes':[{'end_station_name':null,'end_time':null,'id':'M','start_station_name':null,'start_time':null,'value':'一等座'},{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'二等座'},{'end_station_name':null,'end_time':null,'id':'P','start_station_name':null,'start_time':null,'value':'特等座'}],'ticket_seat_codeMap':{'3':[{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'二等座'}],'2':[{'end_station_name':null,'end_time':null,'id':'M','start_station_name':null,'start_time':null,'value':'一等座'},{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'二等座'},{'end_station_name':null,'end_time':null,'id':'P','start_station_name':null,'start_time':null,'value':'特等座'}],'1':[{'end_station_name':null,'end_time':null,'id':'M','start_station_name':null,'start_time':null,'value':'一等座'},{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'二等座'},{'end_station_name':null,'end_time':null,'id':'P','start_station_name':null,'start_time':null,'value':'特等座'}],'4':[{'end_station_name':null,'end_time':null,'id':'M','start_station_name':null,'start_time':null,'value':'一等座'},{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'二等座'},{'end_station_name':null,'end_time':null,'id':'P','start_station_name':null,'start_time':null,'value':'特等座'}]},'ticket_type_codes':[{'end_station_name':null,'end_time':null,'id':'1','start_station_name':null,'start_time':null,'value':'成人票'},{'end_station_name':null,'end_time':null,'id':'2','start_station_name':null,'start_time':null,'value':'儿童票'},{'end_station_name':null,'end_time':null,'id':'3','start_station_name':null,'start_time':null,'value':'学生票'},{'end_station_name':null,'end_time':null,'id':'4','start_station_name':null,'start_time':null,'value':'残军票'}]},'maxTicketNum':'9','orderRequestDTO':{'adult_num':0,'append_list_per_ticket':null,'appendix_list_sequence':null,'appidToken':null,'apply_order_no':null,'bed_level_order_num':null,'bureau_code':null,'cancel_flag':null,'card_num':null,'channel':null,'child_num':0,'choose_seat':null,'country_flag':'CHN,CHN','disability_num':0,'dw_flag':'0,0,0,0,z,0,z','end_time':{'date':1,'day':4,'hours':7,'minutes':32,'month':0,'seconds':0,'time':-1680000,'timezoneOffset':-480,'year':70},'exchange_train_flag':'1','from_station_name':'上海','from_station_telecode':'SHH','get_ticket_pass':null,'id_mode':'Y','isShowPassCode':null,'leftTicketGenTime':null,'orderBatchNo':null,'orderId':null,'order_date':null,'passengerFlag':null,'realleftTicket':null,'reqIpAddress':null,'reqTimeLeftStr':null,'reserve_flag':'A','saleTimeSecond':1126190,'seat_detail_type_code':null,'seat_type_code':null,'sequence_no':null,'start_time':{'date':1,'day':4,'hours':6,'minutes':19,'month':0,'seconds':0,'time':-6060000,'timezoneOffset':-480,'year':70},'start_time_str':null,'station_train_code':'G7080','student_num':0,'ticket_num':0,'ticket_type_order_num':null,'to_station_name':'常州','to_station_telecode':'CZH','tour_flag':'dc','trainCodeText':null,'train_date':{'date':24,'day':3,'hours':0,'minutes':0,'month':3,'seconds':0,'time':1713888000000,'timezoneOffset':-480,'year':124},'train_date_str':null,'train_location':null,'train_no':'55000G7080F1','train_order':null,'trms_train_flag':null,'varStr':null},'purpose_codes':'00','queryLeftNewDetailDTO':{'BXRZ_num':'-1','BXRZ_price':'0','BXYW_num':'-1','BXYW_price':'0','EDRZ_num':'-1','EDRZ_price':'0','EDSR_num':'-1','EDSR_price':'0','ERRB_num':'-1','ERRB_price':'0','GG_num':'-1','GG_price':'0','GR_num':'-1','GR_price':'0','HBRW_num':'-1','HBRW_price':'0','HBRZ_num':'-1','HBRZ_price':'0','HBYW_num':'-1','HBYW_price':'0','HBYZ_num':'-1','HBYZ_price':'0','RW_num':'-1','RW_price':'0','RZ_num':'-1','RZ_price':'0','SRRB_num':'-1','SRRB_price':'0','SWZ_num':'-1','SWZ_price':'0','TDRZ_num':'-1','TDRZ_price':'0','TZ_num':'6','TZ_price':'01220','WZ_num':'0','WZ_price':'00680','WZ_seat_type_code':'O','YB_num':'-1','YB_price':'0','YDRZ_num':'-1','YDRZ_price':'0','YDSR_num':'-1','YDSR_price':'0','YRRB_num':'-1','YRRB_price':'0','YW_num':'-1','YW_price':'0','YYRW_num':'-1','YYRW_price':'0','YZ_num':'-1','YZ_price':'0','ZE_num':'331','ZE_price':'00680','ZY_num':'7','ZY_price':'01090','arrive_time':'0732','arrive_time_local':null,'bed_level_info':'','control_train_day':'','controlled_train_flag':null,'controlled_train_message':null,'country_flag':null,'day_difference':null,'end_station_name':null,'end_station_telecode':null,'from_station_name':'上海','from_station_telecode':'SHH','infoAll_list':null,'is_support_card':null,'lishi':'01:13','seat_discount_info':'M0076O0075P0075W0075','seat_feature':'','start_station_name':null,'start_station_telecode':null,'start_time':'0619','start_time_local':null,'start_train_date':'','station_train_code':'G7080','to_station_name':'常州','to_station_telecode':'CZH','train_class_name':null,'train_no':'55000G7080F1','train_seat_feature':'','ypInfoDetail':'M010900007O006800331P012200006O006803000','yp_ex':''},'queryLeftTicketRequestDTO':{'arrive_time':'07:32','arrive_time_local':'','bed_level_info':'','bigger20':'Y','country_flag':'CHN,CHN','dw_flag':'0,0,0,0,z,0,z','exchange_train_flag':'1','from_station':'SHH','from_station_name':'上海','from_station_no':'01','lishi':'01:13','login_id':null,'login_mode':null,'login_site':null,'purpose_codes':'00','query_type':null,'saleTimeSecond':1126190,'seatTypeAndNum':null,'seat_discount_info':'M0076O0075P0075W0075','seat_types':'MOPO','start_time':'06:19','start_time_begin':null,'start_time_end':null,'start_time_local':'','station_train_code':'G7080','ticket_type':null,'to_station':'CZH','to_station_name':'常州','to_station_no':'06','train_date':'20240424','train_flag':null,'train_headers':null,'train_no':'55000G7080F1','trms_train_flag':null,'useMasterPool':true,'useWB10LimitTime':true,'usingGemfireCache':false,'ypInfoDetail':'dJd%2F6513p7iX9JJ%2BuotPddACkT%2FSc%2BvPM7eZlXv%2FZ2LWGWIf3jybVUku%2Bx4%3D'},'tour_flag':'dc','train_location':'H1'};
        //
        // var
        // orderRequestDTO={'adult_num':0,'append_list_per_ticket':null,'appendix_list_sequence':null,'appidToken':null,'apply_order_no':null,'bed_level_order_num':null,'bureau_code':null,'cancel_flag':null,'card_num':null,'channel':null,'child_num':0,'choose_seat':null,'country_flag':'CHN,CHN','disability_num':0,'dw_flag':'0,0,0,0,z,0,z','end_time':{'date':1,'day':4,'hours':7,'minutes':32,'month':0,'seconds':0,'time':-1680000,'timezoneOffset':-480,'year':70},'exchange_train_flag':'1','from_station_name':'上海','from_station_telecode':'SHH','get_ticket_pass':null,'id_mode':'Y','isShowPassCode':null,'leftTicketGenTime':null,'orderBatchNo':null,'orderId':null,'order_date':null,'passengerFlag':null,'realleftTicket':null,'reqIpAddress':null,'reqTimeLeftStr':null,'reserve_flag':'A','saleTimeSecond':1126190,'seat_detail_type_code':null,'seat_type_code':null,'sequence_no':null,'start_time':{'date':1,'day':4,'hours':6,'minutes':19,'month':0,'seconds':0,'time':-6060000,'timezoneOffset':-480,'year':70},'start_time_str':null,'station_train_code':'G7080','student_num':0,'ticket_num':0,'ticket_type_order_num':null,'to_station_name':'常州','to_station_telecode':'CZH','tour_flag':'dc','trainCodeText':null,'train_date':{'date':24,'day':3,'hours':0,'minutes':0,'month':3,'seconds':0,'time':1713888000000,'timezoneOffset':-480,'year':124},'train_date_str':null,'train_location':null,'train_no':'55000G7080F1','train_order':null,'trms_train_flag':null,'varStr':null};

    }

    private static final Map<String, String> ticketTypeCodeForName = new LinkedHashMap() {
        {
            // 1成人 2儿童 3学生 4军人
            put("1", "成人");
            put("2", "儿童");
            put("3", "学生");
            put("4", "军人");
        }
    };


    private static final Map<String, String> seatTypeCodeForName = new LinkedHashMap() {
        {
            // var dp = "9AMO6IJF4321";

            // put("P", "特等座");
            put("9", "商务座");
            // put("A", "高级动卧");
            put("M", "一等座");
            put("O", "二等座");
            put("6", "高级软卧");
            // put("I", "一等卧");
            // put("J", "二等卧");
            put("4", "软卧");
            put("3", "硬卧");
            // put("F", "动卧");
            put("2", "软座");
            put("1", "硬座");
            put("H", "其他");
            put("WZ", "无座");
            put("W", "无座");

            // for (var dk = 0; dk < seat_types.length; dk++) {
            // var df = seat_types.substring(dk, dk + 1);
            // var dd = seatTypeCodeForName[df];
            // if (!dd) {
            // continue
            // }
            // if (!db[df]) {
            // dq.push(new dg(dd,df));
            // db[df] = true
            // }
            // }
        }
    };

    private String getPurposeCodes() {
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

    private HttpEntity<?> getEntity(String header) {
        return getEntity(header, null);
    }

    private HttpEntity<?> getEntity(String header, String body) {
        MultiValueMap<String, String> headers = parseHeader(header);
        return new HttpEntity<>(body, headers);
    }

    private MultiValueMap<String, String> parseHeader(String header) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        for (String s : header.split("\n")) {
            int i = s.indexOf(":");
            if (i == -1) {
                continue;
            }
            headers.add(s.substring(0, i), s.substring(i + 2));
        }
        return headers;
    }

    private Map<String, String> parseCookie(List<String> cookie) {
        Map<String, String> cookies = new HashMap<>();
        if (cookie != null) {
            for (String string : cookie) {
                for (String s : string.split("; ")) {
                    String[] split = s.split("=");
                    cookies.put(split[0], split[1]);
                }
            }
        }
        return cookies;
    }
}
