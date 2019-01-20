package com.codingapi.txlcn.logger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Description:
 * Date: 19-1-17 下午2:47
 *
 * @author ujued
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopTime implements Field {

    private String stopTime;

    @Override
    public boolean ok() {
        return StringUtils.hasText(stopTime);
    }
}
