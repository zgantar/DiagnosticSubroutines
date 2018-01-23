package org.gantar.openhab.diagnostics.AmbientLight;

import com.tinkerforge.*;
import org.gantar.openhab.diagnostics.DiagnosticInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by zgantar on 4. 05. 2017.
 */
public class AmbientLightSubroutine implements DiagnosticInterface {

    @Override
    public void diagnose(HashMap<String, HashMap<String, Device>> deviceTree, ResourceBundle resources, IPConnection ipcon) {
        HashMap<String, Device> devices = deviceTree.get(BrickletAmbientLight.DEVICE_DISPLAY_NAME);
        HashMap<String, Device> parentDevices = deviceTree.get(BrickMaster.DEVICE_DISPLAY_NAME);

        for (Map.Entry<String, Device> entry : devices.entrySet()) {
            BrickletAmbientLight device = (BrickletAmbientLight) entry.getValue();
            try {
                if (device.getIlluminance() < 0 || device.getIlluminance() > 10000) {
                    String parent = device.getIdentity().connectedUid;
                    BrickMaster parentDevice = (BrickMaster) parentDevices.get(parent);
                    parentDevice.reset();
                }
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }

        }
    }

    public AmbientLightSubroutine() {
    }
}
