package com.prgrms.mukvengers.domain.crew.model.vo;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
	QUIET("조용한");

	private final String category;

	Category(String category) {
		this.category = category;
	}

	@JsonCreator
	public static Category of(String categoryName) {
		return Arrays.stream(Category.values())
			.filter(category -> Objects.equals(category.getCategory(), categoryName))
			.findFirst()
			.orElseThrow();
	}

	@JsonValue
	public String getCategory() {
		return category;
	}
}
