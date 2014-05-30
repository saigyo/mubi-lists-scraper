/*
 * Copyright 2014 Markus Ackermann <markus@kaixo.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package de.kaixo.mubi.lists.store;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;

public class MubiListsToElasticSearchLoader
{
    private static final Logger logger = LoggerFactory.getLogger(MubiListsToElasticSearchLoader.class);

    public static final String INDEX_NAME = "mubi";
    public static final String TYPE = "list";

    private static final String LIST_MAPPING_JSON = "mubi-list-mapping.json";
    private static final String INDEX_SETTINGS_JSON = "mubi-index-settings.json";

    private final Client client;

    private final JsonFactory factory = new JsonFactory();

    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);

    public MubiListsToElasticSearchLoader()
    {
        client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
    }

    public boolean prepareIndex(boolean dropAndCreate)
    {
        final IndicesAdminClient indexClient = client.admin().indices();

        if (indexClient.exists(new IndicesExistsRequest(INDEX_NAME)).actionGet().isExists()) {
            if (dropAndCreate) {
                indexClient.delete(new DeleteIndexRequest(INDEX_NAME)).actionGet();
            } else {
                return false;
            }
        }

        InputStream indexSettingsStream = MubiListsToElasticSearchLoader.class.getResourceAsStream(INDEX_SETTINGS_JSON);
        if (indexSettingsStream == null) {
            logger.error("Resource not found: " + INDEX_SETTINGS_JSON);
            return false;
        }

        Settings indexSettings = ImmutableSettings.builder().loadFromStream(INDEX_SETTINGS_JSON, indexSettingsStream).build();

        InputStream mappingInputStream = MubiListsToElasticSearchLoader.class.getResourceAsStream(LIST_MAPPING_JSON);
        if (mappingInputStream == null) {
            logger.error("Resource not found: " + LIST_MAPPING_JSON);
            return false;
        }

        try {
            String mapping = CharStreams.toString(new InputStreamReader(mappingInputStream, "UTF-8"));
            CompletableFuture<? extends ActionResponse> future = supplyAsync(
                    indexClient.prepareCreate(INDEX_NAME).setSettings(indexSettings).addMapping(TYPE, mapping)::get).thenApply(
                    r -> indexClient.prepareFlush(INDEX_NAME).get());
            future.join();
        } catch (IOException e) {
            // We do not expect this to happen!
            logger.error("This should not have happened!", e);
            return false;
        } catch (ElasticsearchException e) {
            logger.error("While trying to create index " + INDEX_NAME + ": ", e);
            return false;
        } catch (CompletionException e) {
            logger.error("While trying to create index " + INDEX_NAME + ": ", e);
            return false;
        }
        return true;
    }

    public void loadAllFromDir(Path dir)
    {
        try {
            Files.newDirectoryStream(dir, "*.json").forEach(this::loadFromJson);
        } catch (IOException e) {
            logger.error("Failing to process " + dir.toString());
        }
    }

    public void loadFromJson(Path path)
    {
        logger.info("Processing " + path.toString());
        try {
            JsonNode node = mapper.readTree(path.toFile());
            if (node.isArray()) {
                node.elements().forEachRemaining(elem -> {
                    StringWriter writer = new StringWriter();
                    try {
                        JsonGenerator generator = factory.createGenerator(writer);
                        mapper.writeTree(generator, elem);
                        client.prepareIndex(INDEX_NAME, TYPE).setSource(writer.toString()).get();
                    } catch (IOException e) {
                        logger.error("Failed to write item", e);
                    }
                });
            }
        } catch (IOException e) {
            logger.error("Failed to process " + path.toString(), e);
        }
    }

    public void close()
    {
        client.close();
    }

    public static void main(String arg[])
    {
        if (arg.length < 1) {
            logger.error("Usage: MubiListsToElasticSearchLoader <directory>");
            System.exit(1);
        }

        Path path = Paths.get(arg[0]);

        if (!Files.isDirectory(path)) {
            logger.error("Does not exist or is not a directory: " + path.toString());
            System.exit(1);
        }

        MubiListsToElasticSearchLoader loader = new MubiListsToElasticSearchLoader();
        if (loader.prepareIndex(true))
            loader.loadAllFromDir(path);
        loader.close();
    }
}
