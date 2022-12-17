import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

/**
 * Tests add, remove, size
 * @author Junhui Guo
 */
public class ArrayListRandomTest {
    private static final int TEST_ITERATIONS = 100000;
    private Random random;
    private List<Integer> realList;
    private ArrayList<Integer> studentList;

    private void init() {
        this.random = new Random();
        this.realList = new java.util.ArrayList<>();
        this.studentList = new ArrayList<>();
    }

    @Test()
    public void randomTest() {
        init();
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            if (studentList.size() == 0) {
                testAddToBack();
                continue;
            }
            int choice = random.nextInt(11);
            if (choice <= 4) {
                testAddToBack();
            } else if (choice <= 6) {
                testAdd();
            } else if (choice <= 8){
                testRemoveFromBack();
            } else {
                testRemove();
            }
        }
    }

    private void testAddToBack() {
        int randElement = random.nextInt(Integer.MAX_VALUE);
        realList.add(randElement);
        studentList.addToBack(randElement);
        assertListsEqual();
    }

    private void testRemoveFromBack() {
        studentList.removeFromBack();
        realList.remove(realList.size() - 1);
        assertListsEqual();
    }

    private void testAdd() {
        int randElement = random.nextInt(Integer.MAX_VALUE);
        int randIndex = random.nextInt(studentList.size());
        studentList.addAtIndex(randIndex, randElement);
        realList.add(randIndex, randElement);
        assertListsEqual();
    }

    private void testRemove() {
        int randIndex = random.nextInt(studentList.size());
        studentList.removeAtIndex(randIndex);
        realList.remove(randIndex);
        assertListsEqual();
    }

    private void assertListsEqual() {
        Assert.assertEquals(realList.size(), studentList.size());
        for (int i = 0; i < realList.size(); i++) {
            Assert.assertEquals(realList.get(i), studentList.get(i));
        }
    }
}
