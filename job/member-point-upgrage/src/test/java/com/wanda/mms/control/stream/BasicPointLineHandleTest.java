package com.wanda.mms.control.stream;

import static org.junit.Assert.*;

import org.junit.Test;

import com.solar.etl.config.mapping.FieldSet;

public class BasicPointLineHandleTest {

	@Test
	public void testHandle() {
		BasicPointLineHandle handle = new BasicPointLineHandle();
		FieldSet fieldset = null;
		handle.handle(fieldset );
	}

}
