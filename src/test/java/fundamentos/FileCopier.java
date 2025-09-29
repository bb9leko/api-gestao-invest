package fundamentos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileCopier {
    public static void copy(String records1, String records2) throws IOException {
        try (
            InputStream is = new FileInputStream(records1);
            OutputStream os = new FileOutputStream(records2)
        ) {
            var buffer = new byte[1024];
            var bytesRead = 0;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (FileNotFoundException | java.io.InvalidClassException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        copy("test1.txt", "test2.txt");
    }
}
