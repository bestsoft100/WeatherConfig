package b100;

import static b100.WeatherConfigMod.*;

import java.io.File;

import net.minecraft.client.Minecraft;

public class WeatherConfigModConfig {
	
	private static boolean loaded = false;
	
	public static int thunderTimeMin = 180;
	public static int thunderTimeRand = 600;
	
	public static int thunderDelayMin = 600;
	public static int thunderDelayRand = 8400;
	
	public static int rainTimeMin = 600;
	public static int rainTimeRand = 600;
	
	public static int rainDelayMin = 600;
	public static int rainDelayRand = 8400;
	
	static {
		loadConfig();
	}
	
	public static void loadConfig() {
		if(loaded) {
			return;
		}
		loaded = true;
		
		long start = System.currentTimeMillis();
		
		File configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/WeatherConfig.cfg");
		
		Config config = new Config();
		config.setLog(log);
		config.load(configFile);
		
		rainDelayMin = config.getInt("rainDelayMin", 600, 1, null);
		rainDelayRand = config.getInt("rainDelayRand", 8400, 1, null);
		
		rainTimeMin = config.getInt("rainTimeMin", 600, 1, null);
		rainTimeRand = config.getInt("rainTimeRand", 600, 1, null);
		
		thunderDelayMin = config.getInt("thunderDelayMin", 600, 1, null);
		thunderDelayRand = config.getInt("thunderDelayRand", 8400, 1, null);
		
		thunderTimeMin = config.getInt("thunderTimeMin", 180, 1, null);
		thunderTimeRand = config.getInt("thunderTimeRand", 600, 1, null);
		
		if(config.hasChanged()) {
			config.save(configFile);
		}

		print("Rain Delay Min: " + rainDelayMin + "s");
		print("Rain Delay Rand: " + rainDelayRand + "s");

		print("Rain Time Min: " + rainTimeMin + "s");
		print("Rain Time Rand: " + rainTimeRand + "s");

		print("Thunder Delay Min: " + thunderDelayMin + "s");
		print("Thunder Delay Rand: " + thunderDelayRand + "s");

		print("Thunder Time Min: " + thunderTimeMin + "s");
		print("Thunder Time Rand: " + thunderTimeRand + "s");
		
		long end = System.currentTimeMillis();
		
		print("Loaded config in "+(end - start)+" ms");
	}
	
	public static int getThunderTimeMin() {
		return thunderTimeMin * 20;
	}
	
	public static int getThunderTimeRand() {
		return thunderTimeRand * 20;
	}
	
	public static int getThunderDelayMin() {
		return thunderDelayMin * 20;
	}
	
	public static int getThunderDelayRand() {
		return thunderDelayRand * 20;
	}
	
	public static int getRainTimeMin() {
		return rainTimeMin * 20;
	}
	
	public static int getRainTimeRand() {
		return rainTimeRand * 20;
	}
	
	public static int getRainDelayMin() {
		return rainDelayMin * 20;
	}
	
	public static int getRainDelayRand() {
		return rainDelayRand * 20;
	}

}
