package com.company;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class Main {

    public static Runtime runtime = Runtime.getRuntime();
    private static int MB = 1024 * 1024;

    public static void main(String[] args) {
//        Output:
//        ----Before allocation-------------------------
//        max heapSize:  8154 Mb
//        current heapSize: 510 Mb
//        free heapSize: 506 Mb
//        used heapSize: 4 Mb
//        RAM: 32611 Mb
//        ----After allocation-------------------------
//        max heapSize:  8154 Mb
//        current heapSize: 6600 Mb
//        free heapSize: 4549 Mb
//        used heapSize: 2051 Mb
//        Cannot allocate single block more then 2046 Mb
//        ----After gc-------------------------
//        max heapSize:  8154 Mb
//        current heapSize: 28 Mb
//        free heapSize: 26 Mb
//        used heapSize: 2 Mb

        System.out.println("----Before allocation-------------------------");
        dumpMemory();

        // Get RAM
        OperatingSystemMXBean mxbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        System.out.println("RAM: " + mxbean.getTotalPhysicalMemorySize() / MB + " Mb ");

        // Get max size of single block available in heap to allocate
        int index = 0;
        byte arr[] = null;
        try {
            for (int i = 1; ; index = i++) {
                arr = new byte[i * MB];
                runtime.gc();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            System.out.println("----After allocation-------------------------");
            dumpMemory();
            System.out.println("Cannot allocate single block more then " + --index + " Mb");
            System.out.println("----After gc-------------------------");
            arr = null;
            runtime.gc();
            dumpMemory();
        }
    }

    private static void dumpMemory(){
        System.out.println("max heapSize:  " + runtime.maxMemory() / MB + " Mb");
        System.out.println("current heapSize: " + runtime.totalMemory() / MB + " Mb");
        System.out.println("free heapSize: " + runtime.freeMemory() / MB + " Mb");
        System.out.println("used heapSize: " + (runtime.totalMemory() / MB - runtime.freeMemory() / MB) + " Mb");
    }
}