/* Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved. */
/*
 * WebLogicDomain
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package oracle.kubernetes.operator.domain.model.oracle.kubernetes.weblogic.domain.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * DomainList is a list of Domains.
 */
@ApiModel(description = "DomainList is a list of Domains.")

public class DomainList {
  @SerializedName("apiVersion")
  private String apiVersion = null;

  @SerializedName("items")
  private List<Domain> items = new ArrayList<Domain>();

  @SerializedName("kind")
  private String kind = null;

  @SerializedName("metadata")
  private io.kubernetes.client.models.V1ListMeta metadata = null;

  public DomainList apiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
    return this;
  }

  /**
   * APIVersion defines the versioned schema of this representation of an object.
   * Servers should convert recognized schemas to the latest internal value, and
   * may reject unrecognized values. More info:
   * https://git.k8s.io/community/contributors/devel/api-conventions.md#resources
   * 
   * @return apiVersion
   **/
  @ApiModelProperty(value = "APIVersion defines the versioned schema of this representation of an object. Servers should convert recognized schemas to the latest internal value, and may reject unrecognized values. More info: https://git.k8s.io/community/contributors/devel/api-conventions.md#resources")
  public String getApiVersion() {
    return apiVersion;
  }

  public void setApiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
  }

  public DomainList items(List<Domain> items) {
    this.items = items;
    return this;
  }

  public DomainList addItemsItem(Domain itemsItem) {
    this.items.add(itemsItem);
    return this;
  }

  /**
   * List of domains. More info:
   * https://git.k8s.io/community/contributors/devel/api-conventions.md
   * 
   * @return items
   **/
  @ApiModelProperty(required = true, value = "List of domains. More info: https://git.k8s.io/community/contributors/devel/api-conventions.md")
  public List<Domain> getItems() {
    return items;
  }

  public void setItems(List<Domain> items) {
    this.items = items;
  }

  public DomainList kind(String kind) {
    this.kind = kind;
    return this;
  }

  /**
   * Kind is a string value representing the REST resource this object represents.
   * Servers may infer this from the endpoint the client submits requests to.
   * Cannot be updated. In CamelCase. More info:
   * https://git.k8s.io/community/contributors/devel/api-conventions.md#types-kinds
   * 
   * @return kind
   **/
  @ApiModelProperty(value = "Kind is a string value representing the REST resource this object represents. Servers may infer this from the endpoint the client submits requests to. Cannot be updated. In CamelCase. More info: https://git.k8s.io/community/contributors/devel/api-conventions.md#types-kinds")
  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public DomainList metadata(io.kubernetes.client.models.V1ListMeta metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Standard list metadata. More info:
   * https://git.k8s.io/community/contributors/devel/api-conventions.md#types-kinds
   * 
   * @return metadata
   **/
  @ApiModelProperty(value = "Standard list metadata. More info: https://git.k8s.io/community/contributors/devel/api-conventions.md#types-kinds")
  public io.kubernetes.client.models.V1ListMeta getMetadata() {
    return metadata;
  }

  public void setMetadata(io.kubernetes.client.models.V1ListMeta metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DomainList oracleKubernetesWeblogicDomainV1DomainList = (DomainList) o;
    return Objects.equals(this.apiVersion, oracleKubernetesWeblogicDomainV1DomainList.apiVersion)
        && Objects.equals(this.items, oracleKubernetesWeblogicDomainV1DomainList.items)
        && Objects.equals(this.kind, oracleKubernetesWeblogicDomainV1DomainList.kind)
        && Objects.equals(this.metadata, oracleKubernetesWeblogicDomainV1DomainList.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apiVersion, items, kind, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DomainList {\n");

    sb.append("    apiVersion: ").append(toIndentedString(apiVersion)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
