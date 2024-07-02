package ru.mpei.rest.clients;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ru.mpei.model.unit.SiesUnitCommandAnswerData;
import ru.mpei.model.unit.SiesUnitCommandData;
import ru.mpei.utils.HttpUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


@Slf4j
public class SiesUnitClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    { objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); }

    private final HttpClient httpClient = HttpClientBuilder.create().build();

    private String unitAddress = null;

    @Getter
    private String siesId = null;


    public void initialize() {
        if (unitAddress == null) throw new RuntimeException("Address is not set");
    }

    public Optional<SiesUnitCommandAnswerData> sendCommand(String command) {
        try {
            var requestJsonData = objectMapper.writeValueAsString(new SiesUnitCommandData(command));
            var postRequest = HttpUtils.createHttpPost(unitAddress, requestJsonData);
            var response = httpClient.execute(postRequest);

            return Optional.of(objectMapper.readValue(EntityUtils.toString(response.getEntity()), SiesUnitCommandAnswerData.class));
        } catch (IOException e) {
            log.error("Failed to get response from API: {}", e.toString());
            return Optional.empty();
        }
    }

    public static SiesUnitClient instance() { return new SiesUnitClient(); }

    /** Задать адрес MC */
    public SiesUnitClient setAddress(String address) {
        unitAddress = address + "api/v2/mccommand";

        return this;
    }

    /** Задать фильтр по SIES ID */
    public SiesUnitClient setSiesId(String filterSiesId) {
        this.siesId = Hex.encodeHexString(filterSiesId.getBytes(StandardCharsets.UTF_8));

        return this;
    }
}
