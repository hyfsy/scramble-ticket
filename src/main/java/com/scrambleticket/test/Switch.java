
package com.scrambleticket.test;

public class Switch {

    public static final boolean login_by_password = false; // true qr false password

    public static final boolean commit_order_real = true;

    public static final int log_level = 1; // 0 debug 1 info

    public static final boolean console_enabled = true;

    public static final boolean proxy_enabled = false;
    public static final String proxy_host = "localhost";
    public static final int proxy_port = 8888;

    public static final boolean log_cookie_string = true;

    public static final boolean log_renew_cookie_string = true;

    public static final boolean log_channel_lease = false;

    public static final boolean log_limit_acquire_time = false;

    public static final boolean log_url_statistics = false;

    public static final int max_retry_time = 3;

    // 总流程六个接口，压缩主要针对流程的最后三个接口，因为前三个接口有限流约束，慢点无所谓，而后三个接口数据很少，也不是很需要压缩
    public static final boolean compress_enabled = false;

}
