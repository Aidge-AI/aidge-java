English | [简体中文](./README-CN.md)

<p align="center">

<h1 align="center">Aidge API Examples for Java</h1>

The Aidge API examples for java provide you  to access Aidge services such as Text Translation.

## Requirements

- To run the examples, you must have an Aidge API account as well as an `API Key Name` and an `API Key Secret`. Create and view your AccessKey on Aidge dashboard.
- To use the Aidge API examples for Java to access the APIs of a product, you must first activate the product on the [Aidge console](https://www.aidge.com) if required.
- The Aidge API examples require JDK 1.8 or later.

## Quick Examples

The following code example:

```java
package com.aidge.api;

import com.aidge.utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GeneralHttpExample {

    static class ApiConfig{
        /**
         * The name and secret of your api key. e.g. 512345 and S4etzZ73nF08vOXVhk3wZjIaLSHw0123
         */
        public static String accessKeyName = "your api key name";
        public static String accessKeySecret = "your api key secret";

        /**
         * The domain of the API.
         * for api purchased on global site. set apiDomain to "api.aidc-ai.com"
         * 中文站购买的API请使用"cn-api.aidc-ai.com"域名 (for api purchased on chinese site) set apiDomain to "cn-api.aidc-ai.com"
         */
        public static String apiDomain = "api.aidc-ai.com";
        // public static String apiDomain = "cn-api.aidc-ai.com";

        /**
         * We offer trial quota to help you familiarize and test how to use the Aidge API in your account
         * To use trial quota, please set useTrialResource to true
         * If you set useTrialResource to false before you purchase the API
         * You will receive "Sorry, your calling resources have been exhausted........"
         * 我们为您的账号提供一定数量的免费试用额度可以试用任何API。请将useTrialResource设置为true用于试用。
         * 如设置为false，且您未购买该API，将会收到"Sorry, your calling resources have been exhausted........."的错误提示
         */
        public static boolean useTrialResource = false;
        // public static boolean useTrialResource = true;
    }

    public static void main(String[] args) throws IOException {
        // Your api request data
        String apiName = "api name"; // e.g. /ai/text/translation/and/polishment
        String data = "{your api request params}"; // e.g. for translation "{\"sourceTextList\":\"[\\\"how are you\\\"]\",\"sourceLanguage\":\"en\",\"targetLanguage\":\"ko\",\"formatType\":\"text\"}"
        String timestamp = System.currentTimeMillis() + "";

        // Calculate sign
        StringBuilder sign = new StringBuilder();
        try {
            javax.crypto.SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(ApiConfig.accessKeySecret.getBytes(java.nio.charset.StandardCharsets.UTF_8), "HmacSHA256");
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            byte[] bytes = mac.doFinal((ApiConfig.accessKeySecret + timestamp).getBytes(java.nio.charset.StandardCharsets.UTF_8));
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(bytes[i] & 0xFF);
                if (hex.length() == 1) {
                    sign.append("0");
                }
                sign.append(hex.toUpperCase());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Replace url with your real data
        String url = "https://[api domain]/rest[api name]?partner_id=aidge&sign_method=sha256&sign_ver=v2&app_key=[you api key name]&timestamp=[timestamp]&sign=[HmacSHA256 sign]";
        url = url.replace("[api domain]", ApiConfig.apiDomain)
                .replace("[api name]", apiName)
                .replace("[you api key name]", ApiConfig.accessKeyName)
                .replace("[timestamp]", timestamp)
                .replace("[HmacSHA256 sign]", sign);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        if (ApiConfig.useTrialResource) {
            // Add "x-iop-trial": "true" for trial
            headers.put("x-iop-trial", "true");
        }

        // Call api
        String result = HttpUtils.doPost(url, data, headers);
        System.out.println(result);
    }
}

```

> For security reason, we don't recommend to hard code credentials information in source code. You should access
> credentials from external configurations or environment variables.

## Changelog

Detailed changes for each release are documented in the [release notes](./ChangeLog.txt).


## References

- [Aidge Home Page](https://www.aidge.com/)

## License

This project is licensed under [Apache License Version 2](./LICENSE-2.0.txt) (SPDX-License-identifier: Apache-2.0).