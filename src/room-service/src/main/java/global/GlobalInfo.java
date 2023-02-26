package global;

import java.time.Duration;
import java.time.LocalTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import java.util.HashMap;
import java.util.Map;

public class GlobalInfo {
	private static boolean adminControl = false;
	private static boolean presence = false;
	private static boolean enoughLight = false;
	private static boolean rollControl = false;
	private static boolean lightControl = false;
	private static int alpha = 0;
	private static boolean lastLight = false;
	private static LocalTime lastTime = LocalTime.now();
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH mm ss");
	private static Map<String, Long> durationLightOn = new HashMap<>();
	
	public static void updateLightHours(boolean ll) {
		if(lastLight == false && ll == true) {
			lastTime = LocalTime.now();
			lastLight = true;
		}else if(lastLight == true && ll == false) {
			lastLight = false;
			long hours = Duration.between(lastTime, LocalTime.now()).toMinutes();
			durationLightOn.put(formatter.format(lastTime), hours);
		}
	}
	
	public static void updatePresence(boolean p) {
		presence = p;
	}
	
	public static boolean getPresence() {
		return presence;
	}
	
	public static Map<String, Long> getDurationLight() {
		return durationLightOn;
	}
	
	
}
