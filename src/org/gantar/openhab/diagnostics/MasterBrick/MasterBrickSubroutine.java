package org.gantar.openhab.diagnostics.MasterBrick;

import com.tinkerforge.*;
import org.gantar.openhab.diagnostics.DiagnosticInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by zgantar on 4. 05. 2017.
 */
public class MasterBrickSubroutine implements DiagnosticInterface {

    @Override
    public void diagnose(HashMap<String, HashMap<String, Device>> deviceTree, ResourceBundle resources, IPConnection ipcon) {
        HashMap<String, Device> devices = deviceTree.get(BrickMaster.DEVICE_DISPLAY_NAME);

        for (Map.Entry<String, Device> entry : devices.entrySet()) {
            BrickMaster device = (BrickMaster) entry.getValue();
            try {
                if (device.isStatusLEDEnabled()) {
                    device.disableStatusLED();
                }
                if (!device.isRS485Present()) {
                    System.out.println("Pri testiranju " + BrickMaster.DEVICE_DISPLAY_NAME + " " + device.getIdentity().uid + ", nisem zaznal RS485 razširitve!");
                    device.reset();
                }
            } catch (TimeoutException | NotConnectedException e) {
                System.out.println("Napaka pri diagnozi: " + BrickMaster.DEVICE_DISPLAY_NAME + "!");
                e.printStackTrace();
            }
        }
    }

    public MasterBrickSubroutine() {
    }
}
