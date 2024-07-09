
package com.scrambleticket.handler.context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.util.StringUtil;

import lombok.Data;

@Data
public class TrainScrambleContext {

    ScrambleContext scrambleContext;

    String trainCode;
    String initDC_html;
    String REPEAT_SUBMIT_TOKEN;
    JSONObject ticketInfoForPassengerForm;
    Integer init_limit_ticket_num = 9;

    public static TrainScrambleContext get(FlowContext context, FlowHandler.FlowHandlerChain chain) {
        return context.getAttribute(chain.toString(), TrainScrambleContext.class);
    }

    static Pattern globalRepeatSubmitTokenPattern = Pattern.compile("var globalRepeatSubmitToken = '(.*?)';");
    static Pattern ticketInfoForPassengerFormPattern = Pattern.compile("var ticketInfoForPassengerForm=(.*?);");

    public void setInitDC_html(String initDC_html) {
        this.initDC_html = initDC_html;
        init_REPEAT_SUBMIT_TOKEN();
        init_limit_ticket_num();
        init_ticketInfoForPassengerForm();
    }

    // TODO 参数好像并不需要
    private void init_REPEAT_SUBMIT_TOKEN() {
        if (StringUtil.isBlank(initDC_html)) {
            return;
        }
        if (REPEAT_SUBMIT_TOKEN != null) {
            return;
        }
        Matcher matcher = globalRepeatSubmitTokenPattern.matcher(initDC_html);
        if (!matcher.find()) {
            throw new ScrambleTicketException("未知错误，找不到globalRepeatSubmitToken：" + initDC_html);
        }
        this.REPEAT_SUBMIT_TOKEN = matcher.group(1);
    }

    private void init_ticketInfoForPassengerForm() {
        if (StringUtil.isBlank(initDC_html)) {
            return;
        }
        if (ticketInfoForPassengerForm != null) {
            return;
        }

        Matcher matcher = ticketInfoForPassengerFormPattern.matcher(initDC_html);
        if (!matcher.find()) {
            throw new ScrambleTicketException("未知错误，找不到ticketInfoForPassengerForm：" + initDC_html);
        }
        String ticketInfoForPassengerFormString = matcher.group(1);
        ticketInfoForPassengerFormString = ticketInfoForPassengerFormString.replace("'", "\"");
        JSONObject ticketInfoForPassengerForm = JSON.parseObject(ticketInfoForPassengerFormString);

        String ticket_submit_order_request_flag_isAsync = "1";
        if (!ticket_submit_order_request_flag_isAsync.equals(ticketInfoForPassengerForm.get("isAsync")) || ""
            .equals(ticketInfoForPassengerForm.getJSONObject("queryLeftTicketRequestDTO").getString("ypInfoDetail"))) {
            throw new UnsupportedOperationException(
                "不支持非异步功能，ticketInfoForPassengerForm：" + ticketInfoForPassengerFormString);
        }
        this.ticketInfoForPassengerForm = ticketInfoForPassengerForm;
    }

    private void init_limit_ticket_num() {
        if (StringUtil.isBlank(initDC_html)) {
            return;
        }
        if (init_limit_ticket_num != null) {
            return;
        }
        Pattern init_limit_ticket_numPattern = Pattern.compile("var init_limit_ticket_num='(.*?)';");
        Matcher matcher = init_limit_ticket_numPattern.matcher(initDC_html);
        if (!matcher.find()) {
            // TODO
            // <div class="tit error" id="ERROR">系统忙，请稍后重试</div>
            throw new ScrambleTicketException("未知错误，找不到init_limit_ticket_num：" + initDC_html);
        }
        // 默认9 是否可以提升性能，此处不解析
        this.init_limit_ticket_num = Integer.parseInt(matcher.group(1));
    }

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
