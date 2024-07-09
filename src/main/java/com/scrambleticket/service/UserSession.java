
package com.scrambleticket.service;

import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.model.LoginUser;
import lombok.Data;

@Data
public class UserSession {
    LoginUser loginUser;
    FlowContext context;
}
