package model;

import java.util.HashMap;

public class Menu {
	
	private HashMap<String, MenuItem> items;
	
	public Menu() {
		items = new HashMap<>();
		items.put("BigMac", new BigMac());
		items.put("MediumMac", new MediumMac());
		items.put("SmallMac", new SmallMac());
		items.put("MiceCream", new MiceCream());
	}
	
	public double getPrice(String in) {
		return items.get(in).getPrice();
	}
	
	public String getQuip(String in) {
		return items.get(in).toString();
	}
	
	public String toString() {
		String ret = "Menu:\n";

		for(String item: items.keySet()) {
			ret += "  " + item + "\n";
		}
		return ret;
	}
}
