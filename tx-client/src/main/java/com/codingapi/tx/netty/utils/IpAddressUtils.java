package com.codingapi.tx.netty.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * create by lorne on 2017/12/12
 */
public class IpAddressUtils {

    private final static String ipAddressRegex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}:(\\d{1,5})";

    private final static Pattern pattern = Pattern.compile(ipAddressRegex);

    public static boolean isIpAddress(String ipAddress){
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

}
