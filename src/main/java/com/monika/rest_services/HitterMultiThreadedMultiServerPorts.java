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
public class HitterMultiThreadedMultiServerPorts {
    public static void main(String[] args) throws InterruptedException {
        //int[] ports = new int[]{8090,8091,8092,8093,8094};
        /*
         * Run multiple RestServiceApplication with below command with specific server
         * java -jar rest_services-0.0.1-SNAPSHOT.jar --server.port=8091
         */

        String param = "num";
        int hitNumberCount = 1000;
        int[] numbers = new int[hitNumberCount];
        String[] queries = new String[hitNumberCount];
        String[] urls = new String[hitNumberCount];


        Random rand = new Random();
        for (int i=0; i<hitNumberCount; i++) {
            numbers[i] = rand.nextInt(100);
            queries[i] = param + "=" + numbers[i];
            int port = 4040 + rand.nextInt(3);

            //Hitting Kanchan's server
            urls[i] = "http://localhost:" + port + "/square";
        }

        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i=0; i<hitNumberCount; i++) {
            executor.submit(new Runner(urls[i], queries[i]));
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
