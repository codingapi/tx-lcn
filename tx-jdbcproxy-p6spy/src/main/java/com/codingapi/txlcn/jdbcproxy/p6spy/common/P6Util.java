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

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class P6Util {
    static Pattern lineBreakPattern = Pattern.compile("(\\r?\\n)+");
    public static String singleLine(String str) {
        return lineBreakPattern.matcher(str).replaceAll(" ");
    }

    private P6Util() {
      // preventing instantiation of the util class
    }

    public static int parseInt(String i, int defaultValue) {
        if (i == null || i.isEmpty()) {
            return defaultValue;
        }
        try {
            return (Integer.parseInt(i));
        }
        catch(NumberFormatException nfe) {
//            P6LogQuery.error("NumberFormatException occured parsing value "+i);
            return defaultValue;
        }
    }

    public static boolean isTrue(String s, boolean defaultValue) {
        if (s == null) {
            return defaultValue;
        }
        return("1".equals(s) || "true".equalsIgnoreCase(s.trim()));
    }

  /**
   * Locates a file on the file system or on the classpath.  
   * <p>
   * Search order:
   * <ol>
   *   <li>current working directory (for relative filePath) or any directory (for absolute filePath)</li>
   *   <li>class filePath</li>
   * </ol>
   * 
   * @param file the relative filePath of the file to locate
   * @return A URL to the file or null if not found
   */
  public static URL locateFile(String file) {
    File fp;
    URL result = null;

    try {
      // try to find relative to current working directory first
      fp = new File(file);
      if (fp.exists()) {
        result = fp.toURI().toURL();
      }

      // next try to load from context class loader
      if (result == null) {
        result = locateOnClassPath(file);
      }
    } catch (Exception e) {
    }

    return result;

  }

  /**
   * Locates a file on the classpath.
   * 
   * @param filename the relative filePath of the file to load
   * @return the URL of the file or null if not found
   */
  public static URL locateOnClassPath(String filename) {
    URL result;
    // first try to load from context class loader
    result = Thread.currentThread().getContextClassLoader().getResource(filename);

    // next try the current class loader which loaded p6spy
    if (result == null) {
      result = P6Util.class.getClassLoader().getResource(filename);
    }

    // finally try the system class loader
    if (result == null) {
      result = ClassLoader.getSystemResource(filename);
    }

    return result;
  }

    /**
     * A utility for using the current class loader (rather than the
     * system class loader) when instantiating a new class.
     * <p>
     * The idea is that the thread's current loader might have an
     * obscure notion of what your class filePath is (e.g. an app server) that
     * will not be captured properly by the system class loader.
     * <p>
     * taken from http://sourceforge.net/forum/message.php?msg_id=1720229
     *
     * @param name class name to load
     * @return the newly loaded class
     * @throws  ClassNotFoundException ClassNotFoundException
     */
    public static Class<?> forName(String name) throws ClassNotFoundException {
        ClassLoader ctxLoader = null;
        try {
            ctxLoader = Thread.currentThread().getContextClassLoader();
            return Class.forName(name, true, ctxLoader);

        } catch(ClassNotFoundException ex) {
            // try to fall through and use the default
            // Class.forName
            //if(ctxLoader == null) { throw ex; }
        } catch(SecurityException ex) {
        }
        return Class.forName(name);
    }

    public static String getPath(URL theURL) {
     	String file = theURL.getFile();
     	String path = null;
     	if (file != null) {
			int q = file.lastIndexOf('?');
	       	if (q != -1) {
	         path = file.substring(0, q);
			} else {
	       		path = file;
	     	}
   		}
     	return path;
     }
    
	/**
	 * @param properties
	 *            to be converted to {@link Map}.
	 * @return {@link Map} of the properties. Please note, that properties that
	 *         have no value specified are contained in a result map, but with
	 *         value equal to "" (empty {@link String}).
	 */
	public static Map<String, String> getPropertiesMap(final Properties properties) {
		if (null == properties) {
			return null;
		}

		final Map<String, String> map = new HashMap<String, String>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			map.put((String) entry.getKey(), (String) entry.getValue());
		}
		return map;
	}
    
    public static List<String> parseCSVList(String csv) {
      if (csv == null || csv.isEmpty()) {
    	  return Collections.emptyList();
      }
      
      return new ArrayList<String>(Arrays.asList(csv.split(",")));
    }

    public static Properties getProperties(Map<String, String> map) {
      if (map == null) {
        return null;
      }
      
      final Properties properties = new Properties();
      properties.putAll(map);
      return properties;
    }
    
  /**
   * @param collection
   *          to be joined elements.
   * @param separator
   *          used in join.
   * @return {@code collections} elements joined via {@code separator}.
   */
  public static String joinNullSafe(Collection<String> collection, String separator) {
    if (null == collection || collection.isEmpty()) {
      return "";
    }

    if (null == separator) {
      separator = "";
    }

    final StringBuilder sb = new StringBuilder();
    for (String str : collection) {
      if (sb.length() > 0) {
        sb.append(separator);
      }
      sb.append(str);
    }
    return sb.toString();
  }

}

