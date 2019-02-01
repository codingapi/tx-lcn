package com.codingapi.txlcn.common.util;

import java.util.Map;
import java.util.Optional;

/**
 * Description:
 * Date: 2/1/19
 *
 * @author ujued
 */
public abstract class Strings {

    public static String format(String input, Map<String, Object> params, Object... args) {
        StringBuilder varString = new StringBuilder();
        StringBuilder finalString = new StringBuilder();
        int varMaxLen = 5;
        int curVarLen = 0;
        boolean wait = false;
        int argIndex = -1;
        char startChar = '%';
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '{' || c == '%') {
                wait = true;
                startChar = c;
                continue;
            }
            if (wait) {
                if (c == '}' || (startChar == '%' && (c == 's' || c == 'd'))) {
                    if (varString.length() > 0) {
                        finalString.append(Optional.ofNullable(params.get(varString.toString())).orElse(startChar + c));
                        curVarLen = 0;
                        varString.delete(0, varString.length());
                    } else if (++argIndex >= args.length) {
                        finalString.append(startChar).append(c);
                    } else {
                        finalString.append(args[argIndex]);
                    }
                } else if (curVarLen < varMaxLen) {
                    varString.append(c);
                    curVarLen++;
                    continue;
                } else {
                    finalString.append(startChar).append(varString);
                    curVarLen = 0;
                    varString.delete(0, varString.length());
                }
                wait = false;
                continue;
            }
            finalString.append(c);
        }
        return finalString.toString();
    }

    public static void main(String[] args) {
        String s1 = "hello, {}. {} is in {}";
        System.out.println(format(s1, Maps.of("who", "ujued"), "world", "jinan"));
    }
}
