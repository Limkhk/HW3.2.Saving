import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(100, 2, 5, 10.1);
        GameProgress gameProgress2 = new GameProgress(37, 1, 10, 35);
        GameProgress gameProgress3 = new GameProgress(49, 3, 20, 105.8);

        String dirPathSave = "D:\\Курсы\\netology\\Games\\savegames\\";
        String fileZip = "D:\\Курсы\\netology\\Games\\savegames\\save.zip";


        saveGame(dirPathSave + "save1.dat", gameProgress1);
        saveGame(dirPathSave + "save2.dat", gameProgress2);
        saveGame(dirPathSave + "save3.dat", gameProgress3);

        zipFiles(fileZip, dirPathSave);

        deleteFileNotArch(fileZip, dirPathSave);

        openZip(fileZip, dirPathSave);

        System.out.println(openProgress(dirPathSave + "save1.dat"));

    }
    // 1. Создадим сохранения
    private static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            System.out.println("Игра сохранена в директорию: " + filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // 2. Добавим сохранения в архив
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
    // 3. Удалим сохранения вне архива
    public static void deleteFileNotArch(String fileName, String fileDir) {
        File dirFiles = new File(fileDir);
        File nameFiles = new File(fileName);
        for (File file : dirFiles.listFiles()) {
            if (!file.getName().equals(nameFiles.getName())) {
                file.delete();
            }
        }
    }
    // 4. Разархивируем сохранения
    private static void openZip(String fileZip, String dirPathSave) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip))) {
            ZipEntry entry;
            String fileName;
            while ((entry = zis.getNextEntry()) != null) {
                fileName = entry.getName();
                FileOutputStream fout = new FileOutputStream(dirPathSave + fileName);
                for (int s = zis.read(); s != -1; s = zis.read()) {
                    fout.write(s);
                }
                fout.flush();
                zis.closeEntry();
                fout.close();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // 5. Десериализуем сохранение
    private static GameProgress openProgress(String dirPathSave) {
        try (FileInputStream fis = new FileInputStream(dirPathSave);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (GameProgress) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}