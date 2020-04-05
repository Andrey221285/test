import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.json.*;


public class Test {
boolean test = true;
    public String token;
    public int countMembers = 1;


    public static void main(String[] args) {
        String nameGroup = "uniton";
        String cityName = "Новосибирск";
        Test test = new Test();
//        test.token = askToken("https://oauth.vk.com/authorize?client_id=7122524&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=friends&response_type=token&v=5.101&state=123456");
        test.token = "073b8c004b9eef8d88253b027e70ee852afe2515e00c3f1202f76c111e8ae9b78e4d1ab110ba934dcd3c8";

       /* List<User> list = test.getUsersFromGroup(nameGroup);
        for (User user : list) {
            System.out.println(user);
        }
        String out = String.format("Найдено пользователей:%d из города:%s состоящих в группе:\"%s\".", list.size(), cityName, nameGroup);
        String line = "";
        for (int i = 0; i < out.length(); i++) {
            line = line.concat("=");
        }
        System.out.println(line);*/

        String group = "31275425";
//       String group = "mogutvse";
        List<User> users = test.getUsersFromGroup(group);
        System.out.println("count user in group=" + test.countMembers);
        System.out.println("users in " + group + ": " + users.size());

        test.findGroup(users);


//       List <String> groups = test.getGroupsId("18002842");
//        System.out.println(groups.toString());
        // System.out.println(out);
        //    test.save(list);
    }

    private void findGroup(List<User> users) {
        if (test){
            for(int i =0;i<100;i++){
                User u = users.get(i);
                u.addGroupId(getGroupsId(u.getId()));
            }
        }else{
            for (User u : users){
                u.addGroupId(getGroupsId(u.getId()));
            }
        }
    }

    public List<String> getGroupsId(String userId) {
        List<String> list = null;
        String inputLine = "";
        int count = 1;
        for (int offset = 0; offset < count; offset = offset + 200) {
            try {
                String s = String.format("https://api.vk.com/method/users.getSubscriptions?user_id=%s&offset=%d&access_token=%s&v=5.101", userId, offset, token);
                URL url = new URL(s);
                URLConnection uc = url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                inputLine = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject obj = new JSONObject(inputLine);
           // System.out.println(obj.toString(2));
            if (!obj.has("error")){
                if (count == 1) {
                    count = obj.getJSONObject("response").getJSONObject("groups").getInt("count");
                }
                JSONArray array = obj.getJSONObject("response").getJSONObject("groups").getJSONArray("items");
            } else{
                System.out.println(obj.toString(2));
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            list = getListFromJsonArray(array);
        }
        return list;
    }

    public List<JSONObject> getListFromJsonArray(JSONArray jsonArray) {
        ArrayList<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getJSONObject(i));
        }
        return list;
    }

    public List<User> getUsersFromGroup(String groupId) {
        List<User> userList = new ArrayList<>();
        String inputLine = "";
        countMembers = 1;
        for (int offset = 0; offset < countMembers; offset = offset + 1000) {
            try {
                String s = String.format("https://api.vk.com/method/groups.getMembers?group_id=%s&offset=%d&fields=sex,bdate,city,country&access_token=%s&v=5.101", groupId, offset, token);
                URL url = new URL(s);
                URLConnection uc = url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                inputLine = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject obj = new JSONObject(inputLine);
//            System.out.println(obj.toString(1));
            JSONArray item = obj.getJSONObject("response").getJSONArray("items");
            countMembers = test ? 1 : obj.getJSONObject("response").getInt("count");
            for (JSONObject jsonObject : getListFromJsonArray(item)) {
                if (jsonObject.has("deactivated")){
                    continue;
                }
                if (jsonObject.has("is_closed")){
                    boolean isClosed = jsonObject.getBoolean("is_closed");
                    if (isClosed){
                        continue;
                    }
                }
                String id = jsonObject.get("id").toString();
                String last_name = jsonObject.get("last_name").toString();
                String first_name = jsonObject.get("first_name").toString();
                String bdate = jsonObject.has("bdate") ? jsonObject.get("sex").toString() : null;
                String sex = jsonObject.has("sex") ? jsonObject.get("sex").toString() : null;
                City city = jsonObject.has("city") ? new City(jsonObject.getJSONObject("city").get("id").toString(),
                        jsonObject.getJSONObject("city").get("title").toString()) : null;
                Country country = jsonObject.has("country") ? new Country(jsonObject.getJSONObject("country").get("id").toString(),
                        jsonObject.getJSONObject("country").get("title").toString()) : null;
                User user = new User(id, last_name, first_name, bdate, sex, country, city);
                user.addGroupId(groupId);

                userList.add(user);
            }
            System.out.println("offset=" + offset + ", list.size=" + userList.size());
        }
        return userList;
    }

    public void userInfo() {
        /*for (int i = 0; i < item.length(); i++) {
            JSONObject jsonObject = item.getJSONObject(i);
            try {
                String id = jsonObject.get("id").toString();
                String last_name = jsonObject.get("last_name").toString();
                String first_name = jsonObject.get("first_name").toString();
                String city = jsonObject.has("city") ? jsonObject.getJSONObject("city").get("title").toString() : null;
                list.add(new User(id, last_name, first_name, city));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
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


