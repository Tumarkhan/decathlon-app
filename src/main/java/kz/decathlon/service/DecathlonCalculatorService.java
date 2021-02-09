package kz.decathlon.service;

import kz.decathlon.dto.DecathlonScoreDetails;
import kz.decathlon.parser.FileParserTemplate;
import kz.decathlon.writer.FileWriter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DecathlonCalculatorService {
    private Map<Integer, List<Double>> scoreMap;
    private FileParserTemplate parser;
    private FileWriter writer;

    public DecathlonCalculatorService(FileParserTemplate fileParser, FileWriter fileWriter) {
        this();
        this.parser = fileParser;
        this.writer = fileWriter;
    }

    public DecathlonCalculatorService() {
        initializeScoreMap();
    }

    private void initializeScoreMap() {
        // Decathlon constants table
        List<Double> scores_100 = List.of(25.4347, 18.0, 1.81);
        List<Double> scores_LongJump = List.of(0.14354, 220.0, 1.4);
        List<Double> scores_ShotPut = List.of(51.39, 1.5, 1.05);
        List<Double> scores_HighJump = List.of(0.8465, 75.0, 1.42);
        List<Double> scores_400 = List.of(1.53775, 82.0, 1.81);
        List<Double> scores_110 = List.of(5.74352, 28.5, 1.92);
        List<Double> scores_DiscusThrow = List.of(12.91, 4.0, 1.1);
        List<Double> scores_PoleVault = List.of(0.2797, 100.0, 1.35);
        List<Double> scores_JavelinThrow = List.of(10.14, 7.0, 1.08);
        List<Double> scores_1500 = List.of(0.03768, 480.0, 1.85);

        this.scoreMap = Map.of(
                1, scores_100,
                2, scores_LongJump,
                3, scores_ShotPut,
                4, scores_HighJump,
                5, scores_400,
                6, scores_110,
                7, scores_DiscusThrow,
                8, scores_PoleVault,
                9, scores_JavelinThrow,
                10, scores_1500
        );
    }

    public void calculateFromFileAndExport(String path, String exportFilePath) throws IOException, URISyntaxException {
        Map<String, List<Integer>> pointsMap = new HashMap<>();
        Map<String, String> scoresMap = new HashMap<>();
        List<List<String>> resultList = parser.getParsedListAfterReadingFile(path, String.class);

        for (List<String> list : resultList) {
            String[] scores = list.toArray(new String[0]);
            String name = scores[0];

            String pureScores = Arrays.stream(scores).filter(score -> score.contains(".")).collect(Collectors.joining("; "));
            List<Integer> intList = new ArrayList<>();
            scoresMap.put(name, pureScores);

            for (int i = 1; i < scores.length; i++) {
                double performance;
                if (i == scores.length - 1) {
                    int mins = Integer.parseInt(scores[i].substring(0, 1));
                    double seconds = Double.parseDouble(scores[i].substring(2));
                    performance = mins * 60 + seconds;
                } else {
                    performance = Double.parseDouble(scores[i]);
                }
                intList.add(calcPoint(i, performance));
            }
            pointsMap.put(name, intList);
        }

        List<DecathlonScoreDetails> decathlonScoreDetails = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> e : pointsMap.entrySet()) {
            List<Integer> scores = e.getValue();
            String key = e.getKey();

            DecathlonScoreDetails detail = new DecathlonScoreDetails();
            detail.setName(key);
            detail.setScores(scoresMap.get(key));
            detail.setTotalPoints(scores.stream().mapToInt(Integer::intValue).sum());
            decathlonScoreDetails.add(detail);
        }

        decathlonScoreDetails.sort(DecathlonScoreDetails.comparator());
        AtomicInteger decreePoints = new AtomicInteger(decathlonScoreDetails.size());
        decathlonScoreDetails.forEach(d -> d.setPlace(decreePoints.getAndDecrement()));

        writer.write(decathlonScoreDetails, exportFilePath, DecathlonScoreDetails.class);
    }

    public int calcPoint(int position, double p) {
        List<Double> scores = scoreMap.get(position);
        double A = scores.get(0);
        double B = scores.get(1);
        double C = scores.get(2);

        if (position == 2 || position == 4 || position == 8) {
            p = p * 100;
        }

        int pointTime = (int) (A * Math.pow((B - p), C));
        int pointDist = (int) (A * Math.pow((p - B), C));

        return isTimePoint(position) ? pointTime : pointDist;
    }

    private boolean isTimePoint(int i) {
        return i == 1 || i == 5 || i == 6 || i == 10;
    }
}
