import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class YandexDiskExplorer {

    private static final String TOKEN = "y0_AgAAAABzdJ5VAAzs2wAAAAEbrbXdAACO2qIh_ntKc5BMx96-V0qRiYk6dw"; // Замените на ваш токен
    private static final String LIST_URL = "https://cloud-api.yandex.net/v1/disk/resources/files";

    public void listFilesAndDirectories(String diskPath) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Кодируем путь к директории
        String encodedPath = URLEncoder.encode(diskPath, "UTF-8");
        HttpUrl url = HttpUrl.parse(LIST_URL)
                .newBuilder()
                .addQueryParameter("path", encodedPath)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "OAuth " + TOKEN)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        // Получаем ответ в виде JSON
        String jsonResponse = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonResponse);

        // Проверяем наличие файлов и папок
        if (jsonObject.has("_embedded")) {
            JSONArray items = jsonObject.getJSONObject("_embedded").getJSONArray("items");
            System.out.println("Содержимое директории: " + diskPath);
            if (items.length() == 0) {
                System.out.println("Директория пуста.");
            } else {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    String name = item.getString("name");
                    String type = item.getString("type");
                    System.out.println(type + ": " + name);
                }
            }
        } else {
            System.out.println("Директория пуста или не найдена.");
        }
    }
}
