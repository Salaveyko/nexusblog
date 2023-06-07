package com.nexusblog.events.event;

import com.nexusblog.persistence.entity.Profile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnChangeEmailEvent extends ApplicationEvent {

    private Profile profile;
    private String email;

    public OnChangeEmailEvent(Profile profile, String email) {
        super(profile);

        this.profile = profile;
        this.email = email;
    }
}
