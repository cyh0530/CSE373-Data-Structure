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
        for (int j = 0; j < 10; j++) {
            List<Point> p = new ArrayList<>();
            Random r = new Random();
            int num = 10000;
            for (int i = 0; i < num; i++) {
                double x = r.nextDouble() * 2 * num - num;
                double y = r.nextDouble() * 2 * num - num;
                Point point = new Point(x, y);
                p.add(point);
            }


            KDTreePointSet actual = new KDTreePointSet(p);
            NaivePointSet expect = new NaivePointSet(p);

            int correct = 0;
            for (int i = 0; i < num; i++) {
                double x = r.nextDouble() * 4 * num - 2 * num;
                double y = r.nextDouble() * 4 * num - 2 * num;
                String expectResult = expect.nearest(x, y).toString();
                String actualResult = actual.nearest(x, y).toString();

                if (expectResult.equals(actualResult)) {
                    correct++;
                } else {
                    System.out.printf("Target = (%d, %d)  ", x, y);
                    System.out.printf("Expect = %s  ", expectResult);
                    System.out.printf("Actual = %s\n", actualResult);
                }
            assertEquals(expect.nearest(x, y), actual.nearest(x, y));
            }


        }
    }

    @Test
    public void testHelp() {
        List<Point> p = new ArrayList<>();
        p.add(new Point(-7, 2));
        p.add(new Point(-2, -4));

        KDTreePointSet actual = new KDTreePointSet(p);
        NaivePointSet expect = new NaivePointSet(p);

        int x = -12;
        int y = -11;
        System.out.printf("Target = (%d, %d)  ", x, y);
        System.out.printf("Expect = %s  ", expect.nearest(x, y));
        System.out.printf("Actual = %s\n", actual.nearest(x, y));

    }
}
