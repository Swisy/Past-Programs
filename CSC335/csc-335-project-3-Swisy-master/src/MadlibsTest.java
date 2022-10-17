import static org.junit.Assert.assertEquals;


import org.junit.jupiter.api.Test;

import customMap.ListMap;

class MadlibsTest {

	@Test
	void ListMapTest() {
		ListMap<String, String> map = new ListMap<String, String>();
		
		assertEquals(map.isEmpty(), true);
		
		map.put("one", "two");
		assertEquals(map.get("one"), "two");
		
		assertEquals(map.containsKey("one"), true);
		
		assertEquals(map.containsValue("two"), true);
		
		assertEquals(map.containsKey("two"), false);
		
		
		map.put("three", "four");
		map.put("five", "six");
		map.put("five", "five");
		
		assertEquals(map.get("five"), "five");
		
		assertEquals(map.containsKey("three"), true);
		
		assertEquals(map.containsKey("five"), true);
		
		assertEquals(map.size(), 3);
		
		ListMap<String, String> map1 = new ListMap<String, String>();
		ListMap<String, String> map2 = new ListMap<String, String>();
		
		map1.put("1", "2");
		map2.put("1", "2");
		
		assertEquals(map1.equals(map2), true);
		
		assertEquals(map.entrySet().contains("one"), false);
		
		assertEquals(map1.entrySet().equals(map2.entrySet()), true);
		
		assertEquals(map.entrySet().equals(map2.entrySet()), false);
		
		map1.put("1", "1");
		assertEquals(map1.entrySet().equals(map2.entrySet()), false);
		
		map2.put("1", "1");
		map1.put("2", "1");
		map2.put("2", "2");
		assertEquals(map1.entrySet().equals(map2.entrySet()), false);
		
		ListMap<String, String> map3 = new ListMap<String, String>();
		ListMap<String, String> map4 = new ListMap<String, String>();
		
		map3.put("2", "1");
		map4.put("1", "1");
		assertEquals(map3.entrySet().equals(map4.entrySet()), false);
		
	}
}
