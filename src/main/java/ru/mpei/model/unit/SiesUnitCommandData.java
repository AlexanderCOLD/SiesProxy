package ru.mpei.model.unit;

import lombok.Data;
import lombok.NonNull;

/**
 * Отправка команды от MC в Unit
 */
@Data
public class SiesUnitCommandData {
    @NonNull
    private final String command; // base64
}
