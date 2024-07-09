



# 打开网站页面

```
POST https://kyfw.12306.cn/otn/login/conf HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 0
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/resources/login.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=D03B6646E292240404E476B5A01DDA5A; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000

```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:08:16 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 586
Connection: keep-alive
Set-Cookie: route=495c805987d0f5c8c84b14f60212447d; Path=/
Set-Cookie: JSESSIONID=D03B6646E292240404E476B5A01DDA5A; Path=/otn
Set-Cookie: BIGipServerotn=484966666.64545.0000; path=/
ct: C1_232_28_9
Access-Control-Allow-Origin: *
X-Via: 1.1 PS-JDZ-01M3U44:18 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 6625fed0_PS-JDZ-01zlo49_8737-56572
X-Cdn-Src-Port: 37388

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"is_open_updateHBTime":"Y","isstudentDate":false,"is_message_passCode":"N","is_phone_check":"N","studentDate":["2020-04-01","2020-11-30","2020-12-01","2020-12-31","2021-01-01","2025-09-30"],"is_uam_login":"Y","is_login_passCode":"Y","is_sweep_login":"Y","nowStr":"20240422140818","queryUrl":"leftTicket/queryG","psr_qr_code_result":"N","now":1713766098347,"login_url":"resources/login.html","stu_control":15,"is_login":"N","is_olympicLogin":"N","other_control":15},"messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/passport/web/auth/uamtk-static HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 9
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/resources/login.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000

appid=otn
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:08:16 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
Access-Control-Allow-Origin: https://kyfw.12306.cn
Access-Control-Allow-Credentials: true
ct: c1_51
Set-Cookie: _passport_session=46162d6937c14441bbffde73af38a7ff8693; Path=/passport
Set-Cookie: BIGipServerpassport=937951498.50215.0000; path=/
X-Via: 1.1 zhendianxin40:5 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 6625fed0_PS-JDZ-01zlo49_8737-56641
X-Cdn-Src-Port: 37388
Content-Length: 52

{"result_message":"用户未登录","result_code":1}
```



# login_new.js



# 输入用户名、密码

```
POST https://kyfw.12306.cn/passport/web/checkLoginVerify HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 25
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/resources/login.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: _passport_session=46162d6937c14441bbffde73af38a7ff8693; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off

username=hyfsya&appid=otn
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:13:46 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
Access-Control-Allow-Origin: https://kyfw.12306.cn
Access-Control-Allow-Credentials: true
ct: c1_52
Set-Cookie: _passport_session=0b86730d699e46378a70e9d933132c7e1285; Path=/passport
X-Via: 1.1 dianx57:20 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 6626001a_dianx57_34573-37153
X-Cdn-Src-Port: 1555
Content-Length: 60

{"result_message":"","login_check_code":"3","result_code":0}
```



# 输入证件号后四位，点击获取验证码

```
POST https://kyfw.12306.cn/passport/web/getMessageCode HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 38
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/resources/login.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: _passport_session=0b86730d699e46378a70e9d933132c7e1285; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off

appid=otn&username=hyfsya&castNum=4815
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:16:25 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
Access-Control-Allow-Origin: https://kyfw.12306.cn
Access-Control-Allow-Credentials: true
ct: c1_59
X-Via: 1.1 anxin41:22 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 662600b9_dianx57_34487-5480
X-Cdn-Src-Port: 46288
Content-Length: 67

{"result_message":"获取手机验证码成功！","result_code":0}
```



# 输入验证码后

```
POST https://kyfw.12306.cn/passport/web/login HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 150
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
appFlag: 
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
Accept: application/json, text/javascript, */*; q=0.01
isPasswordCopy: N
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/resources/login.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: _passport_session=0b86730d699e46378a70e9d933132c7e1285; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off

sessionId=&sig=&if_check_slide_passcode_token=&scene=&checkMode=0&randCode=067962&username=hyfsya&password=%40%2Bll1WUPX8RN8lnEfwqU45Q%3D%3D&appid=otn
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:17:25 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
Access-Control-Allow-Origin: https://kyfw.12306.cn
Access-Control-Allow-Credentials: true
ct: c1_45
Set-Cookie: uamtk=Ssl-vAVHmlQQk5N4SwbkldfG3SoGGPQD1ph1h0; Path=/passport
X-Via: 1.1 dianx57:16 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 662600f5_dianx57_34473-15625
X-Cdn-Src-Port: 63060
Content-Length: 98

{"result_message":"登录成功","uamtk":"Ssl-vAVHmlQQk5N4SwbkldfG3SoGGPQD1ph1h0","result_code":0}
```



加载 /ton 的JSESSION  cookie

```
GET https://kyfw.12306.cn/otn/login/userLogin HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Referer: https://kyfw.12306.cn/otn/resources/login.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=D03B6646E292240404E476B5A01DDA5A; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off


```

```
HTTP/1.1 302 Moved Temporarily
Date: Mon, 22 Apr 2024 06:17:25 GMT
Content-Length: 0
Connection: keep-alive
Location: https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin
X-Via: 1.1 dianx57:16 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 662600f5_dianx57_34473-15709
X-Cdn-Src-Port: 63060


```



```
GET https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Referer: https://kyfw.12306.cn/otn/resources/login.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=D03B6646E292240404E476B5A01DDA5A; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:17:26 GMT
Content-Type: text/html;charset=utf-8
Connection: keep-alive
Set-Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; Path=/otn
ct: C1_232_28_9
Content-Language: zh-CN
X-Via: 1.1 PS-JDZ-019Zp58:7 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 662600f5_dianx57_34473-15731
X-Cdn-Src-Port: 63060
Content-Length: 14918

<!DOCTYPE html 
```



# passport.html



```
POST https://kyfw.12306.cn/passport/web/auth/uamtk HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 9
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: application/json, text/javascript, */*; q=0.01
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: _passport_session=0b86730d699e46378a70e9d933132c7e1285; uamtk=Ssl-vAVHmlQQk5N4SwbkldfG3SoGGPQD1ph1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off

appid=otn
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:17:26 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
Access-Control-Allow-Origin: https://kyfw.12306.cn
Access-Control-Allow-Credentials: true
ct: c1_54
Set-Cookie: uamtk=Ssl-vAVHmlQQk5N4SwbkldfG3SoGGPQD1ph1h0; Path=/passport
X-Via: 1.1 dianx57:16 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 662600f6_dianx57_34535-40797
X-Cdn-Src-Port: 63224
Content-Length: 114

{"apptk":null,"result_message":"验证通过","result_code":0,"newapptk":"2XRG5wcEaTCZHItpZfFNufGdG7fFyY8Sxhh1h0"}
```



```
POST https://kyfw.12306.cn/otn/uamauthclient HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 41
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off

tk=2XRG5wcEaTCZHItpZfFNufGdG7fFyY8Sxhh1h0
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:17:26 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
Set-Cookie: tk=2XRG5wcEaTCZHItpZfFNufGdG7fFyY8Sxhh1h0; Path=/otn
X-Via: 1.1 dianx57:16 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 662600f6_dianx57_34535-40814
X-Cdn-Src-Port: 63224
Content-Length: 121

{"result_code":0,"result_message":"验证通过","username":"何映峰","apptk":"2XRG5wcEaTCZHItpZfFNufGdG7fFyY8Sxhh1h0"}
```



```
GET https://kyfw.12306.cn/otn/login/userLogin HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: navigate
Sec-Fetch-Dest: document
Referer: https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=2XRG5wcEaTCZHItpZfFNufGdG7fFyY8Sxhh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off


```

```
HTTP/1.1 302 Moved Temporarily
Date: Mon, 22 Apr 2024 06:17:26 GMT
Content-Length: 0
Connection: keep-alive
Set-Cookie: uKey=7e7b69b8747bdf73600b3b0b5a0c7a1d96c1c6938a36e2ddc4a44d71997dbba7; Path=/
ct: C1_232_28_9
Location: https://kyfw.12306.cn/otn/view/index.html
Content-Language: zh-CN
X-Via: 1.1 dianx57:16 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 662600f6_dianx57_34535-40838
X-Cdn-Src-Port: 63224


```



# index.html

```
GET https://kyfw.12306.cn/otn/view/index.html HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: navigate
Sec-Fetch-Dest: document
Referer: https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=2XRG5wcEaTCZHItpZfFNufGdG7fFyY8Sxhh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; uKey=7e7b69b8747bdf73600b3b0b5a0c7a1d96c1c6938a36e2ddc4a44d71997dbba7


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:17:26 GMT
Content-Type: text/html
Connection: keep-alive
Expires: Mon, 22 Apr 2024 01:24:01 GMT
Cache-Control: max-age=0
Age: 17605
X-Via: 1.1 PS-JDZ-019Zp58:10 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 662600f6_dianx57_34535-40859
X-Cdn-Src-Port: 63224
Content-Length: 47038

<!DOCTYPE html>

```



# index.html



```
POST https://kyfw.12306.cn/otn/login/conf HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 0
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/view/index.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=2XRG5wcEaTCZHItpZfFNufGdG7fFyY8Sxhh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; uKey=7e7b69b8747bdf73600b3b0b5a0c7a1d96c1c6938a36e2ddc4a44d71997dbba7


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:17:27 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 682
Connection: keep-alive
ct: C1_232_28_9
Access-Control-Allow-Origin: *
X-Via: 1.1 PS-JDZ-019Zp58:21 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 662600f7_dianx57_34535-40961
X-Cdn-Src-Port: 63224

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"is_open_updateHBTime":"Y","isstudentDate":false,"is_message_passCode":"N","born_date":"2000-01-15","is_phone_check":"N","ei_email":"15******40@qq.com","studentDate":["2020-04-01","2020-11-30","2020-12-01","2020-12-31","2021-01-01","2025-09-30"],"is_uam_login":"Y","is_login_passCode":"Y","user_name":"hyfsya","is_sweep_login":"Y","queryUrl":"leftTicket/queryG","nowStr":"20240422141729","psr_qr_code_result":"N","now":1713766649446,"name":"何映峰","login_url":"resources/login.html","stu_control":15,"is_login":"Y","is_olympicLogin":"N","other_control":15},"messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/otn/index/initMy12306Api HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 0
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/view/index.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=2XRG5wcEaTCZHItpZfFNufGdG7fFyY8Sxhh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; uKey=7e7b69b8747bdf73600b3b0b5a0c7a1d96c1c6938a36e2ddc4a44d71997dbba7


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:17:27 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 496
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 PS-JDZ-019Zp58:13 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 662600f7_dianx57_34535-40994
X-Cdn-Src-Port: 63224

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"notify_way":"7","qr_code_url":"Y","if_show_ali_qr_code":true,"isSuperUser":"N","_email":"15******40@qq.com","user_status":"1","_is_needModifyPassword":null,"needEdit":false,"member_status":"","control_code":null,"id_type_code":"1","user_name":"何映峰","member_level":"*","isCanRegistMember":true,"user_regard":"先生,下午好！","resetMemberPwd":"N","_is_active":"N"},"messages":[],"validateMessages":{}}
```



# 输入出发地、目的地、出发日，点击查询按钮

```
GET https://kyfw.12306.cn/otn/leftTicket/queryG?leftTicketDTO.train_date=2024-04-22&leftTicketDTO.from_station=SHH&leftTicketDTO.to_station=CZH&purpose_codes=ADULT HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Accept: */*
X-Requested-With: XMLHttpRequest
If-Modified-Since: 0
sec-ch-ua-platform: "Windows"
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=2XRG5wcEaTCZHItpZfFNufGdG7fFyY8Sxhh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; uKey=8dff2075b4a7e3ed51395469a594236082e1e6d10dfc9cf8a7c7f1eaf3c16ff9; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_fromDate=2024-04-22; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:34:00 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
center: hw-B2
X-Via: 1.1 PS-JDZ-01M3U44:17 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 662604d8_PS-JDZ-01zlo49_8705-28823
X-Cdn-Src-Port: 27999
Cache-Control: no-cache, no-store
Content-Length: 80032

