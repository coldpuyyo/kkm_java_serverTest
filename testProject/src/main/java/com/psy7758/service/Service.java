package com.psy7758.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.psy7758.dto.Client;

public interface Service {
	ArrayList<Client> getClient() throws SQLException;
	ArrayList<Client> getClient(String searchField, String SearchWord) throws SQLException;
}
