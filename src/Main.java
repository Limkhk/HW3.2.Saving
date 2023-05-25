import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(100, 2, 5, 10.1);
        GameProgress gameProgress2 = new GameProgress(37, 1, 10, 35);
        GameProgress gameProgress3 = new GameProgress(49, 3, 20, 105.8);

        saveGame("D:\\Курсы\\netology\\Games\\savegames\\save1.dat", gameProgress1);
        saveGame("D:\\Курсы\\netology\\Games\\savegames\\save2.dat", gameProgress2);
        saveGame("D:\\Курсы\\netology\\Games\\savegames\\save3.dat", gameProgress3);

        zipFiles("D:\\Курсы\\netology\\Games\\savegames\\save.zip", "D:\\Курсы\\netology\\Games\\savegames");

        deleteFileNotArch("D:\\Курсы\\netology\\Games\\savegames\\save.zip", "D:\\Курсы\\netology\\Games\\savegames");
    }

    private static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            System.out.println("Игра сохранена в директорию: " + filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void zipFiles(String zipFilePath, String filesToZip) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            File dirFileToZip = new File(filesToZip);
            File dirZipFilePath = new File(zipFilePath);
            for (File file : dirFileToZip.listFiles()) {
                if (!file.getName().equals(dirZipFilePath.getName())) {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    fis.close();

                    ZipEntry entry = new ZipEntry(file.getName());
                    zout.putNextEntry(entry);
                    zout.write(buffer);
                    zout.closeEntry();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteFileNotArch(String fileName, String fileDir) {
        File dirFiles = new File(fileDir);
        File nameFiles = new File(fileName);
        System.out.println(dirFiles.getName());
        for (File file : dirFiles.listFiles()) {
            System.out.println(file);
            if (!file.getName().equals(nameFiles.getName())) {
                file.delete();
            }
        }
    }
}