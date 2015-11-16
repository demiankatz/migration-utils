/*
 * Copyright 2015 DuraSpace, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fcrepo.migration.foxml;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * An extension of DirectoryScanningIDResolver for datastream directories of fedora
 * repositories using the akubra-fs storage implementation.
 *
 * @author mdurbin
 */
public class AkubraFSIDResolver extends DirectoryScanningIDResolver {

    /**
     * Basic constructor.
     * @param indexDir A directory that will serve as a lucene index directory to cache ID resolution.
     * @param dsRoot the root directory of the AkubraFS datastream store.
     * @throws IOException
     */
    public AkubraFSIDResolver(final File indexDir, final File dsRoot) throws IOException {
        super(indexDir, dsRoot);
    }

    /**
     * Basic constructor.
     * @param dsRoot the root directory of the AkubraFS datastream store.
     * @throws IOException
     */
    public AkubraFSIDResolver(final File dsRoot) throws IOException {
        super(null, dsRoot);
    }

    @Override
    protected String getInternalIdForFile(final File f) {
        String id = f.getName();
        try {
            id = URLDecoder.decode(id, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (!id.startsWith("info:fedora/")) {
            throw new IllegalArgumentException(f.getName()
                    + " does not appear to be a valid akubraFS datastream file!");
        }
        id = id.substring("info:fedora/".length());
        id = id.replace('/', '+');
        return id;
    }
}
