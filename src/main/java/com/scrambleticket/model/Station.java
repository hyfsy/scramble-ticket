
package com.scrambleticket.model;

import lombok.Data;

@Data
public class Station {
    private String at_name_pinyin_only_prefix;
    private String name;
    private String code;
    private String name_pinyin;
    private String name_pinyin_only_prefix;
    private String sequenceNumber;
    private String country_code;
    private String country;
}
