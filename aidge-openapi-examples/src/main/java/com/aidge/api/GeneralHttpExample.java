/**
 * Copyright (C) 2024 NEURALNETICS PTE. LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aidge.api;

import com.aidge.utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GeneralHttpExample {
    public static void main(String[] args) throws IOException {
        // Your personal data
        String timestamp = System.currentTimeMillis() + "";
        String accessKeyName = "your api key name";  // e.g. 512345
        String accessKeySecret = "your api key secret";
        String apiName = "api name";  // e.g. /ai/text/translation/and/polishment
        String apiDomain = "api domain";  // e.g. api.aidc-ai.com or cn-api.aidc-ai.com
        String data = "{your api request params}"; // e.g. for translation "{\"sourceTextList\":\"[\\\"how are you\\\"]\",\"sourceLanguage\":\"en\",\"targetLanguage\":\"ko\",\"formatType\":\"text\"}"

        // Calculate sign
        StringBuilder sign = new StringBuilder();
        try {
            javax.crypto.SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(accessKeySecret.getBytes(java.nio.charset.StandardCharsets.UTF_8), "HmacSHA256");
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            byte[] bytes = mac.doFinal((accessKeySecret + timestamp).getBytes(java.nio.charset.StandardCharsets.UTF_8));
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
        url = url.replace("[api domain]", apiDomain)
                .replace("[api name]", apiName)
                .replace("[you api key name]", accessKeyName)
                .replace("[timestamp]", timestamp)
                .replace("[HmacSHA256 sign]", sign);

        // Add "x-iop-trial": "true" for trial
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // Call api
        String result = HttpUtils.doPost(url, data, headers);
        System.out.println(result);
    }
}
