
package com.scrambleticket.service;

import com.scrambleticket.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultUserSessionService implements UserSessionService {

    // TODO 心跳
    Map<Integer, UserSession> userSessionMap = new ConcurrentHashMap<>();

    @Override
    public void putUserSession(int sessionId, UserSession userSession) {
        userSessionMap.put(sessionId, userSession);
    }

    @Override
    public UserSession getUserSession(int sessionId) {
        return userSessionMap.get(sessionId);
    }

    @Override
    public UserSession getUserSessionByLoginUserName(String name) {
        for (UserSession userSession : userSessionMap.values()) {
            if (name != null && name.equals(userSession.getLoginUser().getRealUserName())) {
                return userSession;
            }
        }
        return null;
    }

    @Override
    public UserSession getRandomUserSession() {
        return userSessionMap.isEmpty() ? null : userSessionMap.values().iterator().next();
    }

    @Override
    public void heartbeat(String name) {
        Logger.info("heartbeat");
    }
}
