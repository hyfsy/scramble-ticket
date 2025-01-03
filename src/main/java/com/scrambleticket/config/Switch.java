
package com.scrambleticket.config;

public class Switch {

    public static final boolean login_by_password = SystemConfig.getBool("login_by_password");

    public static final boolean commit_order_real = SystemConfig.getBool("commit_order_real");

    public static final int log_level = SystemConfig.getInt("log_level");

    public static final boolean console_enabled = SystemConfig.getBool("console_enabled");

    public static final boolean proxy_enabled = SystemConfig.getBool("proxy_enabled");
    public static final String proxy_host = SystemConfig.getStr("proxy_host");
    public static final int proxy_port = SystemConfig.getInt("proxy_port");

    public static final boolean log_cookie_string = SystemConfig.getBool("log_cookie_string");

    public static final boolean log_renew_cookie_string = SystemConfig.getBool("log_renew_cookie_string");

    public static final boolean log_channel_lease = SystemConfig.getBool("log_channel_lease");

    public static final boolean log_limit_acquire_time = SystemConfig.getBool("log_limit_acquire_time");

    public static final boolean log_url_statistics = SystemConfig.getBool("log_url_statistics");

    public static final boolean log_req_resp = SystemConfig.getBool("log_req_resp");

    public static final int max_retry_time = SystemConfig.getInt("max_retry_time");

    // 总流程六个接口，压缩主要针对流程的最后三个接口，因为前三个接口有限流约束，慢点无所谓，而后三个接口数据很少，也不是很需要压缩
    public static final boolean compress_enabled = SystemConfig.getBool("compress_enabled");

    public static final boolean candidate_enabled = SystemConfig.getBool("candidate_enabled");
    public static final boolean candidate_commit_order_real = SystemConfig.getBool("candidate_commit_order_real");

    public static final boolean pay_flow_enabled = SystemConfig.getBool("pay_flow_enabled");
}
