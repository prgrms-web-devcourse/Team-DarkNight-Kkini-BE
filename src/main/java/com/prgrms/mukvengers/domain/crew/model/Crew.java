package com.prgrms.mukvengers.domain.crew.model;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;

import com.prgrms.mukvengers.domain.crew.model.vo.Category;
import com.prgrms.mukvengers.domain.crew.model.vo.Status;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.global.common.domain.BaseEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE crew set deleted = true where id=?")
public class Crew extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "leader_id", referencedColumnName = "id")
	private User leader;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "store_id", referencedColumnName = "id")
	private Store store;

	@Column(nullable = false, length = 20)
	private String name;

	@Convert(converter = Store.PointConverter.class)
	@ColumnTransformer(write = "ST_PointFromText(?, 4326)", read = "ST_AsText(location)")
	@Column(nullable = false)
	private Point location;

	@Column(nullable = false)
	private Integer capacity;

	@Column(nullable = false, length = 255)
	@Enumerated(STRING)
	private Status status;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false, length = 255)
	@Enumerated(STRING)
	private Category category;

	@Column(nullable = false)
	private LocalDateTime promiseTime;

	@OneToMany(mappedBy = "crew")
	private List<CrewMember> crewMembers = new ArrayList<>();

	@Builder
	protected Crew(User leader, Store store, String name, Point location, Integer capacity, Status status,
		String content, Category category, LocalDateTime promiseTime) {
		this.leader = leader;
		this.store = store;
		this.name = name;
		this.location = location;
		this.capacity = capacity;
		this.status = status;
		this.content = content;
		this.category = category;
		this.promiseTime = promiseTime;
	}

	@Component
	public static class PointConverter implements AttributeConverter<Point, String> {
		static WKTReader wktReader = new WKTReader();

		@Override
		public String convertToDatabaseColumn(Point attribute) {
			return attribute.toText();
		}

		@Override
		public Point convertToEntityAttribute(String dbData) {
			try {
				String decoded = new String(dbData.getBytes(), StandardCharsets.UTF_8);
				System.out.println(decoded);
				return (Point)wktReader.read(decoded);
			} catch (ParseException e) {
				throw new IllegalArgumentException();
			}
		}
	}
}
