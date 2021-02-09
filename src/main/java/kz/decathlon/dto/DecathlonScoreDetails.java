package kz.decathlon.dto;

import java.util.Comparator;

public class DecathlonScoreDetails {
    public static final String fullName = "DecathlonScoreDetails";
    private int place;
    private String name;
    private String scores;
    private Integer totalPoints;

    public static Comparator<DecathlonScoreDetails> comparator() {
        return Comparator.comparing(DecathlonScoreDetails::getTotalPoints);
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public String toString() {
        return "DecathlonScoreDetails{" +
                "place=" + place +
                ", name='" + name + '\'' +
                ", scores='" + scores + '\'' +
                ", totalPoints=" + totalPoints +
                '}';
    }
}
