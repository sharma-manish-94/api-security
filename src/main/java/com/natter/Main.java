package com.natter;

import static spark.Spark.get;
import static spark.Spark.port;

public class Main {
	public static void main(String[] args) {
		port(4567);
		get("/hello", (req, res) -> "Welcome to the world of API Security!");
		System.out.println("Server started on http://localhost:4567");
	}
}
