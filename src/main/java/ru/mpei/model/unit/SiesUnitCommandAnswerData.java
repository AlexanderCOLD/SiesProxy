package ru.mpei.model.unit;

import lombok.Getter;
import lombok.ToString;

/**
 * Ответ от Unit
 */
@Getter
@ToString
public class SiesUnitCommandAnswerData {
    private int errorCode;
    private String answerToMC;
}
