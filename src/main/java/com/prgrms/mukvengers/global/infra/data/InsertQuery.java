package com.prgrms.mukvengers.global.infra.data;

public enum InsertQuery {

	USER_INSERT(
		"""
			INSERT IGNORE INTO users (nickname, introduction, profile_img_url, provider, oauth_id)
			VALUES (?, ?, ?, ?, ?)
			"""
	),

	STORE_INSERT_SQL(
		"""
			INSERT IGNORE INTO store (location, place_id, place_name, categories, road_address_name, photo_urls, kakao_place_url, phone_number)
			VALUES (ST_GeomFromText(?, 5179), ?, ?, ?, ?, ?, ?, ?)
			"""
	),

	CREW_INSERT_SQL(
		"""
			INSERT IGNORE INTO crew (store_id, name, location, status, promise_time, content, category)
			VALUES (?, ?, ST_GeomFromText(?, 5179), ?, ?, ?, ?)
			"""
	),

	CREW_MEMBER_INSERT_SQL(
		"""
			INSERT IGNORE INTO crew_member (user_id, crew_id, crew_member_role)
			VALUES (?, ?, ?)
			"""
	);

	private final String sql;

	InsertQuery(String sql) {
		this.sql = sql;
	}

	public String sql() {
		return sql;
	}

}
