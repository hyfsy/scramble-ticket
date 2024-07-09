
package com.scrambleticket.service;

import com.scrambleticket.entity.Config;
import com.scrambleticket.entity.Passenger;

import java.util.ArrayList;
import java.util.List;

public class DefaultPassengerService implements PassengerService {

    ConfigService configService;

    public DefaultPassengerService(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public List<Passenger> list() {
        return new ArrayList<>(configService.getConfig().getPassengers().values());
    }

    @Override
    public Passenger get(Long id) {
        return configService.getConfig().getPassengers().get(id);
    }

    @Override
    public int update(Passenger passenger) {
        Config config = configService.getConfig();
        config.getPassengers().put(passenger.getId(), passenger);
        return configService.updateConfig(config) ? 1 : 0;
    }

    @Override
    public int delete(Long id) {
        Config config = configService.getConfig();
        config.getPassengers().remove(id);
        return configService.updateConfig(config) ? 1 : 0;
    }
}
