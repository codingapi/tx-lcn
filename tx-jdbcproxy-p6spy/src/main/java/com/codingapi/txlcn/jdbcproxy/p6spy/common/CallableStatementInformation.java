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
 * Stores information about the callable statement and bind variables.
 *
 * @author Quinton McCombs
 * @since 09/2013
 */
public class CallableStatementInformation extends PreparedStatementInformation {
  private final Map<String, Value> namedParameterValues = new HashMap<String, Value>();

  public CallableStatementInformation(ConnectionInformation connectionInformation, String query) {
    super(connectionInformation, query);
  }

  /**
   * Generates the query for the callable statement with all parameter placeholders
   * replaced with the actual parameter values
   *
   * @return the SQL
   */
  @Override
  public String getSqlWithValues() {

    if( namedParameterValues.size() == 0 ) {
      return super.getSqlWithValues();
    }

    /*
      If named parameters were used, it is no longer possible to simply replace the placeholders in the
      original statement with the values of the bind variables.  The only way it could be done is if the names
      could be resolved by to the ordinal positions which is not possible on all databases.

      New log format:  <original statement> name:value, name:value

      Example: {? = call test_proc(?,?)} param1:value1, param2:value2, param3:value3

      In the event that ordinal and named parameters are both used, the original position will be used as the name.
      Example:  {? = call test_proc(?,?)} 1:value1, 3:value3, param2:value2
    */

    final StringBuilder result = new StringBuilder();
    final String statementQuery = getStatementQuery();

    // first append the original statement
    result.append(statementQuery);
    result.append(" ");

    StringBuilder parameters = new StringBuilder();

    // add parameters set with ordinal positions
    for( Map.Entry<Integer, Value> entry : getParameterValues().entrySet() ) {
      appendParameter(parameters, entry.getKey().toString(), entry.getValue());
    }

    // add named parameters
    for( Map.Entry<String, Value> entry : namedParameterValues.entrySet() ) {
      appendParameter(parameters, entry.getKey(), entry.getValue());
    }


    result.append(parameters);

    return result.toString();
  }

  private void appendParameter(StringBuilder parameters, String name, Value value) {
    if( parameters.length() > 0 ) {
      parameters.append(", ");
    }

    parameters.append(name);
    parameters.append(":");
    parameters.append(value != null ? value.toString() : new Value().toString());
  }

  /**
   * Records the value of a parameter.
   *
   * @param name the name of the parameter
   * @param value the value of the parameter
   */
  public void setParameterValue(final String name, final Object value) {
    namedParameterValues.put(name, new Value(value));
  }
}
