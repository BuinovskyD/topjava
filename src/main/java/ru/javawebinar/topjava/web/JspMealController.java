package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {

    @Autowired
    private MealService service;

    @GetMapping
    public ModelAndView getAll() {
        ModelAndView modelAndView = new ModelAndView();
        int userId = SecurityUtil.authUserId();
        modelAndView.addObject("meals", MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        modelAndView.setViewName("meals");
        return modelAndView;
    }

    @GetMapping("/filter")
    public ModelAndView getAllFiltered(HttpServletRequest request) {
        ModelAndView  modelAndView = new ModelAndView();
        int userId = SecurityUtil.authUserId();
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, userId);
        modelAndView.addObject("meals", MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        modelAndView.setViewName("meals");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable int id) {
        ModelAndView  modelAndView = new ModelAndView();
        int userId = SecurityUtil.authUserId();
        modelAndView.setViewName("redirect:/meals");
        service.delete(id, userId);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView createPage() {
        ModelAndView  modelAndView = new ModelAndView();
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        modelAndView.addObject(meal);
        modelAndView.setViewName("mealForm");
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createMeal(HttpServletRequest request) {
        ModelAndView  modelAndView = new ModelAndView();
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        service.create(meal, userId);
        modelAndView.setViewName("redirect:/meals");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editPage(@PathVariable int id) {
        ModelAndView  modelAndView = new ModelAndView();
        int userId = SecurityUtil.authUserId();
        modelAndView.addObject("meal", service.get(id, userId));
        modelAndView.setViewName("mealForm");
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView editMeal(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        service.update(meal, userId);
        modelAndView.setViewName("redirect:/meals");
        return modelAndView;
    }
}
