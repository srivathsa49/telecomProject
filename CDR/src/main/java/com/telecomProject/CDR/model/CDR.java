package com.telecomProject.CDR.model;

import java.time.Instant;

public record CDR(String firstName, String lastName, Long caller, Long receiver, int callDuration,
                  String callType, String callDirection, Instant callDate, String ip
) {
}
