package com.codingapi.txlcn.client.config;

import com.codingapi.txlcn.client.CoreConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.Objects;

/**
 * Description:
 * Date: 1/19/19
 *
 * @author ujued
 */
public class EnableDTXImportSelector implements ImportSelector {

    @Override
    @NonNull
    public String[] selectImports(@NonNull AnnotationMetadata importingClassMetadata) {
        Map<String, Object> params = importingClassMetadata.getAnnotationAttributes(EnableDTX.class.getName());
        Objects.requireNonNull(params);
        if (params.get("enabled").equals(true)) {
            return new String[]{CoreConfiguration.class.getName()};
        }
        return new String[0];
    }
}
