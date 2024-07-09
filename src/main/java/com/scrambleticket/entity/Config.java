
package com.scrambleticket.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Config {

    private Integer version;
    private Long lastModified;
    private Map<Long, Task> tasks = new HashMap<>();
    private Map<Long, Passenger> passengers = new HashMap<>();

}
