package kdtree;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.assertEquals;

public class KDTreePointSetTest {

    @Test
    public void test1() {
        List<Point> p = new ArrayList<>();
        p.add(new Point(2, 3));
        p.add(new Point(4, 2));
        p.add(new Point(4, 5));
        p.add(new Point(3, 3));
        p.add(new Point(1, 5));
        p.add(new Point(4, 4));

        KDTreePointSet set = new KDTreePointSet(p);
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            int x = r.nextInt(20)-10;
            int y = r.nextInt(20)-10;
            System.out.printf("Target = (%d, %d)\n \t%s\n", x, y, set.nearest(x, y));
        }
    }

    @Test
    public void testRandom() {
        for (int num = 10; num <= 100000; num *= 10) {
            double total = 0;
            for (int j = 0; j < 10; j++) {
                List<Point> p = new ArrayList<>();
                Random r = new Random();
                for (int i = 0; i < num; i++) {
                    double x = r.nextDouble() * 2 * num - num;
                    double y = r.nextDouble() * 2 * num - num;
                    Point point = new Point(x, y);
                    p.add(point);
                }


                KDTreePointSet actual = new KDTreePointSet(p);
                NaivePointSet expect = new NaivePointSet(p);
                long start = System.currentTimeMillis();
                int correct = 0;
                for (int i = 0; i < 1000; i++) {
                    double x = r.nextDouble() * 4 * num - 2 * num;
                    double y = r.nextDouble() * 4 * num - 2 * num;

                    actual.nearest(x, y);

                assertEquals(expect.nearest(x, y), actual.nearest(x, y));
                }
                total += System.currentTimeMillis() - start;
            }
            System.out.printf("N = %d, Average = %.4f seconds\n", num, total / 1000.0);
        }
    }

    @Test
    public void testHelp() {
        List<Point> p = new ArrayList<>();
        p.add(new Point(5, -6));
        p.add(new Point(-10, 8));
        p.add(new Point(-14, 10));
        p.add(new Point(5, -3));
        p.add(new Point(-7, 2));
        p.add(new Point(8, -4));
        p.add(new Point(0, 5));
        p.add(new Point(7, 0));
        p.add(new Point(-2, -4));

        p.add(new Point(9, -4));

        KDTreePointSet actual = new KDTreePointSet(p);
        NaivePointSet expect = new NaivePointSet(p);

        int x = 0;
        int y = 0;
        //System.out.printf("Target = (%d, %d)  ", x, y);
        //System.out.printf("Expect = %s  ", expect.nearest(x, y));
        //System.out.printf("Actual = %s\n", actual.nearest(x, y));
        System.out.println("Nearest = " + actual.nearest(x, y));
    }
}
