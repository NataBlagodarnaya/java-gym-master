package ru.yandex.practicum.gym;

import java.util.*;

public class CounterOfTrainings {
    //тренер
    private Coach coach;
    //количество тренировок
    private Integer trainingsCount;

    public CounterOfTrainings(Coach coach, int trainingsCount) {
        this.coach = coach;
        this.trainingsCount = trainingsCount;
    }

    public Integer getTrainingsCount() {
        return trainingsCount;
    }

    public Coach getCoach() {
        return coach;
    }
}
