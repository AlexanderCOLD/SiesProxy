package ru.mpei.model.mc;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Результат запроса команд от MC
 */
@Getter
@ToString
public class SiesMcResponseCommandData {
    private int resultCode;
    private final List<SiesMcResponseResultData> result = new ArrayList<>();
}
