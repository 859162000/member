package com.wanda.mms.control.response.handle;

import java.sql.Connection;

public class AlterActHandler extends AbstractActHandler {

	public AlterActHandler(Connection conn) {
		super("has_response2", conn);
	}
}
