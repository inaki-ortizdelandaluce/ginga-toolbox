package org.ginga.toolbox.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.TableHDU;

import org.apache.log4j.Logger;

/**
 * The lac2xspec routine generates MPC3 PHA files which cannot be opened with XSPEC unless you
 * remove channels 12-47 from the file.
 */
public class SpectraMpc3Fixer {

    private final static Logger LOGGER = Logger.getLogger(SpectraMpc3Fixer.class);

    public SpectraMpc3Fixer() {

    }

    public static void main(String[] args) throws Exception {
        File directory = new File("/Users/iortiz/Copy/4U2129+11/MPC3/500ms/simple");
        SpectraMpc3Fixer fixer = new SpectraMpc3Fixer();
        fixer.fixDirectory(directory);
    }

    /**
     * Fixes all MPC3 PHA files located under a given directory
     * @param directory directory where the pha files are located
     * @throws FitsException
     * @throws IOException
     */
    public void fixDirectory(File directory) throws FitsException, IOException {
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Directory " + directory.getPath()
                    + " does not exist");
        } else {
            File[] phaFiles = directory.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    if (name.endsWith("_MPC3.pha")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            fixPhaFiles(Arrays.asList(phaFiles));
        }
    }

    public void fixPhaFiles(List<File> phaFiles) throws FitsException, IOException {
        for (File phaFile : phaFiles) {
            fix(phaFile);
        }
    }

    public void fixPhaFile(File phaFile) throws FitsException, IOException {
        LOGGER.info("Fixing " + phaFile.getName() + " file...");
        Fits fits = new Fits(phaFile);
        fits.read();
        TableHDU spectrumHdu = (TableHDU) fits.getHDU(1);
        spectrumHdu.deleteRows(12, 48);
        spectrumHdu.rewrite();
        LOGGER.info(phaFile.getName() + " fixed.");
    }
}
