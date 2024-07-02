package ru.mpei.rest.clients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ru.mpei.utils.HttpUtils;
import java.io.IOException;


@Slf4j
public class UnitClient {

    private final Gson json = new GsonBuilder().setPrettyPrinting().create();
    private final HttpClient httpClient = HttpClientBuilder.create().build();

    static String unitAddress = "http://127.0.0.1:9876/api/v2/mccommand";

    public String sendRequest(String command) {
        try {
            String requestJsonData = createJsonStructure(command);
            HttpPost postRequest = HttpUtils.createHttpPost(unitAddress, requestJsonData);
            HttpResponse response = httpClient.execute(postRequest);

            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            log.error("Failed to get response from API: {}", e.toString());

            return null;
        }
    }


    private String createJsonStructure(String input) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("command", input);

        String jsonStructure = json.toJson(jsonObject);
        return jsonStructure;
    }

    protected String getAnswerFromJson(String content) {
//        JSONObject json;
//        String command = "";
//        try {
//            json = new JSONObject(content);
//            if (json.get("errorCode").toString().equals("0")) {
//                command = json.get("" + "").toString();
//                log.info("answerToMC: " + command);
//            } else {
//                log.error("errorCode: {}", json.get("errorCode").toString());
//            }
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
        return content;
    }

}
