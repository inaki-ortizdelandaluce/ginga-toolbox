package org.ginga.toolbox.gti;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class SimbadTargetResolver {

    public static final String SIMBAD_URL = "http://simbad.u-strasbg.fr/simbad/sim-id?output.format=ASCII&NbIndent=1&Ident=";

    /** Connection timeout. */
    public static final int CONNECTION_TIMEOUT = 5000;

    /** Read timeout. */
    public static final int READ_TIMEOUT = 5000;

    /** Url to use for the search. */
    protected String serviceUrl;

    /** Logger. */
    private static final Logger log = Logger.getLogger(SimbadTargetResolver.class);

    public static void main(String[] args) {
        try {
            TargetFk4Coordinates coords = new SimbadTargetResolver().resolve("GS2000+25");
            log.info("RA B1950 " + coords.getRaDeg() + "deg");
            log.info("DEC B1950 " + coords.getDecDeg() + "deg");
        } catch (Exception e) {
            log.error("Error resolving target GS2000+25", e);
        }
    }

    public SimbadTargetResolver() {
        this(SIMBAD_URL);
    }

    public SimbadTargetResolver(String url) {
        this.serviceUrl = url;
    }

    public TargetFk4Coordinates resolve(String target) throws TargetNotResolvedException {
        TargetFk4Coordinates coords = null;
        try {
            coords = parseResponse(callService(target));
            coords.setTargetName(target);
            return coords;
        } catch (Exception e) {
            throw new TargetNotResolvedException(target, e);
        }
    }

    private String callService(String target) throws IOException {
        // Build the URL to GET
        String nameEncoded = URLEncoder.encode(target.trim(), "UTF-8");
        URL url = new URL(this.serviceUrl + nameEncoded);
        log.debug("Calling Service at [" + url.toString() + "] ...");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setRequestMethod("GET");
        connection.connect();
        InputStream responseBodyStream = connection.getInputStream();
        StringBuffer responseBody = new StringBuffer();
        byte buffer[] = new byte[8192];

        // Get the entire VOTable in a String
        int bytes;
        while ((bytes = responseBodyStream.read(buffer)) != -1) {
            responseBody.append(new String(buffer, 0, bytes));
        }
        connection.disconnect();

        // log.info("Service at [" + this.serviceUrl + "] returns:\n" +
        // responseBody.toString());
        return responseBody.toString();
    }

    private TargetFk4Coordinates parseResponse(String response) throws IllegalArgumentException {
        TargetFk4Coordinates coords = new TargetFk4Coordinates();
        // split response in lines
        String lines[] = response.split("\\r?\\n");
        log.debug(lines.length + " lines(s) found");
        // strip RA and DEC in B1950 out from response
        String raRegex = "(\\d{2}\\s\\d{2}\\s\\d{2}(\\.\\d+)+)";
        String decRegex = "((\\+|\\-)\\d{2}\\s\\d{2}\\s\\d{2}(\\.\\d+)+)";
        String regex = "^Coordinates\\(FK4,ep=B1950,eq=1950\\):\\s*" + raRegex + "\\s*" + decRegex
                + ".*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = null;
        boolean targetResolved = false;
        for (int i = 0; i < lines.length; i++) {
            matcher = pattern.matcher(lines[i].trim());
            if (matcher.matches()) {
                log.debug("Line matching pattern found " + lines[i]);
                log.debug("RA[deg]" + matcher.group(1));
                log.debug("DEC[deg]" + matcher.group(3));
                coords.setRaDeg(getRaDeg(matcher.group(1)));
                coords.setDecDeg(getDecDeg(matcher.group(3)));
                targetResolved = true;
                break;
            }
        }
        if (!targetResolved) {
            throw new IllegalArgumentException("FK4 coordinates not found in the reponse");
        }
        return coords;
    }

    private double getRaDeg(final String raHours) {
        String splitString = "&&";
        String raTrimmed = raHours.replaceAll("\\s+", splitString);
        String[] coordinates = raTrimmed.split(splitString);
        // String[] coordinates = inputRa.split(" ");
        Double hours = 0.0;
        Double minutes = 0.0;
        Double seconds = 0.0;
        if (coordinates != null) {
            if (coordinates.length > 0) {
                hours = Double.parseDouble(coordinates[0]);
            }
            if (coordinates.length > 1) {
                minutes = Double.parseDouble(coordinates[1]);
            }
            if (coordinates.length > 2) {
                seconds = Double.parseDouble(coordinates[2]);
            }
            return (hours + (minutes / 60.0) + (seconds / 3600.0)) * 15.0;
        } else {
            return 0;
        }
    }

    private double getDecDeg(final String decDegrees) {
        String splitString = "&&";
        String decTrimmed = decDegrees.replaceAll("\\s+", splitString);
        String[] coordinates = decTrimmed.split(splitString);
        // String[] coordinates = inputDec.split(" ");
        Double deg = 0.0;
        Double arcmin = 0.0;
        Double arcsec = 0.0;
        if (coordinates != null) {
            if (coordinates.length > 0) {
                deg = Double.parseDouble(coordinates[0]);
            }
            if (coordinates.length > 1) {
                arcmin = Double.parseDouble(coordinates[1]);
            }
            if (coordinates.length > 2) {
                arcsec = Double.parseDouble(coordinates[2]);
            }
            return (deg + (arcmin / 60.0) + (arcsec / 3600.0));
        } else {
            return 0;
        }
    }
}