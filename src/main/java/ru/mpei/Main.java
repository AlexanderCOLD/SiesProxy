package ru.mpei;

import ru.mpei.rest.clients.McClient;
import ru.mpei.rest.clients.UnitClient;

public class Main {

    static McClient mcClient = new McClient()
            .setAddress("http://127.0.0.1:8000/") // http://192.168.5.10:8000/
            .setFilterSiesId("79d5e046882f");

    static UnitClient unitClient = new UnitClient();

    public static void main(String[] args) {

        var mcResponse = mcClient.sendRequest();
        System.out.println("MC response: " + mcResponse);
//        System.out.println("MC command: " + mcCommand);

//        String unitResponse = unitClient.sendRequest(mcCommand);
//        String mcResponse2 = mcClient.sendResponse(siesAddr, siesId, unitResponse);
    }
}
