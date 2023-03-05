package com.prgrms.mukvengers.utils;

import static com.prgrms.mukvengers.domain.crew.model.vo.Category.*;
import static com.prgrms.mukvengers.domain.crew.model.vo.Status.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.Category;
import com.prgrms.mukvengers.domain.crew.model.vo.Status;
import com.prgrms.mukvengers.domain.store.model.Store;

public class CrewObjectProvider {

	private final static GeometryFactory GF = new GeometryFactory();
	private static final String LATITUDE = "35.75413579";
	private static final String LONGITUDE = "-147.4654321321";
	private static final String NAME = "원정대이름";
	private static final Integer CAPACITY = 5;
	private static final Status STATUS_RECRUITING = RECRUITING;
	private static final Status STATUS_CLOSE = CLOSE;
	private static final String CONTENT = "저는 백엔드 개발자 입니다. 프론트 엔드 개발자 구해요";
	private static final Category CATEGORY = QUIET;
	private static final LocalDateTime PROMISE_TIME = LocalDateTime.now();
	private static final Point LOCATION = GF.createPoint(
		new Coordinate(Double.parseDouble(LONGITUDE), Double.parseDouble(LATITUDE)));

	public static Crew createCrew(Store store, Status status) {
		return Crew.builder()
			.store(store)
			.name(NAME)
			.location(LOCATION)
			.promiseTime(PROMISE_TIME)
			.capacity(CAPACITY)
			.content(CONTENT)
			.category(CATEGORY)
			.build();
	}

	public static List<Crew> createCrews(Store store) {
		return IntStream.range(0, 20)
			.mapToObj(i -> createCrew(store, i % 2 == 0 ? STATUS_CLOSE : STATUS_RECRUITING))
			.collect(Collectors.toList());
	}

	public static CreateCrewRequest getCreateCrewRequest(String mapStoreId) {
		return new CreateCrewRequest(
			mapStoreId,
			NAME,
			LONGITUDE,
			LATITUDE,
			PROMISE_TIME,
			CAPACITY,
			CONTENT,
			CATEGORY.getCategory()
		);
	}
}