{"httpstatus":200,"data":{"result":["Oy1n3fY3wHl5K7F3QBADSZhu3Gt6nIrmTSKwbGPrW9kVv7sxrMRIsckP2yblzvsMVlZJuWXcqse0%0ANgPhUaqki8uKa96lc8QU4XthZ3SzswRZQ7MyN35hn6RFTIk6mlpwlCAr8J3xxzKD9vS4szs%2FnBj5%0AOvV37q2csNoHoekd75wLY%2FBqP3aWr8lJALTNXnX2cKcPKCagUK3q4Fu0eYMrBceLHYPMpEmV%2FTch%0AjcvfDsU0QUK15EB%2BlUg7Psb1lEXBYvIW57jNrWB5ztqf0AOVhslOwNVeo%2FmyZ8fSmkYXdwmuhQgG%0A%2FYIK1PrnnO%2F56WQuQwdNn%2FvEZk8aQrmUY%2FYncg%3D%3D|预订|5l000D301002|D3010|AOH|HKN|AOH|CZH|14:42|15:44|01:02|Y|0G8LkaB6U6b1%2FgJtmhT%2BsByCIKGR3%2BESnvDmPIhFkfBqYCIk|20240422|3|H3|01|04|1|0|||||||有||||3|14|||M0O0W0|MOO|1|0||M009800014O006100003O006103076|0|||||1|0#0#0#0#z#0#z||7|CHN,CHN|||N#N#|||202404081330|","OXpsqw%2FLc6CQaHzTZiudBzJwTRFcC%2FCxr1wO8K9I6y9y5D3pBQykDv959BN4vQkg9UsePHZoSmJA%0A69FJ4zYHInx%2FfJGHNB63QLEexwW6DAb7m6kQvd0DjLEcKLWjIJJZr0ouByc%2B7rA%2BUTygrUpy8rn9%0APF%2BaMQFsdSxA4Fr0YHSlihDZzpRPWb7%2BFGAMdQ8dY9Y5aMEE5E8GGG4r5AHXxupNThxtlSViDxKy%0A5k2XCH5dzk0NGYSUCyZYe%2F%2BF26jiU%2BtxyYX%2FGyablUx8ksPTOke7Lqbzt70FqWRen6wmBHVKWs5X%0AyW0mTSP%2BI5wrcDSEUVsdaA9PWtJsU79t7Dn0N2lLs9I%3D|预订|55000G829420|G8294|SHH|AFH|SHH|CZH|14:44|15:53|01:09|Y|VR9Ie34ncBMeiQE8jHi1ls1m4GXi6N42Jc1hS5%2BUTv4xTqkw9raLcJg7kY4%3D|20240422|3|H7|01|05|1|0|||||||有||||有|有|13||90M0O0W0|9MOO|1|0||9028900013M014500021O009100021O009103110|0|||||1|0#0#0#0#z#0#z||7|CHN,CHN|||N#N#||90091|202404081430|"],"flag":"1","level":"0","map":{"CZH":"常州","QYH":"戚墅堰","SHH":"上海","SXH":"上海西","WJU":"武进","IMH":"松江南","EGH":"金山北","AOH":"上海虹桥","JTU":"金坛","ESH":"常州北","SAH":"松江","SNH":"上海南"}},"messages":"","status":true}
```



# 点击预定按钮

```
POST https://kyfw.12306.cn/otn/login/checkUser HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 10
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
Accept: */*
X-Requested-With: XMLHttpRequest
If-Modified-Since: 0
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=2XRG5wcEaTCZHItpZfFNufGdG7fFyY8Sxhh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; uKey=8dff2075b4a7e3ed51395469a594236082e1e6d10dfc9cf8a7c7f1eaf3c16ff9; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_fromDate=2024-04-22; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH

_json_att=
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:36:54 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 134
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 PS-JDZ-01Mt655:18 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260586_PS-JDZ-01zlo49_8373-30164
X-Cdn-Src-Port: 13509

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"flag":true},"messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 589
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=2XRG5wcEaTCZHItpZfFNufGdG7fFyY8Sxhh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; uKey=8dff2075b4a7e3ed51395469a594236082e1e6d10dfc9cf8a7c7f1eaf3c16ff9; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_fromDate=2024-04-22; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH

secretStr=OXpsqw%2FLc6CQaHzTZiudBzJwTRFcC%2FCxr1wO8K9I6y9y5D3pBQykDv959BN4vQkg9UsePHZoSmJA%0A69FJ4zYHInx%2FfJGHNB63QLEexwW6DAb7m6kQvd0DjLEcKLWjIJJZr0ouByc%2B7rA%2BUTygrUpy8rn9%0APF%2BaMQFsdSxA4Fr0YHSlihDZzpRPWb7%2BFGAMdQ8dY9Y5aMEE5E8GGG4r5AHXxupNThxtlSViDxKy%0A5k2XCH5dzk0NGYSUCyZYe%2F%2BF26jiU%2BtxyYX%2FGyablUx8ksPTOke7Lqbzt70FqWRen6wmBHVKWs5X%0AyW0mTSP%2BI5wrcDSEUVsdaA9PWtJsU79t7Dn0N2lLs9I%3D&train_date=2024-04-22&back_train_date=2024-04-22&tour_flag=dc&purpose_codes=ADULT&query_from_station_name=上海&query_to_station_name=常州&bed_level_info=&seat_discount_info=90091&undefined
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:36:54 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 126
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 PS-JDZ-01Mt655:21 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260586_PS-JDZ-01zlo49_8373-30210
X-Cdn-Src-Port: 13509

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":"220","messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/otn/confirmPassenger/initDc HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 10
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
Upgrade-Insecure-Requests: 1
Origin: https://kyfw.12306.cn
Content-Type: application/x-www-form-urlencoded
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Referer: https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; uKey=f6971e90e4ee73da23b4b91401d30eda4f5181da3d241de84a036cbadbd30754; _jc_save_fromDate=2024-04-25

_json_att=
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:48:04 GMT
Content-Type: text/html;charset=utf-8
Connection: keep-alive
ct: C1_232_28_9
Content-Language: zh-CN
X-Via: 1.1 PS-JDZ-01zlo49:3 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260824_PS-JDZ-01zlo49_8411-30021
X-Cdn-Src-Port: 56657
Content-Length: 112049

<!DOCTYPE html
```



# initDC.html

```
POST https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 63
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=2XRG5wcEaTCZHItpZfFNufGdG7fFyY8Sxhh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; uKey=8dff2075b4a7e3ed51395469a594236082e1e6d10dfc9cf8a7c7f1eaf3c16ff9; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_fromDate=2024-04-22; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH

_json_att=&REPEAT_SUBMIT_TOKEN=9e024bb654fc9a7face35e6e302592cf
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:37:18 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 PS-JDZ-01Mt655:18 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 6626059e_PS-JDZ-01zlo49_8730-24945
X-Cdn-Src-Port: 20426
Content-Length: 4641

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"notify_for_gat":"","isExist":true,"exMsg":"","two_isOpenClick":["93","95","97","99"],"other_isOpenClick":["91","93","98","99","95","97"],"normal_passengers":[{"passenger_name":"何映峰","sex_code":"M","sex_name":"男","born_date":"2000-01-15 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********815","passenger_type":"1","passenger_type_name":"成人","mobile_no":"177****5166","phone_no":"","email":"1577975140@qq.com","address":"","postalcode":"","first_letter":"HYF","recordCount":"5","total_times":"99","index_id":"0","allEncStr":"2dcb3f1fd997c1ac3c1384d5e9425af93acd655663abe115d07f0afb26ac8ef550cac39d64fca2c8541c1e7265c9d0af8f41855fba4a0dd7f0218ce69d046a8c4a76746eb2a05700c60e204a01a89ea8443bbd37894cc28911a60e93cf6ba535","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"Y","is_active":"N","last_time":"20180707070419","mobile_code":"86","gat_born_date":"20000115","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""},{"passenger_name":"何飞","sex_code":"M","sex_name":"男","born_date":"1973-04-17 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********819","passenger_type":"1","passenger_type_name":"成人","mobile_no":"138****6360","phone_no":"","email":"","address":"","postalcode":"","first_letter":"HF","recordCount":"5","total_times":"99","index_id":"1","allEncStr":"b0916d0034fc9370c055f47e821b6df68b7c39e3e1a898cf2b6fc05157c22fbb1fc3921d30f5cfd7898255b69a859ef072694f9b735758b33d1845bb0797b64b812b20e9abcb7b8b2f8243ad2b4b3615","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"Y","is_active":"N","last_time":"20180722094038","mobile_code":"86","gat_born_date":"19730417","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""},{"passenger_name":"何文杰","sex_code":"M","sex_name":"男","born_date":"2001-02-16 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********836","passenger_type":"1","passenger_type_name":"成人","mobile_no":"13205202167","phone_no":"","email":"","address":"","postalcode":"","first_letter":"HWJ","recordCount":"5","total_times":"99","index_id":"2","allEncStr":"75570daa7c351f12e31a36aa79a953d9f77510051ee72b62ca31253e660065a23312847a1d28924887204629597c69d76800c2b40d4e9afc49fa431d85f8b171496de82f6248e5ed5c3dbb7bc2d78a78","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"N","is_active":"N","last_time":"20190613210622","mobile_code":"86","gat_born_date":"20190613","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""},{"passenger_name":"何瑶瑶","sex_code":"F","sex_name":"女","born_date":"1995-11-16 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********823","passenger_type":"1","passenger_type_name":"成人","mobile_no":"136****9578","phone_no":"","email":"","address":"","postalcode":"","first_letter":"HYY","recordCount":"5","total_times":"99","index_id":"3","allEncStr":"9ec5f02fd840798f870e02884c57409db760d204117762423f1e939ea7caf01f1c4f810fef4c0952238414e1ea06e4effebf27b9d578c33a79a94f7cf2f72f61496de82f6248e5ed5c3dbb7bc2d78a78","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"Y","is_active":"N","last_time":"20230923154415","mobile_code":"86","gat_born_date":"19951116","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""},{"passenger_name":"孙长凤","sex_code":"F","sex_name":"女","born_date":"1970-03-18 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********845","passenger_type":"1","passenger_type_name":"成人","mobile_no":"177****5166","phone_no":"","email":"","address":"","postalcode":"","first_letter":"SCF","recordCount":"5","total_times":"99","index_id":"4","allEncStr":"eb4b68a50bfec61f3562e4852c363fe188750ceaf6c022a5f79f99660695b6d80bc157b191a077fdd51a924505dce7280509d02e168813345453131bc61eba4d496de82f6248e5ed5c3dbb7bc2d78a78","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"Y","is_active":"N","last_time":"20210919192735","mobile_code":"86","gat_born_date":"19700318","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""}],"dj_passengers":[]},"messages":[],"validateMessages":{}}
```



# 提交订单

```
POST https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 1145
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: application/json, text/javascript, */*; q=0.01
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; uKey=f6971e90e4ee73da23b4b91401d30eda4f5181da3d241de84a036cbadbd30754; _jc_save_fromDate=2024-04-25

