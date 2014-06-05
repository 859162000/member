package com.wanda.mms.control.response.handle;

import java.sql.Connection;

public class ResActHandler extends AbstractActHandler {

	public ResActHandler(Connection conn) {
		super("has_response", conn);
	}
}
