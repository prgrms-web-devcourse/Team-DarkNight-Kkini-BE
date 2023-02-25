package com.prgrms.mukvengers.domain.crew.model.vo;

import java.util.Arrays;

public enum Category {
	QUIET("조용한");

	private final String name;

	Category(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static Category getCategory(String name) {
		return Arrays.stream(Category.values())
			.filter(a -> a.name.equals(name))
			.findAny()
			.orElseThrow();
	}
}
