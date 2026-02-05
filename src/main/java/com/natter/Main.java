package com.natter;

import com.natter.controller.SpaceController;
import org.dalesbred.Database;
import org.h2.jdbcx.JdbcConnectionPool;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static spark.Spark.after;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;

public class Main {
	public static void main(String[] args) throws URISyntaxException, IOException {
		JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create("jdbc:h2:mem:natter", "natter", "password");
		Database database = Database.forDataSource(jdbcConnectionPool);
		createTables(database);

		SpaceController spaceController = new SpaceController(database);
		post("/spaces", spaceController::createSpace);
		after((req, res) -> {
			res.type("application/json");
		});
		internalServerError(new JSONObject().put("error", "Internal Server Error").toString());
		notFound(new JSONObject().put("error", "Not Found").toString());
	}

	private static void createTables(Database database) throws URISyntaxException, IOException {
		Path path = Paths.get(Objects.requireNonNull(Main.class.getResource("/schema.sql")).toURI());
		database.update(Files.readString(path));
	}
}
