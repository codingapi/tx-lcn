package com.codingapi.txlcn.client.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * Description:
 * Date: 1/19/19
 *
 * @author ujued
 */
public class DependenciesImportSelector implements ImportSelector {

    /**
     * spi classes
     *
     * @param importingClassMetadata
     * @return
     */
    @Override
    @NonNull
    public String[] selectImports(@NonNull AnnotationMetadata importingClassMetadata) {
        return new String[]{"com.codingapi.txlcn.spi.MessageConfiguration",
                "com.codingapi.txlcn.client.spi.SleuthConfiguration"};
    }
}
