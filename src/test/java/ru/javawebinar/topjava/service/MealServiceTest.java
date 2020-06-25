package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    private MealRepository repository;

    @Test
    public void get() {
        Meal meal = service.get(MealTestData.MEAL_ID, MealTestData.USER_ID);
        MealTestData.assertMatch(meal, MealTestData.MEAL_1);
    }

    @Test
    public void getAnotherPersonsFood() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(MealTestData.MEAL_ID, MealTestData.USER_ID_ANOTHER_PERSON));
    }

    @Test
    public void delete() {
        service.delete(MealTestData.MEAL_ID, MealTestData.USER_ID);
        assertNull(repository.get(MealTestData.MEAL_ID, MealTestData.USER_ID));
    }

    @Test
    public void deleteAnotherPersonsFood() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(MealTestData.MEAL_ID, MealTestData.USER_ID_ANOTHER_PERSON));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(MealTestData.MEAL_START_DATE,
                                                        MealTestData.MEAL_END_DATE,
                                                        MealTestData.USER_ID);
        MealTestData.assertMatch(meals, MealTestData.MEAL_2, MealTestData.MEAL_1);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(MealTestData.USER_ID);
        MealTestData.assertMatch(meals, MealTestData.MEAL_3,
                                        MealTestData.MEAL_2,
                                        MealTestData.MEAL_1);
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, MealTestData.USER_ID);
        MealTestData.assertMatch(service.get(MealTestData.MEAL_ID, MealTestData.USER_ID), updated);
    }

    @Test
    public void updateAnotherPersonsFood() throws Exception {
        Meal updated = MealTestData.getUpdated();
        assertThrows(NotFoundException.class, () ->
                service.update(updated, MealTestData.USER_ID_ANOTHER_PERSON));
    }

    @Test
    public void create() {
        Meal newMeal = MealTestData.getNew();
        Meal created = service.create(newMeal, MealTestData.USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertEquals(newMeal, created);
//        MealTestData.assertMatch(service.get(newId, MealTestData.USER_ID), newMeal);
    }
}