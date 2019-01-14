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
package com.codingapi.txlcn.jdbcproxy.p6spy.wrapper;

import java.sql.SQLException;
import java.sql.Wrapper;

public abstract class AbstractWrapper implements Wrapper, P6Proxy {

  private final Object delegate;

  protected AbstractWrapper(Object delegate) {
    this.delegate = delegate;
  }

  /**
   * Used to determine is a given object is a Proxy created by this proxy factory.
   *
   * @param obj the object in question
   * @return true if it is a proxy - false otherwise
   */
  public static boolean isProxy(final Object obj) {
    return (obj != null && isProxy(obj.getClass()));
  }

  /**
   * Used to determine if the given class is a proxy class.
   *
   * @param clazz the class in question
   * @return true if proxy - false otherwise
   */
  public static boolean isProxy(final Class<?> clazz) {
    return (clazz != null && P6Proxy.class.isAssignableFrom(clazz));
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    final Object result;
    if (iface.isAssignableFrom(getClass())) {
      // if the proxy directly implements the interface or extends it, return the proxy
      result = this;
    } else if (iface.isAssignableFrom(delegate.getClass())) {
      // if the proxied object directly implements the interface or extends it, return
      // the proxied object
      result = unwrapP6SpyProxy();
    } else if (Wrapper.class.isAssignableFrom(delegate.getClass())) {
      // if the proxied object implements the wrapper interface, then
      // return the result of it's unwrap method.
      result = ((Wrapper) unwrapP6SpyProxy()).unwrap(iface);
    } else {
      /*
         This line of code can only be reached when the underlying object does not implement the wrapper
         interface.  This would mean that either the JDBC driver or the wrapper of the underlying object
         does not implement the JDBC 4.0 API.
       */
      throw new SQLException("Can not unwrap to " + iface.getName());
    }
    return iface.cast(result);
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    if (iface.isAssignableFrom(getClass())) {
      // if the proxy directly proxy the interface or extends it, return true
      return true;
    } else if (iface.isAssignableFrom(delegate.getClass())) {
      // if the proxied object directly implements the interface or extends it, return true
      return true;
    } else if (Wrapper.class.isAssignableFrom(delegate.getClass())) {
      // if the proxied object implements the wrapper interface, then
      // return the result of it's isWrapperFor method.
      return ((Wrapper) unwrapP6SpyProxy()).isWrapperFor(iface);
    }
    return false;
  }

  @Override
  public boolean equals(Object obj) {
    // If the object to pass to the equals method is another P6Spy proxy, then unwrap it first.
    if (obj instanceof P6Proxy) {
      obj = ((P6Proxy) obj).unwrapP6SpyProxy();
    }
    return delegate.equals(obj);
  }

  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  @Override
  public Object unwrapP6SpyProxy() {
    return delegate;
  }
}
