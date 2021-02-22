package com.sproutt.eussyaeussyaapi.domain.activity;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CompleteActivityEvent extends ApplicationEvent {

    private final Activity activity;

    public CompleteActivityEvent(Activity activity) {
        super(activity);
        this.activity = activity;
    }
}
