package ru.mpei.model.mc;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SiesMcResponseResultData {
    private int id;
    private String address;
    private String siesId;
    private String command; // base64
}

