package com.prgrms.mukvengers.global.config.db.p6spy;

import java.util.Arrays;
import java.util.Locale;

import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

@Profile("default")
@Configuration
public class P6spyMessageFormatter implements MessageFormattingStrategy {

	@Override
	public String formatMessage(int connectionId, String now, long elapsed, String category,
		String prepared, String sql, String url) {

		sql = formatSql(category, sql);
		return category + " | " + "OperationTime : " + elapsed + "ms" + sql;
	}

	private String formatSql(String category, String sql) {
		if (sql == null || sql.isBlank()) {
			return sql;
		}

		if (Category.STATEMENT.getName().equals(category)) {
			String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
			if (tmpsql.startsWith("create") || tmpsql.startsWith("alter")) {
				sql = FormatStyle.DDL.getFormatter().format(sql);
			} else {
				sql = FormatStyle.BASIC.getFormatter().format(sql);
			}
		}

		sql += "\n";

		String[] stackTrace = stackTrace();

		if (stackTrace.length > 0) {
			sql += Arrays.toString(stackTrace).replace(", ", "\n");
		}

		return sql;
	}

	private String[] stackTrace() {
		return Arrays.stream(new Throwable().getStackTrace())
			.map(StackTraceElement::toString)
			.filter(string -> string.startsWith("com.prgrms.mukvengers")
			                  && !string.startsWith("com.prgrms.mukvengers.global.config.p6spy")
			                  && !string.startsWith("com.prgrms.mukvengers.MukvengersApplication.main"))
			.toArray(String[]::new);
	}

}