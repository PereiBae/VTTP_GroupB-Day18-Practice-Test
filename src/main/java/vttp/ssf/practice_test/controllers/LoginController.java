package vttp.ssf.practice_test.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import vttp.ssf.practice_test.Utils.Sessions;
import vttp.ssf.practice_test.models.Person;

import java.util.Random;

@Controller
@RequestMapping
public class LoginController {

    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("Person", new Person());
        return "login";
    }

    @PostMapping("/newSession")
    public String newSession(@Valid @ModelAttribute Person person, HttpSession session, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "login";
        }

        int age = person.getAge();

        if (age < 10) {
            return "underage";
        }

        Sessions.createSession(session, person.getFullName());

        return "redirect:/listing/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loggedInUser");
        session.invalidate();
        return "redirect:/";
    }

    // Day 18 - slide 8
    @GetMapping("/health")
    public ModelAndView getHealth() {
        ModelAndView mav = new ModelAndView();

        try {
            checkHealth();

            mav.setViewName("healthy");
            mav.setStatus(HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            mav.setViewName("unhealthy");
            mav.setStatus(HttpStatusCode.valueOf(500));
        }
        return mav;
    }

    private void checkHealth() throws Exception {
        // get random number between 0 and 10
        Random random = new Random();
        // if random number is between 0 and 5
        // throw an exception
        if (random.nextInt(10) < 5) {
            throw new Exception("Random number is <5");
        }
        // means there is an exception/error (simulating exception)

        // else do nothing
    }
}
