package com.prgrms.mukvengers.global.config.p6spy;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

@Configuration
public class P6spyConfig implements MessageFormattingStrategy {

	@PostConstruct
	public void setLogMessageFormat() {
		P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
	}

	@Override
	public String formatMessage(int connectionId, String now, long elapsed, String category,
		String prepared, String sql, String url) {
		sql = formatSql(category, sql);
		Date currentDate = new Date();

		SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd HH:mm:ss");

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
			sql = "\n" + stackTrace() + "\n" + sql + "\n";
		}

		return sql;
	}

	private String stackTrace() {
		return Arrays.toString(Arrays.stream(new Throwable().getStackTrace())
			.filter(t -> t.toString().startsWith("com.prgrms.mukvengers"))
			.toArray()).replace(", ", "\n");
	}

}
