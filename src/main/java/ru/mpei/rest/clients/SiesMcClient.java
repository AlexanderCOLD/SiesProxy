package ru.mpei.rest.clients;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ru.mpei.model.mc.SiesMcGetCommandData;
import ru.mpei.model.mc.SiesMcResponseCommandData;
import ru.mpei.model.mc.SiesMcUploadResponseData;
import ru.mpei.utils.HttpUtils;
import java.util.List;
import java.util.Optional;


/**
 * Rest Client MC
 */
@Slf4j
public class SiesMcClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    { objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); }

    private final HttpClient httpClient = HttpClientBuilder.create().build();

    @Getter
    private String mcRequestAddress = null;
    private String mcResponseAddress = null;


    public void initialize() {
        if (mcRequestAddress == null || mcResponseAddress == null) throw new RuntimeException("Address is not set");
    }


    /**
     * Запрос списка команд от МС
     * @param siesId - фильтр юнита
     * @return - список команд для юнита
     */
    public Optional<SiesMcResponseCommandData> getCommands(String siesId) {
        try {
            var requestJsonData = createJsonRequest(siesId);
            var postRequest = HttpUtils.createHttpPost(mcRequestAddress, requestJsonData);
            var response = httpClient.execute(postRequest);

            return Optional.of(objectMapper.readValue(EntityUtils.toString(response.getEntity()), SiesMcResponseCommandData.class));
        } catch (Exception e) {
            log.error("Failed to get response from API by send request: {}", e.toString());
            return Optional.empty();
        }
    }

    /**
     * Отправка ответов от юнитов
     * @param responseData - адрес и ответ
     * @return - результат запроса
     */
    public Optional<String> sendResponse(List<SiesMcUploadResponseData> responseData) {
        try {
            var responseJsonData = objectMapper.writeValueAsString(responseData);
            var postRequest = HttpUtils.createHttpPost(mcResponseAddress, responseJsonData);
            var response = httpClient.execute(postRequest);

            return Optional.of(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            log.error("Failed to get response from API by send response: {}", e.toString());
            return Optional.empty();
        }
    }

    /**
     * Создать Json для запроса
     * @param siesId - фильтр юнита
     * @return - json содержимое запроса
     */
    @SneakyThrows
    private String createJsonRequest(String siesId) {
        var mcRequestData = new SiesMcGetCommandData();
        mcRequestData.setMaxCommandCount(10);
        mcRequestData.getFilterSiesId().add(siesId);

        return objectMapper.writeValueAsString(mcRequestData);
    }

    public static SiesMcClient instance() { return new SiesMcClient(); }

    /**
     * Задать адрес MC
     * @param address - сетевой адрес в формате<a href=" http://127.0.0.1:8000">...</a>/
     */
    public SiesMcClient setAddress(String address) {
        this.mcRequestAddress = address + "api/v2/proxy/get-commands";
        this.mcResponseAddress = address + "api/v2/proxy/upload-response";

        return this;
    }
}
