package ru.mpei.model.mc;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class McResponseResultData {
    private int id;
    private String address;
    private String siesId;
    private String command; // base64
}

