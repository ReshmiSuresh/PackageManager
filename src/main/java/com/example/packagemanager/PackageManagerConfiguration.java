package com.example.packagemanager;

import com.yammer.dropwizard.config.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class PackageManagerConfiguration extends Configuration {
    @NotEmpty
    private String gitRepo;

    @NotEmpty
    private String localRepo;

    public String getLocalRepo() {
        return localRepo;
    }

    public void setLocalRepo(String localRepo) {
        this.localRepo = localRepo;
    }

    public String getGitRepo() {
        return gitRepo;
    }

    public void setGitRepo(String gitRepo) {
        this.gitRepo = gitRepo;
    }
}
