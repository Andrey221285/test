public class User {

    private String id;
    private String last_name;
    private String first_name;
    private String city;
    private String group;

    public User(String id, String last_name, String first_name, String city, String group) {
        this.id = id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.city = city;
        this.group=group;
    }

    @Override
    public String toString() {
        return "id = " + id + "\t Фамилия:" + last_name + ", Имя:" + first_name + ", Город:" + city+ ", состоит в группе:" + group;
    }
}
