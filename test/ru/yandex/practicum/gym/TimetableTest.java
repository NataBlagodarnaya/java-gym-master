package ru.yandex.practicum.gym;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static ru.yandex.practicum.gym.DayOfWeek.*;


public class TimetableTest {
    private Timetable timetable;
    private Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
    private Coach coach1 = new Coach("Петрова", "Вера", "Александровна");
    private Group group =new Group("Акробатика для детей", Age.CHILD, 60);
    private TimeOfDay time13 = new TimeOfDay(13, 0);
    private TimeOfDay time20 = new TimeOfDay(20, 0);
    private TrainingSession trainingSession = new TrainingSession(group, coach,
            MONDAY, time13);

    @BeforeEach
    public void beforeEach() {
        timetable = new Timetable();
        timetable.addNewTrainingSession(trainingSession);
    }

    @Test
    void testTimetableIsNotNull() {
        //Проверить что расписание создано
        assertNotNull(timetable);
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        //Проверить, что за понедельник вернулось одно занятие
        assertEquals(1, timetable.getTrainingSessionsForDay(MONDAY).size());
        assertEquals(1, timetable.getTrainingSessionsForDay(MONDAY).firstEntry().getValue().size());
        //Проверить, что за вторник не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDay(TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, time20);

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        TrainingSession thursdayChildTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.THURSDAY, time13);
        TrainingSession saturdayChildTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        assertEquals(1, timetable.getTrainingSessionsForDay(MONDAY).size());
        assertEquals(1, timetable.getTrainingSessionsForDay(MONDAY).firstEntry().getValue().size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        ArrayList<TimeOfDay> keys = new ArrayList<>(timetable.getTrainingSessionsForDay(THURSDAY).keySet());
        assertEquals(2, keys.size());
        assertEquals(time13, keys.get(0));
        assertEquals(1, timetable.getTrainingSessionsForDay(THURSDAY).get(time13).size());
        assertEquals(time20, keys.get(1));
        assertEquals(1, timetable.getTrainingSessionsForDay(THURSDAY).get(time20).size());

        // Проверить, что за вторник не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDay(TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        assertEquals(1, timetable.getTrainingSessionsForDayAndTime(MONDAY, time13).size());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        TimeOfDay time14 = new TimeOfDay(14, 0);
        assertNull(timetable.getTrainingSessionsForDayAndTime(MONDAY, time14));
    }


    @Test
    void testGetCoach() {
        //Проверить что в расписании правильно указан тренер
        ArrayList<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(MONDAY, time13);
        TrainingSession session = sessions.get(0);
        assertEquals(coach, session.getCoach());
    }

    @Test
    void testGetGroup() {
        //Проверить что в расписании правильно указана группа
        ArrayList<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(MONDAY, time13);
        TrainingSession session = sessions.get(0);
        assertEquals(group, session.getGroup());
    }

    @Test
    void testЕrainingsByCoachIsNotNull() {
        //Проверить что список тренеров с количеством тренировок создан
        assertNotNull(timetable.countTrainingsByCoach());
    }

    @Test
    void testCountTrainingsByCoach() {
        int trainingsCount = 0;
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, time20);

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        TrainingSession thursdayChildTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.THURSDAY, time13);
        TrainingSession saturdayChildTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        //Проверить что у тренера 4 тренировки
        for(CounterOfTrainings counter : timetable.countTrainingsByCoach()){
            if(counter.getCoach().equals(coach)) {
                trainingsCount = counter.getTrainingsCount();
            }
        }
        assertEquals(4, trainingsCount);
    }

    @Test
    void testSortCountTrainingsByCoach() {
        int trainingsCount = 0;
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach1,
                DayOfWeek.THURSDAY, time20);
        TrainingSession fridayAdultTrainingSession = new TrainingSession(groupAdult, coach1,
                DayOfWeek.FRIDAY, time20);
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        timetable.addNewTrainingSession(fridayAdultTrainingSession);

        //Проверить порядок сортировки по убыванию количества тренировок
        List<CounterOfTrainings> sessionsCount = timetable.countTrainingsByCoach();
        assertTrue(sessionsCount.size() == 2);
        assertTrue(sessionsCount.get(0).getTrainingsCount() > sessionsCount.get(1).getTrainingsCount());
        assertEquals(coach1, sessionsCount.get(0).getCoach());
        }
    }
