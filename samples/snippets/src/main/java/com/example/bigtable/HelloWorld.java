/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bigtable;

// [START bigtable_hw_imports]

import static com.google.cloud.bigtable.data.v2.models.Filters.FILTERS;

import com.google.api.core.ApiFuture;
import com.google.api.gax.batching.Batcher;
import com.google.api.gax.rpc.NotFoundException;
import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.Filters;
import com.google.cloud.bigtable.data.v2.models.Filters.ChainFilter;
import com.google.cloud.bigtable.data.v2.models.Filters.Filter;
import com.google.cloud.bigtable.data.v2.models.KeyOffset;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Range;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import com.google.protobuf.ByteString;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.management.relation.RelationNotFoundException;

// [END bigtable_hw_imports]

/**
 * An example of using Google Cloud Bigtable.
 *
 * <p>This example is a very simple "hello world" application, that illustrates how to create a new
 * table, write to the table, read the data back, and delete the table.
 *
 * <ul>
 *   <li>create table
 *   <li>read single row
 *   <li>read table
 *   <li>delete table
 * </ul>
 */
public class HelloWorld {

  private static final String COLUMN_FAMILY = "cf1";
  private static final String COLUMN_QUALIFIER_GREETING = "greeting";
  private static final String COLUMN_QUALIFIER_NAME = "name";
  private static final String ROW_KEY_PREFIX = "rowKey";
  private final String tableId;
  private final BigtableDataClient dataClient;
  private final BigtableTableAdminClient adminClient;

  public static void main(String[] args) throws Exception {

    if (args.length != 3) {
      System.out.println("Requires exactly 3 args: projectId, instanceId and tableId");
      return;
    }
    String projectId = args[0];
    String instanceId = args[1];
    String tableId = args[2];

    HelloWorld helloWorld = new HelloWorld(projectId, instanceId, tableId);
    helloWorld.run();
  }

  public HelloWorld(String projectId, String instanceId, String tableId) throws IOException {
    this.tableId = tableId;

    // [START bigtable_hw_connect]
    // Creates the settings to configure a bigtable data client.
    BigtableDataSettings settings =
        BigtableDataSettings.newBuilder().setProjectId(projectId).setInstanceId(instanceId).build();

    // Creates a bigtable data client.
    dataClient = BigtableDataClient.create(settings);

    // Creates the settings to configure a bigtable table admin client.
    BigtableTableAdminSettings adminSettings =
        BigtableTableAdminSettings.newBuilder()
            .setProjectId(projectId)
            .setInstanceId(instanceId)
            .build();

    // Creates a bigtable table admin client.
    adminClient = BigtableTableAdminClient.create(adminSettings);
    // [END bigtable_hw_connect]
  }

  public void run() throws Exception {
    System.out.println("Running HelloWorld");

    // sampleRowKeys();
    // readRange("9985", "9991");

    // createTable();

    // writeSingleRow("row0");

    // writeToTable();

    // writeBigRow();
    // readSingleRow("bigrow");

    // batchMutation();

    // readSpecificCells();
    readTable(1);
    // deleteSingleRow("bigrow");
    // deleteTable();

    // System.out.println(System.getProperty("user.dir"));
    // List<String> rowkeys = loadRowKeys("rowkeys.txt");
    // List<String> families = loadColumnFamily(rowkeys.get(0));
    // List<String> columns = loadColumns(rowkeys.get(0));
    // readRandomRows(rowkeys, families.get(0), columns, 100, 60 * 5, 2000);

    // testGson();

    close();
  }

  public void close() {
    dataClient.close();
    adminClient.close();
  }

  public List<String> loadRowKeys(String filename) throws IOException {
    File file = new File(filename);
    Scanner sc = new Scanner(file);

    List<String> list = Lists.newArrayList();

    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      // System.out.println(line);
      list.add(line);
    }

    System.out.println("Loaded " + list.size() + " rowkeys.");

