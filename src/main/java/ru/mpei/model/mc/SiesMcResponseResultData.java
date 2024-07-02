package ru.mpei.model.mc;

import lombok.Getter;
import lombok.ToString;

/**
 * Описание запрошенной команды от MC
 */
@Getter
@ToString
public class SiesMcResponseResultData {
    private int id;
    private String address;
    private String siesId;
    private String command; // base64
}

