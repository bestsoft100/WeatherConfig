package b100;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
	
	private Map<String, String> config = new HashMap<>();
	private Log log;
	private boolean hasChanged = false;
	
	public int getInt(String name, int defaultValue, Integer min, Integer max) {
		if(min != null && max != null && min > max) {
			throw new IllegalArgumentException("min > max");
		}
		
		String valueString = config.get(name);
		if(valueString == null) {
			print("No value set for '"+name+"', using default '"+defaultValue+"'");
			return setInt(name, defaultValue);
		}
		
		int value;
		try {
			value = Integer.parseInt(valueString);
		}catch (NumberFormatException e) {
			print("Invalid input string: '"+valueString+"'");
			return setInt(name, defaultValue);
		}
		
		if(min != null && value < min || max != null && value > max) {
			print("Value '"+value+"' out of bounds for option '"+name+"'!");
			return setInt(name, defaultValue);
		}
		
		return value;
	}
	
	private int setInt(String name, int val) {
		config.put(name, String.valueOf(val));
		hasChanged = true;
		return val;
	}
	
	public void save(File configFile) {
		try {
			if(configFile.exists()) {
				configFile.delete();
			}
			configFile.createNewFile();
			
			List<String> entries = new ArrayList<>(config.keySet());
			entries.sort(String.CASE_INSENSITIVE_ORDER);
			
			FileWriter fw = new FileWriter(configFile);
			for(String key : entries) {
				String val = config.get(key);
				
				fw.write(key+":"+val+"\n");
			}
			
			fw.close();
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void load(File configFile) {
		if(!configFile.exists()) {
			return;
		}
		
		print("Loading config: " + configFile.getAbsolutePath());
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(configFile));
			
			config.clear();
			while(true) {
				String line = br.readLine();
				if(line == null) {
					break;
				}
				line = line.trim();
				if(line.length() == 0 || line.startsWith("#")) {
					continue;
				}
				
				int i = line.indexOf(':');
				if(i == -1) {
					print("Invalid line '"+line+"'");
					continue;
				}
				
				String key = line.substring(0, i).trim();
				String val = line.substring(i + 1).trim();
				config.put(key, val);
			}
			
			hasChanged = false;
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				br.close();
			}catch (Exception e) {}
		}
	}
	
	public boolean hasChanged() {
		return hasChanged;
	}
	
	public void setLog(Log log) {
		this.log = log;
	}
	
	private void print(String string) {
		if(log != null) {
			log.print(string);
		}
	}
	
}
