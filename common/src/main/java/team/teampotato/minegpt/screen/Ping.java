package team.teampotato.minegpt.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import team.teampotato.minegpt.config.Config;

import java.io.IOException;
import java.net.InetAddress;

@Environment(EnvType.CLIENT)
public class Ping {
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