cancel_flag=2&bed_level_order_num=000000000000000000000000000000&passengerTicketStr=M%2C0%2C1%2C%E4%BD%95%E6%98%A0%E5%B3%B0%2C1%2C3207***********815%2C177****5166%2CN%2C2dcb3f1fd997c1ac3c1384d5e9425af93acd655663abe115d07f0afb26ac8ef550cac39d64fca2c8541c1e7265c9d0af8f41855fba4a0dd7f0218ce69d046a8c4a76746eb2a05700c60e204a01a89ea8443bbd37894cc28911a60e93cf6ba535_O%2C0%2C1%2C%E4%BD%95%E9%A3%9E%2C1%2C3207***********819%2C138****6360%2CN%2Cb0916d0034fc9370c055f47e821b6df68b7c39e3e1a898cf2b6fc05157c22fbb1fc3921d30f5cfd7898255b69a859ef072694f9b735758b33d1845bb0797b64b812b20e9abcb7b8b2f8243ad2b4b3615_P%2C0%2C1%2C%E4%BD%95%E6%96%87%E6%9D%B0%2C1%2C3207***********836%2C13205202167%2CN%2C75570daa7c351f12e31a36aa79a953d9f77510051ee72b62ca31253e660065a23312847a1d28924887204629597c69d76800c2b40d4e9afc49fa431d85f8b171496de82f6248e5ed5c3dbb7bc2d78a78&oldPassengerStr=%E4%BD%95%E6%98%A0%E5%B3%B0%2C1%2C3207***********815%2C1_%E4%BD%95%E9%A3%9E%2C1%2C3207***********819%2C1_%E4%BD%95%E6%96%87%E6%9D%B0%2C1%2C3207***********836%2C1_&tour_flag=dc&whatsSelect=1&sessionId=&sig=&scene=nc_login&_json_att=&REPEAT_SUBMIT_TOKEN=98480ec31d8d4b4276175a0038fe10d0
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:50:00 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 264
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 dianx57:16 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260898_dianx57_34473-1592
X-Cdn-Src-Port: 45693

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"canChooseBeds":"N","canChooseSeats":"Y","choose_Seats":"MOP","isCanChooseMid":"N","ifShowPassCodeTime":"1","submitStatus":true,"smokeStr":""},"messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 412
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: application/json, text/javascript, */*; q=0.01
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; uKey=f6971e90e4ee73da23b4b91401d30eda4f5181da3d241de84a036cbadbd30754; _jc_save_fromDate=2024-04-25

train_date=Thu+Apr+25+2024+00%3A00%3A00+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&train_no=55000G778410&stationTrainCode=G7784&seatType=M&fromStationTelecode=SHH&toStationTelecode=CZH&leftTicket=zcugJ6OsM3a%252BmIhDrsVIQCxnd5ylruo6Kwc%252FZcIVG1o8mIf2Y1cN85MydYV72hoiEPCux%252BeZZYI%253D&purpose_codes=00&train_location=H1&_json_att=&REPEAT_SUBMIT_TOKEN=98480ec31d8d4b4276175a0038fe10d0
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:50:00 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 190
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 dianx57:16 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260898_dianx57_34473-1631
X-Cdn-Src-Port: 45693

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"count":"0","ticket":"8","op_2":"false","countT":"0","op_1":"false"},"messages":[],"validateMessages":{}}
```



页面展示：请核对以下信息

选的三个票信息、第一个用户的余票数

# 点击确认



```
POST https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 4453
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: application/json, text/javascript, */*; q=0.01
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; uKey=f6971e90e4ee73da23b4b91401d30eda4f5181da3d241de84a036cbadbd30754; _jc_save_fromDate=2024-04-25

passengerTicketStr=M%2C0%2C1%2C%E4%BD%95%E6%98%A0%E5%B3%B0%2C1%2C3207***********815%2C177****5166%2CN%2C2dcb3f1fd997c1ac3c1384d5e9425af93acd655663abe115d07f0afb26ac8ef550cac39d64fca2c8541c1e7265c9d0af8f41855fba4a0dd7f0218ce69d046a8c4a76746eb2a05700c60e204a01a89ea8443bbd37894cc28911a60e93cf6ba535_O%2C0%2C1%2C%E4%BD%95%E9%A3%9E%2C1%2C3207***********819%2C138****6360%2CN%2Cb0916d0034fc9370c055f47e821b6df68b7c39e3e1a898cf2b6fc05157c22fbb1fc3921d30f5cfd7898255b69a859ef072694f9b735758b33d1845bb0797b64b812b20e9abcb7b8b2f8243ad2b4b3615_P%2C0%2C1%2C%E4%BD%95%E6%96%87%E6%9D%B0%2C1%2C3207***********836%2C13205202167%2CN%2C75570daa7c351f12e31a36aa79a953d9f77510051ee72b62ca31253e660065a23312847a1d28924887204629597c69d76800c2b40d4e9afc49fa431d85f8b171496de82f6248e5ed5c3dbb7bc2d78a78&oldPassengerStr=%E4%BD%95%E6%98%A0%E5%B3%B0%2C1%2C3207***********815%2C1_%E4%BD%95%E9%A3%9E%2C1%2C3207***********819%2C1_%E4%BD%95%E6%96%87%E6%9D%B0%2C1%2C3207***********836%2C1_&purpose_codes=00&key_check_isChange=FF448467A608DCDA07997B887EFE9D111DFFB14A77B1D0E638C41B18&leftTicketStr=zcugJ6OsM3a%252BmIhDrsVIQCxnd5ylruo6Kwc%252FZcIVG1o8mIf2Y1cN85MydYV72hoiEPCux%252BeZZYI%253D&train_location=H1&choose_seats=&seatDetailType=000&is_jy=N&is_cj=Y&encryptedData=Os_eF3n.PqNdNE1AyoIXksRbImOl2oKxPfILDdakVFiLY2Bmsj3N8ZytK_BKHQhB8LTzyk4kxSrr9UORDKL78aLONlMUvS8rwJaeSz1_sBd5zwAtqtOnkPyifm24TSWMsNgyuXUazJbbsIEVmGKa5D.BkCbclZ1Sb0NqK6kQasKQoFt7zESOOfr8g0pqLP9gpQf9Bwk17dfM9KDQSfsMU6av4PcBxCzHU45GNqPBhJ6CGcKg7rupnpXLV6pTj_j9oRGsrrS9NRcfdvWULKNMAcKZcDQK.M1M1ZOABVRa6zqgK2INJbuIHgeYojIPaLWg1tNcvlv8hkMkDn1Bca0Ly8puDRTSYQuIS2iUlEe10b_17l8fffoFhrSlczF1Ztml4pze.ogykHddfY9ZO_FMchbtXNDdg4KtaWdOhCUYQ66ZR58gPhzBu85.AeeYU87YYtFDJliS.gKFNardoC4ILuKQX7wRa1qDLzphqzviZuGatMmX8aoS.ko_PHOndC1SL.2Spqi1_l_6arX8rQVbYMNA8EUhbmDWjuznnQhA6_CGZMqJTPkN1BFTUPle52mTF9VxWKel7nw5PJnL53VTZ_0pOu_.ByrbEqut02tSkhg7kD78mLDAKW_dClUMIB0Eemby0EipISQQmCWZIR.zk71Fe2H5xxyrKgmLeZiqvHrsyKJmZwRolSgzOa.8ynoX7xuLBUdWKcTaNETINFP.VI6XLMPdCXWgru.lx5_5Vr.trCd5iOGTC76BEONPBcXTzWlo5XSZcs4.QZaTJh9SpXbAdMCYml.gjnuA90DcGDvf2c5P5syMOu2nPVNMTukuKphl_3dUQ1T2KKr_i7v63Lg3xak00EMgIZszzKndrrKZk6mgJv_MyPaa5CJfAVAxkdk9apJ5utDlcbquPv_V1I3i2btUTqNJwPK83f4NJLc9AfV2jJUMXqxlm0cRmTD3IGkrmyqu8LyxhEcdC2xIzftLXhRiuHCY.3IC4pyviPw8HL0Xb9fUxZ5XVcx5gyf7wa.039EOW8r083XLeen7kp4CmSTe6q2SymYNsm9vWTDFFOXUMVdD3onxYO.WaQEvqfSusFdDXNLQwiKJHibpV3oMfaZP8Kunt8QOgxvulUALmUiPZ8kM2iQO6_x5eGtpwg7GIVPVzIexOGv5Yr9TBV4MnNoFbArBlIYIBsmrZnG4P7dI8CcS87PritvnQg7t_jkX4L50k8t3AgA_HIcjhEyyxy_ek508AymARtpiGVx7eDFnPQTeybeYieiiUgnazk_CNRFlXSK6lHFhfyn5Nt.WgI.QDyGI3cUnmV4IsunWVmyycqcNDxn_Kptfea559qr3Zf8WP8lZqdCZwgN8KSBavBoDitwvxLlk3gJTHxxE0y9aXUBNqs5TjxFUpnSHNI6V89ectljS7Y4YtCigdeGiwhXxJ5PQfdrb.SXHOHwLI8B2I7KqjNUC2c2XM1WuRXi_iBZSmMixSoMs9KbUiflkHW6cCoH9QeIJ4YBcS1S_KyIBcSzCsv8VKFzcfEB.9W0BfUc8KCON4UIi._fopaDtpNBz84rSFmvpSnq7PcOuGV20vuFHt2S794HntRGPHJc78wCT1Ay8AE5UUJ5.8SaMCXRrmM2PUvj_lqEx_b3IOl6xQKVvPtucSIlYyXLsV9nKuk2Olsfl4.4m0HuD3iDqP2fEMBny_ZGl0DZdCrXGf5NIdpj6k.ewgGmQx9e4hZZZDHELEodoI9TQJXcqk6RPKvvq0YMJ__WwmSYT7eq952xyumnCxRCCyez439xwJjrXPys4OyMTmvd2vMqx5Iu.5tm6sDyWWuqSLxFCWK37usNGiwJo0PtreCcwr3kTONkJl6yh5PYC3bO.ygS_mbpjLc_QV2sT.08MG6txwmwhatwEuUtpnVTZypjDN0VTfPujcKxnlUjj0kdsbKf..HbvAzAWaS1O2lFiCvf0YmjrRB3EklvGEqol8WCGIPRk0u8yWLzUVWRR6Lh3halWS1YVK4Rycx78jPEuGgFWLm_r_hf8GYFc9uJ86T9rJ5NK_fCmzqrdZHX43o_KbKmfwf8uBIzA7PojiScukhQ5fe6tm_UTiH8MA9eUKbSImKn8fcT.oAxwuKOnOztBCuTOo5RP7aCjYyhCV0AKpZbGoBYNs2vfBm23iyXwHGJFweeOOJ9hPoo_5gXTVa8JQyp7V6yk7hNvipMh0ST1m43I9E_T7Hz.h_PDYYV7Gkg3eEQYUIXPxneomFvtGNLdBw6AiutyWJ8jt9lqPx9617TU3Y2b7hkkOMlxPtbyLzI8mi_ksnMwvCp7bv059LheieXZAb_mS4ePnTQ5OE2vEp77eJX5vgR0ZysC5Vpvy_VMizQV9pb1__MTkbpHDFhySNeQ1ghaMf1mp_h6ZOqifvkO57V8bsdO4YavlpVkGtSjMFhDi6I5YtaWsOkGD1EeMroczRqjS7_lPn9Wq1kSHCnepB6UiSdny8ZSYK4Vp5Vyt8uxtlGEZB.QHSsgF6i1RIRbi5tvJzMRMiCuha9qhApCLlmZC_2pG4AoXHJQ0870IUrDb7Ciaf8l754eV9U9Uvf_iILwcOmnBVcjr6JQvFdZ_7pagiLrs2ZWgIaulOvX.lSMfn4jRIXEABBe8S1lpYtgA9PSCCRiqIsDmvsJ52cNZfhqLGRYJn11W9MJs82daucHcnpd2ad9xBffOb1TzeYCHZaskVaS4DoW8AWrzZDNzTCBWDF13dX.ezCAtaH2woaBwA6uZ2AZHJWC1EZ1UTSpoAzfXi_Ga74n08WGorfNTHxaarH7.X1Todprh4nN87vCueoyjVoz6qQlTUkQJZLkkR6quQD7knpWzFB_HMmN6uJWVk3LG3J6.gsif8OmaL0E602zhwX8d1fONAwEaXITFH2E9jvsl2GzpXjOe4doNLh8xkKja1yCWqGtrY_HU74E72QpMrqOuArmrX5esm9f0D3UovPDIGpXeWLNBJY2OaaugaOV4fhGUU_7B5WEgUxNH1pdT99vaf5yT4.8NOP76qQYRQ4BHYLob_PLSN_FYLIO1r6cLro81E877jVkGFIjoSFVtYiLn3LHe_VAboB17WbFpkUz5rVn5rW9veE5oSw&whatsSelect=1&roomType=00&dwAll=N&_json_att=&REPEAT_SUBMIT_TOKEN=98480ec31d8d4b4276175a0038fe10d0
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:52:32 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 156
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 PS-JDZ-01Mt655:18 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260930_PS-JDZ-01zlo49_8480-47875
X-Cdn-Src-Port: 23971

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"isAsync":"1","submitStatus":true},"messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/otn/basedata/log HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 71
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: application/json, text/javascript, */*; q=0.01
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; uKey=f6971e90e4ee73da23b4b91401d30eda4f5181da3d241de84a036cbadbd30754; _jc_save_fromDate=2024-04-25

type=dc&_json_att=&REPEAT_SUBMIT_TOKEN=98480ec31d8d4b4276175a0038fe10d0
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:52:32 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 125
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 PS-JDZ-01zlo49:2 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260930_PS-JDZ-01zlo49_8424-6862
X-Cdn-Src-Port: 23972

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":true,"messages":[],"validateMessages":{}}
```



查询订单状态-每秒调用一次

```
GET https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime?random=1713768752217&tourFlag=dc&_json_att=&REPEAT_SUBMIT_TOKEN=98480ec31d8d4b4276175a0038fe10d0 HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: application/json, text/javascript, */*; q=0.01
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; uKey=f6971e90e4ee73da23b4b91401d30eda4f5181da3d241de84a036cbadbd30754; _jc_save_fromDate=2024-04-25


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:52:32 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 257
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 zhendianxin40:19 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260930_PS-JDZ-01zlo49_8480-47962
X-Cdn-Src-Port: 23971

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"queryOrderWaitTimeStatus":true,"count":0,"waitTime":-100,"requestId":7188067141190850573,"waitCount":0,"tourFlag":"dc","orderId":null},"messages":[],"validateMessages":{}}
```

```
GET https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime?random=1713768763224&tourFlag=dc&_json_att=&REPEAT_SUBMIT_TOKEN=98480ec31d8d4b4276175a0038fe10d0 HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: application/json, text/javascript, */*; q=0.01
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; uKey=f6971e90e4ee73da23b4b91401d30eda4f5181da3d241de84a036cbadbd30754; _jc_save_fromDate=2024-04-25


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:52:43 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 254
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 zhendianxin40:19 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 6626093b_PS-JDZ-01zlo49_8480-50520
X-Cdn-Src-Port: 23971

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"queryOrderWaitTimeStatus":true,"count":0,"waitTime":4,"requestId":7188067184035665933,"waitCount":0,"tourFlag":"dc","orderId":null},"messages":[],"validateMessages":{}}
```

```
GET https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime?random=1713768766223&tourFlag=dc&_json_att=&REPEAT_SUBMIT_TOKEN=98480ec31d8d4b4276175a0038fe10d0 HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: application/json, text/javascript, */*; q=0.01
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; uKey=f6971e90e4ee73da23b4b91401d30eda4f5181da3d241de84a036cbadbd30754; _jc_save_fromDate=2024-04-25


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:52:46 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 263
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 zhendianxin40:19 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 6626093e_PS-JDZ-01zlo49_8480-51189
X-Cdn-Src-Port: 23971

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"queryOrderWaitTimeStatus":true,"count":0,"waitTime":-1,"requestId":7188067184035665933,"waitCount":0,"tourFlag":"dc","orderId":"EH12506279"},"messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/otn/confirmPassenger/resultOrderForDcQueue HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 91
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: application/json, text/javascript, */*; q=0.01
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; uKey=f6971e90e4ee73da23b4b91401d30eda4f5181da3d241de84a036cbadbd30754; _jc_save_fromDate=2024-04-25

orderSequence_no=EH12506279&_json_att=&REPEAT_SUBMIT_TOKEN=98480ec31d8d4b4276175a0038fe10d0
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:52:47 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 142
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 PS-JDZ-01Mt655:18 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 6626093f_PS-JDZ-01zlo49_8480-51398
X-Cdn-Src-Port: 23971

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"submitStatus":true},"messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/otn/payOrder/init?random=1713768767434 HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 63
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
Upgrade-Insecure-Requests: 1
Origin: https://kyfw.12306.cn
Content-Type: application/x-www-form-urlencoded
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: navigate
Sec-Fetch-Dest: document
Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; uKey=f6971e90e4ee73da23b4b91401d30eda4f5181da3d241de84a036cbadbd30754; _jc_save_fromDate=2024-04-25

_json_att=&REPEAT_SUBMIT_TOKEN=98480ec31d8d4b4276175a0038fe10d0
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 06:52:47 GMT
Content-Type: text/html;charset=utf-8
Connection: keep-alive
ct: C1_232_28_9
Content-Language: zh-CN
X-Via: 1.1 PS-JDZ-01KFD56:9 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 6626093f_PS-JDZ-01zlo49_8480-51445
X-Cdn-Src-Port: 23971
Content-Length: 142922

<!DOCTYPE html 
```



# init.html

# 支付订单

页面一个单框警告，后面是席位已锁定，请。。。支付，剩余时间：xxx





# 候补

# 点击候补按钮

```
POST https://kyfw.12306.cn/otn/afterNate/chechFace HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 421
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
Accept: */*
X-Requested-With: XMLHttpRequest
If-Modified-Since: 0
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/leftTicket/init
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; uKey=c6b0e96c538fb6b378a669ea8b74952e1e302c3df800c3d80191ad470cd10aee; _jc_save_fromDate=2024-05-01

