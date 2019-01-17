package com.codingapi.txlcn.logger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Description:
 * Date: 19-1-17 下午2:46
 *
 * @author ujued
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartTime implements Field {
    private String startTime;

    @Override
    public boolean ok() {
        return StringUtils.hasText(startTime);
    }
}
