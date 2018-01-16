package com.codingapi.tm.framework.utils;

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
