package com.hhwyz;

import com.alibaba.fastjson.JSONObject;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author erniu.wzh
 * @date 2022/11/17 11:43
 */
public class DirectTest {

    public void testAll(List<Object> configs) {
        for (Object config : configs) {
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
            for (int i = 0; i < 30; i++) {
                JSONObject c = (JSONObject) config;
                String server = c.getString("server");
                CompletableFuture
                        .supplyAsync(() -> this.testOne(server), fixedThreadPool)
                        .thenAccept(resultLong -> Result.add(new Server(server, resultLong)));
            }
        }
    }

    public Long testOne(String newRemoteAddr) {
        try (CloseableHttpAsyncClient http2Client = HttpAsyncClients.createHttp2Default()) {
            Thread.sleep(1000L);
            http2Client.start();
            SimpleHttpRequest simpleHttpRequest = SimpleHttpRequest.create("GET", "https://" + newRemoteAddr);

            long a = System.currentTimeMillis();
            Future<SimpleHttpResponse> execute = http2Client.execute(simpleHttpRequest, new FutureCallback<SimpleHttpResponse>() {

                @Override
                public void completed(SimpleHttpResponse result) {
//                    System.out.println("completed HTTP request: " + result.getBody().getBodyText());
                }

                @Override
                public void failed(Exception ex) {
//                    System.out.println("Error executing HTTP request: " + ex.getMessage());
                }

                @Override
                public void cancelled() {
//                    System.out.println("HTTP request execution cancelled");
                }

            });
            execute.get(10L, TimeUnit.SECONDS);
            long b = System.currentTimeMillis();
            return b - a;
        } catch (Exception e) {
            return null;
        }
    }
}
