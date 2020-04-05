import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {

    private String id;
    private String last_name;
    private String first_name;
    private String bdate;
    private String sex;
    private City city;
    private Country country;
    private Set<String> groupId;

    public User(String id, String last_name, String first_name, String bdate, String sex, Country country, City city) {
        this.id = id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.bdate = bdate;
        this.sex = sex;
        this.country = country;
        this.city = city;
        this.groupId = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void addGroupId(String id) {
        groupId.add(id);
    }
    public void addGroupId(List<String> list) {
        if (list != null){
            groupId.addAll(list);
        }
    }

    @Override
    public String toString() {
        return "id = " + id + "\t Фамилия:" + last_name + ", Имя:" + first_name + ", Город:" + city + ", состоит в группе:" + groupId;
    }
}
