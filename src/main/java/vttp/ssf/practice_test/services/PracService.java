package vttp.ssf.practice_test.services;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vttp.ssf.practice_test.Utils.DateConverter;
import vttp.ssf.practice_test.models.Listing;
import vttp.ssf.practice_test.repositories.PracRepository;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class PracService {

    @Autowired
    private PracRepository pracRepo;

    public static List<Listing> readFile(String filePath) {
        List<Listing> listings = new ArrayList<>();

        try {
            JsonReader reader = Json.createReader(new FileReader(filePath));
            JsonArray array = reader.readArray();
            for (JsonObject obj : array.getValuesAs(JsonObject.class)) {
                String id = obj.getString("id");
                String name = obj.getString("name");
                String description = obj.getString("description");
                String dateString = obj.getString("due_date");
                Date dueDate= DateConverter.stringToDate(dateString);
                String priority = obj.getString("priority_level");
                String status = obj.getString("status").replace("_"," ");
                String created_At = obj.getString("created_at");
                Date createdDate = DateConverter.stringToDate(created_At);
                String updated_At = obj.getString("updated_at");
                Date updatedDate = DateConverter.stringToDate(updated_At);
                // Create Event object and add it to the list

                Listing listing = new Listing(id, name, description, dueDate, priority, status, createdDate, updatedDate);
                listings.add(listing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listings;
    }

    public void saveRecord(Listing listing) {
        long dueDate= DateConverter.dateTolong(listing.getDueDate());
        long createdAt = DateConverter.dateTolong(listing.getCreatedAt());
        long updatedAt = DateConverter.dateTolong(listing.getUpdatedAt());
        pracRepo.saveRecord(listing.getId(),listing.getName(),listing.getDescription(),dueDate,listing.getPriority(),listing.getStatus(),createdAt,updatedAt);
    }


    public List<Listing> getAllListings() {
        // Fetch all keys from Redis
        Set<String> keys = pracRepo.getAllKeys();

        List<Listing> events = new ArrayList<>();
        for (String key : keys) {
            Listing listing = pracRepo.getListingById((key));
            if (listing != null) {
                events.add(listing);
            }
        }
        return events;
    }

    public List<Listing> getListingsByStatus(String status) {
        Set<String> keys = pracRepo.getAllKeys();
        List<Listing> filteredListings = new ArrayList<>();
        for (String key : keys) {
            Listing listing = pracRepo.getListingById(key);
            if (listing != null && listing.getStatus().equalsIgnoreCase(status)) {
                filteredListings.add(listing);
            }
        }
        return filteredListings;
    }

    public Listing getListingById(String id) {
        return pracRepo.getListingById(id);
    }

    public void deleteListing(String id) {
        pracRepo.deleteRecord(id);
    }


}
