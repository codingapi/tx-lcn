package com.codingapi.txlcn.tc.config;

import com.codingapi.txlcn.protocol.config.Config;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tx config properties
 */
@Data
@Slf4j
@Accessors(chain = true)
@NoArgsConstructor
public class TxConfig {

  /**
   * 数据库类型
   */
  private String sqlType;

  /**
   * 事务最大等待时间 单位:毫秒
   */
  private int maxWaitTransactionTime = 1000;

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

  private List<InetSocketAddress> iNetSocketAddresses;

  /**
   * 用户自行配置需要连接的 TM 数量
   */
  private int tmResource = 1;

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
