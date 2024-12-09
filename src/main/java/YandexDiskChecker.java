import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class YandexDiskChecker {

    private static final String TOKEN = "y0_AgAAAABzdJ5VAAzs2wAAAAEbrbXdAACO2qIh_ntKc5BMx96-V0qRiYk6dw"; // Замените на ваш токен
    private static final String CHECK_URL = "https://cloud-api.yandex.net/v1/disk/resources";

    public void checkDirectoryExists(String diskPath) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Кодируем путь к файлу
        String encodedPath = URLEncoder.encode(diskPath, "UTF-8");
        HttpUrl url = HttpUrl.parse(CHECK_URL)
                .newBuilder()
                .addQueryParameter("path", encodedPath)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "OAuth " + TOKEN)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            System.out.println("Directory exists: " + diskPath);
        } else if (response.code() == 404) {
            System.out.println("Directory not found: " + diskPath);
        } else {
            throw new IOException("Unexpected code: " + response);
        }
    }
}
