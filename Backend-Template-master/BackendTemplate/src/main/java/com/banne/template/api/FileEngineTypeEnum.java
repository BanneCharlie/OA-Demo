package com.banne.template.api;

import lombok.Getter;

@Getter
public enum FileEngineTypeEnum {
        /** 本地 */
        LOCAL("LOCAL"),

        /** 阿里云 */
        ALIYUN("ALIYUN"),

        /** 腾讯云 */
        TENCENT("TENCENT"),

        /** MINIO */
        MINIO("MINIO");

        private final String value;

        FileEngineTypeEnum(String value) {
            this.value = value;
        }
}