    return list;
  }

  List<String> loadColumnFamily(String rowkey) {
    Set<String> columnFamilies = Sets.newHashSet();
    Row row = dataClient.readRow(tableId, rowkey);
    for (RowCell cell : row.getCells()) {
      columnFamilies.add(cell.getFamily());
    }
    System.out.println("Families: " + columnFamilies);
    return Lists.newArrayList(columnFamilies);
  }

  List<String> loadColumns(String rowkey) {
    List<String> columns = Lists.newArrayList();
    Row row = dataClient.readRow(tableId, rowkey);
    for (RowCell cell : row.getCells()) {
      String qualifier = cell.getQualifier().toStringUtf8();
      if (!qualifier.contains("rep")) {
        columns.add(cell.getQualifier().toStringUtf8());
      }
    }
    System.out.println("Columns: " + columns);
    return columns;
  }

  public void sampleRowKeys() {
    List<KeyOffset> offsets = dataClient.sampleRowKeys(tableId);
    for (KeyOffset offset : offsets) {
      System.out.println("key: " + offset.getKey() + " offset: " + offset.getOffsetBytes());
    }
    System.out.println("number of key ranges: " + offsets.size());
  }

  /**
   * Demonstrates how to create a table.
   */
  public void createTable() {
    // [START bigtable_hw_create_table]
    // Checks if table exists, creates table if does not exist.
    if (!adminClient.exists(tableId)) {
      System.out.println("Creating table: " + tableId);
      CreateTableRequest createTableRequest =
          CreateTableRequest.of(tableId).addFamily(COLUMN_FAMILY);
      adminClient.createTable(createTableRequest);
      System.out.printf("Table %s created successfully%n", tableId);
    }
    // [END bigtable_hw_create_table]
  }

  public void writeSingleRow(String rowKey) {
    long timestamp = 1234000;
    ByteString value = ByteString.copyFromUtf8("hello2");
    ByteString columnQualifier = ByteString.copyFromUtf8("col");
    RowMutation mutation = RowMutation.create(tableId, rowKey)
        .deleteCells(COLUMN_FAMILY, columnQualifier, Range.TimestampRange.unbounded())
        .setCell(COLUMN_FAMILY, columnQualifier, timestamp, value);
    dataClient.mutateRow(mutation);
  }

  /**
   * Demonstrates how to write some rows to a table.
   */
  public void writeToTable() {
    // [START bigtable_hw_write_rows]
    try {
      System.out.println("\nWriting some greetings to the table");
      String[] names = {"World", "Bigtable", "Java"};
      for (int i = 0; i < names.length; i++) {
        String greeting = "Hello " + names[i] + "!";
        RowMutation rowMutation =
            RowMutation.create(tableId, ROW_KEY_PREFIX + i)
                .setCell(COLUMN_FAMILY, COLUMN_QUALIFIER_NAME, names[i])
                .setCell(COLUMN_FAMILY, COLUMN_QUALIFIER_GREETING, greeting);
        dataClient.mutateRow(rowMutation);
        System.out.println(greeting);
      }
    } catch (NotFoundException e) {
      System.err.println("Failed to write to non-existent table: " + e.getMessage());
    }
    // [END bigtable_hw_write_rows]
  }

  public void writeBigRow() {
    try {

      System.out.println("\nWriting a big row to the table");

      final SecureRandom random = new SecureRandom();

      final int BYTES_IN_A_MB = 1024 * 1024;
      byte[] randomData = new byte[101 * BYTES_IN_A_MB];

      for (int i = 0; i < 30; i++) {
        random.nextBytes(randomData);
        // System.out.println(randomData);

        RowMutation rowMutation =
            RowMutation.create(tableId, "bigrow")
                .setCell(COLUMN_FAMILY, ByteString.copyFromUtf8("bigcell" + i), 0,
                    ByteString.copyFrom(randomData));
        dataClient.mutateRow(rowMutation);

      }

    } catch (Exception e) {
      System.out.println("writeBigRow failed: " + e.getMessage());
    }
  }

  public void batchMutation() {
    try (Batcher<RowMutationEntry, Void> batcher = dataClient.newBulkMutationBatcher(tableId)) {
      List<ApiFuture<Void>> futures = new ArrayList<>();
      for (int i = 0; i < 3; i++) {
        futures.add(
            batcher.add(
                RowMutationEntry.create("row" + i)
                    .setCell(COLUMN_FAMILY, COLUMN_QUALIFIER_NAME, 0, "val" + i)));
      }

      // Blocks until mutations are applied on all submitted row entries.
      batcher.flush();

      for (ApiFuture<Void> future : futures) {
        System.out.println("Waiting for future to finish");
        future.get();
      }

    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

  }

  public void deleteSingleRow(String rowkey) {
    try {
      RowMutation rowMutation = RowMutation.create(tableId, "bigrow").deleteRow();
      dataClient.mutateRow(rowMutation);
    } catch (Exception e) {
      System.out.println("deleteSingleRow failed: " + e.getMessage());
    }

  }

  /**
   * Demonstrates how to read a single row from a table.
   */
  public Row readSingleRow() {
    return readSingleRow(ROW_KEY_PREFIX + 0);
  }

  public Row readSingleRow(String rowkey) {
    // [START bigtable_hw_get_by_key]
    try {
      System.out.println("\nReading a single row by row key");

      // ChainFilter filter = FILTERS.chain().filter(FILTERS.key().exactMatch("rowkey")).filter(FILTERS.limit().cellsPerRow(1));
      // Query query = Query.create(tableId).filter(filter);
      //
      // ServerStream<Row> rows = dataClient.readRows(query);
      // rows.
      //
      // Row row = rows.iterator().next();

      Filter filter = FILTERS.chain()
          .filter(FILTERS.key().exactMatch(rowkey))
          .filter(FILTERS.limit().cellsPerRow(1));

      Filter filter2 = FILTERS.limit().cellsPerRow(1);

      Row row = dataClient.readRow(tableId, rowkey, filter);

      System.out.println("Row: " + row.getKey().toStringUtf8());
      int size = 0;
      for (RowCell cell : row.getCells()) {
        size += cell.getValue().size();
        System.out.printf(
            "Family: %s    Qualifier: %s    Value-size: %s%n",
            cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getValue().size());

        // System.out.printf(
        //     "Family: %s    Qualifier: %s    Value: %s%n",
        //     cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getValue().toStringUtf8());
      }
      System.out.println("Row size=" + size);
      return row;
    } catch (NotFoundException e) {
      System.err.println("Failed to read from a non-existent table: " + e.getMessage());
      return null;
    }
    // [END bigtable_hw_get_by_key]
  }

  /**
   * Demonstrates how to access specific cells by family and qualifier.
   */
  public List<RowCell> readSpecificCells() {
    // [START bigtable_hw_get_by_key]
    try {
      System.out.println("\nReading specific cells by family and qualifier");
      Row row = dataClient.readRow(tableId, ROW_KEY_PREFIX + 0);
      System.out.println("Row: " + row.getKey().toStringUtf8());
      List<RowCell> cells = row.getCells(COLUMN_FAMILY, COLUMN_QUALIFIER_NAME);
      for (RowCell cell : cells) {
        System.out.printf(
            "Family: %s    Qualifier: %s    Value: %s%n",
            cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getValue().toStringUtf8());
      }
      return cells;
    } catch (NotFoundException e) {
      System.err.println("Failed to read from a non-existent table: " + e.getMessage());
      return null;
    }
    // [END bigtable_hw_get_by_key]
  }

  /**
   * Demonstrates how to read an entire table.
   */
  public List<Row> readRange(String start, String end) {
    // [START bigtable_hw_scan_all]
    try {
      int count = 0;
      System.out.println("\nReading key range from " + start + " to " + end);
      Query query = Query.create(tableId).range(start, end);
      ServerStream<Row> rowStream = dataClient.readRows(query);
      List<Row> tableRows = new ArrayList<>();
      for (Row r : rowStream) {
        count++;
        System.out.println("Row Key: " + r.getKey().toStringUtf8());
        tableRows.add(r);
        for (RowCell cell : r.getCells()) {
          // System.out.printf(
          //     "Family: %s    Qualifier: %s    Value: %s%n",
          //     cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getValue().toStringUtf8());
          System.out.printf(
              "Family: %s    Qualifier: %s",
              cell.getFamily(), cell.getQualifier().toStringUtf8());
        }
      }
      System.out.println("Number of rows read: " + count);
      return tableRows;
    } catch (NotFoundException e) {
      System.err.println("Failed to read a non-existent table: " + e.getMessage());
      return null;
    }
    // [END bigtable_hw_scan_all]
  }

  /**
   * Demonstrates how to read an entire table.
   */
  public void readTable() {
    readTable(Integer.MAX_VALUE);
  }

  public void readTable(int rowsLimit) {
    // [START bigtable_hw_scan_all]
    try {
      System.out.println("\nReading first " + rowsLimit + " rows in table " + tableId);
      Query query = Query.create(tableId).limit(rowsLimit);
      // Query query = Query.create(tableId).limit(rowsLimit);
      ServerStream<Row> rowStream = dataClient.readRows(query);
      int rowNum = 0;
      for (Row r : rowStream) {
        System.out.println("Row Key: " + r.getKey().toStringUtf8());
        rowNum++;
        int cellCount = 0;
        int rowSize = 0;
        for (RowCell cell : r.getCells()) {
          cellCount++;
          int cellSize = r.getKey().size() + cell.getFamily().length() + cell.getQualifier().size()
              + cell.getValue().size() + 8;
          System.out.printf(
              "Rowkey: %s (%d bytes)  Family: %s (%d bytes)   Qualifier: %s (%d bytes)   Value: %s (%d bytes)  TS: %d (%d bytes)  Total cell size: %d bytes\n",
              r.getKey().toStringUtf8(), r.getKey().size(), cell.getFamily(),
              cell.getFamily().length(), cell.getQualifier().toStringUtf8(),
              cell.getQualifier().size(),
              cell.getValue().toStringUtf8(),
              cell.getValue().size(), cell.getTimestamp(), 8, cellSize);
          rowSize += cellSize;
        }
        System.out.println("Number of cells: " + cellCount + " row size: " + rowSize + " bytes");
      }
      System.out.println("row " + rowNum);
      return;
    } catch (NotFoundException e) {
      System.err.println("Failed to read a non-existent table: " + e.getMessage());
      return;
    }
    // [END bigtable_hw_scan_all]
  }

  /**
   * Demonstrates how to delete a table.
   */
  public void deleteTable() {
    // [START bigtable_hw_delete_table]
    System.out.println("\nDeleting table: " + tableId);
    try {
      adminClient.deleteTable(tableId);
      System.out.printf("Table %s deleted successfully%n", tableId);
    } catch (NotFoundException e) {
      System.err.println("Failed to delete a non-existent table: " + e.getMessage());
    }
    // [END bigtable_hw_delete_table]
  }

  enum State {
    NONE,
    IN_TOKEN,
    IN_SET,
  }

  public static void appendIndent(StringBuilder sb, int level) {
    for (int i = 0; i < level; i++) {
      sb.append("  ");
    }
  }

  public static class ReadRandomRowsRunnable implements Runnable {

    private Thread t;
    private String threadName;
    private int duration_seconds;
    BigtableDataClient dataClient;
    String tableId;
    List<String> rowkeys;
    String family;
    List<String> columns;
    int numColumns;

    ReadRandomRowsRunnable(String name, int duration_seconds, BigtableDataClient dataClient,
        String tableId, List<String> rowkeys, String family, List<String> columns, int numColumns) {
      threadName = name;
      this.duration_seconds = duration_seconds;
      this.dataClient = dataClient;
      this.tableId = tableId;
      this.rowkeys = rowkeys;
      this.family = family;
      this.columns = columns;
      this.numColumns = numColumns;
      System.out.printf("Creating thread %s, table=%s, duration=%d\n", threadName, tableId,
          duration_seconds);
    }

    List<String> getRandomColumns(List<String> columns, int numColumns) {

      List<Integer> ints = Lists.newArrayList();

      IntConsumer consumer = a -> ints.add(a);

      ThreadLocalRandom.current()
          .ints(0, 1000).distinct().limit(numColumns / 5)
          .forEach(consumer);
      Collections.sort(ints);
      // System.out.println("kk89" + ints);
      List<String> selectedColumns = Lists.newArrayList();
      for (int i : ints) {
        selectedColumns.add(columns.get(i));
      }
      System.out.println("Selected columns: " + selectedColumns);
      return selectedColumns;
    }

    Filter createFilter(String family, List<String> columns) {
      // If the needed columns have a common prefix:
      // Filter f1 = FILTERS.qualifier().rangeWithinFamily("my-family")
      //     .of(
      //         ByteString.copyFromUtf8("col-prefix"),
      //         ByteString.copyFromUtf8("col-preifx")
      //             .concat(ByteString.copyFrom(new byte[]{0})));

      // Using exact names or regex
      Filters.InterleaveFilter interleaveFilter = FILTERS.interleave();
      for (int i = 0; i < columns.size(); i++) {
        // System.out.println("add qualifier regex=" + columns.get(i) + ".*");
        // interleaveFilter.filter(FILTERS.qualifier().regex(columns.get(i) + ".*"));
        interleaveFilter.filter(FILTERS.qualifier().exactMatch(columns.get(i)));
      }

      return FILTERS.chain()
          .filter(FILTERS.family().exactMatch(family))
          .filter(interleaveFilter);
    }

    @Override
    public void run() {
      try {
        long endTime = System.currentTimeMillis() + duration_seconds * 1000;
        long countTime = System.currentTimeMillis() + 1000;

        long requestCount = 0;

        List<String> selectedColumns = getRandomColumns(columns, numColumns);

        Random rand = new Random();

        while (System.currentTimeMillis() < endTime) {
          if (System.currentTimeMillis() >= countTime) {
            countTime = System.currentTimeMillis() + 1000;
            System.out.printf("Thread=%s time=%s requests=%d\n", threadName,
                new Timestamp(System.currentTimeMillis()), requestCount);
            requestCount = 0;
          }

          Filter filter = createFilter(family, selectedColumns);

          Row row = dataClient.readRow(tableId, rowkeys.get(rand.nextInt(rowkeys.size())), filter);
          // System.out.println(row.getCells().size() + "\n\n\n");

          requestCount++;

          // System.out.printf("Thread %s rowkey %s\n", threadName, row.getKey().toStringUtf8());
        }

      } catch (NotFoundException e) {
        System.err.println("Exception: " + e.getMessage());
      }
    }

    public void start() {
      System.out.println("Starting thread " + threadName);
      if (t == null) {
        t = new Thread(this, threadName);
        t.start();
      }
    }

    public void join() throws InterruptedException {
      if (t != null) {
        t.join();
      }
    }
  }

  public void readRandomRows(List<String> rowkeys, String family, List<String> columns,
      int num_threads,
      int duration_seconds, int numColumns)
      throws InterruptedException {
    // ExecutorService executor = Executors.newFixedThreadPool(50);

    List<ReadRandomRowsRunnable> runnables = Lists.newArrayList();
    for (int i = 0; i < num_threads; i++) {
      ReadRandomRowsRunnable runnable = new ReadRandomRowsRunnable("t-" + i, duration_seconds,
          dataClient,
          tableId, rowkeys, family, columns, numColumns);
      runnable.start();
      runnables.add(runnable);
    }
    for (int i = 0; i < num_threads; i++) {
      runnables.get(i).join();
    }


    /*
    try {
      long endTime = System.currentTimeMillis() + duration_seconds * 1000;
      long countTime = System.currentTimeMillis() + 1000;

      long requestCount = 0;

      Random rand = new Random();
      while (System.currentTimeMillis() < endTime) {
        if (System.currentTimeMillis() >= countTime) {
          countTime = System.currentTimeMillis() + 1000;
          System.out.println(new Timestamp(System.currentTimeMillis()) + " : " + requestCount);
          requestCount = 0;
        }

        Row row = dataClient.readRow(tableId, rowkeys.get(rand.nextInt(rowkeys.size())));
        requestCount++;

        System.out.println("Row Key: " + row.getKey().toStringUtf8());
        // int colCount = 0;
        // int rowSize = 0;
        // for (RowCell cell : row.getCells()) {
        //   colCount++;
        //   rowSize += cell.getQualifier().size() + cell.getValue().size() + 8;
          // System.out.printf(
          //     "Family: %s    Qualifier: %s (%d bytes)   Value: %s (%d bytes)  TS: %d (%d bytes)  Total size: %d bytes\n",
          //     cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getQualifier().size(),
          //     cell.getValue().toStringUtf8(),
          //     cell.getValue().size(), cell.getTimestamp(), 8, cell.getQualifier().size() + cell.getValue().size() + 8);
        // }
        // System.out.println("Number of columns: " + colCount + " row size: " + rowSize + " bytes");

      }

    } catch (NotFoundException e) {
      System.err.println("Failed to read from a non-existent table: " + e.getMessage());
    }
    */

  }

  public static String parse(String str) {

    StringBuilder sb = new StringBuilder();
    int indent = 0;
    State state = State.NONE;
    for (char c : str.toCharArray()) {
      switch (c) {
        case '{':
          state = State.NONE;
          sb.append(" {\n");
          appendIndent(sb, ++indent);
          break;
        case '=':
          state = State.NONE;
          sb.append(" = ");
          break;
        case ',':
          if (state == State.IN_SET) {
            sb.append(',');
            break;
          }
          state = State.NONE;
          sb.append(",\n");
          appendIndent(sb, indent);
          break;
        case '}':
          state = State.NONE;
          sb.append("\n");
          appendIndent(sb, --indent);
          sb.append("}");
          break;
        case ' ':
          if (state == State.IN_TOKEN || state == State.IN_SET) {
            sb.append(" ");
          }
          break;
        case '[':
          sb.append('[');
          state = State.IN_SET;
          break;
        case ']':
          sb.append(']');
          state = State.NONE;
          break;
        default:
          if (state != State.IN_SET) {
            state = State.IN_TOKEN;
          }
          sb.append(c);
      }
    }
    return sb.toString();
  }

  public void testGson() {
    ToStringHelper helper = MoreObjects.toStringHelper("TestClass")
        .add("foo", 10)
        .add("obj3",
            MoreObjects.toStringHelper("NestedClass").add("nest1", Sets.newHashSet(1, 2, 3))
                .add("nest2", 456))
        .add("bar", "Weihan Kong");

    // System.out.println("kk regular:\n" + helper);
    System.out.println("kk parsed:\n" + parse(
        "BigtableHBaseVeneerSettings{dataSettings=BigtableDataSettings{stubSettings=EnhancedBigtableStubSettings{projectId=autonomous-mote-782, instanceId=kongwh-df-prod, appProfileId=, isRefreshingChannel=false, primedTableIds=[], jwtAudienceMapping={batch-bigtable.googleapis.com=https://bigtable.googleapis.com/}, readRowsSettings=ServerStreamingCallSettings{idleTimeout=PT5M, retryableCodes=[DEADLINE_EXCEEDED, UNAVAILABLE, ABORTED], retrySettings=RetrySettings{totalTimeout=PT12H, initialRetryDelay=PT0.01S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=10, jittered=true, initialRpcTimeout=PT5M, rpcTimeoutMultiplier=2.0, maxRpcTimeout=PT5M}}, readRowSettings=UnaryCallSettings{retryableCodes=[DEADLINE_EXCEEDED, UNAVAILABLE, ABORTED], retrySettings=RetrySettings{totalTimeout=PT5M, initialRetryDelay=PT0.01S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=10, jittered=true, initialRpcTimeout=PT20S, rpcTimeoutMultiplier=2.0, maxRpcTimeout=PT20S}}, sampleRowKeysSettings=UnaryCallSettings{retryableCodes=[DEADLINE_EXCEEDED, UNAVAILABLE], retrySettings=RetrySettings{totalTimeout=PT5M, initialRetryDelay=PT0.01S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT20S, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT20S}}, mutateRowSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT5M, initialRetryDelay=PT0.01S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT20S, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT20S}}, bulkMutateRowsSettings=BigtableBatchingCallSettings{batchingCallSettings=BatchingCallSettings{retryableCodes=[DEADLINE_EXCEEDED, UNAVAILABLE], retrySettings=RetrySettings{totalTimeout=PT20M, initialRetryDelay=PT0.01S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}, batchingSettings=BatchingSettings{elementCountThreshold=100, requestByteThreshold=20971520, delayThreshold=PT1S, isEnabled=true, flowControlSettings=FlowControlSettings{maxOutstandingElementCount=4000, maxOutstandingRequestBytes=104857600, limitExceededBehavior=Block}}}, isLatencyBasedThrottlingEnabled=false, targetRpcLatency=null, dynamicFlowControlSettings=DynamicFlowControlSettings{initialOutstandingElementCount=4000, initialOutstandingRequestBytes=104857600, maxOutstandingElementCount=4000, maxOutstandingRequestBytes=104857600, minOutstandingElementCount=4000, minOutstandingRequestBytes=104857600, limitExceededBehavior=Block}}, bulkReadRowsSettings=BigtableBulkReadRowsCallSettings{retryableCodes=[DEADLINE_EXCEEDED, UNAVAILABLE, ABORTED], retrySettings=RetrySettings{totalTimeout=PT10M, initialRetryDelay=PT0.01S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT10M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT10M}}, checkAndMutateRowSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT5M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT5M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT5M}}, readModifyWriteRowSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT5M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT5M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT5M}}, pingAndWarmSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT30S, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=1, jittered=true, initialRpcTimeout=PT30S, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT30S}}, parent=EnhancedBigtableStubSettings{backgroundExecutorProvider=InstantiatingExecutorProvider{executorThreadCount=4, threadFactory=com.google.bigtable.repackaged.com.google.api.gax.core.InstantiatingExecutorProvider$1@219e0330}, transportChannelProvider=com.google.bigtable.repackaged.com.google.api.gax.grpc.InstantiatingGrpcChannelProvider@5914cce0, credentialsProvider=GoogleCredentialsProvider{scopesToApply=[https://www.googleapis.com/auth/bigtable.data, https://www.googleapis.com/auth/bigtable.data.readonly, https://www.googleapis.com/auth/cloud-bigtable.data, https://www.googleapis.com/auth/cloud-bigtable.data.readonly, https://www.googleapis.com/auth/cloud-platform, https://www.googleapis.com/auth/cloud-platform.read-only], jwtEnabledScopes=[https://www.googleapis.com/auth/bigtable.data, https://www.googleapis.com/auth/cloud-bigtable.data, https://www.googleapis.com/auth/cloud-platform], useJwtAccessWithScope=true, OAuth2Credentials=null}, headerProvider=FixedHeaderProvider{headers={user-agent=hbase/1.7.2 bigtable-hbase/2.6.4-SNAPSHOT HBaseBeam}}, internalHeaderProvider=FixedHeaderProvider{headers={x-goog-api-client=gl-java/17.0.2 gapic/ gax/2.19.5 grpc/, user-agent=bigtable-java/2.16.0}}, clock=com.google.bigtable.repackaged.com.google.api.core.NanoClock@4307b25, endpoint=batch-bigtable.googleapis.com:443, mtlsEndpoint=bigtable.mtls.googleapis.com:443, switchToMtlsEndpointAllowed=false, quotaProjectId=null, streamWatchdogProvider=com.google.bigtable.repackaged.com.google.api.gax.rpc.InstantiatingWatchdogProvider@1589661c, streamWatchdogCheckInterval=PT10S, tracerFactory=com.google.cloud.bigtable.hbase.wrappers.veneer.metrics.MetricsApiTracerAdapterFactory@42d3915e}}}, tableAdminSettings=BigtableTableAdminSettings{projectId=autonomous-mote-782, instanceId=kongwh-df-prod, createTableSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT5M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT5M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT5M}}, createTableFromSnapshotSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT0S, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT0S, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT0S}}, createTableFromSnapshotOperationSettings=com.google.bigtable.repackaged.com.google.api.gax.rpc.OperationCallSettings@1a7b4582, listTablesSettings=PagedCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, getTableSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, deleteTableSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, modifyColumnFamiliesSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT5M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT5M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT5M}}, dropRowRangeSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1H, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1H, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1H}}, generateConsistencyTokenSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, checkConsistencySettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, getIamPolicySettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, setIamPolicySettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, testIamPermissionsSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, snapshotTableSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT0S, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT0S, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT0S}}, snapshotTableOperationSettings=com.google.bigtable.repackaged.com.google.api.gax.rpc.OperationCallSettings@1d15ecbb, getSnapshotSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, listSnapshotsSettings=PagedCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, deleteSnapshotSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, createBackupSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, createBackupOperationSettings=com.google.bigtable.repackaged.com.google.api.gax.rpc.OperationCallSettings@43f2d083, getBackupSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, listBackupsSettings=PagedCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, updateBackupSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, deleteBackupSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, restoreTableSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, restoreTableOperationSettings=com.google.bigtable.repackaged.com.google.api.gax.rpc.OperationCallSettings@1df5e0c2, stubSettings=BigtableTableAdminStubSettings{backgroundExecutorProvider=InstantiatingExecutorProvider{executorThreadCount=4, threadFactory=com.google.bigtable.repackaged.com.google.api.gax.core.InstantiatingExecutorProvider$1@219e0330}, transportChannelProvider=com.google.bigtable.repackaged.com.google.api.gax.grpc.InstantiatingGrpcChannelProvider@5de2e1ab, credentialsProvider=GoogleCredentialsProvider{scopesToApply=[https://www.googleapis.com/auth/bigtable.admin, https://www.googleapis.com/auth/bigtable.admin.table, https://www.googleapis.com/auth/cloud-bigtable.admin, https://www.googleapis.com/auth/cloud-bigtable.admin.table, https://www.googleapis.com/auth/cloud-platform, https://www.googleapis.com/auth/cloud-platform.read-only], jwtEnabledScopes=[], useJwtAccessWithScope=true, OAuth2Credentials=null}, headerProvider=FixedHeaderProvider{headers={user-agent=hbase/1.7.2 bigtable-hbase/2.6.4-SNAPSHOT HBaseBeam}}, internalHeaderProvider=com.google.bigtable.repackaged.com.google.api.gax.rpc.ApiClientHeaderProvider@6f20ae98, clock=com.google.bigtable.repackaged.com.google.api.core.NanoClock@4307b25, endpoint=bigtableadmin.googleapis.com:443, mtlsEndpoint=bigtableadmin.mtls.googleapis.com:443, switchToMtlsEndpointAllowed=true, quotaProjectId=null, streamWatchdogProvider=com.google.bigtable.repackaged.com.google.api.gax.rpc.InstantiatingWatchdogProvider@3a830d37, streamWatchdogCheckInterval=PT10S, tracerFactory=com.google.bigtable.repackaged.com.google.api.gax.tracing.BaseApiTracerFactory@8c20955}, undeleteTableSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT0S, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT0S, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT0S}}, undeleteTableOperationSettings=com.google.bigtable.repackaged.com.google.api.gax.rpc.OperationCallSettings@42674e5f, updateTableSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT0S, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT0S, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT0S}}, updateTableOperationSettings=com.google.bigtable.repackaged.com.google.api.gax.rpc.OperationCallSettings@28d96ce5}, instanceAdminSettings=BigtableInstanceAdminSettings{projectId=autonomous-mote-782, createInstanceSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT5M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT5M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT5M}}, createInstanceOperationSettings=com.google.bigtable.repackaged.com.google.api.gax.rpc.OperationCallSettings@a11367c, getInstanceSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, listInstancesSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, partialUpdateInstanceSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, partialUpdateInstanceOperationSettings=com.google.bigtable.repackaged.com.google.api.gax.rpc.OperationCallSettings@12011eca, deleteInstanceSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, createClusterSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, createClusterOperationSettings=com.google.bigtable.repackaged.com.google.api.gax.rpc.OperationCallSettings@6aecaf63, getClusterSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, listClustersSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, updateClusterSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, updateClusterOperationSettings=com.google.bigtable.repackaged.com.google.api.gax.rpc.OperationCallSettings@9b1589f, deleteClusterSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, createAppProfileSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, getAppProfileSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, listAppProfilesSettings=PagedCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, updateAppProfileSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, updateAppProfileOperationSettings=com.google.bigtable.repackaged.com.google.api.gax.rpc.OperationCallSettings@a903d03, deleteAppProfileSettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, getIamPolicySettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, setIamPolicySettings=UnaryCallSettings{retryableCodes=[], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT0S, retryDelayMultiplier=1.0, maxRetryDelay=PT0S, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, testIamPermissionsSettings=UnaryCallSettings{retryableCodes=[UNAVAILABLE, DEADLINE_EXCEEDED], retrySettings=RetrySettings{totalTimeout=PT1M, initialRetryDelay=PT1S, retryDelayMultiplier=2.0, maxRetryDelay=PT1M, maxAttempts=0, jittered=true, initialRpcTimeout=PT1M, rpcTimeoutMultiplier=1.0, maxRpcTimeout=PT1M}}, stubSettings=BigtableInstanceAdminStubSettings{backgroundExecutorProvider=InstantiatingExecutorProvider{executorThreadCount=4, threadFactory=com.google.bigtable.repackaged.com.google.api.gax.core.InstantiatingExecutorProvider$1@219e0330}, transportChannelProvider=com.google.bigtable.repackaged.com.google.api.gax.grpc.InstantiatingGrpcChannelProvider@10fd85d5, credentialsProvider=GoogleCredentialsProvider{scopesToApply=[https://www.googleapis.com/auth/bigtable.admin, https://www.googleapis.com/auth/bigtable.admin.cluster, https://www.googleapis.com/auth/bigtable.admin.instance, https://www.googleapis.com/auth/cloud-bigtable.admin, https://www.googleapis.com/auth/cloud-bigtable.admin.cluster, https://www.googleapis.com/auth/cloud-platform, https://www.googleapis.com/auth/cloud-platform.read-only], jwtEnabledScopes=[], useJwtAccessWithScope=true, OAuth2Credentials=null}, headerProvider=FixedHeaderProvider{headers={user-agent=hbase/1.7.2 bigtable-hbase/2.6.4-SNAPSHOT HBaseBeam}}, internalHeaderProvider=com.google.bigtable.repackaged.com.google.api.gax.rpc.ApiClientHeaderProvider@1f65b71e, clock=com.google.bigtable.repackaged.com.google.api.core.NanoClock@4307b25, endpoint=bigtableadmin.googleapis.com:443, mtlsEndpoint=bigtableadmin.mtls.googleapis.com:443, switchToMtlsEndpointAllowed=true, quotaProjectId=null, streamWatchdogProvider=com.google.bigtable.repackaged.com.google.api.gax.rpc.InstantiatingWatchdogProvider@1c1371cd, streamWatchdogCheckInterval=PT10S, tracerFactory=com.google.bigtable.repackaged.com.google.api.gax.tracing.BaseApiTracerFactory@8c20955}}}"));
    // System.out.println("kk gson:" + new GsonBuilder().setPrettyPrinting().create().toJson(helper));
  }
}
