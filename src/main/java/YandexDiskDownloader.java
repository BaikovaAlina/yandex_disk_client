import okhttp3.*;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

public class YandexDiskDownloader {

    private static final String TOKEN = "y0_AgAAAABzdJ5VAAzs2wAAAAEbrbXdAACO2qIh_ntKc5BMx96-V0qRiYk6dw"; // Замените на ваш токен
    private static final String DOWNLOAD_URL = "https://cloud-api.yandex.net/v1/disk/resources/download";

    public void downloadFile(String diskPath, String localPath) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Кодируем путь к файлу
        String encodedPath = URLEncoder.encode(diskPath, "UTF-8");
        HttpUrl url = HttpUrl.parse(DOWNLOAD_URL)
                .newBuilder()
                .addQueryParameter("path", encodedPath)
                .build();

        System.out.println("Request for downloading a file from a URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "OAuth " + TOKEN)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        // Получаем JSON-ответ
        String jsonResponse = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonResponse);

        // Извлекаем ссылку для скачивания
        String downloadLink = jsonObject.getString("href");

        // Скачиваем файл по полученной ссылке
        Request downloadRequest = new Request.Builder()
                .url(downloadLink)
                .build();

        Response downloadResponse = client.newCall(downloadRequest).execute();
        if (!downloadResponse.isSuccessful()) throw new IOException("Download failed: " + downloadResponse);

        // Сохраняем файл на локальном диске
        try (FileOutputStream fos = new FileOutputStream(localPath)) {
            fos.write(downloadResponse.body().bytes());
            System.out.println("File was downloaded and saved to: " + localPath);
        }
    }
}
