package ru.mpei;

import lombok.extern.slf4j.Slf4j;
import ru.mpei.model.mc.SiesMcUploadResponseData;
import ru.mpei.rest.clients.SiesMcClient;
import ru.mpei.rest.clients.SiesUnitClient;

import java.util.List;

@Slf4j
public class Main {

    static SiesMcClient siesMcClient = SiesMcClient.instance()
            .setAddress("http://127.0.0.1:8000/") // http://192.168.5.10:8000/
            .setFilterSiesId("79d5e046882f");

    static SiesUnitClient siesUnitClient = SiesUnitClient.instance()
            .setAddress("http://127.0.0.1:9876/");

    public static void main(String[] args) {
        siesMcClient.initialize();
        siesUnitClient.initialize();

        var mcResponse = siesMcClient.getCommands();
        log.info("MC response: {}", mcResponse);



        var unitResponse = siesUnitClient.sendCommand(mcResponse.getResult().get(0).getCommand());
        log.info("Unit response: {}", unitResponse);

        if (unitResponse.getErrorCode() == 0 && unitResponse.getAnswerToMC() != null) {
            log.info("Code is OK and answer exists");

            var test = siesMcClient.sendResponse(
                    List.of(
                            new SiesMcUploadResponseData(mcResponse.getResult().get(0).getSiesId(), null, unitResponse.getAnswerToMC())
                    )
            );

            System.out.println(test);
        }
    }
}
