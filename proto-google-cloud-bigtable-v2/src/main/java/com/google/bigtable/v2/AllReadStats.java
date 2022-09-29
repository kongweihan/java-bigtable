/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/v2/request_stats.proto

package com.google.bigtable.v2;

/**
 *
 *
 * <pre>
 * AllReadStats captures all known information about a read.
 * </pre>
 *
 * Protobuf type {@code google.bigtable.v2.AllReadStats}
 */
public final class AllReadStats extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.v2.AllReadStats)
    AllReadStatsOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use AllReadStats.newBuilder() to construct.
  private AllReadStats(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private AllReadStats() {}

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new AllReadStats();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
    return this.unknownFields;
  }

  private AllReadStats(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10:
            {
              com.google.bigtable.v2.ReadIteratorStats.Builder subBuilder = null;
              if (readIteratorStats_ != null) {
                subBuilder = readIteratorStats_.toBuilder();
              }
              readIteratorStats_ =
                  input.readMessage(
                      com.google.bigtable.v2.ReadIteratorStats.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(readIteratorStats_);
                readIteratorStats_ = subBuilder.buildPartial();
              }

              break;
            }
          case 18:
            {
              com.google.bigtable.v2.RequestLatencyStats.Builder subBuilder = null;
              if (requestLatencyStats_ != null) {
                subBuilder = requestLatencyStats_.toBuilder();
              }
              requestLatencyStats_ =
                  input.readMessage(
                      com.google.bigtable.v2.RequestLatencyStats.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(requestLatencyStats_);
                requestLatencyStats_ = subBuilder.buildPartial();
              }

              break;
            }
          default:
            {
              if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (com.google.protobuf.UninitializedMessageException e) {
      throw e.asInvalidProtocolBufferException().setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.v2.RequestStatsProto
        .internal_static_google_bigtable_v2_AllReadStats_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.v2.RequestStatsProto
        .internal_static_google_bigtable_v2_AllReadStats_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.v2.AllReadStats.class,
            com.google.bigtable.v2.AllReadStats.Builder.class);
  }

  public static final int READ_ITERATOR_STATS_FIELD_NUMBER = 1;
  private com.google.bigtable.v2.ReadIteratorStats readIteratorStats_;
  /**
   *
   *
   * <pre>
   * Iteration stats describe how efficient the read is, e.g. comparing
   * rows seen vs. rows returned or cells seen vs cells returned can provide an
   * indication of read efficiency (the higher the ratio of seen to retuned the
   * better).
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadIteratorStats read_iterator_stats = 1;</code>
   *
   * @return Whether the readIteratorStats field is set.
   */
  @java.lang.Override
  public boolean hasReadIteratorStats() {
    return readIteratorStats_ != null;
  }
  /**
   *
   *
   * <pre>
   * Iteration stats describe how efficient the read is, e.g. comparing
   * rows seen vs. rows returned or cells seen vs cells returned can provide an
   * indication of read efficiency (the higher the ratio of seen to retuned the
   * better).
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadIteratorStats read_iterator_stats = 1;</code>
   *
   * @return The readIteratorStats.
   */
  @java.lang.Override
  public com.google.bigtable.v2.ReadIteratorStats getReadIteratorStats() {
    return readIteratorStats_ == null
        ? com.google.bigtable.v2.ReadIteratorStats.getDefaultInstance()
        : readIteratorStats_;
  }
  /**
   *
   *
   * <pre>
   * Iteration stats describe how efficient the read is, e.g. comparing
   * rows seen vs. rows returned or cells seen vs cells returned can provide an
   * indication of read efficiency (the higher the ratio of seen to retuned the
   * better).
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadIteratorStats read_iterator_stats = 1;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.ReadIteratorStatsOrBuilder getReadIteratorStatsOrBuilder() {
    return getReadIteratorStats();
  }

  public static final int REQUEST_LATENCY_STATS_FIELD_NUMBER = 2;
  private com.google.bigtable.v2.RequestLatencyStats requestLatencyStats_;
  /**
   *
   *
   * <pre>
   * Request latency stats describe the time taken to complete a request, from
   * the server side.
   * </pre>
   *
   * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
   *
   * @return Whether the requestLatencyStats field is set.
   */
  @java.lang.Override
  public boolean hasRequestLatencyStats() {
    return requestLatencyStats_ != null;
  }
  /**
   *
   *
   * <pre>
   * Request latency stats describe the time taken to complete a request, from
   * the server side.
   * </pre>
   *
   * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
   *
   * @return The requestLatencyStats.
   */
  @java.lang.Override
  public com.google.bigtable.v2.RequestLatencyStats getRequestLatencyStats() {
    return requestLatencyStats_ == null
        ? com.google.bigtable.v2.RequestLatencyStats.getDefaultInstance()
        : requestLatencyStats_;
  }
  /**
   *
   *
   * <pre>
   * Request latency stats describe the time taken to complete a request, from
   * the server side.
   * </pre>
   *
   * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.RequestLatencyStatsOrBuilder getRequestLatencyStatsOrBuilder() {
    return getRequestLatencyStats();
  }

  private byte memoizedIsInitialized = -1;

  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
    if (readIteratorStats_ != null) {
      output.writeMessage(1, getReadIteratorStats());
    }
    if (requestLatencyStats_ != null) {
      output.writeMessage(2, getRequestLatencyStats());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (readIteratorStats_ != null) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, getReadIteratorStats());
    }
    if (requestLatencyStats_ != null) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(2, getRequestLatencyStats());
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof com.google.bigtable.v2.AllReadStats)) {
      return super.equals(obj);
    }
    com.google.bigtable.v2.AllReadStats other = (com.google.bigtable.v2.AllReadStats) obj;

    if (hasReadIteratorStats() != other.hasReadIteratorStats()) return false;
    if (hasReadIteratorStats()) {
      if (!getReadIteratorStats().equals(other.getReadIteratorStats())) return false;
    }
    if (hasRequestLatencyStats() != other.hasRequestLatencyStats()) return false;
    if (hasRequestLatencyStats()) {
      if (!getRequestLatencyStats().equals(other.getRequestLatencyStats())) return false;
    }
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasReadIteratorStats()) {
      hash = (37 * hash) + READ_ITERATOR_STATS_FIELD_NUMBER;
      hash = (53 * hash) + getReadIteratorStats().hashCode();
    }
    if (hasRequestLatencyStats()) {
      hash = (37 * hash) + REQUEST_LATENCY_STATS_FIELD_NUMBER;
      hash = (53 * hash) + getRequestLatencyStats().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.v2.AllReadStats parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.AllReadStats parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.AllReadStats parseFrom(com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.AllReadStats parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.AllReadStats parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.AllReadStats parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.AllReadStats parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.AllReadStats parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.AllReadStats parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.AllReadStats parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.AllReadStats parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.AllReadStats parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() {
    return newBuilder();
  }

  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }

  public static Builder newBuilder(com.google.bigtable.v2.AllReadStats prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }

  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   *
   *
   * <pre>
   * AllReadStats captures all known information about a read.
   * </pre>
   *
   * Protobuf type {@code google.bigtable.v2.AllReadStats}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.v2.AllReadStats)
      com.google.bigtable.v2.AllReadStatsOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.v2.RequestStatsProto
          .internal_static_google_bigtable_v2_AllReadStats_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.v2.RequestStatsProto
          .internal_static_google_bigtable_v2_AllReadStats_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.v2.AllReadStats.class,
              com.google.bigtable.v2.AllReadStats.Builder.class);
    }

    // Construct using com.google.bigtable.v2.AllReadStats.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }

    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders) {}
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (readIteratorStatsBuilder_ == null) {
        readIteratorStats_ = null;
      } else {
        readIteratorStats_ = null;
        readIteratorStatsBuilder_ = null;
      }
      if (requestLatencyStatsBuilder_ == null) {
        requestLatencyStats_ = null;
      } else {
        requestLatencyStats_ = null;
        requestLatencyStatsBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.v2.RequestStatsProto
          .internal_static_google_bigtable_v2_AllReadStats_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.v2.AllReadStats getDefaultInstanceForType() {
      return com.google.bigtable.v2.AllReadStats.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.v2.AllReadStats build() {
      com.google.bigtable.v2.AllReadStats result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.v2.AllReadStats buildPartial() {
      com.google.bigtable.v2.AllReadStats result = new com.google.bigtable.v2.AllReadStats(this);
      if (readIteratorStatsBuilder_ == null) {
        result.readIteratorStats_ = readIteratorStats_;
      } else {
        result.readIteratorStats_ = readIteratorStatsBuilder_.build();
      }
      if (requestLatencyStatsBuilder_ == null) {
        result.requestLatencyStats_ = requestLatencyStats_;
      } else {
        result.requestLatencyStats_ = requestLatencyStatsBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }

    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
      return super.setField(field, value);
    }

    @java.lang.Override
    public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }

    @java.lang.Override
    public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }

    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field, int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }

    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.google.bigtable.v2.AllReadStats) {
        return mergeFrom((com.google.bigtable.v2.AllReadStats) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.v2.AllReadStats other) {
      if (other == com.google.bigtable.v2.AllReadStats.getDefaultInstance()) return this;
      if (other.hasReadIteratorStats()) {
        mergeReadIteratorStats(other.getReadIteratorStats());
      }
      if (other.hasRequestLatencyStats()) {
        mergeRequestLatencyStats(other.getRequestLatencyStats());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.google.bigtable.v2.AllReadStats parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.google.bigtable.v2.AllReadStats) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private com.google.bigtable.v2.ReadIteratorStats readIteratorStats_;
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.v2.ReadIteratorStats,
            com.google.bigtable.v2.ReadIteratorStats.Builder,
            com.google.bigtable.v2.ReadIteratorStatsOrBuilder>
        readIteratorStatsBuilder_;
    /**
     *
     *
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIteratorStats read_iterator_stats = 1;</code>
     *
     * @return Whether the readIteratorStats field is set.
     */
    public boolean hasReadIteratorStats() {
      return readIteratorStatsBuilder_ != null || readIteratorStats_ != null;
    }
    /**
     *
     *
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIteratorStats read_iterator_stats = 1;</code>
     *
     * @return The readIteratorStats.
     */
    public com.google.bigtable.v2.ReadIteratorStats getReadIteratorStats() {
      if (readIteratorStatsBuilder_ == null) {
        return readIteratorStats_ == null
            ? com.google.bigtable.v2.ReadIteratorStats.getDefaultInstance()
            : readIteratorStats_;
      } else {
        return readIteratorStatsBuilder_.getMessage();
      }
    }
    /**
     *
     *
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIteratorStats read_iterator_stats = 1;</code>
     */
    public Builder setReadIteratorStats(com.google.bigtable.v2.ReadIteratorStats value) {
      if (readIteratorStatsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        readIteratorStats_ = value;
        onChanged();
      } else {
        readIteratorStatsBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIteratorStats read_iterator_stats = 1;</code>
     */
    public Builder setReadIteratorStats(
        com.google.bigtable.v2.ReadIteratorStats.Builder builderForValue) {
      if (readIteratorStatsBuilder_ == null) {
        readIteratorStats_ = builderForValue.build();
        onChanged();
      } else {
        readIteratorStatsBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIteratorStats read_iterator_stats = 1;</code>
     */
    public Builder mergeReadIteratorStats(com.google.bigtable.v2.ReadIteratorStats value) {
      if (readIteratorStatsBuilder_ == null) {
        if (readIteratorStats_ != null) {
          readIteratorStats_ =
              com.google.bigtable.v2.ReadIteratorStats.newBuilder(readIteratorStats_)
                  .mergeFrom(value)
                  .buildPartial();
        } else {
          readIteratorStats_ = value;
        }
        onChanged();
      } else {
        readIteratorStatsBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIteratorStats read_iterator_stats = 1;</code>
     */
    public Builder clearReadIteratorStats() {
      if (readIteratorStatsBuilder_ == null) {
        readIteratorStats_ = null;
        onChanged();
      } else {
        readIteratorStats_ = null;
        readIteratorStatsBuilder_ = null;
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIteratorStats read_iterator_stats = 1;</code>
     */
    public com.google.bigtable.v2.ReadIteratorStats.Builder getReadIteratorStatsBuilder() {

      onChanged();
      return getReadIteratorStatsFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIteratorStats read_iterator_stats = 1;</code>
     */
    public com.google.bigtable.v2.ReadIteratorStatsOrBuilder getReadIteratorStatsOrBuilder() {
      if (readIteratorStatsBuilder_ != null) {
        return readIteratorStatsBuilder_.getMessageOrBuilder();
      } else {
        return readIteratorStats_ == null
            ? com.google.bigtable.v2.ReadIteratorStats.getDefaultInstance()
            : readIteratorStats_;
      }
    }
    /**
     *
     *
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIteratorStats read_iterator_stats = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.v2.ReadIteratorStats,
            com.google.bigtable.v2.ReadIteratorStats.Builder,
            com.google.bigtable.v2.ReadIteratorStatsOrBuilder>
        getReadIteratorStatsFieldBuilder() {
      if (readIteratorStatsBuilder_ == null) {
        readIteratorStatsBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.bigtable.v2.ReadIteratorStats,
                com.google.bigtable.v2.ReadIteratorStats.Builder,
                com.google.bigtable.v2.ReadIteratorStatsOrBuilder>(
                getReadIteratorStats(), getParentForChildren(), isClean());
        readIteratorStats_ = null;
      }
      return readIteratorStatsBuilder_;
    }

    private com.google.bigtable.v2.RequestLatencyStats requestLatencyStats_;
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.v2.RequestLatencyStats,
            com.google.bigtable.v2.RequestLatencyStats.Builder,
            com.google.bigtable.v2.RequestLatencyStatsOrBuilder>
        requestLatencyStatsBuilder_;
    /**
     *
     *
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     *
     * @return Whether the requestLatencyStats field is set.
     */
    public boolean hasRequestLatencyStats() {
      return requestLatencyStatsBuilder_ != null || requestLatencyStats_ != null;
    }
    /**
     *
     *
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     *
     * @return The requestLatencyStats.
     */
    public com.google.bigtable.v2.RequestLatencyStats getRequestLatencyStats() {
      if (requestLatencyStatsBuilder_ == null) {
        return requestLatencyStats_ == null
            ? com.google.bigtable.v2.RequestLatencyStats.getDefaultInstance()
            : requestLatencyStats_;
      } else {
        return requestLatencyStatsBuilder_.getMessage();
      }
    }
    /**
     *
     *
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    public Builder setRequestLatencyStats(com.google.bigtable.v2.RequestLatencyStats value) {
      if (requestLatencyStatsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        requestLatencyStats_ = value;
        onChanged();
      } else {
        requestLatencyStatsBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    public Builder setRequestLatencyStats(
        com.google.bigtable.v2.RequestLatencyStats.Builder builderForValue) {
      if (requestLatencyStatsBuilder_ == null) {
        requestLatencyStats_ = builderForValue.build();
        onChanged();
      } else {
        requestLatencyStatsBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    public Builder mergeRequestLatencyStats(com.google.bigtable.v2.RequestLatencyStats value) {
      if (requestLatencyStatsBuilder_ == null) {
        if (requestLatencyStats_ != null) {
          requestLatencyStats_ =
              com.google.bigtable.v2.RequestLatencyStats.newBuilder(requestLatencyStats_)
                  .mergeFrom(value)
                  .buildPartial();
        } else {
          requestLatencyStats_ = value;
        }
        onChanged();
      } else {
        requestLatencyStatsBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    public Builder clearRequestLatencyStats() {
      if (requestLatencyStatsBuilder_ == null) {
        requestLatencyStats_ = null;
        onChanged();
      } else {
        requestLatencyStats_ = null;
        requestLatencyStatsBuilder_ = null;
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    public com.google.bigtable.v2.RequestLatencyStats.Builder getRequestLatencyStatsBuilder() {

      onChanged();
      return getRequestLatencyStatsFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    public com.google.bigtable.v2.RequestLatencyStatsOrBuilder getRequestLatencyStatsOrBuilder() {
      if (requestLatencyStatsBuilder_ != null) {
        return requestLatencyStatsBuilder_.getMessageOrBuilder();
      } else {
        return requestLatencyStats_ == null
            ? com.google.bigtable.v2.RequestLatencyStats.getDefaultInstance()
            : requestLatencyStats_;
      }
    }
    /**
     *
     *
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.v2.RequestLatencyStats,
            com.google.bigtable.v2.RequestLatencyStats.Builder,
            com.google.bigtable.v2.RequestLatencyStatsOrBuilder>
        getRequestLatencyStatsFieldBuilder() {
      if (requestLatencyStatsBuilder_ == null) {
        requestLatencyStatsBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.bigtable.v2.RequestLatencyStats,
                com.google.bigtable.v2.RequestLatencyStats.Builder,
                com.google.bigtable.v2.RequestLatencyStatsOrBuilder>(
                getRequestLatencyStats(), getParentForChildren(), isClean());
        requestLatencyStats_ = null;
      }
      return requestLatencyStatsBuilder_;
    }

    @java.lang.Override
    public final Builder setUnknownFields(final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }

    // @@protoc_insertion_point(builder_scope:google.bigtable.v2.AllReadStats)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.v2.AllReadStats)
  private static final com.google.bigtable.v2.AllReadStats DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.v2.AllReadStats();
  }

  public static com.google.bigtable.v2.AllReadStats getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<AllReadStats> PARSER =
      new com.google.protobuf.AbstractParser<AllReadStats>() {
        @java.lang.Override
        public AllReadStats parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new AllReadStats(input, extensionRegistry);
        }
      };

  public static com.google.protobuf.Parser<AllReadStats> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<AllReadStats> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.v2.AllReadStats getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}