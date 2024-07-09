
package com.scrambleticket.entity;

import lombok.Data;

@Data
public class Passenger {
    private Long id;
    private String name;
    private String passenger_id_no;
    private String key; // name_allEncStr
}
