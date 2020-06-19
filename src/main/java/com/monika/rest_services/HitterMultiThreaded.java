package com.monika.rest_services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HitterMultiThreaded {
    public static void main(String[] args) throws InterruptedException {
        String url = "http://localhost:8090/square";
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

        ExecutorService executor = Executors.newFixedThreadPool(100);

        for (int i=0; i<hitNumberCount; i++) {
            executor.submit(new Runner(url, queries[i]));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        long endTime = System.currentTimeMillis();

        long totalTime = endTime - startTime;
        System.out.println("Total time: " + totalTime);

        long qps = (hitNumberCount * 1000)/totalTime;
        System.out.println("QPS: " + qps);
    }


    static class Runner implements Runnable {

        URLConnection connection;
        InputStream response;
        BufferedReader br;
        String url;
        String query;

        Runner(String url, String query) {
            this.url = url;
            this.query = query;
        }

        @Override
        public void run() {
            try {
                connection = new URL(url + "?" + query).openConnection();
                response = connection.getInputStream();

                br = new BufferedReader(new InputStreamReader(response));
                System.out.println(br.readLine());
            } catch (Exception e) {
                System.out.println("Exception:" + e);
            }
        }
    }
}

//hitNumberCount = 1000
//noOfThreads = 1000
//Total time: 1247
//QPS: 801

//hitNumberCount = 1000
//noOfThreads = 100
//Total time: 825
//QPS: 1212

//hitNumberCount = 1000
//noOfThreads = 10
//Total time: 732
//QPS: 1366

//hitNumberCount = 1000
//noOfThreads = 5
//Total time: 669
//QPS: 1494

//hitNumberCount = 1000
//noOfThreads = 2
//Total time: 1002
//QPS: 998


