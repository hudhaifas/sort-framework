/*
 * ParallelQuickSort.java
 * Sep 24, 2013 - 5:51:57 PM
 *
 * Copyright (C) 2013 Hudhaifa Shatnawi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mum.sort.parallel;

import edu.mum.sort.Sort;
import edu.mum.sort.util.ForkJoin;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hudhaifa Shatnawi <hudhaifa.shatnawi@gmail.com>
 */
public class ParallelQuickSort
        extends Sort {

    public ParallelQuickSort(int[] arr, BlockingQueue<Integer> notifier) {
        this(arr, notifier, 0, arr.length - 1);
    }

    private ParallelQuickSort(int[] arr, BlockingQueue<Integer> notifier, int low, int high) {
        super(arr, notifier);
        this.low = low;
        this.high = high;
    }

    @Override
    public void run() {
        int i = low;
        int j = high;
        // Get the pivot element from the middle of the list
        int pivot = arr[low + (high - low) / 2];
        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller then the pivot
            // element then get the next element from the left list
            while (arr[i] < pivot) {
                i++;
            }
            // If the current value from the right list is larger then the pivot
            // element then get the next element from the right list
            while (arr[j] > pivot) {
                j--;
            }
            // If we have found a values in the left list which is larger then
            // the pivot element and if we have found a value in the right list
            // which is smaller then the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
            }
        }
        ForkJoin fj = new ForkJoin();
        // Recursion
        if (low < j) {
            fj.add(new ParallelQuickSort(arr, notifier, low, j));
        }
        if (i < high) {
            fj.add(new ParallelQuickSort(arr, notifier, i, high));
        }

        try {
            fj.fork();
        } catch (InterruptedException ex) {
            Logger.getLogger(ParallelQuickSort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getName() {
        return "Parallel QuickSort";
    }

    private int low;
    private int high;
}