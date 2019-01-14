/**
 * P6Spy
 *
 * Copyright (C) 2002 - 2018 P6Spy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.jdbcproxy.p6spy.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores information about the prepared statement and bind variables.
 *
 * @author Quinton McCombs
 * @since 09/2013
 */
public class PreparedStatementInformation extends StatementInformation implements Loggable {
  private final Map<Integer, Value> parameterValues = new HashMap<Integer, Value>();

  public PreparedStatementInformation(final ConnectionInformation connectionInformation, String query) {
    super(connectionInformation);
    setStatementQuery(query);
  }

  /**
   * Generates the query for the prepared statement with all parameter placeholders
   * replaced with the actual parameter values
   *
   * @return the SQL
   */
  @Override
  public String getSqlWithValues() {
    final StringBuilder sb = new StringBuilder();
    final String statementQuery = getStatementQuery();

    // iterate over the characters in the query replacing the parameter placeholders
    // with the actual values
    int currentParameter = 0;
    for( int pos = 0; pos < statementQuery.length(); pos ++) {
      char character = statementQuery.charAt(pos);
      if( statementQuery.charAt(pos) == '?' && currentParameter <= parameterValues.size()) {
        // replace with parameter value
        Value value = parameterValues.get(currentParameter);
        sb.append(value != null ? value.toString() : new Value().toString());
        currentParameter++;
      } else {
        sb.append(character);
      }
    }

    return sb.toString();
  }

  /**
   * Records the value of a parameter.
   * @param position the position of the parameter (starts with 1 not 0)
   * @param value the value of the parameter
   */
  public void setParameterValue(final int position, final Object value) {
    parameterValues.put(position - 1, new Value(value));
  }

  protected Map<Integer, Value> getParameterValues() {
    return parameterValues;
  }

}
