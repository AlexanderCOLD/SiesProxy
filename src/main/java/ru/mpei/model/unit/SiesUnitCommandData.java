package ru.mpei.model.unit;

import lombok.Data;
import lombok.NonNull;

@Data
public class SiesUnitCommandData {
    @NonNull
    private final String command; // base64
}
