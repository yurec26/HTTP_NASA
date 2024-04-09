package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {


    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        //
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=3dAZdUecFhFumwcofF2y1L2XOH1S7K5IcqOE76q9");
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        //
        CloseableHttpResponse response = httpClient.execute(request);
        //
        ObjectMapper objectMapper = new ObjectMapper();
        //
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String url = jsonNode.get("url").asText();
        //
        HttpGet imageRequest = new HttpGet(url);
        CloseableHttpResponse imageResponse = httpClient.execute(imageRequest);
        InputStream imageContent = imageResponse.getEntity().getContent();
        //
        byte[] imageData = EntityUtils.toByteArray(imageResponse.getEntity());
        //
        String fileName = "C://Users//yuras//Desktop//downloaded_image.jpg";
        Path filePath = Paths.get(fileName);
        //
        Files.write(filePath, imageData);
        //
        imageContent.close();
        imageResponse.close();
    }
}
