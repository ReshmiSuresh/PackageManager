package com.example.packagemanager.resolver;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;

public class DependencyResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyResolver.class);

    private final String packageToDownload;
    private final Path basePath;

    public DependencyResolver(String packageToDownload, Path basePath) {
        this.packageToDownload = packageToDownload;
        this.basePath = basePath;
    }

    private void dfs(String packageToDownload, LinkedHashMap<String, URI> packageToURI) {
        URI selfPath = null;
        try (BufferedReader reader = Files.newBufferedReader(basePath.resolve(packageToDownload), Charset.defaultCharset())) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    selfPath = URI.create(line);
                    firstLine = false;
                } else {
                    if (!packageToURI.containsKey(line)) {
                        dfs(line, packageToURI);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Could not open dependency file for %s. Check if it exists", packageToDownload, e);
        }

        Preconditions.checkState(
                !packageToURI.containsKey(packageToDownload),
                packageToDownload + " requires itself. Cannot resolve cyclic dependencies");

        packageToURI.put(packageToDownload, selfPath);
    }

    /**
     * Performs a depth first search to get a list of all required dependencies in the order they should be installed.
     *
     * @return Linked hash map showing in order what dependencies need to be downloaded along with their locations
     * @throws IOException           if the dependency file does not exist or cannot be opened
     * @throws IllegalStateException if there is a cyclic dependency
     */
    public LinkedHashMap<String, URI> getDownloadList() {
        LinkedHashMap<String, URI> packageToURI = Maps.<String, URI>newLinkedHashMap();
        dfs(packageToDownload, packageToURI);
        return packageToURI;
    }
}
