package com.codingapi.txlcn.tc.runner;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.tc.config.TxConfig;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class TMServerRunner {

  private TxConfig txConfig;

  private ProtocolServer protocolServer;

  public void init() {
    List<String> list = txConfig.getTms();
    log.info("TM servers:{}", list);
    if (list != null) {
      for (String item : list) {
        Optional<InetSocketAddress> optional = Optional.of(txConfig.addressFormat(item));
        optional.ifPresent(address -> protocolServer
            .connectTo(address.getHostString(), address.getPort()));
      }
    }
  }
}
