package ru.mpei.rest.clients;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ru.mpei.model.mc.SiesMcGetCommandData;
import ru.mpei.model.mc.SiesMcResponseCommandData;
import ru.mpei.model.mc.SiesMcUploadResponseData;
import ru.mpei.utils.HttpUtils;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Slf4j
public class SiesMcClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    { objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); }

    private final HttpClient httpClient = HttpClientBuilder.create().build();

    private String mcRequestAddress = null;
    private String mcResponseAddress = null;
    private String filterSiesId = null;


    public void initialize() {
        if (mcRequestAddress == null || mcResponseAddress == null) throw new RuntimeException("Address is not set");
        if (filterSiesId == null) throw new RuntimeException("SiesID filter is not set");
    }

    public SiesMcResponseCommandData getCommands() {
        try {
            var requestJsonData = createJsonRequest();
            var postRequest = HttpUtils.createHttpPost(mcRequestAddress, requestJsonData);
            var response = httpClient.execute(postRequest);

            return objectMapper.readValue(EntityUtils.toString(response.getEntity()), SiesMcResponseCommandData.class);
        } catch (Exception e) {
            log.error("Failed to get response from API by send request: {}", e.toString());
            return null;
        }
    }

    public String sendResponse(List<SiesMcUploadResponseData> responseData) {
        try {
            var responseJsonData = objectMapper.writeValueAsString(responseData);
            var postRequest = HttpUtils.createHttpPost(mcResponseAddress, responseJsonData);
            var response = httpClient.execute(postRequest);

            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            log.error("Failed to get response from API by send response: {}", e.toString());

            return null;
        }
    }

    /** Создать Json для запроса */
    private String createJsonRequest() throws JsonProcessingException {
        var mcRequestData = new SiesMcGetCommandData();
        mcRequestData.setMaxCommandCount(10);
        mcRequestData.getFilterSiesId().add(filterSiesId);

        return objectMapper.writeValueAsString(mcRequestData);
    }

    public static SiesMcClient instance() { return new SiesMcClient(); }

    /** Задать адрес MC */
    public SiesMcClient setAddress(String address) {
        this.mcRequestAddress = address + "api/v2/proxy/get-commands";
        this.mcResponseAddress = address + "api/v2/proxy/upload-response";

        return this;
    }

    /** Задать фильтр по SIES ID */
    public SiesMcClient setFilterSiesId(String filterSiesId) {
        this.filterSiesId = Hex.encodeHexString(filterSiesId.getBytes(StandardCharsets.UTF_8));

        return this;
    }
}
