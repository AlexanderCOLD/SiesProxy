package ru.mpei;

import lombok.extern.slf4j.Slf4j;
import ru.mpei.gateway.SiesRestGateway;
import ru.mpei.rest.clients.SiesMcClient;
import ru.mpei.rest.clients.SiesUnitClient;


@Slf4j
public class Main {

    public static void main(String[] args) {

        var siesMcClient = SiesMcClient.instance()
                .setAddress("http://127.0.0.1:8000/"); // http://192.168.5.10:8000/

        var siesUnitClient = SiesUnitClient.instance()
                .setAddress("http://127.0.0.1:9876/")
                .setSiesId("79d5e046882f");

        var gateway = new SiesRestGateway(siesMcClient);
        gateway.getUnitClients().add(siesUnitClient);
        gateway.start();
    }
}
