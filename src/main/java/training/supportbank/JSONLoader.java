package training.supportbank;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;


public class JSONLoader {

    public static List<User> loadJSON(String filename) throws Exception {
        List<User> users = new ArrayList<>(); 
        Path path = Paths.get(filename);

        try (Reader reader = Files.newBufferedReader(path,
            StandardCharsets.UTF_8)) {
            JsonParser parser = new JsonParser();
            JsonElement tree = parser.parse(reader);
            JsonArray array = tree.getAsJsonArray();

            for (JsonElement element: array) {
                if (element.isJsonObject()) {
                    Gson gson = new Gson();
                    Transaction tr = gson.fromJson(element.toString(), Transaction.class);

                    User usFrom = Main.getUserByName(users, tr.getFrom());
                    User usTo = Main.getUserByName(users, tr.getTo());

                    usFrom.addTransactionFrom(tr);
                    usTo.addTransactionTo(tr);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }

    public static void writeJSON(String filename, List<Transaction> tr){
        try{
            File file = new File(filename);
            file.createNewFile();
            FileWriter writer = new FileWriter(filename);
            JsonArray list = new JsonArray();
            for (Transaction trans : tr){
                String jsonString = new Gson().toJson(trans);
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = (JsonObject) parser.parse(jsonString);
                list.add(jsonObject);
            }

            writer.write(list.toString());
            writer.close();
            System.out.println("Successfully exported to " + filename);
        }
        catch(IOException e){
            System.out.println("An error occured");
            e.printStackTrace();
        }
    }
    
}
