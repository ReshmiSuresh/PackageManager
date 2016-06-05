package com.example.packagemanager.cli;

import com.example.packagemanager.PackageManagerConfiguration;
import com.google.common.base.Preconditions;
import com.yammer.dropwizard.cli.ConfiguredCommand;
import com.yammer.dropwizard.config.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UpdateCommand extends ConfiguredCommand<PackageManagerConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstallCommand.class);

    public UpdateCommand() {
        super("update", "Update package list and dependencies");
    }

    @Override
    public void configure(Subparser subparser) {
        subparser.addArgument("file").nargs("?")
                .help("package manager configuration file");
    }

    @Override
    protected void run(
            Bootstrap<PackageManagerConfiguration> bootstrap,
            Namespace namespace,
            PackageManagerConfiguration configuration) throws Exception {
        Path localRepoPath = Paths.get(configuration.getLocalRepo());

        try {
            Git.cloneRepository()
                    .setURI(configuration.getGitRepo())
                    .setDirectory(new File(localRepoPath.toAbsolutePath().toString()))
                    .call();
        } catch (JGitInternalException e) {
            // repo already exists, just pull
        }

        Repository repository = new FileRepositoryBuilder()
                .setMustExist(true)
                .setGitDir(new File(localRepoPath.resolve(".git").toAbsolutePath().toString()))
                .build();
        Git git = new Git(repository);

        checkRemoteUrl(configuration, git);
        PullCommand pull = git.pull();
        PullResult call = pull.call();
        if (call.isSuccessful()) {
            LOGGER.info("Pulling was successful");
        } else {
            LOGGER.error("Pulling was unsuccessful " + call.toString());
        }
    }

    private void checkRemoteUrl(PackageManagerConfiguration configuration, Git git) throws IOException {
        StoredConfig config = git.getRepository().getConfig();
        String remoteUrl = config.getString("remote", "origin", "url");
        if (remoteUrl == null) {
            config.setString("remote", "origin", "url", configuration.getGitRepo());
            return;
        }
        Preconditions.checkState(configuration.getGitRepo().equals(remoteUrl),
                "Remote URL is set to %s. It should be %s", remoteUrl, configuration.getGitRepo());
    }
}
