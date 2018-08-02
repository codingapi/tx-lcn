package com.codingapi.tx.datasource.relational.txc;

import com.codingapi.tx.datasource.relational.txc.parser.ExecutePaser;
import com.codingapi.tx.datasource.relational.txc.parser.SQLType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * [类描述]
 *
 * @author caican
 * @date 17/11/28
 * @title [confluence页面的title]
 */
public class TxcPreparedStatement extends TxcStatement implements PreparedStatement {

    private static final Logger logger = LoggerFactory.getLogger(TxcPreparedStatement.class);

    public TxcPreparedStatement(PreparedStatement localPreparedStatement, AbstractTxcConnection abstractTxcConnection,
                                String sql) {
        super(localPreparedStatement, abstractTxcConnection);
        this.sql = sql;
    }

    /**
     * 把这个参数缓存下来 有什么用？
     */
    private List<Object> paramsList = new ArrayList<>();

    public int getParameterCount() throws SQLException {
        return getParameterMetaData().getParameterCount();
    }

    public synchronized List<Object> getParamsList() {
        return this.paramsList;
    }

    private void addParam(int paramInt, Object paramObject1, Object paramObject2, Object paramObject3)
            throws SQLException {
        List<Object> paramsList = getParamsList();
        paramsList.add(paramObject1);
    }

    private void addParam(int paramInt, Object paramObject1, Object paramObject2) throws SQLException {
        List<Object> paramsList = getParamsList();
        paramsList.add(paramObject1);
    }

    private void addParam(int paramInt, Object paramObject) throws SQLException {
        List<Object> paramsList = getParamsList();
        paramsList.add(paramObject);
    }

    @Override
    public void addBatch() throws SQLException {
        ((PreparedStatement) this.statement).addBatch();
    }

    @Override
    public void clearParameters() throws SQLException {
        ((PreparedStatement) this.statement).clearParameters();
    }

    @Override
    public boolean execute() throws SQLException {
        if (isInTxcTransaction()) {
            SQLType sqlType = ExecutePaser.parse(this);
            boolean execute = ((PreparedStatement) this.statement).execute();
            ExecutePaser.after(this, sqlType);
            return  execute;
        }

        return ((PreparedStatement) this.statement).execute();
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        // 查询语句也要包装么？
        return ((PreparedStatement) this.statement).executeQuery();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        if (isInTxcTransaction()) {
            ExecutePaser.parse(this);
        }
        return this.statement.executeBatch();
    }

    @Override
    public int executeUpdate() throws SQLException {
        if (isInTxcTransaction()) {
            SQLType sqlType = ExecutePaser.parse(this);
            int num = ((PreparedStatement) this.statement).executeUpdate();
            ExecutePaser.after(this, sqlType);
            return num;
        }

        return ((PreparedStatement) this.statement).executeUpdate();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return ((PreparedStatement) this.statement).getMetaData();
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return ((PreparedStatement) this.statement).getParameterMetaData();
    }

    @Override
    public void setArray(int paramInt, Array paramArray) throws SQLException {
        addParam(paramInt, paramArray);
        ((PreparedStatement) this.statement).setArray(paramInt, paramArray);
    }

    @Override
    public void setAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException {
        addParam(paramInt, paramInputStream);
        ((PreparedStatement) this.statement).setAsciiStream(paramInt, paramInputStream);
    }

