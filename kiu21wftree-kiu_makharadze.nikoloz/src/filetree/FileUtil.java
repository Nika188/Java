package filetree;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.List;
import java.nio.file.Paths;


public class FileUtil {

	public static File toFileRepresentation(Path path) throws IOException {
		if (Files.isRegularFile(path)){
			return new RegularFile(path);
		}
		return new Directory(path, getFiles(path));
	}

	private static List<File> getFiles(Path path)  {
		try {
			return Files.list(path).map(file -> {
				if (Files.isRegularFile(file)){
					return new RegularFile(file);
				}else{
					return new Directory(file, getFiles(file));
				}
			}).collect(Collectors.toList());
		} catch (Exception e) {
			return null;
		}
	}
}
