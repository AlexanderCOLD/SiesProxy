package ru.mpei.rest.clients;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ru.mpei.model.mc.McGetCommandData;
import ru.mpei.model.mc.McResponseCommandData;
import ru.mpei.model.mc.McUploadResponseData;
import ru.mpei.utils.HttpUtils;
import java.util.List;


@Slf4j
public class McClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    { objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); }

    private final HttpClient httpClient = HttpClientBuilder.create().build();

    private String address = null;
    private String filterSiesId = null;

    public McResponseCommandData sendRequest() {
        try {
            if (address == null) throw new Exception("Address is not set");
            String mcRequestAddress = address + "api/v2/proxy/get-commands";

            String requestJsonData = createJsonRequest();
            HttpPost postRequest = HttpUtils.createHttpPost(mcRequestAddress, requestJsonData);
            HttpResponse response = httpClient.execute(postRequest);

            return objectMapper.readValue(EntityUtils.toString(response.getEntity()), McResponseCommandData.class);
        } catch (Exception e) {
            log.error("Failed to get response from API by send request: {}", e.toString());
            return null;
        }
    }

    public String sendResponse(List<McUploadResponseData> responseData) {
        try {
            if (address == null) throw new Exception("Address is not set");
            String mcResponseAddress = address + "api/v2/proxy/upload-response";

            String responseJsonData = objectMapper.writeValueAsString(responseData);
            HttpPost postRequest = HttpUtils.createHttpPost(mcResponseAddress, responseJsonData);
            HttpResponse response = httpClient.execute(postRequest);

            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            log.error("Failed to get response from API by send response: {}", e.toString());

            return null;
        }
    }

    private String createJsonRequest() throws JsonProcessingException {
        McGetCommandData mcRequestData = new McGetCommandData();
        mcRequestData.setMaxCommandCount(10);

        if (filterSiesId != null) {
//            mcRequestData.getFilterSiesId().add(filterSiesId);
        }

        return objectMapper.writeValueAsString(mcRequestData);
    }

    public McClient setAddress(String address) {
        this.address = address;

        return this;
    }

    public McClient setFilterSiesId(String filterSiesId) {
        this.filterSiesId = filterSiesId;

        return this;
    }
}
