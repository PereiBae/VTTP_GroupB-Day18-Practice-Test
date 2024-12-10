package vttp.ssf.practice_test.services;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vttp.ssf.practice_test.Utils.DateConverter;
import vttp.ssf.practice_test.models.Listing;
import vttp.ssf.practice_test.models.Products;
import vttp.ssf.practice_test.repositories.PracRepository;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PracService {

    @Autowired
    private PracRepository pracRepo;

    public List<Listing> readFile(String filePath) {
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

    public void saveProduct(Products product) {
        pracRepo.saveProducts(product.getId(), product.getTitle(), product.getDescription(), product.getPrice(), product.getDiscountPercentage(), product.getRating(), product.getStock(), product.getBrand(), product.getCategory(), product.getDated(), product.getBuy());
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

    public List<Products> readAndSaveProducts(String filePath) {
        List<Products> products = new ArrayList<>();

        try{

            JsonReader reader = Json.createReader(new FileReader(filePath));
            JsonArray array = reader.readArray();
            for (JsonObject obj : array.getValuesAs(JsonObject.class)) {
                int id =obj.getJsonNumber("id").intValue();
                String title = obj.getString("title");
                String description = obj.getString("description");
                double price = obj.getJsonNumber("price").doubleValue();
                double discountPercentage = obj.getJsonNumber("discountPercentage").doubleValue();
                double rating = obj.getJsonNumber("rating").doubleValue();
                int stock = obj.getJsonNumber("stock").intValue();
                String brand = obj.getString("brand");
                String category = obj.getString("category");
                Date dated = DateConverter.longToDate(obj,"dated");
                int buy = obj.getJsonNumber("buy").intValue();

                Products product = new Products(id, title, description, price, discountPercentage, rating, stock, brand, category, dated, buy);
                products.add(product);

                pracRepo.saveProducts(id,title, description, price, discountPercentage, rating, stock, brand, category, dated, buy);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return products;

    }

    public List<Products> getAllProducts() {
        // Fetch all keys from Redis
        Set<String> keys = pracRepo.getAllProductKeys();
        // Use a TreeSet with a custom comparator to sort numerically
        Set<String> sortedKeys = new TreeSet<>(Comparator.comparingInt(Integer::parseInt));
        sortedKeys.addAll(keys);

        List<Products> products = new ArrayList<>();
        for (String key : sortedKeys) {
            Products product = pracRepo.getProductById(key);
            if(product != null) {
                products.add(product);
            }
        }
        return products;

        // Use this for faster code to run
//        return pracRepo.getAllProductKeys().stream()
//                .sorted(Comparator.comparingInt(Integer::parseInt))
//                .map(pracRepo::getProductById)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
    }

    public Products getProductById(String id) {
        return pracRepo.getProductById(id);
    }

}
