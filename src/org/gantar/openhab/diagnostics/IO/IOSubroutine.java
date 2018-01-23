package org.gantar.openhab.diagnostics.IO;

import com.tinkerforge.*;
import org.gantar.openhab.diagnostics.DiagnosticInterface;
import org.gantar.openhab.diagnostics.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by zgantar on 4. 05. 2017.
 */
public class IOSubroutine implements DiagnosticInterface {

    @Override
    public void diagnose(HashMap<String, HashMap<String, Device>> deviceTree, ResourceBundle resources, IPConnection ipcon) {
        HashMap<String, Device> devices = deviceTree.get(BrickletIO4.DEVICE_DISPLAY_NAME);

        try {
            for (Map.Entry<String, Device> entry : devices.entrySet()) {
                BrickletIO4 device = (BrickletIO4) entry.getValue();
                Device.Identity identity = device.getIdentity();

                BrickletIO4.Configuration conf = device.getConfiguration();
                if (conf.directionMask != Integer.parseInt(resources.getString("IO4_" + identity.uid))) {
                    //first the input side
                    System.out.println("Neki nau uredu pr IO4 - " + identity.uid + "! Resetiram master - " + identity.connectedUid + "!");
                    String settingsInput = resources.getString("Conf_" + "IO4_" + identity.uid + "_i");
                    if (settingsInput != null && !settingsInput.equals("")) {
                        String[] elements = settingsInput.split("\\_");
                        device.setConfiguration(Short.parseShort(elements[0]), elements[1].toCharArray()[0], Boolean.parseBoolean(elements[2]));
                    }
                    //then the output side
                    String settingsOutput = resources.getString("Conf_" + "IO4_" + identity.uid + "_o");
                    if (settingsOutput != null && !settingsOutput.equals("")) {
                        String[] elements = settingsOutput.split("\\_");
                        device.setConfiguration(Short.parseShort(elements[0]), elements[1].toCharArray()[0], Boolean.parseBoolean(elements[2]));
                    }
//                    device.setValue((short) Integer.parseInt(resources.getString("IO4_" + identity.uid)));
//                    Util.reset(ipcon, identity);
                }
            }
            devices = deviceTree.get(BrickletIO16.DEVICE_DISPLAY_NAME);

            for (Map.Entry<String, Device> entry : devices.entrySet()) {
                BrickletIO16 device = (BrickletIO16) entry.getValue();
                Device.Identity identity = device.getIdentity();

                BrickletIO16.PortConfiguration conf = device.getPortConfiguration('A');
                if (conf.directionMask != Integer.parseInt(resources.getString("IO16_" + identity.uid))) {
                    System.out.println("Neki nau uredu pr IO16 - " + identity.uid + "! Resetiram master - " + identity.connectedUid + "!");
                    //first the input side
                    //port a
                    String settingsInput = resources.getString("Conf_" + "IO16_" + identity.uid + "_i_a");
                    if (settingsInput != null && !settingsInput.equals("")) {
                        String[] elements = settingsInput.split("\\_");
                        device.setPortConfiguration('a', Short.parseShort(elements[0]), elements[1].toCharArray()[0], Boolean.parseBoolean(elements[2]));
                    }
                    //port b
                    settingsInput = resources.getString("Conf_" + "IO16_" + identity.uid + "_i_b");
                    if (settingsInput != null && !settingsInput.equals("")) {
                        String[] elements = settingsInput.split("\\_");
                        device.setPortConfiguration('b', Short.parseShort(elements[0]), elements[1].toCharArray()[0], Boolean.parseBoolean(elements[2]));
                    }
                    //then the output
                    //port a
                    String settingsOutput = resources.getString("Conf_" + "IO16_" + identity.uid + "_o_a");
                    if (settingsOutput != null && !settingsOutput.equals("")) {
                        String[] elements = settingsOutput.split("\\_");
                        device.setPortConfiguration('a', Short.parseShort(elements[0]), elements[1].toCharArray()[0], Boolean.parseBoolean(elements[2]));
                    }
                    //port b
                    settingsOutput = resources.getString("Conf_" + "IO16_" + identity.uid + "_o_b");
                    if (settingsOutput != null && !settingsOutput.equals("")) {
                        String[] elements = settingsOutput.split("\\_");
                        device.setPortConfiguration('b', Short.parseShort(elements[0]), elements[1].toCharArray()[0], Boolean.parseBoolean(elements[2]));
                    }

//                    Util.reset(ipcon, identity);
                }
            }
        } catch (TimeoutException | NotConnectedException e) {
            System.out.println("Napaka pri diagnozi: " + BrickletIO4.DEVICE_DISPLAY_NAME + " ali " + BrickletIO16.DEVICE_DISPLAY_NAME + "!");
            e.printStackTrace();
        } catch (MissingResourceException e) {
            System.out.println("Napaka pri iskanju nastavitev: " + BrickletIO4.DEVICE_DISPLAY_NAME + " ali " + BrickletIO16.DEVICE_DISPLAY_NAME + "!");
            e.printStackTrace();
        }
    }

    public IOSubroutine() {
    }
}
