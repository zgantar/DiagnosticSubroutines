package org.gantar.tinkerforge.Humidity;

import com.tinkerforge.*;
import org.gantar.tinkerforge.DiagnosticInterface;
import org.gantar.tinkerforge.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by zgantar on 4. 05. 2017.
 */
public class HumiditySubroutine implements DiagnosticInterface {

    @Override
    public void diagnose(HashMap<String, HashMap<String, Device>> deviceTree, ResourceBundle resources, IPConnection ipcon) {
        HashMap<String, Device> devices = deviceTree.get(BrickletHumidity.DEVICE_DISPLAY_NAME);
        String[] humBricks = resources.getString("BrickletHumidity").split("_");

        HashMap<String, Boolean> deviceVisits = new HashMap<>();
        for (String uid : humBricks) {
            deviceVisits.put(uid, false);
        }

        try {
//            System.out.println("Preverjam število hum senzorjev, ki so odgovoril");
            int i = humBricks.length;
            for (Device device : devices.values()) {
                for (String humUID : humBricks) {
                    if (device.getIdentity().uid.equals(humUID)) {
//                        System.out.println("Našel hum " + device.getIdentity().uid);
                        deviceVisits.put(humUID, true);
                        i--;
                        break;
                    }
                }
            }

            if (i != 0) {
                System.out.println("!!!!!!Število zapisov v nastavitvah ne ustreza številu hum senzorjev!!!!");
                for (Map.Entry entry : deviceVisits.entrySet()) {
                    if (!(boolean)entry.getValue()) {
                        if (!Util.resetTinkerforge(resources, "/")) {
                            return;
                        }
                    }
                }
                //tukaj potrebno dodat reset elektrike!!!!!
                return;
            }

            for (Map.Entry<String, Device> entry : devices.entrySet()) {
                BrickletHumidity device = (BrickletHumidity) entry.getValue();

//                System.out.println("Preverjam poročano vlažnost");
                if (device.getHumidity() <= 100 || device.getHumidity() > 1000) {
                    String parent = device.getIdentity().connectedUid;
                    System.out.println("Poročana vlažnost je izven meja normale, resetiram Master brick - " + parent);
                    if (parent.equals(resources.getString("BrickletHumidity_" + device.getIdentity().uid))) {
                        System.out.println("Poročan in nastavljen UID očeta sta enaka, resetiram!!!!!!!!!!!!!!!!");
                        if (!Util.resetTinkerforge(resources, device.getIdentity().uid)) {
                            return;
                        }
                    } else {
                        System.out.println("Poročan in nastavljen UID očeta NISTA enaka, PREVERI!!!!!!!");
                    }
                }
            }

        } catch (TimeoutException | NotConnectedException e) {
                e.printStackTrace();
        }
    }

    public HumiditySubroutine() {
    }
}
