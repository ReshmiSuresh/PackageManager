package com.example.packagemanager;

import com.example.packagemanager.cli.InstallCommand;
import com.example.packagemanager.cli.UpdateCommand;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class PackageManagerService extends Service<PackageManagerConfiguration> {
    public static void main(String[] args) throws Exception {
        new PackageManagerService().run(args);
    }

    @Override
    public void initialize(Bootstrap<PackageManagerConfiguration> bootstrap) {
        bootstrap.setName("package-manager");
        bootstrap.addCommand(new InstallCommand());
        bootstrap.addCommand(new UpdateCommand());
        bootstrap.addBundle(new AssetsBundle());
    }

    @Override
    public void run(
            PackageManagerConfiguration configuration,
            Environment environment) throws ClassNotFoundException {
    }
}
