import okhttp3.*;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class YandexDiskUploader {

    private static final String TOKEN = "y0_AgAAAABzdJ5VAAzs2wAAAAEbrbXdAACO2qIh_ntKc5BMx96-V0qRiYk6dw";
    private static final String UPLOAD_URL = "https://cloud-api.yandex.net/v1/disk/resources/upload";

    public void uploadFile(String filePath, String diskPath) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = HttpUrl.parse(UPLOAD_URL)
                .newBuilder()
                .addQueryParameter("path", diskPath)
                .addQueryParameter("overwrite", "true") // Убедитесь, что параметр установлен
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "OAuth " + TOKEN)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code: " + response);
        }

        // Получаем JSON-ответ
        String jsonResponse = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonResponse);

        // Извлекаем ссылку для загрузки файла
        String uploadLink = jsonObject.getString("href");

        // Загружаем файл по полученной ссылке
        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        Request uploadRequest = new Request.Builder()
                .url(uploadLink)
                .put(requestBody)
                .build();

        Response uploadResponse = client.newCall(uploadRequest).execute();
        if (!uploadResponse.isSuccessful()) {
            throw new IOException("Upload failed: " + uploadResponse);
        }

        System.out.println("File was downloaded!");
    }
}
