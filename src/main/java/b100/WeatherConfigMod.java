package b100;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "WeatherConfig", name = "WeatherConfig", version = "1.0")
public class WeatherConfigMod {
	
	public static Log log = (string) -> System.out.print("[WeatherConfig] " + string + "\n");
	
	public static void print(String string) {
		log.print(string);
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		WeatherConfigModConfig.loadConfig();
	}
	
}
