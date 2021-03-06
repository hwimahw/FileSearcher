import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileTypeDetector;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSearcher {
    public static void main(String[] args) throws Exception {
        String path = "src/main/resources";
       Map<File, String> filesAndPathes =  findFiles(path, args);
       printDataFiles(filesAndPathes);
    }

    public static Map<File, String> findFiles(String path, String[] searchFiles){
        Map<File, String> filesAndPathes = new HashMap<>();
        File file = new File(path);
        findFilesRecursive(file, path, filesAndPathes, searchFiles);
        return filesAndPathes;
    }

    private static void findFilesRecursive(File file, String path, Map<File, String> filesAndPathes, String[] searchFiles){
        if (existInArraySearchFiles(file, searchFiles)) {
            filesAndPathes.put(file, path);
        }
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File fileInPath = files[i];
               String newPath = path + File.separator + fileInPath.getName();
                findFilesRecursive(fileInPath, newPath, filesAndPathes, searchFiles);
            }
        }
    }

    private static boolean existInArraySearchFiles(File file, String[] searchFiles){
        Pattern pattern;
        for(int i = 0; i < searchFiles.length; i++){
            pattern = Pattern.compile(searchFiles[i]);
            Matcher matcher = pattern.matcher(file.getName());
            if(matcher.find()){
                return true;
            }
        }
        return false;
    }

    public static void printDataFiles(Map<File, String> filesAndPath) throws Exception{
        System.out.printf("%20s%30s%12s%55s%n", "Name:", "Size:", "Type", "Path:");
        File file;
        for(Map.Entry<File, String> entry : filesAndPath.entrySet()) {
            file = entry.getKey();
            Path path = Paths.get(file.getPath());
            System.out.printf("%30s%20s%20s%90s%n", file.getName(), file.length(), Files.probeContentType(path), file.getAbsolutePath());

        }
    }
}
