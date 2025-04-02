package com.psy7758.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import com.psy7758.dto.Client;

public interface Dao {
	ArrayList<Client> getClient(String searchField, String searchWord, boolean pub) throws SQLException;

	int setClientPubTrue(String id) throws SQLException;
}