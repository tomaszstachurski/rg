package com.example.rg.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.yaml.snakeyaml.util.EnumUtils;

public enum Status {
    CREATED, DONE;

    @JsonCreator
    public static Status forValue(String name) { return EnumUtils.findEnumInsensitiveCase(Status.class, name);
    }
}
