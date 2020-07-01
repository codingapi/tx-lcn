package com.codingapi.txlcn.tc.config;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codingapi.maven.uml.annotation.Model;
import com.codingapi.txlcn.protocol.config.Config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Tx config properties
 */
@Data
@Slf4j
@Model(flag = "C",value = "TC模块配置信息",color = "#FF88EE")
public class TxConfig {

  /**
   * 事务切面
   */
  private String transactionPointcut = "@annotation(com.codingapi.txlcn.tc.annotation.LcnTransaction)";

  /**
   * jdbc切面
   */
  private String datasourcePointcut = "execution(* javax.sql.DataSource.getConnection(..))";

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
  private InetSocketAddress addressFormat(String address) {
    Pattern p = Pattern.compile("^\\s*(.*?):(\\d+)\\s*$");
    Matcher m = p.matcher(address);
    if (m.matches()) {
      String host = m.group(1);
      int port = Integer.parseInt(m.group(2));
      return new InetSocketAddress(host, port);
    }
    return null;
  }

  public List<InetSocketAddress> txManagerAddresses() {
    log.info("TM servers:{}", tms);
    List<InetSocketAddress> addresses = new ArrayList<>();
    if (tms != null) {
      for (String item : tms) {
        Optional<InetSocketAddress> optional = Optional.ofNullable(addressFormat(item));
        optional.ifPresent(addresses::add);
      }
    }
    return addresses;
  }


}
