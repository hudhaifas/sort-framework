/*
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
package com.hudhaifa.sortframework.sort;

import com.hudhaifa.sortframework.core.AbstractSort;

/**
 * This class implements the IMerge sort algorithm which is a combination of
 * merge sort algorithm and insertion sort algorithm based on the array size.
 * This algorithm offers:
 *
 * <ul>
 * <li>Best case: n log n, but it is typically faster than traditional Merge
 * sort implementations in the big data sets
 * <li>Average case: n log n
 * <li>Worst case: n log n
 * <li>Space: n
 * </ul>
 *
 *
 * @author Hudhaifa Shatnawi <hudhaifa.shatnawi@gmail.com>
 * @version 1.0, Oct 13, 2013 - 10:34:20 PM
 * @since sort-framework v1.0
 */
public class IMergeSort
        extends AbstractSort {

    public IMergeSort() {
    }

    @Override
    public String getName() {
        return "IMergeSort";
    }

    @Override
    public void sort() {
        helper = new int[arr.length];
        System.arraycopy(arr, 0, helper, 0, arr.length);
        addUnits(arr.length);

        chooseAlgorithm(0, arr.length - 1);

        finish();
    }

    /**
     * Divides the array into two equaled sub arrays.
     *
     * @param left the first index of the array or sub array
     * @param right the last index of the array or sub array
     */
    private void divide(int left, int right) {
        int length = right - left;

        if (length < 1) {
            return;
        }

        updateCursors(left, right);

        // Calculates the middle of the array.
        int middle = left + length / 2;

        chooseAlgorithm(left, middle);
        chooseAlgorithm(middle + 1, right);

        merge(left, middle, right);
    }

    /**
     * Merges two unsorted array parts into one sorted array part.
     *
     * @param left the first index of the array or sub array
     * @param middle the middle index of the array or sub array
     * @param right the last index of the array or sub array
     */
    private void merge(int left, int middle, int right) {
        int targetCount = left, leftCount = left, rightCount = middle + 1;

        updateCursors(left, right);

        // Adding the smaller value from each array in each loop.
        while (leftCount <= middle && rightCount <= right) {
            addUnit();
            if (isLess(helper[leftCount], helper[rightCount])) {
                arr[targetCount++] = helper[leftCount++];
                updateCursors(targetCount, leftCount);
            } else {
                arr[targetCount++] = helper[rightCount++];
                updateCursors(targetCount, rightCount);
            }
        }

        // Adding the rest values of the left array
        while (leftCount <= middle) {
            addUnit();
            arr[targetCount++] = helper[leftCount++];
            updateCursors(targetCount, leftCount);
        }

        // Adding the rest values of the right array
        while (rightCount <= right) {
            addUnit();
            arr[targetCount++] = helper[rightCount++];
            updateCursors(targetCount, rightCount);
        }

        updateHelper(left, right);
    }

    /**
     * AbstractSort the array or sub array by insertion sort.
     *
     * @param left the start index
     * @param right the end index
     */
    private void insertionSort(int left, int right) {
        int temp;

        for (int i = left + 1; i <= right; i++) {
            temp = arr[i];
            int j;
            for (j = i; j > left && isLess(temp, arr[j - 1]); j--) {
                addUnit();
                swap(j, j - 1);
                updateCursors(i, j);
            }
            arr[j] = temp;
            updateCursor(i);
        }
        updateHelper(left, right);
    }

    /**
     * Choose the convenient algorithm based on the array or sub array length.
     *
     * @param left the start index
     * @param right the end index
     */
    private void chooseAlgorithm(int left, int right) {
        // Use the Insertion sort
        if ((right - left) <= INSERTION_THRESHOLD) {
            insertionSort(left, right);
        } else {
            divide(left, right);
        }
    }

    private void updateHelper(int left, int right) {
        for (int i = left; i <= right; i++) {
            addUnit();
            helper[i] = arr[i];
        }
    }

    /**
     * If the length of an array or sub array is less than the threshold,
     * Insertion sort is used rather than Merge sort.
     */
    private static final int INSERTION_THRESHOLD = 45;
    private int[] helper;
}
