package training.supportbank;

import java.io.Reader;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
                    JsonObject input = element.getAsJsonObject();
                    Gson gson = new Gson();
                    Transaction tr = gson.fromJson(element.toString(), Transaction.class);

                    User usFrom = Main.getUserByName(users, tr.getFrom());
                    User usTo = Main.getUserByName(users, tr.getTo());

                    usFrom.addTransactionFrom(tr);
                    usTo.addTransactionTo(tr);
                }
            }
        }
        return users;
    }
    
}
