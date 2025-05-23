package models;

public class Product {
    private int id;
    private String image;
    private String description;
    private String category;
    private String name;
    private double price;
    private String preparationTime; 
    private boolean available;

   
    public Product() {
    }

    public Product(int id, String image, String description, String name, double price, String preparationTime, boolean available,String category) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.name = name;
        this.price = price;
        this.preparationTime = preparationTime;
        this.available = available;
        this.category=category;
    }
    

   
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", preparationTime='" + preparationTime + '\'' +
                ", available=" + available +
                '}';
    }
}
