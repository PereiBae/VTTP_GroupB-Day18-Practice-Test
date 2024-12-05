package vttp.ssf.practice_test.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vttp.ssf.practice_test.Utils.Sessions;
import vttp.ssf.practice_test.models.Listing;
import vttp.ssf.practice_test.services.PracService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/listing")
public class PracController {

    @Autowired
    private PracService pracService;

    @GetMapping("/")
    public String displayListings(@RequestParam(required = false) String status, Model model, HttpSession session) {

        if(!Sessions.checkSession(session)) {
            return "refused";
        }

        List<Listing> listings;
        if (status != null && !status.isEmpty()) {
            listings = pracService.getListingsByStatus(status);
        } else {
            listings = pracService.getAllListings();
        }
        model.addAttribute("listings", listings);
        return "listing";
    }

    @GetMapping("/newTask")
    public String displayNewTaskForm(Model model, HttpSession session) {

        if(!Sessions.checkSession(session)) {
            return "refused";
        }

        model.addAttribute("listing", new Listing());
        return "add";
    }

    @PostMapping(path= {"/submitTask","/update"})
    public String newTask(@Valid @ModelAttribute Listing listing, BindingResult bindingResult, Model model, HttpSession session) {

        if(!Sessions.checkSession(session)) {
            return "refused";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("listing", listing);
            return "add";
        }

        // Set createdAt and updatedAt if it's a new listing
        if (listing.getCreatedAt() == null) {
            listing.setCreatedAt(new Date());
        }
        listing.setUpdatedAt(new Date());

        if( listing.getId() == null ) {
            listing.setId(UUID.randomUUID().toString().substring(0, 36));
        }

        pracService.saveRecord(listing);
        return "redirect:/listing/";
    }

    @GetMapping("/{listingId}")
    public String updateListing(@PathVariable String listingId, Model model, HttpSession session) {

        if(!Sessions.checkSession(session)) {
            return "refused";
        }

        System.out.println(listingId);

        Listing listing = pracService.getListingById(listingId);

        if (listing == null) {
            throw new IllegalArgumentException("Invalid task ID: " + listingId);
        }

        model.addAttribute("listing", listing);
        return "edit";
    }

    @PostMapping("/delete/{listingId}")
    public String deleteTask(@PathVariable("listingId") String listingId, HttpSession session) {

        if(!Sessions.checkSession(session)) {
            return "refused";
        }

        // Delete the listing by ID
        pracService.deleteListing(listingId);

        // Redirect to the listings page
        return "redirect:/listing/";
    }


}
