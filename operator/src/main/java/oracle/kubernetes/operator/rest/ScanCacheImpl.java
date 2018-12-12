// Copyright 2018, Oracle Corporation and/or its affiliates.  All rights reserved.
// Licensed under the Universal Permissive License v 1.0 as shown at
// http://oss.oracle.com/licenses/upl.

package oracle.kubernetes.operator.rest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class ScanCacheImpl implements ScanCache {
  static final ScanCache INSTANCE = new ScanCacheImpl();

  private ScanCacheImpl() {}

  private final ConcurrentMap<String, ConcurrentMap<String, Scan>> map = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, ConcurrentMap<String, Integer>> retryMap =
      new ConcurrentHashMap<>();

  @Override
  public void registerScan(String ns, String domainUID, Scan domainScan) {
    resetRetryCount(ns, domainUID);
    map.computeIfAbsent(ns, k -> new ConcurrentHashMap<>())
        .compute(domainUID, (k, current) -> domainScan);
  }

  @Override
  public Scan lookupScan(String ns, String domainUID) {
    ConcurrentMap<String, Scan> m = map.get(ns);
    return m != null ? m.get(domainUID) : null;
  }

  @Override
  public void unregisterScan(String ns, String domainUID) {
    resetRetryCount(ns, domainUID);
    map.computeIfPresent(
        ns,
        (k, v) -> {
          v.remove(domainUID);
          return v.isEmpty() ? null : v;
        });
  }

  @Override
  public void incrementRetryCount(String ns, String domainUID) {
    retryMap
        .computeIfAbsent(ns, k -> new ConcurrentHashMap<>())
        .compute(
            domainUID, (k, current) -> current == null ? new Integer(1) : new Integer(current + 1));
  }

  @Override
  public Integer lookupRetryCount(String ns, String domainUID) {
    ConcurrentMap<String, Integer> m = retryMap.get(ns);
    return m != null ? m.get(domainUID) : 0;
  }

  public void resetRetryCount(String ns, String domainUID) {
    retryMap.computeIfPresent(
        ns,
        (k, v) -> {
          v.remove(domainUID);
          return v.isEmpty() ? null : v;
        });
  }
}
