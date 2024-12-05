package vttp.ssf.practice_test.repositories;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vttp.ssf.practice_test.Utils.DateConverter;
import vttp.ssf.practice_test.models.Listing;

import java.io.StringReader;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class PracRepository {

    @Autowired
    @Qualifier("redisStringTemplate")
    private RedisTemplate<String, String> redisTemplate;

    // SET "{event.getEventName}" 'event'
    public void saveRecord(String id, String name, String description, long dueDate, String priority, String status, long createdAt, long updatedAt) {
        System.out.printf("Saving record: %s%n", id);
        String savedData = Json.createObjectBuilder()
                .add("id", id)
                .add("name", name)
                .add("description", description)
                .add("dueDate", dueDate)
                .add("priority", priority)
                .add("status", status)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .build()
                .toString();
        redisTemplate.opsForHash().put("To-Dos",id, savedData );
        System.out.printf("Record saved: %s%n", id);
    }

    // hgetall To-Dos
    public Set<String> getAllKeys(){
        System.out.println("Getting all field keys from hash: To-Dos");
        return redisTemplate.opsForHash()
                .keys("To-Dos")
                .stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    // hget To-Dos {id}
    public Listing getListingById(String id){
        System.out.printf("Getting listing by id: %s%n", id);
        String fetchedData = (String) redisTemplate.opsForHash().get("To-Dos",id);

        if (fetchedData == null) {
            System.out.printf("No listing found for id: %s%n", id);
            return null;
        }

        JsonObject jsonObject = Json.createReader(new StringReader(fetchedData)).readObject();

        Listing listing = new Listing();
        listing.setId(jsonObject.getString("id"));
        listing.setName(jsonObject.getString("name"));
        listing.setDescription(jsonObject.getString("description"));
        listing.setDueDate(DateConverter.longToDate(jsonObject,"dueDate"));
        listing.setPriority(jsonObject.getString("priority"));
        listing.setStatus(jsonObject.getString("status"));
        listing.setCreatedAt(DateConverter.longToDate(jsonObject,"createdAt"));
        listing.setUpdatedAt(DateConverter.longToDate(jsonObject,"updatedAt"));

        return listing;

    }

    // hdel To-Dos {id}
    public void deleteRecord(String id){
        System.out.printf("Deleting record: %s%n", id);
        redisTemplate.opsForHash().delete("To-Dos",id);
    }

}