secretList=%252BhA%252Fx1Wk5YiceFBC0FA5x5Pf%252FkG%252FfcYFrfqJt2dy%252FENn0u5y%252FDnOyGJVo9skvxxaXpdEzliIBA8j%250AZH9lvF5xdlDfuUC0trS%252BiTfhNkM7pDG29jM2zii8KHUtbH1zlYFNDSx9TLCWjzKGpCYcbRiSKTFm%250A2k%252FiD%252FzpdcruivFOGHAEGsqkyxq8GlaDDbxfiV4syIHMZ3ELBQewGdqMCHckjlSbfbnhFuKRegEL%250ACMKXEOtpML4H%252Fbud5Hf9SF5x6Vj7gQj0JdpXU5zaMzZUuP9j9ZA%252F6i%252BWx%252FfeYGZ7JCGarhGjo9Ou%250AB0wp8NN8x0lQ8jAk%23O%7C&_json_att=
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:01:07 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 202
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 dianx57:9 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260b33_dianx57_34387-65298
X-Cdn-Src-Port: 35668

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"login_flag":true,"is_show_qrcode":true,"face_check_code":"12","face_flag":true},"messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/otn/login/checkUser HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 10
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
Accept: */*
X-Requested-With: XMLHttpRequest
If-Modified-Since: 0
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/leftTicket/init
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; uKey=c6b0e96c538fb6b378a669ea8b74952e1e302c3df800c3d80191ad470cd10aee; _jc_save_fromDate=2024-05-01

_json_att=
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:01:07 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 134
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 dianx57:16 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260b33_dianx57_34402-65062
X-Cdn-Src-Port: 35667

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"flag":true},"messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/otn/afterNate/submitOrderRequest HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 421
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/leftTicket/init
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; uKey=c6b0e96c538fb6b378a669ea8b74952e1e302c3df800c3d80191ad470cd10aee; _jc_save_fromDate=2024-05-01

secretList=%252BhA%252Fx1Wk5YiceFBC0FA5x5Pf%252FkG%252FfcYFrfqJt2dy%252FENn0u5y%252FDnOyGJVo9skvxxaXpdEzliIBA8j%250AZH9lvF5xdlDfuUC0trS%252BiTfhNkM7pDG29jM2zii8KHUtbH1zlYFNDSx9TLCWjzKGpCYcbRiSKTFm%250A2k%252FiD%252FzpdcruivFOGHAEGsqkyxq8GlaDDbxfiV4syIHMZ3ELBQewGdqMCHckjlSbfbnhFuKRegEL%250ACMKXEOtpML4H%252Fbud5Hf9SF5x6Vj7gQj0JdpXU5zaMzZUuP9j9ZA%252F6i%252BWx%252FfeYGZ7JCGarhGjo9Ou%250AB0wp8NN8x0lQ8jAk%23O%7C&_json_att=
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:01:07 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 134
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 PS-JDZ-01zlo49:1 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260b33_PS-JDZ-01zlo49_8672-20443
X-Cdn-Src-Port: 39860

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"flag":true},"messages":[],"validateMessages":{}}
```



```
GET https://kyfw.12306.cn/otn/view/lineUp_toPay.html HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Referer: https://kyfw.12306.cn/otn/leftTicket/init
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; uKey=c6b0e96c538fb6b378a669ea8b74952e1e302c3df800c3d80191ad470cd10aee; _jc_save_fromDate=2024-05-01


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:01:07 GMT
Content-Type: text/html
Connection: keep-alive
Expires: Mon, 22 Apr 2024 01:24:11 GMT
Cache-Control: max-age=0
Age: 20216
X-Via: 1.1 PS-JDZ-01M3U44:2 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260b33_PS-JDZ-01zlo49_8672-20502
X-Cdn-Src-Port: 39860
Content-Length: 95156

<!DOCTYPE html>

```



# lineUp_toPay.html

注意页面有接受无座按钮，默认不接受

接受新增列车按钮，默认不接受，点击会弹框，功能要后续确认

```
POST https://kyfw.12306.cn/otn/login/conf HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 0
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/view/lineUp_toPay.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; uKey=c6b0e96c538fb6b378a669ea8b74952e1e302c3df800c3d80191ad470cd10aee; _jc_save_fromDate=2024-05-01


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:01:08 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 682
Connection: keep-alive
ct: C1_232_28_9
Access-Control-Allow-Origin: *
X-Via: 1.1 PS-JDZ-01M3U44:14 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260b34_PS-JDZ-01zlo49_8411-20201
X-Cdn-Src-Port: 39956

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"is_open_updateHBTime":"Y","isstudentDate":false,"is_message_passCode":"N","born_date":"2000-01-15","is_phone_check":"N","ei_email":"15******40@qq.com","studentDate":["2020-04-01","2020-11-30","2020-12-01","2020-12-31","2021-01-01","2025-09-30"],"is_uam_login":"Y","is_login_passCode":"Y","user_name":"hyfsya","is_sweep_login":"Y","queryUrl":"leftTicket/queryG","nowStr":"20240422150110","psr_qr_code_result":"N","now":1713769270386,"name":"何映峰","login_url":"resources/login.html","stu_control":15,"is_login":"Y","is_olympicLogin":"N","other_control":15},"messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 0
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/view/lineUp_toPay.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; uKey=c6b0e96c538fb6b378a669ea8b74952e1e302c3df800c3d80191ad470cd10aee; _jc_save_fromDate=2024-05-01


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:01:08 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 PS-JDZ-01Mt655:18 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260b34_PS-JDZ-01zlo49_8411-20234
X-Cdn-Src-Port: 39956
Content-Length: 4641

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"notify_for_gat":"","isExist":true,"exMsg":"","two_isOpenClick":["93","95","97","99"],"other_isOpenClick":["91","93","98","99","95","97"],"normal_passengers":[{"passenger_name":"何映峰","sex_code":"M","sex_name":"男","born_date":"2000-01-15 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********815","passenger_type":"1","passenger_type_name":"成人","mobile_no":"177****5166","phone_no":"","email":"1577975140@qq.com","address":"","postalcode":"","first_letter":"HYF","recordCount":"5","total_times":"99","index_id":"0","allEncStr":"2dcb3f1fd997c1ac3c1384d5e9425af93acd655663abe115d07f0afb26ac8ef550cac39d64fca2c8541c1e7265c9d0af8f41855fba4a0dd7f0218ce69d046a8c4a76746eb2a05700c60e204a01a89ea8443bbd37894cc28911a60e93cf6ba535","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"Y","is_active":"N","last_time":"20180707070419","mobile_code":"86","gat_born_date":"20000115","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""},{"passenger_name":"何飞","sex_code":"M","sex_name":"男","born_date":"1973-04-17 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********819","passenger_type":"1","passenger_type_name":"成人","mobile_no":"138****6360","phone_no":"","email":"","address":"","postalcode":"","first_letter":"HF","recordCount":"5","total_times":"99","index_id":"1","allEncStr":"b0916d0034fc9370c055f47e821b6df68b7c39e3e1a898cf2b6fc05157c22fbb1fc3921d30f5cfd7898255b69a859ef072694f9b735758b33d1845bb0797b64b812b20e9abcb7b8b2f8243ad2b4b3615","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"Y","is_active":"N","last_time":"20180722094038","mobile_code":"86","gat_born_date":"19730417","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""},{"passenger_name":"何文杰","sex_code":"M","sex_name":"男","born_date":"2001-02-16 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********836","passenger_type":"1","passenger_type_name":"成人","mobile_no":"13205202167","phone_no":"","email":"","address":"","postalcode":"","first_letter":"HWJ","recordCount":"5","total_times":"99","index_id":"2","allEncStr":"75570daa7c351f12e31a36aa79a953d9f77510051ee72b62ca31253e660065a23312847a1d28924887204629597c69d76800c2b40d4e9afc49fa431d85f8b171496de82f6248e5ed5c3dbb7bc2d78a78","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"N","is_active":"N","last_time":"20190613210622","mobile_code":"86","gat_born_date":"20190613","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""},{"passenger_name":"何瑶瑶","sex_code":"F","sex_name":"女","born_date":"1995-11-16 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********823","passenger_type":"1","passenger_type_name":"成人","mobile_no":"136****9578","phone_no":"","email":"","address":"","postalcode":"","first_letter":"HYY","recordCount":"5","total_times":"99","index_id":"3","allEncStr":"9ec5f02fd840798f870e02884c57409db760d204117762423f1e939ea7caf01f1c4f810fef4c0952238414e1ea06e4effebf27b9d578c33a79a94f7cf2f72f61496de82f6248e5ed5c3dbb7bc2d78a78","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"Y","is_active":"N","last_time":"20230923154415","mobile_code":"86","gat_born_date":"19951116","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""},{"passenger_name":"孙长凤","sex_code":"F","sex_name":"女","born_date":"1970-03-18 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********845","passenger_type":"1","passenger_type_name":"成人","mobile_no":"177****5166","phone_no":"","email":"","address":"","postalcode":"","first_letter":"SCF","recordCount":"5","total_times":"99","index_id":"4","allEncStr":"eb4b68a50bfec61f3562e4852c363fe188750ceaf6c022a5f79f99660695b6d80bc157b191a077fdd51a924505dce7280509d02e168813345453131bc61eba4d496de82f6248e5ed5c3dbb7bc2d78a78","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"Y","is_active":"N","last_time":"20210919192735","mobile_code":"86","gat_born_date":"19700318","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""}],"dj_passengers":[]},"messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/otn/afterNate/passengerInitApi HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 0
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/view/lineUp_toPay.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; uKey=c6b0e96c538fb6b378a669ea8b74952e1e302c3df800c3d80191ad470cd10aee; _jc_save_fromDate=2024-05-01


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:01:08 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 PS-JDZ-01KFD56:10 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260b34_PS-JDZ-01zlo49_8411-20294
X-Cdn-Src-Port: 39956
Content-Length: 3767

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"limitTranStr":[],"canChooseWseat":"N","jzdhDateE":"2024-05-01","jzdhHourE":"05:41","hbTrainList":[{"start_city_code":"0712","houbu_train_flag":"1","isForegin":"0","lishi":"01:05","location_code":"H1","from_station_no":"01","control_day":"14","day_difference":"0","sale_time":"1430","to_station_name":"常州","station_train_code":"G3124","country_flag":"CHN,CHN","local_arrive_time":"","to_station_no":"05","seat_type_name":"二等座","end_city_code":"1314","train_no":"55000G312420","local_start_time":"","from_station_telecode":"SHH","yp_info":"M010900000O006800000","to_station_telecode":"CZH","seat_type_code":"O","from_station_name":"上海","train_date_str":"2024年5月1日","start_train_date":"20240501","start_station_telecode":"SHH","arrive_time":"07:11","train_date":"2024-05-01","end_station_telecode":"WHN","start_time":"06:06"}],"jzdhDiffSelect":["20","60","120","180","360","720","1440","2880","4320","5760","7200","8640","10080","11520"],"limitSeatType":{"3":[{"id":"1","value":"硬座"},{"id":"3","value":"硬卧"},{"id":"8","value":"二等软座"},{"id":"B","value":"混编硬座"},{"id":"C","value":"混编硬卧"},{"id":"O","value":"二等座"}],"2":[{"id":"1","value":"硬座"},{"id":"2","value":"软座"},{"id":"3","value":"硬卧"},{"id":"4","value":"软卧"},{"id":"5","value":"包厢硬卧"},{"id":"6","value":"高级软卧"},{"id":"7","value":"一等软座"},{"id":"8","value":"二等软座"},{"id":"9","value":"商务座"},{"id":"B","value":"混编硬座"},{"id":"C","value":"混编硬卧"},{"id":"E","value":"特等软座"},{"id":"H","value":"一人软包"},{"id":"K","value":"混编软座"},{"id":"I","value":"一等卧"},{"id":"J","value":"二等卧"},{"id":"L","value":"混编软卧"},{"id":"M","value":"一等座"},{"id":"O","value":"二等座"},{"id":"P","value":"特等座"},{"id":"Q","value":"多功能座"},{"id":"S","value":"二等包座"},{"id":"A","value":"高级动卧"},{"id":"F","value":"动卧"}],"1":[{"id":"1","value":"硬座"},{"id":"2","value":"软座"},{"id":"3","value":"硬卧"},{"id":"4","value":"软卧"},{"id":"5","value":"包厢硬卧"},{"id":"6","value":"高级软卧"},{"id":"7","value":"一等软座"},{"id":"8","value":"二等软座"},{"id":"9","value":"商务座"},{"id":"B","value":"混编硬座"},{"id":"C","value":"混编硬卧"},{"id":"E","value":"特等软座"},{"id":"H","value":"一人软包"},{"id":"K","value":"混编软座"},{"id":"I","value":"一等卧"},{"id":"J","value":"二等卧"},{"id":"L","value":"混编软卧"},{"id":"M","value":"一等座"},{"id":"O","value":"二等座"},{"id":"P","value":"特等座"},{"id":"Q","value":"多功能座"},{"id":"S","value":"二等包座"},{"id":"A","value":"高级动卧"},{"id":"F","value":"动卧"}],"4":[{"id":"1","value":"硬座"},{"id":"2","value":"软座"},{"id":"3","value":"硬卧"},{"id":"4","value":"软卧"},{"id":"5","value":"包厢硬卧"},{"id":"6","value":"高级软卧"},{"id":"7","value":"一等软座"},{"id":"8","value":"二等软座"},{"id":"9","value":"商务座"},{"id":"B","value":"混编硬座"},{"id":"C","value":"混编硬卧"},{"id":"E","value":"特等软座"},{"id":"H","value":"一人软包"},{"id":"K","value":"混编软座"},{"id":"I","value":"一等卧"},{"id":"J","value":"二等卧"},{"id":"L","value":"混编软卧"},{"id":"M","value":"一等座"},{"id":"O","value":"二等座"},{"id":"P","value":"特等座"},{"id":"Q","value":"多功能座"},{"id":"S","value":"二等包座"},{"id":"A","value":"高级动卧"},{"id":"F","value":"动卧"}]},"hb_passenger_max_num":9,"result_code":"0","checkcode":"100","jzdhDateS":"2024-04-22","jzdhHourS":"15:01","isLimitTran":"N","if_check_slide_passcode":"0"},"messages":[],"validateMessages":{}}
```



调了三次，后两次间隔一秒

调用其他接口前，也会触发调用该接口

```
POST https://kyfw.12306.cn/otn/afterNate/querySuccessRate HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 64
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/view/lineUp_toPay.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; uKey=c6b0e96c538fb6b378a669ea8b74952e1e302c3df800c3d80191ad470cd10aee; _jc_save_fromDate=2024-05-01

plans=&realize_limit_time_diff=&add_train_flag=N&passenger_num=0
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:01:08 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 136
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 PS-JDZ-01Mt655:15 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260b34_PS-JDZ-01zlo49_8411-20327
X-Cdn-Src-Port: 39956

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"result":"55"},"messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/otn/afterNate/getQueueNum HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 0
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/view/lineUp_toPay.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; uKey=c6b0e96c538fb6b378a669ea8b74952e1e302c3df800c3d80191ad470cd10aee; _jc_save_fromDate=2024-05-01


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:01:09 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 301
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 zhendianxin43:0 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260b34_PS-JDZ-01zlo49_8608-32185
X-Cdn-Src-Port: 39955

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"flag":true,"queueNum":[{"queue_info":"候补人数较少","station_train_code":"G3124","train_date":"20240501","seat_type_code":"O","train_no":"55000G312420","queue_level":"1"}]},"messages":[],"validateMessages":{}}
```



# 选择用户后，点击提交订单，弹框，点击确认

```
POST https://kyfw.12306.cn/otn/afterNate/confirmHB HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 2867
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/view/lineUp_toPay.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; _jc_save_fromDate=2024-05-01; uKey=67a7ceeeafee15c79befe219cb960a1d7291619ed5452ce6cbd655c89bfa2cea

passengerInfo=1%23%E4%BD%95%E6%98%A0%E5%B3%B0%231%233207***********815%232dcb3f1fd997c1ac3c1384d5e9425af93acd655663abe115d07f0afb26ac8ef550cac39d64fca2c8541c1e7265c9d0af8f41855fba4a0dd7f0218ce69d046a8c4a76746eb2a05700c60e204a01a89ea8443bbd37894cc28911a60e93cf6ba535%230%3B1%23%E4%BD%95%E9%A3%9E%231%233207***********819%23b0916d0034fc9370c055f47e821b6df68b7c39e3e1a898cf2b6fc05157c22fbb1fc3921d30f5cfd7898255b69a859ef072694f9b735758b33d1845bb0797b64b812b20e9abcb7b8b2f8243ad2b4b3615%230%3B&jzParam=&hbTrain=&lkParam=&sessionId=&sig=&scene=nc_login&encryptedData=QYikfN62fF.DSOwgrEVGJcMaGgydP9WSSob.3ppGqONhDTQdiLLt9JI4Jg51ksV4yHt8iOnPEJgTfZkCo7HHL5l24pqOdtocZ6TPz2ngrRGLokSVaU0wq8C7gHolEvK9lzFhU7grCLvbs7cCCB_25qBkZxi9QRfnL9Y8hO6C8DxSISphWlG1rD01xCEQe3JTtY8l8FNH9cK2LKOYLQJ0i41bIrYHIR_V0eUT5AMTy9g8IzU3miqvNsrtesXPE9xPv1..dpNEFpEw0rFb0ZCgdhNGSgJl9U5ja1DTB2YGoS6B95mXOXyiE6IELCSkbQoUavJuuN7RB6G2xp5WsXYzDDMC_.WJa0f2GMUffu.FojMFfFFYMuhHaxhLyRSS2ETAjDFcfwMCYQPsQkDUxpEttAKKSzUvjyDixWduA09SRardfgU5XnfVkoAMKGmhmPJAECE0zouBLRoEhGhVd2JjiuhS9Oisi0KfAVXivo2PGpgQM4dOaIR_0Ps6CMahDLFC0ZizklTJ754xYYDekDLCEid8oR_dJO.49BOzUfwZ6mPfFqmIdydQEt12ARMFT.Bx7A8ic052Xg4AJRhBTeZj6CpMPaIPXa9pa1ec8c3FkdDhz1TnDm1FE9WKn.fisOz_R0CmUTyzDe2TA82n2P6.yeMbKK28tsafEphN4b.xALTzQ6BHtvFzROI1zgULjpFbfZLQWNZohwM09QBgdQ0XD0RcMrd4kRxbSk0j6nKUfxBTFe6hxQ6SBoDExqW4LnZbx57Zed_BB.VxN1GzUYSerTRIReEBfagyRMNKZEJeUZL._KHAjPXY02hdbV7HmG4jLBnJtKrWTSz_zRTfXZ4ANAbOZCmXgLBUkmqvlwX7AgS9TTsp9geWXgyZvHJ9e7es3P7IV7WCLbD47j9Qg8PwiEvPGJt1c6yrZwUe9VNpcRTLajWkgEmCFAu4TKUO9EST8BisEN4gwJ1ysjcSQhvl5Q1M9gHVwqj2whmH_UOzAGlibf6hrtXAe2nDrEIhycFwLwXPrJDutljg4I0PLa_gnHWxJFyrDq98T5vChdQAryApEm6tsT5sCekgY1kX3GLgE4kwHUAYOtTHASmIpFMbI8or76cx2VsFXC5mHZpu4zBusD61SzkKcgUh5AwqIRcOuZ_Y7dl.xwxL.LSiVxqxXxLYUCmpRUa1zX.GQ6Z3C.T3Rs_nktGfjHg.XyjQl3RMZu8Gd4BqSrL0gwKA4jGewZ7BJl_9F_ImzE7XeOL_ZeBLeVikZTMBT2MieMQjMK6mty.t7e7nk3vDAs85AxNgCSMBveKNaIgBnd_T2i_HqvLfQ1biz_pebXHKi4DbWT5QLJQrsT2znsqtEHEDeyH9rvy.l3LMACc7sy0cww0Qdg_wKAlWYezwoYug7Rb.Cu7JR0NLzdO3UM2HQ07SQumIg.u7XZZ87QmHAKV914UwV2jKxyzbINTAhTsf3FuFiso0WhLE6qul4hYc4wQYk29oO96eg7.jjet2XY4tAALe5n7qHZvPnLRoWRTeo.F1gxVRG7je8XGKzNU9yCI4ujSvDBGgsJ0k1JJkj6Uh1qMn9GYpwzoTF.TyZuAxxB35KhujaoN1l9jezDz6WXYq7SdwnE4lxPdKwjIMQ3EyAoJmob90qssj1G71f75DtBn720OQ5GVGYoPR0PEQHd6s.QONAtfOvTpuyV0pfHKMaeLUvNlqHneoDWDbdBiycQNovDKzy79SKbLGnaSfbbAkjTNmjF3sOSQI0TbRKtJfycJZLZxNTvzTredws18WtBfgoc_uVvNdgxAoZXMhVFQcvXHSOVRcT99hJaeWPzyH4iWVi4IFFlFe0GCNLeV26qFLsfKdK__x9JZpTwZRrBOjUkUvSNaid0T7cxjczBZyHg76U9Q2WD3plEg_LuqRzrWq8eWLvyxhdsErdQcP3C.BVd0_g1cn0g0ntuts2W8MdLgZDqlbz2ZNzoBG0lrDp8nmU4znu8e4GAVo1WDCRDww63yo1TG4bUf85MjuR5f5Mq_1S7pPixD1LW_7NkyHYGtsrLaO8AlTtwqBAmoHQv76di9AkN4RlPvkokbUVz52PYqMK3ubdblqrBGE2rPRWmyvHu2zipkiFDRHVBmLCGkJ1V1OFq6UOcnB6SosxorKM_ShyqqIXEteLVXSdHkkoEqdgh.U&if_receive_wseat=Y&realize_limit_time_diff=360&plans=&tmp_train_date=&tmp_train_time=&add_train_flag=N&add_train_seat_type_code=
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:17:21 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 187
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 anxin41:6 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260f01_dianx57_34473-48899
X-Cdn-Src-Port: 58600

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"flag":true,"trace_id":"C1_Y_7188073378091538445","isAsync":true},"messages":[],"validateMessages":{}}
```



查询队列和加载页面，总共循环调用了两次

```
POST https://kyfw.12306.cn/otn/afterNate/queryQueue HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 0
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/view/lineUp_toPay.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; _jc_save_fromDate=2024-05-01; uKey=67a7ceeeafee15c79befe219cb960a1d7291619ed5452ce6cbd655c89bfa2cea


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:17:21 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 194
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 anxin41:10 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260f01_dianx57_34473-48970
X-Cdn-Src-Port: 58600

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"flag":true,"reserve_no":"EH9071000300612700","status":1,"isAsync":true},"messages":[],"validateMessages":{}}
```



```
GET https://kyfw.12306.cn/otn/view/lineUp_payConfirm.html HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Referer: https://kyfw.12306.cn/otn/view/lineUp_toPay.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; _jc_save_fromDate=2024-05-01; uKey=67a7ceeeafee15c79befe219cb960a1d7291619ed5452ce6cbd655c89bfa2cea


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:17:21 GMT
Content-Type: text/html
Connection: keep-alive
Expires: Mon, 22 Apr 2024 01:24:00 GMT
Cache-Control: max-age=0
Age: 21201
X-Via: 1.1 dianx57:4 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260f01_dianx57_34473-48995
X-Cdn-Src-Port: 58600
Content-Length: 60569

<!DOCTYPE html>

```



# lineUp_payConfirm.html



```
POST https://kyfw.12306.cn/otn/login/conf HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 0
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/view/lineUp_payConfirm.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; _jc_save_fromDate=2024-05-01; uKey=67a7ceeeafee15c79befe219cb960a1d7291619ed5452ce6cbd655c89bfa2cea


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:17:22 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 682
Connection: keep-alive
ct: C1_232_28_9
Access-Control-Allow-Origin: *
X-Via: 1.1 anxin41:6 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260f02_dianx57_34364-33062
X-Cdn-Src-Port: 58844

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"is_open_updateHBTime":"Y","isstudentDate":false,"is_message_passCode":"N","born_date":"2000-01-15","is_phone_check":"N","ei_email":"15******40@qq.com","studentDate":["2020-04-01","2020-11-30","2020-12-01","2020-12-31","2021-01-01","2025-09-30"],"is_uam_login":"Y","is_login_passCode":"Y","user_name":"hyfsya","is_sweep_login":"Y","queryUrl":"leftTicket/queryG","nowStr":"20240422151724","psr_qr_code_result":"N","now":1713770244234,"name":"何映峰","login_url":"resources/login.html","stu_control":15,"is_login":"Y","is_olympicLogin":"N","other_control":15},"messages":[],"validateMessages":{}}
```



```
POST https://kyfw.12306.cn/otn/afterNatePay/payOrderInit HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 0
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/view/lineUp_payConfirm.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E501424EFAEBE633CF1BD5B04A69BD14; tk=lO063IJ3JrGH35Fm_X4rpMT1EOVDb49Cqrh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; _jc_save_fromDate=2024-05-01; uKey=67a7ceeeafee15c79befe219cb960a1d7291619ed5452ce6cbd655c89bfa2cea


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:17:22 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 anxin41:9 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66260f02_dianx57_34364-33100
X-Cdn-Src-Port: 58844
Content-Length: 1958

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"order":{"refundable":"0.0","if_receive_wseat":"Y","trace_id":"C1_Y_7188073378091538445","tmps":[{"tmp_train_time":"","tmp_train_date":""}],"passengers":[{"ticket_type":"成人票","passenger_id_type":"1","passenger_name":"何映峰","ticket_type_code":"1","passenger_id_no":"3207***********815","passenger_id_name":"中国居民身份证","pre_passenger_price":"680","preference_word":"7.5折"},{"ticket_type":"成人票","passenger_id_type":"1","passenger_name":"何飞","ticket_type_code":"1","passenger_id_no":"3207***********819","passenger_id_name":"中国居民身份证","pre_passenger_price":"680","preference_word":"7.5折"}],"rate":"50","status_name":"待支付","status_code":"01","total_page":"1000","sequence_no":"","home_or_aboard":"0","reserve_no":"EH9071000300612700","prepay_amount":"136.0","realize_limit_time_diff":"360","needs":[{"board_train_code":"G3124","preference_rate":"","preference_word":"","to_station_name":"常州","realize_tmp_train":"N","local_arrive_time":"202405010711","status_name":"待支付","batch_no":"1","batch_ticket_price":"136.0","train_no":"55000G312420","local_start_time":"202405010606","status_code":"01","ticket_num":"","to_tele_code":"CZH","train_date_str":"2024年5月1日","from_station_name":"上海","seat_type":"O","show_fail_info":"N","start_train_date":"20240501","arrive_time":"07:11","from_tele_code":"SHH","batch_status":"0","train_date":"2024-05-01","arrive_date":"2024-05-01","start_tele_code":"SHH","start_time":"06:06","seat_types":"","seat_name":"二等座","end_tele_code":"WHN"}],"num_in_page":"10","reserve_time":"2024-04-22","relation_no":"","realize_limit_time":"2024-05-01 00:06:00","refund_trade_no":"","current_page":"0","accept_tmp_train":"N","ticket_price":"0.0","refund_diff_flag":"N"},"beginTime":1713770244340,"wait_pay_time":"30","loseTime":1713770840870},"messages":[],"validateMessages":{}}
```



# 请支付预付款，支付剩余时间：xxx

完成候补预付款支付后，候补购票订单立即生效，兑现时，按照候补订单生效的时间顺序

需对接支付功能，快速支付





# 点击支付

```
POST https://kyfw.12306.cn/otn/afterNatePay/paycheck HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 0
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/view/lineUp_payConfirm.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=3F77EA14DB7F29B203A31D40D80D3F00; tk=6tYZ9fnEpYCwdXtI9Mxa9yucj9kez5gCdqh1h0; route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=484966666.64545.0000; BIGipServerpassport=937951498.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toDate=2024-04-22; _jc_save_wfdc_flag=dc; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_showIns=true; _jc_save_fromDate=2024-05-01; uKey=65e5866022571a0caa856d57b931f850d801db18f911649315d1c02e4c2a5e1a


```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:25:01 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
ct: C1_232_28_9
X-Via: 1.1 dianx57:7 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 662610cd_dianx57_34368-37111
X-Cdn-Src-Port: 58653
Content-Length: 2500

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"flag":true,"payForm":{"payOrderId":"1EH907100030061270004152501Y22","interfaceName":"PAY_SERVLET","interfaceVersion":"PAY_SERVLET","tranData":"PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iR0JLIj8+CjxQYXlSZXE+PGludGVyZmFjZU5h\r\nbWU+UEFZX1NFUlZMRVQ8L2ludGVyZmFjZU5hbWU+PGludGVyZmFjZVZlcnNpb24+MS4wPC9pbnRl\r\ncmZhY2VWZXJzaW9uPjxvcmRlckRhdGU+MjAyNDA0MjIxNTI1MDE8L29yZGVyRGF0ZT48b3JkZXJU\r\naW1lb3V0RGF0ZT4yMDI0MDQyMjE1MjcyMDwvb3JkZXJUaW1lb3V0RGF0ZT48b3JkZXJJZD4xRUg5\r\nMDcxMDAwMzAwNjEyNzAwMDQxNTI1MDFZMjI8L29yZGVySWQ+PGFtb3VudD4xMzYwMDwvYW1vdW50\r\nPjxhcHBJZD4wMDAxPC9hcHBJZD48Y3VyVHlwZT4xNTY8L2N1clR5cGU+PG9yZGVyUmVtYXJrLz48\r\nbWVyVVJMPmh0dHBzOi8va3lmdy4xMjMwNi5jbi9vdG4vYWZ0ZXJOYXRlTm90aWZ5L2VwYXlTdGF0\r\ndXM/dXNlcl9uYW1lPWFIbG1jM2xoJmFtcDtzZXF1ZW5jZV9ubz1SVWc1TURjeE1EQXdNekF3TmpF\r\neU56QXcmYW1wO3BheU9yZGVySUQ9TVVWSU9UQTNNVEF3TURNd01EWXhNamN3TURBME1UVXlOVEF4\r\nV1RJeSZhbXA7cmVkaXJlY3RVUkw9YUhSMGNITTZMeTlyZVdaM0xqRXlNekEyTG1OdUwyOTBiaTh2\r\nYjNKa1pYSXZaVTV2ZEdsbWVVRmpkR2x2Ymk1a2J6OXRaWFJvYjJROQ0KY1hWbGNubE5lVTl5WkdW\r\neVUzUmhkR2xqSm5ObGNYVmxibU5sWDI1dlBVVklPVEEzTVRBd01ETXdNRFl4TWpjd01BPT0mYW1w\r\nO3BheVN0YXJ0PU1RPT0mYW1wO2JhdGNoX25vPU1RPT0mYW1wO2xvZ2luX2lkPVYwVkNYMXBJUTA0\r\nPSZhbXA7dG91cl9mbGFnPVNFST0mYW1wO3JldHVybl90b3RhbD0mYW1wO3JldHVybl9jb3N0PSZh\r\nbXA7b2xkX3RpY2tldF9wcmljZT0mYW1wO3BheV9tb2RlPSZhbXA7Y2hhbm5lbD1SUT09JmFtcDtp\r\nZl9mbGFncz1UaU5PSXc9PTwvbWVyVVJMPjxhcHBVUkw+aHR0cHM6Ly8xMC4yLjI0MC4yMTI6NDQz\r\nL29wbi9hZnRlck5hdGVOb3RpZnkvZXBheU5vdGlmeTwvYXBwVVJMPjxpbm5lclVSTD5odHRwOi8v\r\nMTAuMi4yMDEuMTkzOjkwOTkvaG9yZGVyL3Byb2Nlc3NQYXlCYWNrPC9pbm5lclVSTD48bWVyVkFS\r\nPmFIbG1jM2xoT2xkRlFsOWFTRU5PT2tWSU9UQTNNVEF3TURNd01EWXhNamN3TURveE9qRTZNVGMz\r\nS2lvcUtqVXhOalk2T2pvNk9qbzYNCk9raENPa1U2TVRFNlRqcE9Pak5qVFVoS1VrWlJSamhCVjNS\r\nV2JtczBNSFUwVDBZdmFFVlRUekJrYlVSRU1VdE5OalZRTW1sSlFVOXoNCk1VcHNkWFpDWTAxc1pr\r\nNWlkSGRuUTJkVlpXTkxVM2c0UW5Sek9IRXpVVFlLUW1oQlpsZE5aWFZTYzJKVFFXMWtjR2hYTW5o\r\nb1NtOXcNCmRHdFFNVXc1WjBGREsybGpPazQ9PC9tZXJWQVI+PHRyYW5zVHlwZT4wMTwvdHJhbnNU\r\neXBlPjxwYXltZW50VHlwZT4xPC9wYXltZW50VHlwZT48UHJlQmFuaz4zMzAwMDAxMDwvUHJlQmFu\r\naz48L1BheVJlcT4=","merSignMsg":"FztWlRRU0KynQDHe+jG1pAhzmWJ+tqdiqvFlevDlvEbOEvYD4KQojmWUm8eVu8EAA+4EHXknwKd1\r\nOb9foJLiPpXQGF8qDWbI/Viyrd75E6aDsGaSrj0ho38/yWsv3m8PCKayOG70bhlLsGpRCf8N4Gqm\r\nNPUAZBWUr9f8OVRzBuw=","appId":"0001","transType":"01","epayurl":"https://epay.12306.cn/pay/payGateway","paymentType":"1"}},"messages":[],"validateMessages":{}}
```



```
POST https://epay.12306.cn/pay/payGateway HTTP/1.1
Host: epay.12306.cn
Connection: keep-alive
Content-Length: 2329
Cache-Control: max-age=0
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
Origin: https://kyfw.12306.cn
Content-Type: application/x-www-form-urlencoded
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: same-site
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Referer: https://kyfw.12306.cn/
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; JSESSIONID=JI8EmUjDyJ0ZRLD7e8da62DdKQlXhveHBjVozXX-UfyrHn8eT2V7!-577449555

_json_att=&interfaceName=PAY_SERVLET&interfaceVersion=PAY_SERVLET&tranData=PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iR0JLIj8%2BCjxQYXlSZXE%2BPGludGVyZmFjZU5h%0D%0AbWU%2BUEFZX1NFUlZMRVQ8L2ludGVyZmFjZU5hbWU%2BPGludGVyZmFjZVZlcnNpb24%2BMS4wPC9pbnRl%0D%0AcmZhY2VWZXJzaW9uPjxvcmRlckRhdGU%2BMjAyNDA0MjIxNTI1MDE8L29yZGVyRGF0ZT48b3JkZXJU%0D%0AaW1lb3V0RGF0ZT4yMDI0MDQyMjE1MjcyMDwvb3JkZXJUaW1lb3V0RGF0ZT48b3JkZXJJZD4xRUg5%0D%0AMDcxMDAwMzAwNjEyNzAwMDQxNTI1MDFZMjI8L29yZGVySWQ%2BPGFtb3VudD4xMzYwMDwvYW1vdW50%0D%0APjxhcHBJZD4wMDAxPC9hcHBJZD48Y3VyVHlwZT4xNTY8L2N1clR5cGU%2BPG9yZGVyUmVtYXJrLz48%0D%0AbWVyVVJMPmh0dHBzOi8va3lmdy4xMjMwNi5jbi9vdG4vYWZ0ZXJOYXRlTm90aWZ5L2VwYXlTdGF0%0D%0AdXM%2FdXNlcl9uYW1lPWFIbG1jM2xoJmFtcDtzZXF1ZW5jZV9ubz1SVWc1TURjeE1EQXdNekF3TmpF%0D%0AeU56QXcmYW1wO3BheU9yZGVySUQ9TVVWSU9UQTNNVEF3TURNd01EWXhNamN3TURBME1UVXlOVEF4%0D%0AV1RJeSZhbXA7cmVkaXJlY3RVUkw9YUhSMGNITTZMeTlyZVdaM0xqRXlNekEyTG1OdUwyOTBiaTh2%0D%0AYjNKa1pYSXZaVTV2ZEdsbWVVRmpkR2x2Ymk1a2J6OXRaWFJvYjJROQ0KY1hWbGNubE5lVTl5WkdW%0D%0AeVUzUmhkR2xqSm5ObGNYVmxibU5sWDI1dlBVVklPVEEzTVRBd01ETXdNRFl4TWpjd01BPT0mYW1w%0D%0AO3BheVN0YXJ0PU1RPT0mYW1wO2JhdGNoX25vPU1RPT0mYW1wO2xvZ2luX2lkPVYwVkNYMXBJUTA0%0D%0APSZhbXA7dG91cl9mbGFnPVNFST0mYW1wO3JldHVybl90b3RhbD0mYW1wO3JldHVybl9jb3N0PSZh%0D%0AbXA7b2xkX3RpY2tldF9wcmljZT0mYW1wO3BheV9tb2RlPSZhbXA7Y2hhbm5lbD1SUT09JmFtcDtp%0D%0AZl9mbGFncz1UaU5PSXc9PTwvbWVyVVJMPjxhcHBVUkw%2BaHR0cHM6Ly8xMC4yLjI0MC4yMTI6NDQz%0D%0AL29wbi9hZnRlck5hdGVOb3RpZnkvZXBheU5vdGlmeTwvYXBwVVJMPjxpbm5lclVSTD5odHRwOi8v%0D%0AMTAuMi4yMDEuMTkzOjkwOTkvaG9yZGVyL3Byb2Nlc3NQYXlCYWNrPC9pbm5lclVSTD48bWVyVkFS%0D%0APmFIbG1jM2xoT2xkRlFsOWFTRU5PT2tWSU9UQTNNVEF3TURNd01EWXhNamN3TURveE9qRTZNVGMz%0D%0AS2lvcUtqVXhOalk2T2pvNk9qbzYNCk9raENPa1U2TVRFNlRqcE9Pak5qVFVoS1VrWlJSamhCVjNS%0D%0AV2JtczBNSFUwVDBZdmFFVlRUekJrYlVSRU1VdE5OalZRTW1sSlFVOXoNCk1VcHNkWFpDWTAxc1pr%0D%0ANWlkSGRuUTJkVlpXTkxVM2c0UW5Sek9IRXpVVFlLUW1oQlpsZE5aWFZTYzJKVFFXMWtjR2hYTW5o%0D%0Ab1NtOXcNCmRHdFFNVXc1WjBGREsybGpPazQ9PC9tZXJWQVI%2BPHRyYW5zVHlwZT4wMTwvdHJhbnNU%0D%0AeXBlPjxwYXltZW50VHlwZT4xPC9wYXltZW50VHlwZT48UHJlQmFuaz4zMzAwMDAxMDwvUHJlQmFu%0D%0Aaz48L1BheVJlcT4%3D&merSignMsg=FztWlRRU0KynQDHe%2BjG1pAhzmWJ%2BtqdiqvFlevDlvEbOEvYD4KQojmWUm8eVu8EAA%2B4EHXknwKd1%0D%0AOb9foJLiPpXQGF8qDWbI%2FViyrd75E6aDsGaSrj0ho38%2FyWsv3m8PCKayOG70bhlLsGpRCf8N4Gqm%0D%0ANPUAZBWUr9f8OVRzBuw%3D&appId=0001&transType=01&paymentType=1
```

```
HTTP/1.1 200 OK
Date: Mon, 22 Apr 2024 07:25:01 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 9856
Connection: keep-alive
Server: asfep/1.11.2.4
Set-Cookie: JSESSIONID=t4wEsaRfHjLximQ6vIXIHdRWkPXF4DCfgDvSWevQmS3lRMB8jmZ8!521265381; path=/; HttpOnly
X-Via: 1.1 qinzhoudianxin71:11 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 662610cd_qinzhoudianxin72_45806-42116






<!DOCTYPE html 
```



# payGateway

选择：支付宝、微信。。。



。。。















# 扫码

点击扫码登录

```
POST https://kyfw.12306.cn/passport/web/create-qr64 HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 9
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/resources/login.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9

appid=otn
```

```
HTTP/1.1 200 OK
Date: Thu, 09 May 2024 10:07:25 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
Access-Control-Allow-Origin: https://kyfw.12306.cn
Access-Control-Allow-Credentials: true
ct: c1_57
Set-Cookie: BIGipServerpassport=870842634.50215.0000; path=/
X-Via: 1.1 PS-JDZ-01M3U44:6 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 663ca05d_PS-JDZ-01zlo49_11036-14869
X-Cdn-Src-Port: 61240
Content-Length: 2692

{"image":"iVBORw0KGgoAAAANSUhEUgAAAMcAAADHCAIAAAAiZ9CRAAAHMUlEQVR42u3dW3bcMAwE0dn/picrcCyhC6Aglz5tjx7klQ02eJLP18ODPj4OgUeXqk92XLrSzz9cO+F/znzlVm996sp5wpuv3WE4PlfupzAsqlKVqlS1UVU4nbVv1Sa4NkO1+bjyqQEftWtR4K6fR1WqUpWqVqsKSxNqIMKr35pOvLwLv3VL1a23jnrDVaUqValKVWEBEX7rFpTaoFM1U2i6dkL8EqpSlapUparwU2FqgBsKQ/bJuQ+nYGtdpSpVqUpVI6r62pa4mDDywJN0quQaSA3wpENVqlKVqvaqSnbV5OmtX3nOVxIJqvIrqvIri1RRR1g81X6YKgHxJT21jYmqMsPbuDHaqlKVqlS1R9XkQhdXhY9aWFxSb11fV7ivr6AqValKVetUhXueQkPUjquaGFwn3hjou1ZTZawqValKVRtVDWyi6msYhzFE2Aymuu9nN3Uhv1ZUpSpVqWqjqluFSFhFhduY+rYf4WIopviwNMUrqlKVqlT1DlUDpVKtpKCigfCVGGvZzqQG4cuvKlWpSlUvUIWP9WQzGF/bUwkFNQhHGvw3RkBVqlKVqhaqqrU28e1ZfZVE2N8daANM9uObOhaqUpWqVPUyVdTN4SvwUB6eXA+8P3jfOqyrbu9ZUJWqVKWqx6iiLozvQ8I3G1FL6LA+C2MR/BVlx0dVqlKVqhapqvUUw3s6Umn1VX6UDyrHp97Mws2rSlWqUtVqVWGJQyXgYX3Wd4T7mWo++i7BvuqqUpWqVLVIVd8iltrlQ623jyzXw+5yeFAl1/XxUZWqVKWq1apqg4VHz30FDZ6JU73kI58KOx+qUpWqVPUCVQOZL6UKz7LxsowqLsOSNLxWQZ6qVKUqVS1SdXYlH5oOs36qvPu0HXgoQ+0GuNqxUZWqVKWqDarwJT0VK4eBdd9LQoXstcY8FeizrXpVqUpVqtqoCu9W4n/Ua5uN8FuttRzCZIG6Vt/vBVWpSlWqepMqvAHZZzEsDsJqA28VUFVdX7xyO1tXlapUpaqdqqgQOZwhqnzBV9dhfIBfK2zDJ9OkKlWpSlWLVIU+jpQdfd3u2tUHEExm67XQQVWqUpWq1qmqnYv6O43/vQ/LF3x3V1gd9l2raSuYqlSlKlVtVNU3DfheJSqhwF8kPHToK1KbTqgqValKVYtUUTk1lUeE8TQVqff1DMK6KrwW9YCqUpWqVPUmVVQBQW0AQh6yI3rG27pHroUXhapSlapUtU7VQG6OVwBUbRHWeeF5wjeKuhabqqhKVapS1SJVYbGCb4fCKxs89H+sj3AKkP6EqlSlKlUtUoVH2EdatnhtQYX1i/KI2m1c3bOgKlWpSlXPUxXebgiFSuQn92CF4QWVvFC8wpJLVapSlar2qgp7wOGaHC+5BsqOcPKekICXJ/d2ta4qValKVQ9WVVtm41UL9cNUD5i6BPWKUgVWGHD8fiFVqUpVqtqjqq80oVq2eEJR68JONg+oSrRvBq92l1WlKlWp6sGqwuLgSIRN1R9UoIBD6QvZw24EUK2rSlWqUtVjVPUl4N0P0FEUTvYDJmP389W6qlSlKlU9TxW+2RcP2cMtQfjI4tuqzn7qWMdGVapSlap2qprcooT3SvGuOTsx387/WKu7X6IqValKVatVDWyZ6jszFcTjIXv4FHhhFFart+sqValKVapapYoqTagGNr5gxnNq6lN4shBGOWRdpSpVqUpVz1NF7c4J576vhYyfmWIRnie8KPtxValKVapapIrakNT3B7uWdFAL79AZ3gPuu9Xwl4iqVKUqVb1JFfX3frLA6pshKpjAQ/ZaxNA0mKpSlapUtUhV2BntS5xD7lSojd9G2AOmOhZ4cakqValKVS9Q1bdSDVFSe6eojCCMKqgBp8a59s5fratUpSpVqep5qsL1f20JHe4NorJ+JE3+dv7TElQRhnffr3aXVaUqVanqwaoGtuBQa2A8KT77XFRMQyUm4aulKlWpSlXrVNVakuE4htlx2E+lQn9KFf5KUA2GwoukKlWpSlUbVYUTjG/GwucDLzIGyruw3BzYIVfP1lWlKlWp6jGqwrOHj31kaKjiKUwfwjKRGnnkTVCVqlSlqkWqqKcNV/IDbVS831x7HKofj9fBSI9cVapSlaoWqaqNfthhDfu7tZICH3QqH6Hu+QldDVWpSlWq2qiqL8Lu6xwP7OUKEeDFU1+GgjSnVaUqValqkSqqkgjbqLVRm8wRwpwaT1XCb+H5u6pUpSpV7VVFbe4Js4awMJqs6vDlevgmHGlp/1JXqUpVqlLVKlVhu3Hg2fAsuy9b7wu+w2dvajOrSlWqUtVfVjVZ0FC1BfXDeMv2iOmk4FOVqlSlqr+sajIXplrRAwUNNRpU7kM1y1WlKlWp6k2q8BGpVRLhx7uH7zv7b9tR1+qeAlWpSlWq2qiqO3UFn58SQzW5j/jAXxtkBlWlKlWpapEqDw/wUJUHf/wDtitt+GBxZVYAAAAASUVORK5CYII=","result_message":"生成二维码成功","result_code":"0","uuid":"nPiXAU3Qks67_Am8Q0BUB76FFBxVZ_DZHoqTxm34NhQXw8TT42fr7GR3O68uQKEgmPLXIE_J5Zc0y01"}
```



```
POST https://kyfw.12306.cn/passport/web/checkqr HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 94
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/resources/login.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: BIGipServerpassport=870842634.50215.0000

uuid=nPiXAU3Qks67_Am8Q0BUB76FFBxVZ_DZHoqTxm34NhQXw8TT42fr7GR3O68uQKEgmPLXIE_J5Zc0y01&appid=otn
```

```
HTTP/1.1 200 OK
Date: Thu, 09 May 2024 10:07:26 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
Access-Control-Allow-Origin: https://kyfw.12306.cn
Access-Control-Allow-Credentials: true
ct: c1_48
X-Via: 1.1 zhendianxin40:3 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 663ca05e_PS-JDZ-01zlo49_11036-14957
X-Cdn-Src-Port: 61240
Content-Length: 66

{"result_message":"二维码状态查询成功","result_code":"0"}
```



扫描后

```
POST https://kyfw.12306.cn/passport/web/checkqr HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 94
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/resources/login.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: BIGipServerpassport=870842634.50215.0000

uuid=nPiXAU3Qks67_Am8Q0BUB76FFBxVZ_DZHoqTxm34NhQXw8TT42fr7GR3O68uQKEgmPLXIE_J5Zc0y01&appid=otn
```

```
HTTP/1.1 200 OK
Date: Thu, 09 May 2024 10:07:37 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
Access-Control-Allow-Origin: https://kyfw.12306.cn
Access-Control-Allow-Credentials: true
ct: c1_53
X-Via: 1.1 zhendianxin40:3 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 663ca069_PS-JDZ-01zlo49_11036-15964
X-Cdn-Src-Port: 61240
Content-Length: 66

{"result_message":"二维码状态查询成功","result_code":"1"}
```



手机点击确认登录

```
POST https://kyfw.12306.cn/passport/web/checkqr HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 94
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: */*
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/resources/login.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: BIGipServerpassport=870842634.50215.0000

