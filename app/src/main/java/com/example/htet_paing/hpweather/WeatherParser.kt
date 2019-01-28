package com.example.htet_paing.hpweather

import com.example.htet_paing.hpweather.model.WeatherResponse
import com.squareup.moshi.Moshi

object WeatherParser{

    @JvmStatic
    fun main(args: Array<String>) {
        val jsonString = "{\n" +
                "\n" +
                "    \"coord\": {\n" +
                "        \"lon\": -0.13,\n" +
                "        \"lat\": 51.51\n" +
                "    },\n" +
                "    \"weather\": [\n" +
                "        {\n" +
                "            \"id\": 300,\n" +
                "            \"main\": \"Drizzle\",\n" +
                "            \"description\": \"light intensity drizzle\",\n" +
                "            \"icon\": \"09d\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"base\": \"stations\",\n" +
                "    \"main\": {\n" +
                "        \"temp\": 280.32,\n" +
                "        \"pressure\": 1012,\n" +
                "        \"humidity\": 81,\n" +
                "        \"temp_min\": 279.15,\n" +
                "        \"temp_max\": 281.15\n" +
                "    },\n" +
                "    \"visibility\": 10000,\n" +
                "    \"wind\": {\n" +
                "        \"speed\": 4.1,\n" +
                "        \"deg\": 80\n" +
                "    },\n" +
                "    \"clouds\": {\n" +
                "        \"all\": 90\n" +
                "    },\n" +
                "    \"dt\": 1485789600,\n" +
                "    \"sys\": {\n" +
                "        \"type\": 1,\n" +
                "        \"id\": 5091,\n" +
                "        \"message\": 0.0103,\n" +
                "        \"country\": \"GB\",\n" +
                "        \"sunrise\": 1485762037,\n" +
                "        \"sunset\": 1485794875\n" +
                "    },\n" +
                "    \"id\": 2643743,\n" +
                "    \"name\": \"London\",\n" +
                "    \"cod\": 200\n" +
                "\n" +
                "}"

        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(WeatherResponse::class.java)
        val response = jsonAdapter.fromJson(jsonString)
        print(response)
    }
}