package com.barclays.cobalt.security.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

  public static void writeSafely(String contents, Path path) {
    try {
      Files.write(path, contents.getBytes());
    } catch (IOException e) {
      throw new RuntimeException("Failed writing to '" + path + "'", e);
    }
  }
}
