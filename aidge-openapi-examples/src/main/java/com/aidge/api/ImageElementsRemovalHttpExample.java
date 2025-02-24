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

public class ImageElementsRemovalHttpExample {

    public static void main(String[] args) {
        try {
            // Your personal data. In this sample, we use environment variable to get access key and secret.
            String accessKeyName = System.getenv("accessKey");  // e.g. 512345
            String accessKeySecret = System.getenv("secret");

            // Call api
            String apiName = "/ai/image/removal";
            String apiDomain = "api.aidc-ai.com";  // for api purchased on global site
            // String apiDomain = "cn-api.aidc-ai.com";  // 中文站购买的API请使用此域名 (for api purchased on chinese site)
            String apiRequest = "{\"image_url\":\"https://ae01.alicdn.com/kf/Sa78257f1d9a34dad8ee494178db12ec8l.jpg\",\"non_object_remove_elements\":\"[1,2,3,4]\",\"object_remove_elements\":\"[1,2,3,4]\",\"mask\":\"474556 160 475356 160 476156 160 476956 160 477756 160 478556 160 479356 160 480156 160 480956 160 481756 160 482556 160 483356 160 484156 160 484956 160 485756 160 486556 160 487356 160 488156 160 488956 160 489756 160 490556 160 491356 160 492156  160\"}";
            String apiResponse = invokeApi(accessKeyName, accessKeySecret, apiName, apiDomain, apiRequest);

            // Final result
            System.out.println(apiResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String invokeApi(String accessKeyName, String accessKeySecret, String apiName, String apiDomain, String data) throws IOException {
        String timestamp = System.currentTimeMillis() + "";

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

        String url = "https://[api domain]/rest[api name]?partner_id=aidge&sign_method=sha256&sign_ver=v2&app_key=[you api key name]&timestamp=[timestamp]&sign=[HmacSHA256 sign]";
        url = url.replace("[api domain]", apiDomain)
                .replace("[api name]", apiName)
                .replace("[you api key name]", accessKeyName)
                .replace("[timestamp]", timestamp)
                .replace("[HmacSHA256 sign]", sign);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        // Add "x-iop-trial": "true" for trial
        // headers.put("x-iop-trial", "true");


        // Call api
        String result = HttpUtils.doPost(url, data, headers);
        System.out.println(result);
        return result;
    }
}
