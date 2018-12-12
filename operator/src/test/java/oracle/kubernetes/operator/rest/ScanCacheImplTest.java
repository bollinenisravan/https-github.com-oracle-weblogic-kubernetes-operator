package oracle.kubernetes.operator.rest;

import static org.junit.Assert.*;

import oracle.kubernetes.operator.wlsconfig.WlsDomainConfig;
import org.joda.time.DateTime;
import org.junit.Test;

/** Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved. */
public class ScanCacheImplTest {

  @Test
  public void verifyLookupRetryCountReturns0_forNoEntry() throws Exception {
    final String NAMESPACE = "scanCacheImplTestNS-uniqueNS";
    final String DOMAINUID = "scanCacheImplDomainUID-uniqueDomainUID";
    assertEquals(new Integer(0), ScanCacheImpl.INSTANCE.lookupRetryCount(NAMESPACE, DOMAINUID));
  }

  @Test
  public void verifyIncrementRetryCountAdds1ToRetryCount() throws Exception {
    final String NAMESPACE = "scanCacheImplTestNS";
    final String DOMAINUID = "scanCacheImplDomainUID";
    ScanCacheImpl.INSTANCE.incrementRetryCount(NAMESPACE, DOMAINUID);
    assertEquals(new Integer(1), ScanCacheImpl.INSTANCE.lookupRetryCount(NAMESPACE, DOMAINUID));

    ScanCacheImpl.INSTANCE.incrementRetryCount(NAMESPACE, DOMAINUID);
    assertEquals(new Integer(2), ScanCacheImpl.INSTANCE.lookupRetryCount(NAMESPACE, DOMAINUID));

    ScanCacheImpl.INSTANCE.incrementRetryCount(NAMESPACE, DOMAINUID);
    assertEquals(new Integer(3), ScanCacheImpl.INSTANCE.lookupRetryCount(NAMESPACE, DOMAINUID));
  }

  @Test
  public void verifyResetRetryCountResetsRetryCount() throws Exception {
    final String NAMESPACE = "scanCacheImplTestNS";
    final String DOMAINUID = "scanCacheImplDomainUID";

    ScanCacheImpl.INSTANCE.incrementRetryCount(NAMESPACE, DOMAINUID);

    ((ScanCacheImpl) (ScanCacheImpl.INSTANCE)).resetRetryCount(NAMESPACE, DOMAINUID);
    assertEquals(new Integer(0), ScanCacheImpl.INSTANCE.lookupRetryCount(NAMESPACE, DOMAINUID));
  }

  @Test
  public void verifyRegisterScanResetsRetryCount() throws Exception {
    final String NAMESPACE = "scanCacheImplTestNS";
    final String DOMAINUID = "scanCacheImplDomainUID";

    ScanCacheImpl.INSTANCE.incrementRetryCount(NAMESPACE, DOMAINUID);

    ScanCacheImpl.INSTANCE.registerScan(
        NAMESPACE, DOMAINUID, new Scan(new WlsDomainConfig(), new DateTime()));
    assertEquals(new Integer(0), ScanCacheImpl.INSTANCE.lookupRetryCount(NAMESPACE, DOMAINUID));
  }

  @Test
  public void verifyUnegisterScanResetsRetryCount() throws Exception {
    final String NAMESPACE = "scanCacheImplTestNS";
    final String DOMAINUID = "scanCacheImplDomainUID";

    ScanCacheImpl.INSTANCE.incrementRetryCount(NAMESPACE, DOMAINUID);

    ScanCacheImpl.INSTANCE.unregisterScan(NAMESPACE, DOMAINUID);
    assertEquals(new Integer(0), ScanCacheImpl.INSTANCE.lookupRetryCount(NAMESPACE, DOMAINUID));
  }
}
