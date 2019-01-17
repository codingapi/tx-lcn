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
public class GroupId implements Field {
    private String groupId;

    @Override
    public boolean ok() {
        return !StringUtils.isEmpty(groupId);
    }
}
