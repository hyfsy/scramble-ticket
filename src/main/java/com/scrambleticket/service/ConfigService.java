
package com.scrambleticket.service;

import com.scrambleticket.entity.Config;

public interface ConfigService {

    Config getConfig();

    boolean updateConfig(Config config);

}
