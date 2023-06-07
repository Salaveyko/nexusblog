package com.nexusblog.events.event;

import com.nexusblog.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private UserDto user;
    public OnRegistrationCompleteEvent(UserDto user, String appUrl) {
        super(user);

        this.user = user;
        this.appUrl = appUrl;
    }
}
