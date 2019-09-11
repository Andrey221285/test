import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.json.*;


public class Test {
    public static void main(String[] args) {

        String nameGroup = "javarush";
        String cityName = "Новосибирск";
        Test test = new Test();

        List<User> list = test.getUsers(nameGroup, cityName);

        for (User user : list) {
            System.out.println(user);
        }
        System.out.println("==========================================================");
        System.out.println("Найдено пользователей: " + list.size());

        //    test.save(list);
    }

    public List<User> getUsers(String nameGroup, String cityName) {
        ArrayList<User> list = new ArrayList<>();
        String inputLine = "";
        int countMembers = 1;
        String token = askToken("https://oauth.vk.com/authorize?client_id=7122524&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=friends&response_type=token&v=5.101&state=123456");

        for (int j = 0; j < countMembers; j = j + 1000) {
            try {
                String s = String.format("https://api.vk.com/method/groups.getMembers?group_id=%s&offset=%d&fields=city&access_token=%s&v=5.101", nameGroup, j, token);
                URL url = new URL(s);
                URLConnection uc = url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                inputLine = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject obj = new JSONObject(inputLine);
            JSONArray item = obj.getJSONObject("response").getJSONArray("items");
            String count = obj.getJSONObject("response").get("count").toString();
            countMembers = Integer.parseInt(count);

            for (int i = 0; i < item.length(); i++) {
                JSONObject jsonObject = item.getJSONObject(i);
                try {
                    String id = jsonObject.get("id").toString();
                    String last_name = jsonObject.get("last_name").toString();
                    String first_name = jsonObject.get("first_name").toString();
                    String city = jsonObject.getJSONObject("city").get("title").toString();
                    if (city.equals(cityName)) {
                        list.add(new User(id, last_name, first_name, city, nameGroup));
                    }
                } catch (JSONException e) {
                }
            }
        }
        return list;
    }

    public void save(List<User> list) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("g://list.txt"))) {
            writer.write("Найдено пользовтаелей: " + list.size() + "\n");
            writer.write("===========================================\n");
            for (User user : list) {
                writer.write(user.toString() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String askToken(String link) {
        //Opens link in default browser
        try {
            Desktop.getDesktop().browse(new URI(link));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //Asks user to input token from browser manually
        return JOptionPane.showInputDialog("Please input access_token param from browser: ");
    }
}


