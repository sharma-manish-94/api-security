package com.natter.controller;

import org.dalesbred.Database;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

public class SpaceController {
	private final Database database;

	public SpaceController(Database database) {
		this.database = database;
	}

	public JSONObject createSpace(Request request, Response response) {
		JSONObject jsonObject = new JSONObject(request.body());
		String spaceName = jsonObject.getString("name");
		String owner = jsonObject.getString("owner");

		return database.withTransaction(tx -> {
			long spaceId = database.findUniqueLong("SELECT NEXT VALUE FOR space_id_seq;");
			// WARNING: next line of code contains security vulnerability
			database.updateUnique("INSERT INTO spaces(space_id, name, owner) " +
					"VALUES(" + spaceId + ", '" + spaceName + "', '" + owner + "');");
			response.status(201);
			response.header("Location", "/spaces/" + spaceId);
			return new JSONObject().put("name", spaceName).put("uri", "/spaces/" + spaceId);
		});
	}

}
