package kz.decathlon;

import kz.decathlon.service.DecathlonCalculatorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DecathlonTest {
    private DecathlonCalculatorService service;

    @Before
    public void init() {
        service = new DecathlonCalculatorService();
    }

    @Test
    public void should_calculate_correctly() {
        List<Integer> expectedList = List.of(536, 382, 439, 389, 400, 685, 302, 264, 382, 421);
        String[] testScores = {"John Smith", "12.61", "5.00", "9.22", "1.50", "60.39", "16.43", "21.60", "2.60", "35.81", "5.25.72"};
        List<Integer> list = new ArrayList<>();

        for (int i = 1; i < testScores.length; i++) {
            double performance;
            if (i == testScores.length - 1) {
                int mins = Integer.parseInt(testScores[i].substring(0, 1));
                double seconds = Double.parseDouble(testScores[i].substring(2));
                performance = mins * 60 + seconds;
            } else {
                performance = Double.parseDouble(testScores[i]);
            }
            list.add(service.calcPoint(i, performance));
        }

        Assert.assertArrayEquals(expectedList.toArray(), list.toArray());
    }
}
