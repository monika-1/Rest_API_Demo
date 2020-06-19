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

//http://192.168.1.8:3035/square/10
public class HitterMultiThreadedMultiServerPortsKanchan {
    public static void main(String[] args) throws InterruptedException {
        int hitNumberCount = 1000;
        int[] numbers = new int[hitNumberCount];
        String[] urls = new String[hitNumberCount];

        Random rand = new Random();
        for (int i=0; i<hitNumberCount; i++) {
            numbers[i] = rand.nextInt(100);
            int port = 3030 + rand.nextInt(10);

            //Hitting Kanchan's server
            urls[i] = "http://192.168.1.8:" + port + "/square/" + numbers[i];
        }

        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(100);

        for (int i=0; i<hitNumberCount; i++) {
            executor.submit(new Runner(urls[i]));
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

        Runner(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                connection = new URL(url).openConnection();
                response = connection.getInputStream();

                br = new BufferedReader(new InputStreamReader(response));
                System.out.println(br.readLine());
            } catch (Exception e) {
                System.out.println("Exception:" + e);
            }
        }
    }
}

//1 processes in server (one port)
//Total time: 1010
//QPS: 990

//4 processes in server
//Total time: 628
//QPS: 1592

//6 processes in server
//Total time: 748
//QPS: 1336

//7 processes in server
//Total time: 667
//QPS: 1499