/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.tm.support;

import com.codingapi.txlcn.tm.config.TxManagerConfig;
import org.springframework.boot.Banner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/16
 *
 * @author codingapi
 */
public class TxLcnManagerBanner implements Banner {

    public static final String VERSION = "5.0.2.RELEASE";

    private static final String BANNER =
            "          _______   __      _     _____  _   _          \n" +
            "         |_   _\\ \\ / /     | |   /  __ \\| \\ | |     \n" +
            "           | |  \\ V /______| |   | /  \\/|  \\| |      \n" +
            "           | |  /   \\______| |   | |    | . ` |        \n" +
            "           | | / /^\\ \\     | |___| \\__/\\| |\\  |    \n" +
            "           \\_/ \\/   \\/     \\_____/\\____/\\_| \\_/  \n";

    private static final String SERVER_INFO = "      TM-%s HTTP port:%s  DTX port:%s";

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream printStream) {
        String serverPortProperty = environment.getProperty("server.port");
        String managerPortProperty = environment.getProperty("tx-lcn.manager.port");
        int managerPort;
        int httpPort = 8080;
        if (serverPortProperty != null) {
            httpPort = Integer.parseInt(serverPortProperty);
        }
        if (managerPortProperty != null) {
            managerPort = Integer.parseInt(managerPortProperty);
        } else {
            managerPort = httpPort + TxManagerConfig.PORT_CHANGE_VALUE;
        }
        String string = String.format(SERVER_INFO, VERSION, httpPort, managerPort);
        printStream.println();
        printStream.println(AnsiOutput.toString(AnsiColor.GREEN, BANNER));
        printStream.println();
        printStream.println(AnsiOutput.toString(AnsiColor.GREEN, string));
        printStream.println();
    }
}
