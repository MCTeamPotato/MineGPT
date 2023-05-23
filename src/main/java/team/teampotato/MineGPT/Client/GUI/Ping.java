package team.teampotato.MineGPT.Client.GUI;


import net.fabricmc.api.ClientModInitializer;
import team.teampotato.MineGPT.Common.Config;

import java.io.IOException;
import java.net.InetAddress;

public class Ping implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ipDetection(Config.Ping);
    }
    public static boolean status = false;
    public Boolean ipDetection(Integer timeout) {
        try {
            status = InetAddress.getByName(Config.ENDPOINT).isReachable(timeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

}