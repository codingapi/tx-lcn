package com.codingapi.txlcn.tm.util;


import com.codingapi.txlcn.tm.exception.UtilException;

import java.net.*;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020-9-1 22:36:41
 */
public class NetUtil {

    private final static Pattern PATTERN = Pattern.compile("^\\s*(.*?):(\\d+)\\s*$");

    /**
     * 获取所有满足过滤条件的本地IP地址对象
     *
     * @param addressFilter 过滤器，null表示不过滤，获取所有地址
     * @return 过滤后的地址对象列表
     */
    public static LinkedHashSet<InetAddress> localAddressList(Filter<InetAddress> addressFilter) {
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new UtilException(e);
        }

        if (networkInterfaces == null) {
            throw new UtilException("Get network interface error!");
        }

        final LinkedHashSet<InetAddress> ipSet = new LinkedHashSet<>();

        while (networkInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = networkInterfaces.nextElement();
            final Enumeration<InetAddress> iNetAddresses = networkInterface.getInetAddresses();
            while (iNetAddresses.hasMoreElements()) {
                final InetAddress inetAddress = iNetAddresses.nextElement();
                if (inetAddress != null && (null == addressFilter || addressFilter.accept(inetAddress))) {
                    ipSet.add(inetAddress);
                }
            }
        }

        return ipSet;
    }

    public static InetAddress getLocalhost() {
        final LinkedHashSet<InetAddress> localAddressList = localAddressList(address -> {
            // 非loopback地址，指127.*.*.*的地址
            return !address.isLoopbackAddress()
                    // 非地区本地地址，指10.0.0.0 ~ 10.255.255.255、172.16.0.0 ~ 172.31.255.255、192.168.0.0 ~ 192.168.255.255
                    && !address.isSiteLocalAddress()
                    // 需为IPV4地址
                    && address instanceof Inet4Address;
        });

        if (CollUtil.isNotEmpty(localAddressList)) {
            return CollUtil.get(localAddressList, 0);
        }

        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            // ignore
        }

        return null;
    }


    /**
     * address to INetSocketAddress
     *
     * @param address ip:port
     * @return INetSocketAddress
     */
    public static InetSocketAddress addressFormat(String address) {
        Matcher m = PATTERN.matcher(address);
        if (m.matches()) {
            String host = m.group(1);
            int port = Integer.parseInt(m.group(2));
            return new InetSocketAddress(host, port);
        }
        return null;
    }

}
