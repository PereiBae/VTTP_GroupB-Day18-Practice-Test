package vttp.ssf.practice_test.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vttp.ssf.practice_test.models.Listing;
import vttp.ssf.practice_test.models.Products;
import vttp.ssf.practice_test.services.PracService;

import java.util.List;
import java.util.logging.Logger;

@Component
public class JsonFileLoader implements CommandLineRunner {

    private static final String JSON_T0DO_PATH="/app/todos.json";
    private static final String JSON_T1DO_PATH="/app/products.json";

    Logger logger = Logger.getLogger(JsonFileLoader.class.getName());

    @Autowired
    private PracService pracService;

    @Override
    public void run(String... args) {

        try {
            List<Listing> events = pracService.readFile(JSON_T0DO_PATH);

            // Display the list of events
            System.out.println("List of events: ");
            events.forEach(System.out::println);

            for (Listing listing : events) {
                pracService.saveRecord(listing);
            }
            System.out.println("All listings have been saved to Redis");
        } catch (Exception e) {
            logger.warning("Unable to load json file");
            e.printStackTrace();
        }

        try{
            List<Products> products = pracService.readAndSaveProducts(JSON_T1DO_PATH);
            // Display the list of products
            System.out.println("List of products: ");
            products.forEach(System.out::println);

            System.out.println("All listings have been saved to Redis");
        } catch (Exception e) {
            logger.warning("Unable to load json file");
            e.printStackTrace();
        }
    }

}
