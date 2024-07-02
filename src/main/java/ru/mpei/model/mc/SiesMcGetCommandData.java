package ru.mpei.model.mc;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Запрос команд от MC
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SiesMcGetCommandData {
    private Integer maxCommandCount;
    private Integer lastCommandId;

    private final List<String> filterSiesId = new ArrayList<>();
    private final List<String> filterAddress = new ArrayList<>();
    private final List<String> filterOrganization = new ArrayList<>();
    private final List<Integer> filterCommandId = new ArrayList<>();
}
