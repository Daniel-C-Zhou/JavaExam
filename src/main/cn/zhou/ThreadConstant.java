package cn.zhou;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

/**
 * 线程池常量
 */
public class ThreadConstant {

    /**
     * 固定长度线程池
     * 池大小为CPU核数的8倍
     */
    public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(32);


    /**
     * ForkJoinPool 线程池
     * 32并行度的线程池
     */
    public static ForkJoinPool forkJoinPool = new ForkJoinPool(32);

    /**
     * 并发请求超时时间
     */
    public static final long SIRECTOR_TIMEOUT = 3000;

}
