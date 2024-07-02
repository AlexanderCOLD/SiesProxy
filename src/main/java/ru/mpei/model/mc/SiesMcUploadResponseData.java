package ru.mpei.model.mc;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SiesMcUploadResponseData {
    private String siesId;
    private String address;
    private String response;
}
