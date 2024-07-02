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
import ru.mpei.model.mc.McGetCommandData;
import ru.mpei.model.mc.McResponseCommandData;
import ru.mpei.model.mc.McUploadResponseData;
import ru.mpei.utils.HttpUtils;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Slf4j
public class McClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    { objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); }

    private final HttpClient httpClient = HttpClientBuilder.create().build();

    private String mcRequestAddress = null;
    private String mcResponseAddress = null;
    private String filterSiesId = null;

    public McResponseCommandData sendRequest() {
        try {
            if (mcRequestAddress == null) throw new Exception("Address is not set");

            var requestJsonData = createJsonRequest();
            var postRequest = HttpUtils.createHttpPost(mcRequestAddress, requestJsonData);
            var response = httpClient.execute(postRequest);

            return objectMapper.readValue(EntityUtils.toString(response.getEntity()), McResponseCommandData.class);
        } catch (Exception e) {
            log.error("Failed to get response from API by send request: {}", e.toString());
            return null;
        }
    }

    public String sendResponse(List<McUploadResponseData> responseData) {
        try {
            if (mcResponseAddress == null) throw new Exception("Address is not set");

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
        var mcRequestData = new McGetCommandData();
        mcRequestData.setMaxCommandCount(10);

        if (filterSiesId != null) mcRequestData.getFilterSiesId().add(filterSiesId);

        return objectMapper.writeValueAsString(mcRequestData);
    }

    /** Задать адрес MC */
    public McClient setAddress(String address) {
        this.mcRequestAddress = address + "api/v2/proxy/get-commands";
        this.mcResponseAddress = address + "api/v2/proxy/upload-response";

        return this;
    }

    /** Задать фильтр по SIES ID */
    public McClient setFilterSiesId(String filterSiesId) {
        this.filterSiesId = Hex.encodeHexString(filterSiesId.getBytes(StandardCharsets.UTF_8));

        return this;
    }
}
