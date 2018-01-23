package org.gantar.openhab.diagnostics;

import com.tinkerforge.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Main {

    public static void main(String[] args) {
        ResourceBundle resources;
        IPConnection ipcon;

        try {
            resources = ResourceBundle.getBundle("org.gantar.openhab.diagnostics.elements");

            ipcon = new IPConnection(); // Create IP connection
            ipcon.connect(resources.getString("HOST"), new Integer(resources.getString("PORT")));

            HashMap<String, HashMap<String, Device>> devices = Util.getDevices(ipcon);

            Util.startDiagnose(devices, resources, ipcon);
        } catch (AlreadyConnectedException e) {
            System.out.println("Napaka pri začenjanju diagnoze");
            e.printStackTrace();
        } catch (NetworkException e) {
            System.out.println("Napaka pri začenjanju diagnoze");
            e.printStackTrace();
        }

    }
}
