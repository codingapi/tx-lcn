package com.codingapi.txlcn.tc.jdbc.sql.strategy.chan;

import com.codingapi.txlcn.tc.jdbc.database.TableList;
import lombok.Builder;
import lombok.Data;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;

/**
 * @author Gz.
 * @description:
 * @date 2020-09-21 23:35:06
 */
@Data
@Builder
public class FilterFacaer {

    private TableList tableList;

    private Update updateStatement;

    private Delete deleteStatement;

    private Table table;

    private ItemsList itemsList;
}
