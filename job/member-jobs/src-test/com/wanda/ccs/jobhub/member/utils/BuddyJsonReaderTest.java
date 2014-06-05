package com.wanda.ccs.jobhub.member.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class BuddyJsonReaderTest {

	@Test
	public void testJson() {
		BuddyJsonReader buddy = new BuddyJsonReader(this.getClass(), "test.json");
		assertEquals(buddy.get("key1"), "property1");
		assertEquals(buddy.get("key2"), "property2");
	}
	
	@Test
	public void testXml() {
		BuddyXmlReader buddy = new BuddyXmlReader(this.getClass(), "test.xml");
		assertEquals(buddy.get("key1"), "中文");
		assertEquals(buddy.get("key2"), "and rownum<100\nand rownum<101\nand rownum<102");
	}

}