    @Override
    public void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
        addParam(paramInt1, paramInputStream, paramInt2);
        ((PreparedStatement) this.statement).setAsciiStream(paramInt1, paramInputStream, paramInt2);
    }

    @Override
    public void setAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
        addParam(paramInt, paramInputStream, paramLong);
        ((PreparedStatement) this.statement).setAsciiStream(paramInt, paramInputStream, paramLong);
    }

    @Override
    public void setBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
        addParam(paramInt, paramBigDecimal);
        ((PreparedStatement) this.statement).setBigDecimal(paramInt, paramBigDecimal);
    }

    @Override
    public void setBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException {
        addParam(paramInt, paramInputStream);
        ((PreparedStatement) this.statement).setBinaryStream(paramInt, paramInputStream);
    }

    @Override
    public void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
        addParam(paramInt1, paramInputStream, paramInt2);
        ((PreparedStatement) this.statement).setBinaryStream(paramInt1, paramInputStream, paramInt2);
    }

    @Override
    public void setBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
        addParam(paramInt, paramInputStream, paramLong);
        ((PreparedStatement) this.statement).setBinaryStream(paramInt, paramInputStream, paramLong);
    }

    @Override
    public void setBlob(int paramInt, Blob paramBlob) throws SQLException {
        addParam(paramInt, paramBlob);
        ((PreparedStatement) this.statement).setBlob(paramInt, paramBlob);
    }

    @Override
    public void setBlob(int paramInt, InputStream paramInputStream) throws SQLException {
        addParam(paramInt, paramInputStream);
        ((PreparedStatement) this.statement).setBlob(paramInt, paramInputStream);
    }

    @Override
    public void setBlob(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
        addParam(paramInt, paramInputStream, paramLong);
        ((PreparedStatement) this.statement).setBlob(paramInt, paramInputStream, paramLong);
    }

    @Override
    public void setBoolean(int paramInt, boolean paramBoolean) throws SQLException {
        addParam(paramInt, paramBoolean);
        ((PreparedStatement) this.statement).setBoolean(paramInt, paramBoolean);
    }

    @Override
    public void setByte(int paramInt, byte paramByte) throws SQLException {
        addParam(paramInt, paramByte);
        ((PreparedStatement) this.statement).setByte(paramInt, paramByte);
    }

    @Override
    public void setBytes(int paramInt, byte[] paramArrayOfByte) throws SQLException {
        addParam(paramInt, paramArrayOfByte);
        ((PreparedStatement) this.statement).setBytes(paramInt, paramArrayOfByte);
    }

    @Override
    public void setCharacterStream(int paramInt, Reader paramReader) throws SQLException {
        addParam(paramInt, paramReader);
        ((PreparedStatement) this.statement).setCharacterStream(paramInt, paramReader);
    }

    @Override
    public void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
        addParam(paramInt1, paramReader, paramInt2);
        ((PreparedStatement) this.statement).setCharacterStream(paramInt1, paramReader, paramInt2);
    }

    @Override
    public void setCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
        addParam(paramInt, paramReader, paramLong);
        ((PreparedStatement) this.statement).setCharacterStream(paramInt, paramReader, paramLong);
    }

    @Override
    public void setClob(int paramInt, Clob paramClob) throws SQLException {
        addParam(paramInt, paramClob);
        ((PreparedStatement) this.statement).setClob(paramInt, paramClob);
    }

    @Override
    public void setClob(int paramInt, Reader paramReader) throws SQLException {
        addParam(paramInt, paramReader);
        ((PreparedStatement) this.statement).setClob(paramInt, paramReader);
    }

    @Override
    public void setClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
        addParam(paramInt, paramReader, paramLong);
        ((PreparedStatement) this.statement).setClob(paramInt, paramReader, paramLong);
    }

    @Override
    public void setDate(int paramInt, Date paramDate) throws SQLException {
        addParam(paramInt, paramDate);
        ((PreparedStatement) this.statement).setDate(paramInt, paramDate);
    }

    @Override
    public void setDate(int paramInt, Date paramDate, Calendar paramCalendar) throws SQLException {
        addParam(paramInt, paramDate, paramCalendar);
        ((PreparedStatement) this.statement).setDate(paramInt, paramDate, paramCalendar);
    }

    @Override
    public void setDouble(int paramInt, double paramDouble) throws SQLException {
        addParam(paramInt, paramDouble);
        ((PreparedStatement) this.statement).setDouble(paramInt, paramDouble);
    }

    @Override
    public void setFloat(int paramInt, float paramFloat) throws SQLException {
        addParam(paramInt, paramFloat);
        ((PreparedStatement) this.statement).setFloat(paramInt, paramFloat);
    }

    @Override
    public void setInt(int paramInt1, int paramInt2) throws SQLException {
        addParam(paramInt1, paramInt2);
        ((PreparedStatement) this.statement).setInt(paramInt1, paramInt2);
    }

    @Override
    public void setLong(int paramInt, long paramLong) throws SQLException {
        addParam(paramInt, paramLong);
        ((PreparedStatement) this.statement).setLong(paramInt, paramLong);
    }

    @Override
    public void setNCharacterStream(int paramInt, Reader paramReader) throws SQLException {
        addParam(paramInt, paramReader);
        ((PreparedStatement) this.statement).setNCharacterStream(paramInt, paramReader);
    }

    @Override
    public void setNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
        addParam(paramInt, paramReader, paramLong);
        ((PreparedStatement) this.statement).setNCharacterStream(paramInt, paramReader, paramLong);
    }

    @Override
    public void setNClob(int paramInt, NClob paramNClob) throws SQLException {
        addParam(paramInt, paramNClob);
        ((PreparedStatement) this.statement).setNClob(paramInt, paramNClob);
    }

    @Override
    public void setNClob(int paramInt, Reader paramReader) throws SQLException {
        addParam(paramInt, paramReader);
        ((PreparedStatement) this.statement).setNClob(paramInt, paramReader);
    }

    @Override
    public void setNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
        addParam(paramInt, paramReader, paramLong);
        ((PreparedStatement) this.statement).setNClob(paramInt, paramReader, paramLong);
    }

    @Override
    public void setNString(int paramInt, String paramString) throws SQLException {
        addParam(paramInt, paramString);
        ((PreparedStatement) this.statement).setNString(paramInt, paramString);
    }

    @Override
    public void setNull(int paramInt1, int paramInt2) throws SQLException {
        // addParam(paramInt1, g.addParam());
        ((PreparedStatement) this.statement).setNull(paramInt1, paramInt2);
    }

    @Override
    public void setNull(int paramInt1, int paramInt2, String paramString) throws SQLException {
        // addParam(paramInt1, Integer.valueOf(paramInt2), g.addParam());
        ((PreparedStatement) this.statement).setNull(paramInt1, paramInt2, paramString);
    }

    @Override
    public void setObject(int paramInt, Object paramObject) throws SQLException {
        addParam(paramInt, paramObject);
        ((PreparedStatement) this.statement).setObject(paramInt, paramObject);
    }

    @Override
    public void setObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
        addParam(paramInt1, paramObject, paramInt2);
        ((PreparedStatement) this.statement).setObject(paramInt1, paramObject, paramInt2);
    }

    @Override
    public void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3) throws SQLException {
        addParam(paramInt1, paramObject, paramInt2, paramInt3);
        ((PreparedStatement) this.statement).setObject(paramInt1, paramObject, paramInt2, paramInt3);
    }

    @Override
    public void setRef(int paramInt, Ref paramRef) throws SQLException {
        addParam(paramInt, paramRef);
        ((PreparedStatement) this.statement).setRef(paramInt, paramRef);
    }

    @Override
    public void setRowId(int paramInt, RowId paramRowId) throws SQLException {
        addParam(paramInt, paramRowId);
        ((PreparedStatement) this.statement).setRowId(paramInt, paramRowId);
    }

    @Override
    public void setSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
        addParam(paramInt, paramSQLXML);
        ((PreparedStatement) this.statement).setSQLXML(paramInt, paramSQLXML);
    }

    @Override
    public void setShort(int paramInt, short paramShort) throws SQLException {
        addParam(paramInt, paramShort);
        ((PreparedStatement) this.statement).setShort(paramInt, paramShort);
    }

    @Override
    public void setString(int paramInt, String paramString) throws SQLException {
        addParam(paramInt, paramString);
        ((PreparedStatement) this.statement).setString(paramInt, paramString);
    }

    @Override
    public void setTime(int paramInt, Time paramTime) throws SQLException {
        addParam(paramInt, paramTime);
        ((PreparedStatement) this.statement).setTime(paramInt, paramTime);
    }

    @Override
    public void setTime(int paramInt, Time paramTime, Calendar paramCalendar) throws SQLException {
        addParam(paramInt, paramTime, paramCalendar);
        ((PreparedStatement) this.statement).setTime(paramInt, paramTime);
    }

    @Override
    public void setTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
        // addParam(paramInt, new Timestamp(paramTimestamp.getTime() - paramTimestamp.getTime() % 1000));
        paramTimestamp.setNanos(0);
        addParam(paramInt, paramTimestamp);

        ((PreparedStatement) this.statement).setTimestamp(paramInt, paramTimestamp);
    }

    @Override
    public void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
        paramTimestamp.setNanos(0);
        addParam(paramInt, paramTimestamp, paramCalendar);
        ((PreparedStatement) this.statement).setTimestamp(paramInt, paramTimestamp, paramCalendar);
    }

    @Override
    public void setURL(int paramInt, URL paramURL) throws SQLException {
        addParam(paramInt, paramURL);
        ((PreparedStatement) this.statement).setURL(paramInt, paramURL);
    }

    @Override
    public void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
        addParam(paramInt1, paramInputStream, paramInt2);
        ((PreparedStatement) this.statement).setUnicodeStream(paramInt1, paramInputStream, paramInt2);
    }

}
