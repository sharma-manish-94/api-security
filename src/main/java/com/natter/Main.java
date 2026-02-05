package com.natter;

import org.dalesbred.Database;
import org.h2.jdbcx.JdbcConnectionPool;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {
	public static void main(String[] args) throws URISyntaxException, IOException {
		JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create("jdbc:h2:mem:natter", "natter", "password");
		Database database = Database.forDataSource(jdbcConnectionPool);
		createTables(database);
	}

	private static void createTables(Database database) throws URISyntaxException, IOException {
		Path path = Paths.get(Objects.requireNonNull(Main.class.getResource("/schema.sql")).toURI());
		database.update(Files.readString(path));
	}
}
