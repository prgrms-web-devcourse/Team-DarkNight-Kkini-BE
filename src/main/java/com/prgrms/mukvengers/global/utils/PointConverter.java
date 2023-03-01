package com.prgrms.mukvengers.global.utils;

import java.nio.charset.StandardCharsets;

import javax.persistence.AttributeConverter;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;

@Component
public class PointConverter implements AttributeConverter<Point, String> {
	static WKTReader wktReader = new WKTReader();

	@Override
	public String convertToDatabaseColumn(Point attribute) {
		return attribute.toText();
	}

	@Override
	public Point convertToEntityAttribute(String dbData) {
		try {
			String decoded = new String(dbData.getBytes(), StandardCharsets.UTF_8);
			return (Point)wktReader.read(decoded);
		} catch (ParseException e) {
			throw new IllegalArgumentException();
		}
	}
}
