package com.telecomProject.CDR.model;

import java.time.Instant;

public record CDR(String firstName, String lastName, String caller, String receiver, int callDuration,
                  String callType, String callDirection, Instant callDate, String ip,
                  String uuid) {
}
