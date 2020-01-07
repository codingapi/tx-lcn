package com.codingapi.txlcn.tc.config;

import com.codingapi.txlcn.protocol.Config;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;

/**
 * Tx config properties
 */
@Data
public class TxConfig {

  /**
   * application name
   */
  private String applicationName;

  /**
   * TM server host:port for example:127.0.0.1:8070,127.0.0.1:8071
   */
  private List<String> tms;

  /**
   * peer network setting.
   */
  private Config protocol;

  public TxConfig(Config protocol) {
    this.protocol = protocol;
  }

  /**
   * address to InetSocketAddress
   *
   * @param address ip:port
   * @return InetSocketAddress
   */
  public InetSocketAddress addressFormat(String address) {
    Pattern p = Pattern.compile("^\\s*(.*?):(\\d+)\\s*$");
    Matcher m = p.matcher(address);
    if (m.matches()) {
      String host = m.group(1);
      int port = Integer.parseInt(m.group(2));
      return new InetSocketAddress(host, port);
    }
    return null;
  }

}
