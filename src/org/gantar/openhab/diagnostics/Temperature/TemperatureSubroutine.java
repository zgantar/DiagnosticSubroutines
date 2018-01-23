package org.gantar.openhab.diagnostics.Temperature;

import com.tinkerforge.*;
import org.gantar.openhab.diagnostics.DiagnosticInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by zgantar on 4. 05. 2017.
 */
public class TemperatureSubroutine implements DiagnosticInterface {

    public void diagnose(HashMap<String, HashMap<String, Device>> deviceTree, ResourceBundle resources, IPConnection ipcon) {
        HashMap<String, Device> devices = deviceTree.get(BrickletTemperature.DEVICE_DISPLAY_NAME);
        HashMap<String, Device> parentDevices = deviceTree.get(BrickMaster.DEVICE_DISPLAY_NAME);

        try {
            for (Map.Entry<String, Device> entry : devices.entrySet()) {
                BrickletTemperature device = (BrickletTemperature) entry.getValue();
                if (device.getI2CMode() != BrickletTemperature.I2C_MODE_SLOW) {
                    device.setI2CMode(BrickletTemperature.I2C_MODE_SLOW);
                }
                if (device.getTemperature() <= 1000 || device.getTemperature() >= 3500) {
                    String parent = device.getIdentity().connectedUid;
                    BrickMaster parentDevice = (BrickMaster) parentDevices.get(parent);
                    parentDevice.reset();
                }
            }
        } catch (TimeoutException | NotConnectedException e) {
            System.out.println("Napaka pri preverjanju senzorja temperature!");
            e.printStackTrace();
        }

    }

    public TemperatureSubroutine() {
    }
}
