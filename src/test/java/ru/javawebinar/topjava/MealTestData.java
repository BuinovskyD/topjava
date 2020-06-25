package ru.javawebinar.topjava;

import org.assertj.core.api.Assertions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int USER_ID = START_SEQ;
    public static final int USER_ID_ANOTHER_PERSON = START_SEQ + 50;
    public static final int MEAL_ID = START_SEQ + 2;

    public static final LocalDate MEAL_START_DATE = LocalDate.of(2020, 1, 30);
    public static final LocalDate MEAL_END_DATE = LocalDate.of(2020, 1, 31);

    public static final Meal MEAL_1 = new Meal(MEAL_ID, LocalDateTime.of(2020, 1, 30, 10, 0), "Завтрак", 1000);
    public static final Meal MEAL_2 = new Meal(MEAL_ID + 1, LocalDateTime.of(2020, 1, 31, 13, 0), "Обед", 500);
    public static final Meal MEAL_3 = new Meal(MEAL_ID + 2, LocalDateTime.of(2020, 2, 1, 12, 0), "Ужин", 410);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, Month.JUNE, 24, 18, 30), "Тест прием пищи", 555);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL_1);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(555);
        return updated;
    }
}
