/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jackrabbit.vault.cli;

import java.io.File;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.CommandBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.option.Command;
import org.apache.jackrabbit.vault.vlt.VltContext;
import org.apache.jackrabbit.vault.vlt.actions.Commit;

/**
 * Implements the 'export' command.
 *
 */
public class CmdCommit extends AbstractVaultCommand {

    private Option optNonRecursive;
    private Argument argLocalPath;
    private Option optForce;

    @SuppressWarnings("unchecked")
    protected void doExecute(VaultFsApp app, CommandLine cl) throws Exception {
        List<String> localPaths = cl.getValues(argLocalPath);
        List<File> localFiles = app.getPlatformFiles(localPaths, false);
        File localDir = app.getPlatformFile("", true);

        VltContext vCtx = app.createVaultContext(localDir);
        vCtx.setVerbose(cl.hasOption(OPT_VERBOSE));
        vCtx.setQuiet(cl.hasOption(OPT_QUIET));
        Commit s = new Commit(localDir,
                localFiles,
                cl.hasOption(optNonRecursive),
                cl.hasOption(optForce));
        vCtx.execute(s);
    }

    /**
     * {@inheritDoc}
     */
    public String getShortDescription() {
        return "Send changes from your working copy to the repository.";
    }

    protected Command createCommand() {
        return new CommandBuilder()
                .withName("commit")
                .withName("ci")
                .withDescription(getShortDescription())
                .withChildren(new GroupBuilder()
                        .withName("Options:")
                        .withOption(OPT_VERBOSE)
                        .withOption(OPT_QUIET)
                        .withOption(optForce = new DefaultOptionBuilder()
                                .withLongName("force")
                                .withDescription("force comitting even if remote is modified")
                                .create())
                        .withOption(optNonRecursive = new DefaultOptionBuilder()
                                .withShortName("N")
                                .withLongName("non-recursive")
                                .withDescription("operate on single directory")
                                .create())
                        .withOption(argLocalPath = new ArgumentBuilder()
                                .withName("file")
                                .withDescription("file or directory to commit")
                                .withMinimum(0)
                                .create()
                        )
                        .create()
                )
                .create();
    }
}