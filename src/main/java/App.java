import java.io.IOException;

public class App
{
    public static void main( String[] args )
    {
        String diskPath = "suckmy"; //директория в яндекс диске
        String localPath = "/home/alina/Downloads/keys.txt"; //куда сохраняем файлик
        String filePath = "/home/alina/Downloads/keys.txt"; //откуда берем файлик для загрузки


        YandexDiskChecker checker = new YandexDiskChecker();
        try {
            checker.checkDirectoryExists(diskPath);
        } catch (IOException e) {
            System.err.println("Issue of checking for the existence of a directory : " + e.getMessage());
        }


        YandexDiskDownloader downloader = new YandexDiskDownloader();
        try {
            downloader.downloadFile(diskPath, localPath);
        } catch (IOException e) {
            System.err.println("Error downloading file: " + e.getMessage());
        }

        YandexDiskUploader uploader = new YandexDiskUploader();

        String localFilePath = "/home/alina/Downloads/keys.txt";
        String diskPath2 = "test/keys.txt";
        try {
            uploader.uploadFile(localFilePath, diskPath2);
        } catch (IOException e) {
            System.err.println("Error uploading file: " + e.getMessage());
        }

    }

}