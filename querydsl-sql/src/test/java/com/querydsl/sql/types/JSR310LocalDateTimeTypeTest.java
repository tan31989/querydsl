package com.querydsl.sql.types;

import org.easymock.EasyMock;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JSR310LocalDateTimeTypeTest extends AbstractJSR310DateTimeTypeTest<LocalDateTime> {

    public JSR310LocalDateTimeTypeTest() {
        super(new JSR310LocalDateTimeType());
    }

    @Test
    public void set() throws SQLException {
        LocalDateTime value = LocalDateTime.now();
        Timestamp ts = Timestamp.from(value.toInstant(ZoneOffset.UTC));

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setTimestamp(1, ts, UTC);
        EasyMock.replay(stmt);

        type.setValue(stmt, 1, value);
        EasyMock.verify(stmt);
    }

    @Test
    public void jodaSet() throws SQLException {
        LocalDateTime value = LocalDateTime.now();
        Timestamp ts = new Timestamp(value.toInstant(ZoneOffset.UTC).toEpochMilli());

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setTimestamp(1, ts, UTC);
        EasyMock.replay(stmt);

        new LocalDateTimeType().setValue(stmt, 1, toJoda(value));
        EasyMock.verify(stmt);
    }

    @Test
    public void get() throws SQLException {
        ResultSet resultSet = EasyMock.createNiceMock(ResultSet.class);
        EasyMock.expect(resultSet.getTimestamp(1, UTC)).andReturn(Timestamp.from(UTC.toInstant()));
        EasyMock.replay(resultSet);

        LocalDateTime result = type.getValue(resultSet, 1);
        EasyMock.verify(resultSet);

        assertNotNull(result);
        assertTrue(result.getDayOfYear() == 1);
    }
}
