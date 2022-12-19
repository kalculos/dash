package dash;

import dash.internal.util.SortedArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSortedArrayList {
    @Test
    public void testLinearInsert() {
        var arr = new SortedArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            arr.add(i);
        }
        Assertions.assertArrayEquals(arr.toArray(new Integer[0]),
                new Integer[]{
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9
                });
    }

    @Test
    public void testNormalInsert(){
        var arr = new SortedArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            arr.add(i);
        }
        arr.add(3);
        arr.add(5);
        arr.add(5);
        arr.add(5);
        arr.add(9);
        arr.forEach(System.out::println);
    }
}
