package vttp.ssf.practice_test.models;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Listing {

    // ID should be generated using UUID
    private String id;

    @NotEmpty(message = "Name is required")
    @Size(min = 10, max = 50, message = "Name must be between 10 and 50 characters")
    private String name;

    @Size(max = 255, message = "Description can only be 255 characters long")
    private String description;

    @FutureOrPresent(message = "Due Date can only be today or in the Future!")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Match the HTML input format
    // Should be stored in epoch milliseconds on redis
    private Date dueDate;

    // low medium high
    private String priority;

    // pending started progress completed
    private String status;

    // Date of creation, stored in epoch milliseconds
    private Date createdAt;

    // Updated Date each time record is updated
    private Date updatedAt;

    public Listing() {
    }

    public Listing(String id, String name, String description, Date dueDate, String priority, String status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return  id + "," + name + "," + description + dueDate + "," + priority + "," + status + "," + createdAt + "," + updatedAt;
    }
}
