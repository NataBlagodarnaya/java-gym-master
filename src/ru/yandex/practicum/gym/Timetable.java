package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, ArrayList<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        timetable.computeIfAbsent(trainingSession.getDayOfWeek(), k -> new TreeMap<>())
                .computeIfAbsent(trainingSession.getTimeOfDay(), k -> new ArrayList<>())
                .add(trainingSession);
    }

    public TreeMap<TimeOfDay, ArrayList<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (!timetable.containsKey(dayOfWeek)) {
            return new TreeMap<>();
        }
        return timetable.get(dayOfWeek);
    }

    public ArrayList<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (!timetable.containsKey(dayOfWeek) || !timetable.get(dayOfWeek).containsKey(timeOfDay)) {
            return new ArrayList<>();
        }
        return timetable.get(dayOfWeek).get(timeOfDay);
    }

    public List<CounterOfTrainings> countTrainingsByCoach() {
        Map<Coach, Integer> mapForCount = new HashMap<>();
        for (TreeMap<TimeOfDay, ArrayList<TrainingSession>> trainingsForDay : timetable.values()) {
            for (ArrayList<TrainingSession> trainingsForTime : trainingsForDay.values()) {
                for (TrainingSession train : trainingsForTime) {
                    Coach coach = train.getCoach();
                    mapForCount.put(coach, mapForCount.getOrDefault(coach, 0) + 1);
                }
            }
        }
        List<CounterOfTrainings> trainingsByCoach = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : mapForCount.entrySet()) {
            Coach coach = entry.getKey();
            Integer trainingsCount = entry.getValue();
            trainingsByCoach.add(new CounterOfTrainings(coach, trainingsCount));
        }
        trainingsByCoach.sort((a, b) ->
                Integer.compare(b.getTrainingsCount(), a.getTrainingsCount()));
        return trainingsByCoach;
    }
}
