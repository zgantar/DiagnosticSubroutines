package org.gantar.openhab.diagnostics;

import com.tinkerforge.*;
import org.gantar.openhab.diagnostics.AmbientLight.AmbientLightSubroutine;
import org.gantar.openhab.diagnostics.Humidity.HumiditySubroutine;
import org.gantar.openhab.diagnostics.IO.IOSubroutine;
import org.gantar.openhab.diagnostics.MasterBrick.MasterBrickSubroutine;
import org.gantar.openhab.diagnostics.Temperature.TemperatureSubroutine;
//import org.reflections.Reflections;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by zgantar on 5. 05. 2017.
 */
public class Util {
    public static void reset(IPConnection ipcon, Device.Identity identity) throws TimeoutException, NotConnectedException {
        String parentUid = identity.connectedUid;
        BrickMaster parent = new BrickMaster(parentUid, ipcon);
        String masterUid = parent.getIdentity().connectedUid;
        BrickMaster master = new BrickMaster(masterUid, ipcon);
        parent.reset();
        master.reset();
    }

    static HashMap<String, HashMap<String, Device>> getDevices(IPConnection ipConnection) {
        HashMap<String, HashMap<String, Device>> result = new HashMap<>();

        ipConnection.addEnumerateListener((uid, connectedUid, position, hardwareVersion, firmwareVersion, deviceIdentifier, enumerationType) -> {
            System.out.println("Dobili klic naprave - " + uid);
            switch (deviceIdentifier) {
                case BrickMaster.DEVICE_IDENTIFIER:
                    addBrickMaster(result, uid, ipConnection);
                    break;
                case BrickletIO4.DEVICE_IDENTIFIER:
                    addBrickletIO4(result, uid, ipConnection);
                    break;
                case BrickletIO16.DEVICE_IDENTIFIER:
                    addBrickletIO16(result, uid, ipConnection);
                    break;
                case BrickletTemperature.DEVICE_IDENTIFIER:
                    addBrickletTemperature(result, uid, ipConnection);
                    break;
                case BrickletAmbientLight.DEVICE_IDENTIFIER:
                    addBrickletAmbientLight(result, uid, ipConnection);
                    break;
                case BrickletHumidity.DEVICE_IDENTIFIER:
                    addBrickletHumidity(result, uid, ipConnection);
                    break;
                case BrickletDualRelay.DEVICE_IDENTIFIER:
                    addBrickletDualRelay(result, uid, ipConnection);
                    break;
                default:
                    System.out.println("Neki je narobe, ker te naprave pa še nimam v sistemu");
            }

            if (enumerationType == IPConnection.ENUMERATION_TYPE_DISCONNECTED) {
                System.out.println("");
            }
        });

        try {
            ipConnection.enumerate();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static void addBrickletDualRelay(HashMap<String, HashMap<String, Device>> result, String uid, IPConnection ipConnection) {
        HashMap<String, Device> devices = result.computeIfAbsent(BrickletDualRelay.DEVICE_DISPLAY_NAME, k -> new HashMap<>());

        devices.put(uid, new BrickletDualRelay(uid, ipConnection));
    }

    private static void addBrickletHumidity(HashMap<String, HashMap<String, Device>> result, String uid, IPConnection ipConnection) {
        HashMap<String, Device> devices = result.computeIfAbsent(BrickletHumidity.DEVICE_DISPLAY_NAME, k -> new HashMap<>());

        devices.put(uid, new BrickletHumidity(uid, ipConnection));
    }

    private static void addBrickletAmbientLight(HashMap<String, HashMap<String, Device>> result, String uid, IPConnection ipConnection) {
        HashMap<String, Device> devices = result.computeIfAbsent(BrickletAmbientLight.DEVICE_DISPLAY_NAME, k -> new HashMap<>());

        devices.put(uid, new BrickletAmbientLight(uid, ipConnection));
    }

    private static void addBrickletTemperature(HashMap<String, HashMap<String, Device>> result, String uid, IPConnection ipConnection) {
        HashMap<String, Device> devices = result.computeIfAbsent(BrickletTemperature.DEVICE_DISPLAY_NAME, k -> new HashMap<>());

        devices.put(uid, new BrickletTemperature(uid, ipConnection));
    }

    private static void addBrickletIO16(HashMap<String, HashMap<String, Device>> result, String uid, IPConnection ipConnection) {
        HashMap<String, Device> devices = result.computeIfAbsent(BrickletIO16.DEVICE_DISPLAY_NAME, k -> new HashMap<>());

        devices.put(uid, new BrickletIO16(uid, ipConnection));
    }

    private static void addBrickletIO4(HashMap<String, HashMap<String, Device>> result, String uid, IPConnection ipConnection) {
        HashMap<String, Device> devices = result.computeIfAbsent(BrickletIO4.DEVICE_DISPLAY_NAME, k -> new HashMap<>());

        devices.put(uid, new BrickletIO4(uid, ipConnection));
    }

    private static void addBrickMaster(HashMap<String, HashMap<String, Device>> result, String uid, IPConnection ipConnection) {
        HashMap<String, Device> devices = result.computeIfAbsent(BrickMaster.DEVICE_DISPLAY_NAME, k -> new HashMap<>());

        devices.put(uid, new BrickMaster(uid, ipConnection));
    }

    private static void addBrickletDualRelay(HashMap<String, HashMap<String, Device>> result, String uid, IPConnection ipConnection) {
        HashMap<String, Device> devices = result.computeIfAbsent(BrickletDualRelay.DEVICE_DISPLAY_NAME, k -> new HashMap<>());

        devices.put(uid, new BrickletDualRelay(uid, ipConnection));
    }

    static void startDiagnose(HashMap<String, HashMap<String, Device>> devices, ResourceBundle resources, IPConnection ipcon) {
//        Reflections reflections = new Reflections("org.gantar.openhab.diagnostics.");
//        Set<Class<? extends org.gantar.openhab.diagnostics.DiagnosticInterface>> results = reflections.getSubTypesOf(org.gantar.openhab.diagnostics.DiagnosticInterface.class);

        MasterBrickSubroutine masterBrickSubroutine = new MasterBrickSubroutine();
        masterBrickSubroutine.diagnose(devices, resources, ipcon);

        IOSubroutine ioSubroutine = new IOSubroutine();
        ioSubroutine.diagnose(devices, resources, ipcon);

        TemperatureSubroutine temperatureSubroutine = new TemperatureSubroutine();
        temperatureSubroutine.diagnose(devices, resources, ipcon);

        HumiditySubroutine humiditySubroutine = new HumiditySubroutine();
        humiditySubroutine.diagnose(devices, resources, ipcon);

        AmbientLightSubroutine ambientLightSubroutine = new AmbientLightSubroutine();
        ambientLightSubroutine.diagnose(devices, resources, ipcon);

        DualRelaySubroutine dualRelaytSubroutine = new DualRelaySubroutine();
        dualRelaytSubroutine.diagnose(devices, resources, ipcon);


/**     Reflections reflections = new Reflections("org.gantar.openhab.diagnostics.");
        Set<Class<? extends org.gantar.openhab.diagnostics.DiagnosticInterface>> results = reflections.getSubTypesOf(org.gantar.openhab.diagnostics.DiagnosticInterface.class);

        for (Class clazz:results) {
            try {
                Constructor c = clazz.getConstructor();
                Object obj = ((clazz.getClass())); c.newInstance();
                Method[] declaredMethods = clazz.getClass().getDeclaredMethods();
                Class<?>[] params =  new Class[3];
                params[0] = HashMap.class;
                params[1] = ResourceBundle.class;
                params[2] = IPConnection.class;
                Method m = clazz.getClass().getMethod("diagnose", params);
                    String mName = m.getName();
                    if (mName.equalsIgnoreCase("diagnose")) {
                        m.invoke(obj, devices, resources, ipcon);
                    }
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                System.out.println("Napaka pri klicu diagnose!");
                e.printStackTrace();
            }
        }
 */
    }

    private String executeCommand(String command) {

		StringBuffer output = new StringBuffer();
		Process p;

		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}
}
