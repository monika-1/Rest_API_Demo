package com.monika.rest_services;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

//http://localhost:8090/square?num=10
public class HitterSingleThreaded {

    public static void main(String[] args) throws IOException {

        String url = "http://localhost:8080/square";
        String param = "num";
        int hitNumberCount = 1000;
        int[] numbers = new int[hitNumberCount];
        String[] queries = new String[hitNumberCount];

        Random rand = new Random();
        for (int i=0; i<hitNumberCount; i++) {
            numbers[i] = rand.nextInt(100);
            queries[i] = param + "=" + numbers[i];
        }

        long startTime = System.currentTimeMillis();

        URLConnection connection;
        InputStream response;
        BufferedReader br;
        for (int i=0; i<hitNumberCount; i++) {
            connection = new URL(url + "?" + queries[i]).openConnection();
            response = connection.getInputStream();

            br = new BufferedReader(new InputStreamReader(response));
            System.out.println(br.readLine());
        }

        long endTime = System.currentTimeMillis();

        long totalTime = endTime - startTime;
        System.out.println("Total time: " + totalTime);

        long qps = (hitNumberCount * 1000)/totalTime;
        System.out.println("QPS: " + qps);

    }
}

//hitNumberCount = 1000
//Total time: 1313
//QPS: 761