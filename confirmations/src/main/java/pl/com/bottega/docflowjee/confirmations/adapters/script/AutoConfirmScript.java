package pl.com.bottega.docflowjee.confirmations.adapters.script;

import com.opencsv.CSVReader;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoConfirmScript {

    public static void main(String[] args) throws FileNotFoundException {
        if(args.length != 1) {
            System.out.println("Usage: path");
            System.exit(-1);
        }
        var counter = new AtomicInteger();
        var total = (double) Flux.fromIterable(csvReader(args[0])).count().block();
        Flux.fromIterable(csvReader(args[0]))
            .parallel()
            .runOn(Schedulers.parallel())
            .map(row -> sendToServer(row))
            .sequential()
            .onErrorContinue((ex, row) -> {
                 System.err.println(ex);
            })
            .doOnEach((row) -> {
                var count = counter.incrementAndGet();
                var percent = count * 100 / total;
                System.out.println(Thread.currentThread().getName() + " Progres = " + percent + " %");
            })
            .blockLast();
    }

    private static CSVReader csvReader(String arg) throws FileNotFoundException {
        return new CSVReader(
            new InputStreamReader(new FileInputStream(arg))
        );
    }

    private static String[] sendToServer(String[] row)  {
        System.out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return row;
    }
}
