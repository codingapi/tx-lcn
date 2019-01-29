package com.codingapi.txlcn.logger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * Description:
 * Date: 19-1-17 下午2:46
 *
 * @author ujued
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag implements Field {

    private String tag;

    @Override
    public boolean ok() {
        return StringUtils.hasText(tag);
    }
}
