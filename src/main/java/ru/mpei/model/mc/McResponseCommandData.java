package ru.mpei.model.mc;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class McResponseCommandData {
    private int resultCode;
    private final List<McResponseResultData> result = new ArrayList<>();
}
