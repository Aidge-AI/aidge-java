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
import org.json.JSONObject;



public class ImageTranslationHttpExample {

    static class ApiConfig{
        /**
         * The name and secret of your api key. e.g. 512345 and S4etzZ73nF08vOXVhk3wZjIaLSHw0123
         * In this sample, we use environment variable to get access key and secret.
         */
        public static String accessKeyName = System.getenv("accessKey");
        public static String accessKeySecret = System.getenv("secret");

        /**
         * The domain of the API.
         * for api purchased on global site. set apiDomain to "api.aidc-ai.com"
         * 中文站购买的API请使用"cn-api.aidc-ai.com"域名 (for api purchased on chinese site) set apiDomain to "cn-api.aidc-ai.com"
         */
        public static String apiDomain = "api domain";

        /**
         * We offer trial quota to help you familiarize and test how to use the Aidge API in your account
         * To use trial quota, please set useTrialResource to true
         * If you set useTrialResource to false before you purchase the API
         * You will receive "Sorry, your calling resources have been exhausted........"
         * 我们为您的账号提供一定数量的免费试用额度可以试用任何API。请将useTrialResource设置为true用于试用。
         * 如设置为false，且您未购买该API，将会收到"Sorry, your calling resources have been exhausted........."的错误提示
         */
        public static boolean useTrialResource = true/false;
    }

    public static void main(String[] args) {
        try {
            // Call api
            String apiName = "/ai/image/translation";

            /*
             * Create API request using JSONObject
             * You can use any other json library to build parameters
             * Note: the array type parameter needs to be converted to a string
             */
            JSONObject apiRequestJson = new JSONObject();
            apiRequestJson.put("imageUrl", "https://ae01.alicdn.com/kf/S68468a838ad04cc081a4bd2db32745f1y/M3-Light-emitting-Bluetooth-Headset-Folding-LED-Card-Wireless-Headset-TYPE-C-Charging-Multi-scene-Use.jpg_.webp");
            apiRequestJson.put("sourceLanguage", "en");
            apiRequestJson.put("targetLanguage", "fr");
            apiRequestJson.put("translatingTextInTheProduct", "false");
            apiRequestJson.put("useImageEditor", "false");

            String apiRequest = apiRequestJson.toString();

            // String apiRequest = "{\"imageUrl\":\"https://ae01.alicdn.com/kf/S68468a838ad04cc081a4bd2db32745f1y/M3-Light-emitting-Bluetooth-Headset-Folding-LED-Card-Wireless-Headset-TYPE-C-Charging-Multi-scene-Use.jpg_.webp\",\"sourceLanguage\":\"en\",\"targetLanguage\":\"fr\",\"translatingTextInTheProduct\":\"false\",\"useImageEditor\":\"false\"}";
            String apiResponse = invokeApi(apiName, apiRequest);

            // Final result
            System.out.println(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String invokeApi(String apiName, String data) throws IOException {
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
        return result;
    }
}
