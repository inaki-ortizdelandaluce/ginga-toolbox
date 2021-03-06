package org.ginga.toolbox.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.ginga.toolbox.util.Constants.LacMode;

public class FileUtil {

    private static final Logger log = Logger.getLogger(FileUtil.class);

    public static void main(String[] args) {
        log.info("Next file " + nextFileName(new File("/tmp"), "lacqrd", "input"));
    }

    public static void copy(File srcFile, File destFile) throws IOException {
        if (!srcFile.getPath().equals(destFile.getPath())) {
            FileUtils.copyFile(srcFile, destFile);
        }
    }

    public static String[] splitFileBaseAndExtension(File file) {
        return file.getName().split("\\.(?=[^\\.]+$)");
    }

    public static String nextFileName(String prefix, String startTime, LacMode mode,
            String extension) {
        String fileName = prefix;
        try {
            fileName += "_" + TimeUtil.convertDatabaseToFileFormat(startTime);
        } catch (ParseException e) {
            fileName += "_" + startTime;
        }
        fileName += "_" + mode.toString();
        fileName += "." + extension;
        return fileName;
    }

    public static String nextFileName(File directory, String prefix, String extension) {
        String fileName = null;

        // escape reserved characters in the prefix
        String prefixEscaped = Pattern.quote(prefix);
        log.debug("Prefix escaped" + prefixEscaped);

        // list files starting with same prefix
        File[] files = listAndSortFiles(directory, "^" + prefixEscaped + "-\\d{3}\\." + extension);
        log.debug(files.length + " file(s) found");

        // get latest and increase index
        if (files != null && files.length > 0) {
            File latestFile = files[files.length - 1];
            log.debug("Latest file " + latestFile.getName());

            // split latest file in base and extension tokens
            String[] tokens = splitFileBaseAndExtension(latestFile);
            log.debug("Latest file base " + tokens[0]);
            log.debug("Latest file extension " + tokens[1]);

            // increase number by one
            Pattern pattern = Pattern.compile("^" + prefixEscaped + "-(\\d{3})");
            Matcher matcher = pattern.matcher(tokens[0]);
            if (matcher.matches()) {
                int index = Integer.valueOf(matcher.group(1)).intValue() + 1;
                fileName = prefix + "-" + String.format("%03d", index) + "." + tokens[1];
            }
        } else {
            fileName = prefix + "-001." + extension;
        }
        return fileName;
    }

    public static File[] listAndSortFiles(File directory, final String regex) {
        if (!directory.exists()) {
            throw new IllegalArgumentException("Directory " + directory.getPath()
                    + " does not exists");
        }
        File[] files = directory.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.matches(regex);
            }
        });
        Arrays.sort(files);
        return files;
    }

    public static class LacdumpFileFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.matches("J[0-9]{6}");
        }
    }
}
