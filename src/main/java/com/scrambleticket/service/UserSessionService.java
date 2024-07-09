
package com.scrambleticket.service;

public interface UserSessionService {

    void putUserSession(int sessionId, UserSession userSession);

    UserSession getUserSession(int sessionId);

    UserSession getUserSessionByLoginUserName(String name);

    UserSession getRandomUserSession();

    void heartbeat(String id);

}
