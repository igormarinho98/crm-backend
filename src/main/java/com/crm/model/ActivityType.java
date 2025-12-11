package com.crm.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de activity: CALL, TASK ou EMAIL")
public enum ActivityType {
    CALL,
    TASK,
    EMAIL
}
