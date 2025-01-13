
package com.scrambleticket.handler.pay;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.Logger;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import com.scrambleticket.util.UrlUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class PayGatewayHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        JSONObject payForm = context.getAttribute(PayCheckHandler.PAY_FORM_KEY, JSONObject.class);

        String payOrderId = payForm.getString("payOrderId");
        Logger.info("订单编号: " + payOrderId);

        String epayurl = payForm.getString("epayurl"); // https://epay.12306.cn/pay/payGateway

        String interfaceName = payForm.getString("interfaceName");
        String interfaceVersion = payForm.getString("interfaceVersion");
        String tranData = payForm.getString("tranData");
        String merSignMsg = payForm.getString("merSignMsg");
        String appId = payForm.getString("appId");
        String transType = payForm.getString("transType");
        String paymentType = payForm.getString("paymentType");


        ByteBuf body = ByteBufUtil.create("_json_att=&interfaceName=" + interfaceName + "&interfaceVersion=" + interfaceVersion
                + "&tranData=" + UrlUtil.encode(tranData) + "&merSignMsg=" + UrlUtil.encode(merSignMsg) + "&appId=" + appId + "&transType=" + transType + "&paymentType=" + paymentType);

        FullHttpRequest request = HttpUtils.createPostRequest(epayurl, body);
        HttpUtils.setFormUrlEncodedContentType(request);

        // h()

        // TODO client切换
        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                // html

                String html = "";
                // <form action="/pay/webBusiness" method="post" name="myform">
                // <input type="hidden" value="xxx" name="tranData" />
                // <input type="hidden" value="01" name="transType" />
                // 	<input type="hidden" value="1" name="channelId" />
                // 	<input type="hidden" value="0001" name="appId" />
                // 	<input type="hidden" value="bFJB/WDAvgPh5JV1DYI4da2kJ0iDqJl2X5qHqbgkRWFx8gq3rKsX/CKxXdy2rRwiobRiYm8aRLMd
                // /hW4gDMh6Hsf5bAZ+ouRFD4+hMLpbsqTvfKMeL3Qj4NIgUFlch1xB8M8Ukc/marSOPu/6j2GvglB
                // WXuXUGOFu01aeQeKCf0=" name="merSignMsg" />
                // 	<input type="hidden" value="112.65.27.114" name="merCustomIp" />
                // 	<input type="hidden" value="20250107165725" name="orderTimeoutDate" />
                // 	<input type="hidden" value="1" name="paymentType" />
                // 	<input type="hidden" name="bankId" />
                // 	<input type="hidden" name="businessType" />

                // tranData=PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iR0JLIj8%2BCjxQYXlSZXE%2BPGludGVyZmFjZU5h%0D%0AbWU%2BUEFZX1NFUlZMRVQ8L2ludGVyZmFjZU5hbWU%2BPGludGVyZmFjZVZlcnNpb24%2BMS4wPC9pbnRl%0D%0AcmZhY2VWZXJzaW9uPjxvcmRlckRhdGU%2BMjAyNTAxMDcxNjUyNTk8L29yZGVyRGF0ZT48b3JkZXJU%0D%0AaW1lb3V0RGF0ZT4yMDI1MDEwNzE2NTcyNTwvb3JkZXJUaW1lb3V0RGF0ZT48b3JkZXJJZD4xRUgw%0D%0ANjA4MDg5NTA0MDA3MDYwMDExNjUyNTlZMDc8L29yZGVySWQ%2BPGFtb3VudD4xMDAwMDwvYW1vdW50%0D%0APjxhcHBJZD4wMDAxPC9hcHBJZD48Y3VyVHlwZT4xNTY8L2N1clR5cGU%2BPG9yZGVyUmVtYXJrLz48%0D%0AbWVyVVJMPmh0dHBzOi8va3lmdy4xMjMwNi5jbi9vdG4vYWZ0ZXJOYXRlTm90aWZ5L2VwYXlTdGF0%0D%0AdXM%2FdXNlcl9uYW1lPWFIbG1jM2xoJmFtcDtzZXF1ZW5jZV9ubz1SVWd3TmpBNE1EZzVOVEEwTURB%0D%0AM01EWXcmYW1wO3BheU9yZGVySUQ9TVVWSU1EWXdPREE0T1RVd05EQXdOekEyTURBeE1UWTFNalU1%0D%0AV1RBMyZhbXA7cmVkaXJlY3RVUkw9YUhSMGNITTZMeTlyZVdaM0xqRXlNekEyTG1OdUwyOTBiaTh2%0D%0AYjNKa1pYSXZaVTV2ZEdsbWVVRmpkR2x2Ymk1a2J6OXRaWFJvYjJROQ0KY1hWbGNubE5lVTl5WkdW%0D%0AeVUzUmhkR2xqSm5ObGNYVmxibU5sWDI1dlBVVklNRFl3T0RBNE9UVXdOREF3TnpBMk1BPT0mYW1w%0D%0AO3BheVN0YXJ0PU1RPT0mYW1wO2JhdGNoX25vPU1RPT0mYW1wO2xvZ2luX2lkPVYwVkNYMXBJUTA0%0D%0APSZhbXA7dG91cl9mbGFnPVNFST0mYW1wO3JldHVybl90b3RhbD0mYW1wO3JldHVybl9jb3N0PSZh%0D%0AbXA7b2xkX3RpY2tldF9wcmljZT0mYW1wO3BheV9tb2RlPSZhbXA7Y2hhbm5lbD1SUT09JmFtcDtp%0D%0AZl9mbGFncz1UaU5PSXc9PTwvbWVyVVJMPjxhcHBVUkw%2BaHR0cHM6Ly8xMC4yLjI0MC4yMTI6NDQz%0D%0AL29wbi9hZnRlck5hdGVOb3RpZnkvZXBheU5vdGlmeTwvYXBwVVJMPjxpbm5lclVSTD5odHRwOi8v%0D%0AMTAuMi4yMDEuMTkzOjkwOTkvaG9yZGVyL3Byb2Nlc3NQYXlCYWNrPC9pbm5lclVSTD48bWVyVkFS%0D%0APmFIbG1jM2xoT2xkRlFsOWFTRU5PT2tWSU1EWXdPREE0T1RVd05EQXdOekEyTURveE9qRTZNVGMz%0D%0AS2lvcUtqVXhOalk2T2pvNk9qbzYNCk9raENPa1U2TVRwT09rNDZNMk5OU0VwU1JsRkdPRUZYZEZa%0D%0AdWF6UXdkVFJQUmk5b1JWTlBNR1J0UkVReFMwMDJOVkF5YVVsQlQzTXgNClNteDFka0pqVFd4bVRt%0D%0ASjBkMmREWjFWbFkwdFRlRGhDZEhNNGNUTlJOZ3BDYUVGbVYwMWxkVkp6WWxOQmJXUndhRmN5ZUdo%0D%0AS2IzQjANCmExQXhURGxuUVVNcmFXTTZUZz09PC9tZXJWQVI%2BPHRyYW5zVHlwZT4wMTwvdHJhbnNU%0D%0AeXBlPjxwYXltZW50VHlwZT4xPC9wYXltZW50VHlwZT48UHJlQmFuaz4zMzAwMDAxMDwvUHJlQmFu%0D%0Aaz48L1BheVJlcT4%3D
                // &transType=01
                // &channelId=1
                // &appId=0001
                // &merSignMsg=bFJB%2FWDAvgPh5JV1DYI4da2kJ0iDqJl2X5qHqbgkRWFx8gq3rKsX%2FCKxXdy2rRwiobRiYm8aRLMd%0D%0A%2FhW4gDMh6Hsf5bAZ%2BouRFD4%2BhMLpbsqTvfKMeL3Qj4NIgUFlch1xB8M8Ukc%2FmarSOPu%2F6j2GvglB%0D%0AWXuXUGOFu01aeQeKCf0%3D
                // &merCustomIp=112.65.27.114
                // &orderTimeoutDate=20250107165725
                // &paymentType=1
                // &bankId=33000010
                // &businessType=1


                String bankId = "33000010"; // zfb
                String bankId2 = "33000020"; // wx
                String bizType = "1";

                //



                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("payGateway", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }

}
