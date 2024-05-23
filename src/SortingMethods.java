package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SortingMethods {

    public static void sort(Comparable[] a) { 
        /* See Algorithms 2.1, 2.2, 2.3, 2.4, 2.5, or 2.7. */
    }

    //Current implementation can only compare int data types
    public static boolean less(Comparable v, Comparable w) { 
        return v.compareTo(w) < 0; 
    }

    public static void exch(Comparable[] a, int i, int j) { 
        Comparable t = a[i]; 
        a[i] = a[j]; 
        a[j] = t; 
    }

    private static void show(Comparable[] a) { 
        // Print the array, on a single line.
        for (int i = 0; i < a.length; i++) 
            System.out.print(a[i] + " ");
        System.out.println();
    }

    public static boolean isSorted(Comparable[] a) { 
        // Test whether the array entries are in order.
        for (int i = 1; i < a.length; i++) 
            if (less(a[i], a[i-1])) return false;
        return true;
    }

}
class Selection extends SortingMethods{
    public static void sort(Comparable[] a) {
        int N = a.length; // array length

        // Iterate through the array
        for (int i = 0; i < N; i++) {
            int min = i; // index of the minimal entry

            // Find the index of the smallest element in the unsorted part
            for (int j = i + 1; j < N; j++) {
                if (less(a[j], a[min]))
                    min = j;
            }

            // Swap the current element with the smallest element found
            exch(a, i, min);
        }
    }


}

class Insertion extends SortingMethods{
    public static void sort(Comparable[] a) {
        int N = a.length; // array length

        // Iterate through the array starting from the second element
        for (int i = 1; i < N; i++) {
            // Insert a[i] among a[i-1], a[i-2], a[i-3]...
            for (int j = i; j > 0 && less(a[j], a[j-1]); j--) {
                exch(a, j, j-1); // Swap the current element with its predecessor
            }
        }
    }

}

class Shell extends SortingMethods{
    public static void sort(Comparable[] a) {
        int N = a.length; // array length
        int h = 1;

        // Calculate the initial value of h
        while (h < N/3) 
            h = 3*h + 1; // 1, 4, 13, 40, 121, 364, 1093, ...

        // Outer loop: Decrease h through a sequence of increments
        while (h >= 1) {
            // Inner loop: h-sort the array using insertion sort
            for (int i = h; i < N; i++) {
                // Insert a[i] among a[i-h], a[i-2*h], a[i-3*h], ...
                for (int j = i; j >= h && less(a[j], a[j-h]); j -= h)
                    exch(a, j, j-h);
            }
            h = h/3; // Reduce the increment
        }
    }

}

class Merge extends SortingMethods{
    private static Comparable[] aux; // auxiliary array for merges

    public static void sort(Comparable[] a) {
        aux = new Comparable[a.length]; // Allocate space just once.
        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int lo, int hi) {
        // Sort a[lo..hi].
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, lo, mid);      // Sort left half.
        sort(a, mid + 1, hi);   // Sort right half.
        merge(a, lo, mid, hi);  // Merge results (code on page 271).
    }

    public static void merge(Comparable[] a, int lo, int mid, int hi) {
        // Merge a[lo..mid] with a[mid+1..hi].
        int i = lo, j = mid + 1;

        for (int k = lo; k <= hi; k++)
            aux[k] = a[k]; // Copy a[lo..hi] to aux[lo..hi].

        for (int k = lo; k <= hi; k++) {
            // Merge back to a[lo..hi].
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }

}

class MergeBU extends Merge{
    private static Comparable[] aux; // auxiliary array for merges

    // See page 271 for merge() code.
    public static void sort(Comparable[] a) {
        int N = a.length;
        aux = new Comparable[N];

        for (int sz = 1; sz < N; sz = sz + sz) {
            // sz: subarray size
            for (int lo = 0; lo < N - sz; lo += sz + sz) {
                // lo: subarray index
                merge(a, lo, lo + sz - 1, Math.min(lo + sz + sz - 1, N - 1));
            }
        }
    }

}

class Quick extends SortingMethods{

    public static void sort(Comparable[] a) {
        shuffleArray(a);// Eliminate dependence on input.
        sort(a, 0, a.length - 1);
    }

    // Method to shuffle an array using Fisher-Yates algorithm
    private static void shuffleArray(Comparable[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            swap(array, i, j);
        }
    }

    // Helper method to swap elements in an array
    private static void swap(Comparable[] array, int i, int j) {
        Comparable temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) return;
        int j = partition(a, lo, hi); // Partition (see page 291).
        sort(a, lo, j - 1); // Sort left part a[lo .. j-1].
        sort(a, j + 1, hi); // Sort right part a[j+1 .. hi].
    }

    private static int partition(Comparable[] a, int lo, int hi) {
        // Partition into a[lo..i-1], a[i], a[i+1..hi].
        int i = lo, j = hi + 1; // left and right scan indices
        Comparable v = a[lo]; // partitioning item

        while (true) {
            // Scan right, scan left, check for scan complete, and exchange.
            while (less(a[++i], v)) if (i == hi) break;
            while (less(v, a[--j])) if (j == lo) break;
            if (i >= j) break;
            exch(a, i, j);
        }

        exch(a, lo, j); // Put v = a[j] into position
        return j; // with a[lo..j-1] <= a[j] <= a[j+1..hi].
    }

}

class Quick3way extends Quick{
    private static void sort(Comparable[] a, int lo, int hi) {
        // See page 289 for public sort() that calls this method.
        if (hi <= lo) return;

        int lt = lo, i = lo + 1, gt = hi;
        Comparable v = a[lo];

        while (i <= gt) {
            int cmp = a[i].compareTo(v);

            if (cmp < 0)
                exch(a, lt++, i++);
            else if (cmp > 0)
                exch(a, i, gt--);
            else
                i++;
        }

        // Now a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(a, lo, lt - 1);
        sort(a, gt + 1, hi);
    }

}
