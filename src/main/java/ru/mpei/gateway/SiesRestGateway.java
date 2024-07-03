package ru.mpei.gateway;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.model.mc.SiesMcUploadResponseData;
import ru.mpei.rest.clients.SiesMcClient;
import ru.mpei.rest.clients.SiesUnitClient;
import java.util.ArrayList;
import java.util.List;


/**
 * Шлюз обмена данными между MC и Units по Rest
 */
@Slf4j
public class SiesRestGateway {

    private final long loopTimeMs = 1000;

    private Thread processThread;
    private boolean running = true;

    private final SiesMcClient mcClient;

    @Getter
    private final List<SiesUnitClient> unitClients = new ArrayList<>();



    public SiesRestGateway(@NonNull SiesMcClient mcClient) {
        this.mcClient = mcClient;
    }

    public void start() {
        if (processThread != null) return;
        if (unitClients.isEmpty()) throw new RuntimeException("SiesUnitClient is empty");

        mcClient.initialize();
        unitClients.forEach(SiesUnitClient::initialize);

        running = true;
        processThread = new Thread(() -> {

            try {
                while (running) {
                    Thread.sleep(loopTimeMs);

                    for (var unitClient : unitClients) {
                        var mcResponse = mcClient.getCommands(unitClient.getSiesId());

                        if (mcResponse.isPresent()) {
                            var mcResp = mcResponse.get();
                            log.debug("MC response {}: {}", mcClient.getMcRequestAddress(), mcResp);

                            final var siesResponseList = new ArrayList<SiesMcUploadResponseData>();

                            for (var siesMcResponseResultData : mcResp.getResult()) {
                                var answerData = unitClient.sendCommand(siesMcResponseResultData.getCommand());

                                if (answerData.isPresent()) {
                                    var answer = answerData.get().getAnswerToMC();
                                    if (answer != null) {
                                        log.debug("Answer from Unit {}: {}", unitClient.getUnitAddress(), answer);

                                        var siesResponse = new SiesMcUploadResponseData();
                                        siesResponse.setSiesId(unitClient.getSiesId());
                                        siesResponse.setResponse(answer);

                                        siesResponseList.add(siesResponse);
                                    }
                                }
                            }

                            if (!siesResponseList.isEmpty()) mcClient.sendResponse(siesResponseList);
                        }
                    }
                }
            } catch (Exception e) { throw new RuntimeException(e); }
        });
        processThread.setDaemon(false);
        processThread.start();
    }

    public void stop() {
        running = false;
        processThread = null;
    }
}
