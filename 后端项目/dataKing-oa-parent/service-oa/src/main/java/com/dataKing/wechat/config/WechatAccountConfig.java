package com.dataKing.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ClassName: WechatAccountConfig
 * Package: com.dataKing.wechat.config
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/22 0022 11:40
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {

    private String mpAppId;

    private String mpAppSecret;

}
