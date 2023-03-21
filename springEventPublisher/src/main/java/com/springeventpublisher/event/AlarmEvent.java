package com.springeventpublisher.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AlarmEvent {
    private String usrId;
    private String msg;
}
