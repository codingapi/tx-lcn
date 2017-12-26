/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.codingapi.tx.framework.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * create by lorne on 2017/12/12
 */
public class IpAddressUtils {


    public static boolean isIpAddress(String ipAddress){
        String ipAddressRegex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}:([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5][0-9][0-3][0-5])";
        Pattern ipAddressPattern = Pattern.compile(ipAddressRegex);
        Matcher matcher = ipAddressPattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static boolean isIpFormat(String ipAddress){
        return ipAddress.contains(":");
    }

    public static String getIpByDomain(String domain){
        InetAddress ip= null;
        try {
            ip = InetAddress.getByName(domain);
        } catch (UnknownHostException e) {
            return null;
        }
        return ip.getHostAddress();
    }


    public static boolean isIp(String ipString) {
        String ipRegex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ipRegex);
        Matcher matcher = pattern.matcher(ipString);
        return matcher.matches();
    }


}