uuid=nPiXAU3Qks67_Am8Q0BUB76FFBxVZ_DZHoqTxm34NhQXw8TT42fr7GR3O68uQKEgmPLXIE_J5Zc0y01&appid=otn
```

```
HTTP/1.1 200 OK
Date: Thu, 09 May 2024 10:07:39 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
Access-Control-Allow-Origin: https://kyfw.12306.cn
Access-Control-Allow-Credentials: true
ct: c1_45
Set-Cookie: uamtk=nwprYCNxmjkLXYnLrLBH6JfKfTZL-zIIfsh1h0; Path=/passport
X-Via: 1.1 zhendianxin40:3 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 663ca06b_PS-JDZ-01zlo49_11036-16138
X-Cdn-Src-Port: 61240
Content-Length: 106

{"result_message":"扫码登录成功","uamtk":"nwprYCNxmjkLXYnLrLBH6JfKfTZL-zIIfsh1h0","result_code":"2"}
```





# 查询乘客

```
POST https://kyfw.12306.cn/otn/passengers/query HTTP/1.1
Host: kyfw.12306.cn
Connection: keep-alive
Content-Length: 23
Pragma: no-cache
Cache-Control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
Accept: application/json, text/javascript, */*; q=0.01
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
X-Requested-With: XMLHttpRequest
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Origin: https://kyfw.12306.cn
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: https://kyfw.12306.cn/otn/view/passengers.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cookie: JSESSIONID=E62BCBB75EEA52CCF7C793F2B01870D0; tk=wq3nsh0UonZTrUHcqD6ktzIYHlrPBQsCubh1h0; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; route=9036359bb8a8a461c164a04f8f50b252; BIGipServerotn=1859715338.50210.0000; BIGipServerpassport=854065418.50215.0000; _jc_save_fromStation=%u4E0A%u6D77%2CSHH; _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_fromDate=2024-05-27; _jc_save_wfdc_flag=dc; _jc_save_toDate=2024-05-15; uKey=213c2968c0b29504f2e6ae31a9b2dcc14deaedc34e4fef2aa51ab903aa01fc09

pageIndex=1&pageSize=10
```

```
HTTP/1.1 200 OK
Date: Wed, 15 May 2024 02:30:04 GMT
Content-Type: application/json;charset=UTF-8
Connection: keep-alive
ct: C1_217_110_8
X-Via: 1.1 dianx57:8 (Cdn Cache Server V2.0)
X-Ws-Request-Id: 66441e2c_dianx57_4763-29892
X-Cdn-Src-Port: 17927
Content-Length: 4643

{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"datas":[{"passenger_name":"何飞","sex_code":"M","sex_name":"男","born_date":"1973-04-17 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********819","passenger_type":"1","passenger_type_name":"成人","mobile_no":"138****6360","phone_no":"","email":"","address":"","postalcode":"","first_letter":"HF","recordCount":"5","isUserSelf":"N","total_times":"99","delete_time":"1973/05/17","allEncStr":"b0916d0034fc9370c055f47e821b6df68b7c39e3e1a898cf2b6fc05157c22fbb1fc3921d30f5cfd7898255b69a859ef072694f9b735758b33d1845bb0797b64b812b20e9abcb7b8b2f8243ad2b4b3615","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"Y","is_active":"N","last_time":"20180722094038","mobile_code":"86","gat_born_date":"19730417","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""},{"passenger_name":"何文杰","sex_code":"M","sex_name":"男","born_date":"2001-02-16 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********836","passenger_type":"1","passenger_type_name":"成人","mobile_no":"13205202167","phone_no":"","email":"","address":"","postalcode":"","first_letter":"HWJ","recordCount":"5","isUserSelf":"N","total_times":"99","delete_time":"2001/03/18","allEncStr":"75570daa7c351f12e31a36aa79a953d9f77510051ee72b62ca31253e660065a23312847a1d28924887204629597c69d76800c2b40d4e9afc49fa431d85f8b171496de82f6248e5ed5c3dbb7bc2d78a78","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"N","is_active":"N","last_time":"20190613210622","mobile_code":"86","gat_born_date":"20190613","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""},{"passenger_name":"何映峰","sex_code":"M","sex_name":"男","born_date":"2000-01-15 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********815","passenger_type":"1","passenger_type_name":"成人","mobile_no":"177****5166","phone_no":"","email":"1577975140@qq.com","address":"","postalcode":"","first_letter":"HYF","recordCount":"5","isUserSelf":"Y","total_times":"99","delete_time":"2000/02/14","allEncStr":"2dcb3f1fd997c1ac3c1384d5e9425af93acd655663abe115d07f0afb26ac8ef550cac39d64fca2c8541c1e7265c9d0af8f41855fba4a0dd7f0218ce69d046a8c4a76746eb2a05700c60e204a01a89ea8443bbd37894cc28911a60e93cf6ba535","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"Y","is_active":"N","last_time":"20180707070419","mobile_code":"86","gat_born_date":"20000115","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""},{"passenger_name":"何瑶瑶","sex_code":"F","sex_name":"女","born_date":"1995-11-16 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********823","passenger_type":"1","passenger_type_name":"成人","mobile_no":"136****9578","phone_no":"","email":"","address":"","postalcode":"","first_letter":"HYY","recordCount":"5","isUserSelf":"N","total_times":"99","delete_time":"1995/12/16","allEncStr":"9ec5f02fd840798f870e02884c57409db760d204117762423f1e939ea7caf01f1c4f810fef4c0952238414e1ea06e4effebf27b9d578c33a79a94f7cf2f72f61496de82f6248e5ed5c3dbb7bc2d78a78","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"Y","is_active":"N","last_time":"20230923154415","mobile_code":"86","gat_born_date":"19951116","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""},{"passenger_name":"孙长凤","sex_code":"F","sex_name":"女","born_date":"1970-03-18 00:00:00","country_code":"CN","passenger_id_type_code":"1","passenger_id_type_name":"中国居民身份证","passenger_id_no":"3207***********845","passenger_type":"1","passenger_type_name":"成人","mobile_no":"177****5166","phone_no":"","email":"","address":"","postalcode":"","first_letter":"SCF","recordCount":"5","isUserSelf":"N","total_times":"99","delete_time":"1970/04/17","allEncStr":"eb4b68a50bfec61f3562e4852c363fe188750ceaf6c022a5f79f99660695b6d80bc157b191a077fdd51a924505dce7280509d02e168813345453131bc61eba4d496de82f6248e5ed5c3dbb7bc2d78a78","isAdult":"Y","isYongThan10":"N","isYongThan14":"N","isOldThan60":"N","if_receive":"Y","is_active":"N","last_time":"20210919192735","mobile_code":"86","gat_born_date":"19700318","gat_valid_date_start":"","gat_valid_date_end":"","gat_version":""}],"flag":true,"pageTotal":1},"messages":[],"validateMessages":{}}
```

















# TODO































