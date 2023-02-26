package com.prgrms.mukvengers.utils;

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
import com.prgrms.mukvengers.domain.user.model.User;

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
		new Coordinate(Double.parseDouble(latitude), Double.parseDouble(longitude)));


	public static Crew createCrew(User user, Store store) {

		return Crew.builder()
			.leader(user)
			.store(store)
			.name(name)
			.location(location)
			.capacity(capacity)
			.status(Status.getStatus(status))
			.content(content)
			.category(Category.getCategory(category))
			.build();

	}

	public static List<Crew> createCrews(User user, Store store) {

		return IntStream.range(0, 20)
			.mapToObj(i -> createCrew(user, store)).collect(Collectors.toList());

	}

	public static CreateCrewRequest getCreateCrewRequest(String mapStoreId) {
		return new CreateCrewRequest(
			mapStoreId,
			name,
			latitude,
			longitude,
			capacity,
			status,
			content,
			category
		);
	}
}
