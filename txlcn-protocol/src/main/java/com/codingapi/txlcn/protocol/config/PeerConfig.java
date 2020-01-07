package com.codingapi.txlcn.protocol.config;

import lombok.Data;

@Data
public class PeerConfig {

  /**
   * peer name
   */
  private String name;

  /**
   * peer port
   */
  private int port;

}
