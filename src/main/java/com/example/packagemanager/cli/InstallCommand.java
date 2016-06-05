package com.example.packagemanager.cli;

import com.example.packagemanager.PackageManagerConfiguration;
import com.example.packagemanager.resolver.DependencyResolver;
import com.yammer.dropwizard.cli.ConfiguredCommand;
import com.yammer.dropwizard.config.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class InstallCommand extends ConfiguredCommand<PackageManagerConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstallCommand.class);

    public InstallCommand() {
        super("install", "Install requested package and all it's dependencies");
    }

    @Override
    public void configure(Subparser subparser) {
        subparser.addArgument("file").nargs("?").help("package manager configuration file");
        subparser.addArgument("install").help("package to install");
    }

    @Override
    protected void run(
            Bootstrap<PackageManagerConfiguration> bootstrap,
            Namespace namespace,
            PackageManagerConfiguration configuration) throws Exception {
        Path path = Paths.get(configuration.getLocalRepo());

        DependencyResolver dependencyResolver = new DependencyResolver(namespace.getString("install"), path);

        LinkedHashMap<String, URI> packageToURIMap = dependencyResolver.getDownloadList();

        for (Map.Entry<String, URI> packageToURI : packageToURIMap.entrySet()) {
            System.out.println((packageToURI.getKey() + ": " + packageToURI.getValue()));
        }
    }
}
