package com.prgrms.mukvengers.utils;

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

	private final static GeometryFactory gf = new GeometryFactory();
	private static final String latitude = "35.75413579";
	private static final String longitude = "-147.4654321321";
	private static final String name = "원정대이름";
	private static final Integer capacity = 5;
	private static final String status = "모집중";
	private static final String content = "저는 백엔드 개발자 입니다. 프론트 엔드 개발자 구해요";
	private static final String category = "조용한";
	private static final Point location = gf.createPoint(
		new Coordinate(Double.parseDouble(longitude), Double.parseDouble(latitude)));
	// private??
	private final static GeometryFactory GF = new GeometryFactory();
	private static final String LATITUDE = "35.75413579";
	private static final String LONGITUDE = "-147.4654321321";
	private static final String NAME = "원정대이름";
	private static final Integer CAPACITY = 5;
	private static final String STATUS = "모집중";
	private static final String CONTENT = "저는 백엔드 개발자 입니다. 프론트 엔드 개발자 구해요";
	private static final String CATEGORY = "조용한";
	private static final LocalDateTime PROMISE_TIME = LocalDateTime.now();
	private static final Point LOCATION = GF.createPoint(
		new Coordinate(Double.parseDouble(LATITUDE), Double.parseDouble(LONGITUDE)));

	public static Crew createCrew(Store store) {

		return Crew.builder()
			.store(store)
			.name(name)
			.location(location)
			.promiseTime(LocalDateTime.now())
			.capacity(capacity)
			.status(Status.getStatus(status))
			.content(content)
			.category(Category.getCategory(category))
			.build();

	}

	public static List<Crew> createCrews(Store store) {

		return IntStream.range(0, 20)
			.mapToObj(i -> createCrew(store)).collect(Collectors.toList());

	}

	public static CreateCrewRequest getCreateCrewRequest(String mapStoreId) {
		return new CreateCrewRequest(
			mapStoreId,
			name,
			longitude,
			latitude,
			LocalDateTime.now(),
			capacity,
			status,
			content,
			category
			NAME,
			LATITUDE,
			LONGITUDE,
			PROMISE_TIME,
			CAPACITY,
			STATUS,
			CONTENT,
			CATEGORY
		);
	}
}
