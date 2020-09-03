package com.codingapi.txlcn.tc.config;

import com.codingapi.txlcn.protocol.config.Config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tx config properties
 */
@SuppressWarnings("rawtypes")
@Data
@Slf4j
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

  @Autowired
  private RedisTemplate redisTemplate;

  private static final String SNOWFLAKE_REDIS_KEY = "SnowflakeRedisKey*";

  public TxConfig(Config protocol) {
    this.protocol = protocol;
  }

  public TxConfig(Config protocol, RedisTemplate redisTemplate) {
    this.protocol = protocol;
    this.redisTemplate = redisTemplate;
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

  @SuppressWarnings("unchecked")
  public List<InetSocketAddress> txManagerAddresses() {
    List<InetSocketAddress> addresses = new ArrayList<>();
    Set tmKeys = redisTemplate.keys(SNOWFLAKE_REDIS_KEY);
    if (!CollectionUtils.isEmpty(tmKeys)) {
      tmKeys.forEach(
              tmKey -> Optional.ofNullable(addressFormat(String.valueOf(redisTemplate.opsForValue().get(tmKey))))
                      .ifPresent(addresses::add)
      );
    }
    return addresses;
  }


}
