package ru.netology;

import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        // Создаем пул потоков
        ExecutorService threadPool = Executors.newFixedThreadPool(8);

        //Создаем колеекцию Future
        List<Future> threads = new ArrayList<>();

        for (String text : texts) {
            Callable<Integer> callable = () -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                return maxSize;
            };
            //Отправлем в пул потока задучу callable на сиполнение
            threads.add(threadPool.submit(callable));
        }
        long startTs = System.currentTimeMillis(); // start time

        int a = 0;
        for (Future thread : threads) {
            int b = (Integer) thread.get();
            if (a < b) {
                a = b;
            }
        }
        System.out.println(a);
        threadPool.shutdown();

        long endTs = System.currentTimeMillis(); // end time

        System.out.println("Time: " + (endTs - startTs) + "ms");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}