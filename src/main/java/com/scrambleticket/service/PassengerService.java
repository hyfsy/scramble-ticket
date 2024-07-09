
package com.scrambleticket.service;

import java.util.List;

import com.scrambleticket.entity.Passenger;

public interface PassengerService {
    List<Passenger> list();

    Passenger get(Long id);

    int update(Passenger passenger);

    int delete(Long id);
}
