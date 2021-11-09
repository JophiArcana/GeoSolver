import java.nio.ByteBuffer;
import java.util.*;

public class Experimenting {
    public static int countSetBits(int n) {
        int count = 0;
        while (n > 0) {
            if ((n & 1) == 1) {
                count++;
            }
            n >>= 1;
        }
        return count;
    }

    public static ArrayList<ArrayList<ByteBuffer>> binarySortedSubsets(int n) {
        ArrayList<ArrayList<ByteBuffer>> subsetList = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            subsetList.add(new ArrayList<>());
        }

        byte[] fullList = new byte[n];
        for (byte i = 0; i < n; i++) {
            fullList[i] = i;
        }
        ByteBuffer wrappedFullList = ByteBuffer.wrap(fullList);
        for (int len = 1; len <= n; len++) {
            ArrayList<ByteBuffer> segment = subsetList.get(len);
            for (int i = 0; i <= n - len; i++) {
                segment.add(wrappedFullList.slice(i, len));
            }
        }

        for (int removedBits = 1; removedBits < (1 << (n - 2)); removedBits++) {
            int copy = removedBits + (1 << (n - 2)), numUnsetBits_plus_2 = n - countSetBits(removedBits);
            byte[] list = new byte[numUnsetBits_plus_2];

            int min = -1, max = -1, counter = 1;
            boolean unsetMin = true;
            byte index = 1;
            while (copy > 1) {
                if ((copy & 1) == 0) {
                    list[counter++] = index;
                } else {
                    if (unsetMin) {
                        min = index;
                        unsetMin = false;
                    }
                    max = index;
                }
                copy >>= 1;
                index++;
            }
            list[counter] = (byte) (n - 1);
            ByteBuffer wrappedList = ByteBuffer.wrap(list);

            for (int range = max - min + 2; range < n; range++) {
                int len = numUnsetBits_plus_2 + 1 + range - n;
                int lowerBound = Math.max(0, max + 1 - range), upperBound = Math.min(min, n - range);
                ArrayList<ByteBuffer> segment = subsetList.get(len);
                for (int i = lowerBound; i < upperBound; i++) {
                    segment.add(wrappedList.slice(i, len));
                }
            }
        }
        return subsetList;
    }

    public static void printByteBuffer(ByteBuffer b) {
        for (int i = 0; i < b.remaining(); i++) {
            System.out.print(b.get(i));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        /**byte[] array1 = {0, 1, 2, 3, 4, 5, 6, 7};
        byte[] array2 = {1, 2, 3, 4, 5, 6, 7, 8};
        ByteBuffer a1 = ByteBuffer.wrap(array1, 2, 5).slice();
        System.out.println(a1.get(0));
        ByteBuffer a2 = ByteBuffer.wrap(array2, 1, 5).slice();
        ByteBuffer a3 = a1.slice(1, 4);
        System.out.println(a1.get(0));
        System.out.println(a3.remaining());*/

        ArrayList<ArrayList<ByteBuffer>> subsetList = binarySortedSubsets(10);
        subsetList.forEach(subsets -> subsets.forEach(Experimenting::printByteBuffer));
    }
}