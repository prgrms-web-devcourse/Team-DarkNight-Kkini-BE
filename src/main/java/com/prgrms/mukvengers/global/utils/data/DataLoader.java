package com.prgrms.mukvengers.global.utils.data;

import static com.prgrms.mukvengers.global.utils.data.InsertQuery.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.utils.GeometryUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader {

	private final JdbcTemplate jdbcTemplate;

	private final DataGenerator dataGenerator = new DataGenerator();

	public void loadSetCrewAndPeople(int count, StoreRepository storeRepository,
		UserRepository userRepository, CrewRepository crewRepository) {

		loadUser(count);
		loadCrew(count, storeRepository);

		List<User> userList = userRepository.findRandom(count);
		List<Crew> crewList = crewRepository.findRandom(count);

		int minCount = Math.min(userList.size(), crewList.size());

		List<CrewMember> crewMemberList = dataGenerator.generateCrewMember(minCount, userList, crewList);

		jdbcTemplate.batchUpdate(
			CREW_MEMBER_INSERT_SQL.sql(), new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					CrewMember crewMember = crewMemberList.get(i);
					ps.setLong(1, crewMember.getUserId());
					ps.setLong(2, crewMember.getCrew().getId());
					ps.setString(3, crewMember.getCrewMemberRole().name());
				}

				@Override
				public int getBatchSize() {
					return crewMemberList.size();
				}
			}
		);

	}

	public void loadUser(int count) {

		List<User> userInfoList = dataGenerator.generateUser(count);

		jdbcTemplate.batchUpdate(
			USER_INSERT.sql(), new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					User userInfo = userInfoList.get(i);
					ps.setString(1, userInfo.getNickname());
					ps.setString(2, userInfo.getIntroduction());
					ps.setString(3, userInfo.getProfileImgUrl());
					ps.setString(4, userInfo.getProvider());
					ps.setString(5, userInfo.getOauthId());
				}

				@Override
				public int getBatchSize() {
					return userInfoList.size();
				}
			}
		);
	}

	public void loadStore(int count) {

		List<Store> storeList = dataGenerator.generateStore(count);

		WKTWriter wktWriter = new WKTWriter();

		jdbcTemplate.batchUpdate(
			STORE_INSERT_SQL.sql(), new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Store store = storeList.get(i);
					ps.setString(1, wktWriter.write(store.getLocation()));
					ps.setString(2, store.getPlaceId());
					ps.setString(3, store.getPlaceName());
					ps.setString(4, store.getCategories());
					ps.setString(5, store.getRoadAddressName());
					ps.setString(6, store.getPhotoUrls());
					ps.setString(7, store.getKakaoPlaceUrl());
					ps.setString(8, store.getPhoneNumber());
				}

				@Override
				public int getBatchSize() {
					return storeList.size();
				}
			}
		);

	}

	public void loadCrew(int count, StoreRepository storeRepository) {

		loadStore(count);
		List<Store> storeList = storeRepository.findRandom(count);
		List<Crew> crewList = dataGenerator.generateCrew(storeList.size(), storeList);

		WKTWriter wktWriter = new WKTWriter();

		jdbcTemplate.batchUpdate(
			CREW_INSERT_SQL.sql(), new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Crew crew = crewList.get(i);
					ps.setLong(1, crew.getStore().getId());
					ps.setString(2, crew.getName());
					ps.setString(3, wktWriter.write(crew.getStore().getLocation()));
					ps.setString(4, crew.getStatus().name());
					ps.setTimestamp(5, Timestamp.valueOf(crew.getPromiseTime()));
					ps.setString(6, crew.getContent());
					ps.setString(7, crew.getCategory());
				}

				@Override
				public int getBatchSize() {
					return crewList.size();
				}
			}
		);
	}

	public static class DataGenerator {

		private static final Random random = new Random();
		// Store
		private static final String placeId = "";
		private static final String DEFAULT_STORE_PLACE_NAME = "테스트 음식점";
		private static final String STORE_CATEGORIES = "음식점 > 한식";
		private static final String STORE_ROAD_ADDRESS_NAME = "강남구 테스트로";
		private static final String STORE_PHOTO_URLS = "'https://~', 'https://~'";
		private static final String STORE_DEFAULT_KAKAO_PLACE_URL = "http://place.map.kakao.com/";
		private static final String STORE_PHONE_NUMBER = "010-1234-5678";
		// Crew
		private static final String NAME = "원정대";
		private static final Integer CAPACITY = 2;
		private static final String CONTENT = "저는 백엔드 개발자 입니다. 프론트 엔드 개발자 구해요";
		private static final String CATEGORY = "조용한";
		private static final LocalDateTime PROMISE_TIME = LocalDateTime.now();

		DataGenerator() {
		}

		List<User> generateUser(int count) {

			List<User> userInfoList = new ArrayList<>();

			for (int i = 0; i < count; i++) {

				long randomNum = (random.nextLong(count) * i) + i;

				User userInfo = User.builder()
					.nickname("TEST : " + i + "-" + randomNum)
					.profileImgUrl("Default Profile Image URL")
					.provider("TEST")
					.oauthId(String.valueOf(randomNum))
					.build();

				userInfoList.add(userInfo);
			}

			return userInfoList;
		}

		List<Store> generateStore(int count) {
			GeometryFactory gf = GeometryUtils.getInstance();

			List<Store> storeList = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				// 랜덤 위치 생성하기 - 서울 위경도 범위 내에서
				double minLon = 126.7643;
				double maxLon = 127.1833;
				double minLat = 37.4269;
				double maxLat = 37.7017;

				Random rand = new Random();

				double longitude = minLon + (maxLon - minLon) * rand.nextDouble();
				double latitude = minLat + (maxLat - minLat) * rand.nextDouble();

				Point location = gf.createPoint(new Coordinate(longitude, latitude));

				long randomNum = (random.nextLong(count) * i) + i;

				Store store = Store.builder()
					.location(location)
					.placeId(placeId + randomNum)
					.placeName(DEFAULT_STORE_PLACE_NAME + "-" + randomNum)
					.categories(STORE_CATEGORIES)
					.roadAddressName(STORE_ROAD_ADDRESS_NAME)
					.photoUrls(STORE_PHOTO_URLS)
					.kakaoPlaceUrl(STORE_DEFAULT_KAKAO_PLACE_URL + randomNum)
					.phoneNumber(STORE_PHONE_NUMBER)
					.build();

				storeList.add(store);
			}

			return storeList;
		}

		List<Crew> generateCrew(int count, List<Store> storeList) {

			List<Crew> crewList = new ArrayList<>();

			for (int i = 0; i < count; i++) {
				Store store = storeList.get(i);

				Crew crew = Crew.builder()
					.store(store)
					.name(NAME)
					.location(store.getLocation())
					.promiseTime(PROMISE_TIME)
					.capacity(CAPACITY)
					.content(CONTENT)
					.category(CATEGORY)
					.build();

				crewList.add(crew);
			}

			return crewList;
		}

		// 앞선 모든 과정이 필요함
		List<CrewMember> generateCrewMember(int count, List<User> userList, List<Crew> crewList) {

			List<CrewMember> crewMemberList = new ArrayList<>();

			for (int i = 0; i < count; i++) {
				User user = userList.get(i);
				Crew crew = crewList.get(i);

				CrewMember crewMember = CrewMember.builder()
					.crew(crew)
					.crewMemberRole(CrewMemberRole.LEADER)
					.userId(user.getId())
					.build();

				crewMemberList.add(crewMember);
			}

			return crewMemberList;
		}
	}
}
