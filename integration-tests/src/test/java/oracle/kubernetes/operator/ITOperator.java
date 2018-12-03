// Copyright 2018, Oracle Corporation and/or its affiliates.  All rights reserved.
// Licensed under the Universal Permissive License v 1.0 as shown at
// http://oss.oracle.com/licenses/upl.

package oracle.kubernetes.operator;

import oracle.kubernetes.operator.utils.Domain;
import oracle.kubernetes.operator.utils.ExecCommand;
import oracle.kubernetes.operator.utils.ExecResult;
import oracle.kubernetes.operator.utils.Operator;
import oracle.kubernetes.operator.utils.TestUtils;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Simple JUnit test file used for testing Operator.
 *
 * <p>This test is used for creating Operator(s) and multiple domains which are managed by the
 * Operator(s).
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ITOperator extends BaseTest {

  // property file used to customize operator properties for operator inputs yaml
  private static String op1YamlFile = "operator1.yaml";
  private static String op2YamlFile = "operator2.yaml";
  private static final String opForDelYamlFile1 = "operator_del1.yaml";
  private static final String opForDelYamlFile2 = "operator_del2.yaml";

  // property file used to customize domain properties for domain inputs yaml
  private static String domain1YamlFile = "domain1.yaml";
  private static String domain2YamlFile = "domain2.yaml";
  private static String domain3YamlFile = "domain3.yaml";
  private static String domain4YamlFile = "domain4.yaml";
  private static String domain5YamlFile = "domain5.yaml";
  private static String domain6YamlFile = "domain6.yaml";
  private static String domain7YamlFile = "domain7.yaml";
  private static String domain8YamlFile = "domain8.yaml";
  private static final String domain1ForDelValueYamlFile = "domain_del_1.yaml";
  private static final String domain2ForDelValueYamlFile = "domain_del_2.yaml";
  private static final String domain3ForDelValueYamlFile = "domain_del_3.yaml";
  private static String domain9YamlFile = "domain9.yaml";
  private static String domain10YamlFile = "domain10.yaml";

  // property file used to configure constants for integration tests
  private static String appPropsFile = "OperatorIT.properties";

  private static Operator operator1, operator2;

  private static Operator operatorForDel1;
  private static Operator operatorForDel2;

  /**
   * This method gets called only once before any of the test methods are executed. It does the
   * initialization of the integration test properties defined in OperatorIT.properties and setting
   * the resultRoot, pvRoot and projectRoot attributes.
   *
   * @throws Exception
   */
  @BeforeClass
  public static void staticPrepare() throws Exception {
    // initialize test properties and create the directories
    initialize(appPropsFile);
  }

  /**
   * Releases k8s cluster lease
   *
   * @throws Exception
   */
  @AfterClass
  public static void staticUnPrepare() throws Exception {
    logger.info("+++++++++++++++++++++++++++++++++---------------------------------+");
    logger.info("BEGIN");
    logger.info("Run once, release cluster lease");

    StringBuffer cmd =
        new StringBuffer("export RESULT_ROOT=$RESULT_ROOT && export PV_ROOT=$PV_ROOT && ");
    cmd.append(BaseTest.getProjectRoot())
        .append("/integration-tests/src/test/resources/statedump.sh");
    logger.info("Running " + cmd);

    ExecResult result = ExecCommand.exec(cmd.toString());
    if (result.exitValue() == 0) logger.info("Executed statedump.sh " + result.stdout());
    else
      logger.info("Execution of statedump.sh failed, " + result.stderr() + "\n" + result.stdout());

    if (getLeaseId() != "") {
      logger.info("Release the k8s cluster lease");
      TestUtils.releaseLease(getProjectRoot(), getLeaseId());
    }

    logger.info("SUCCESS");
  }

  @Test
  public void test1CreateFirstOperatorAndDomain() throws Exception {

    logTestBegin("test1CreateFirstOperatorAndDomain");
    testCreateOperatorManagingDefaultAndTest1NS();
    Domain domain1 = testAllUseCasesForADomain(operator1, domain1YamlFile);
    domain1.testWlsLivenessProbe();
    domain1.shutdownUsingServerStartPolicy();

    logger.info("SUCCESS - test1CreateFirstOperatorAndDomain");
  }

  @Test
  public void test2CreateAnotherDomainInDefaultNS() throws Exception {
    Assume.assumeFalse(
        System.getenv("QUICKTEST") != null && System.getenv("QUICKTEST").equalsIgnoreCase("true"));

    logTestBegin("test2CreateAnotherDomainInDefaultNS");
    logger.info("Creating Domain domain2 & verifing the domain creation");
    if (operator1 == null) {
      operator1 = TestUtils.createOperator(op1YamlFile);
    }
    // create domain2
    Domain domain2 = testDomainCreation(domain2YamlFile);

    logger.info("Destroy domain2");
    domain2.destroy();
    logger.info("SUCCESS - test2CreateAnotherDomainInDefaultNS");
  }

  @Test
  public void test3CreateDomainInTest1NS() throws Exception {
    Assume.assumeFalse(
        System.getenv("QUICKTEST") != null && System.getenv("QUICKTEST").equalsIgnoreCase("true"));

    logTestBegin("test3CreateDomainInTest1NS");
    logger.info("Creating Domain domain3 & verifing the domain creation");
    if (operator1 == null) {
      operator1 = TestUtils.createOperator(op1YamlFile);
    }
    // create domain3
    Domain domain3 = testDomainCreation(domain3YamlFile);
    testWLDFScaling(operator1, domain3);
    domain3.destroy();
    logger.info("SUCCESS - test3CreateDomainInTest1NS");
  }

  @Test
  public void test4CreateAnotherOperatorManagingTest2NS() throws Exception {
    Assume.assumeFalse(
        System.getenv("QUICKTEST") != null && System.getenv("QUICKTEST").equalsIgnoreCase("true"));

    logTestBegin("test4CreateAnotherOperatorManagingTest2NS");
    logger.info("Creating Operator & waiting for the script to complete execution");
    // create operator2
    operator2 = TestUtils.createOperator(op2YamlFile);
    logger.info("SUCCESS - test4CreateAnotherOperatorManagingTest2NS");
  }

  @Test
  public void test5CreateConfiguredDomainInTest2NS() throws Exception {
    Assume.assumeFalse(
        System.getenv("QUICKTEST") != null && System.getenv("QUICKTEST").equalsIgnoreCase("true"));

    logTestBegin("test5CreateConfiguredDomainInTest2NS");
    logger.info("Creating Domain domain4 & verifing the domain creation");

    logger.info("Checking if operator1 and domain1 are running, if not creating");
    if (operator1 == null) {
      operator1 = TestUtils.createOperator(op1YamlFile);
    }
    Domain domain4 = TestUtils.createDomain(domain4YamlFile);

    logger.info("Checking if operator2 is running, if not creating");
    if (operator2 == null) {
      operator2 = TestUtils.createOperator(op2YamlFile);
    }
    // create domain5 with configured cluster
    Domain domain5 = testDomainCreation(domain5YamlFile);

    logger.info("Verify the only remaining running domain domain4 is unaffected");
    domain4.verifyDomainCreated();

    testClusterScaling(operator2, domain5);

    logger.info("Verify the only remaining running domain domain4 is unaffected");
    domain4.verifyDomainCreated();

    logger.info("Destroy and create domain4 and verify no impact on domain5");
    domain4.destroy();
    domain4.create();

    logger.info("Verify no impact on domain5");
    domain5.verifyDomainCreated();
    domain5.destroy();
    domain4.destroy();
    logger.info("SUCCESS - test5CreateConfiguredDomainInTest2NS");
  }

  @Test
  public void test6CreateDomainWithStartPolicyAdminOnly() throws Exception {
    Assume.assumeFalse(
        System.getenv("QUICKTEST") != null && System.getenv("QUICKTEST").equalsIgnoreCase("true"));

    logTestBegin("test6CreateDomainWithStartPolicyAdminOnly");
    logger.info("Checking if operator1 is running, if not creating");
    if (operator1 == null) {
      operator1 = TestUtils.createOperator(op1YamlFile);
    }
    logger.info("Creating Domain domain6 & verifing the domain creation");
    // create domain6
    Domain domain6 = TestUtils.createDomain(domain6YamlFile);
    domain6.destroy();
    logger.info("SUCCESS - test6CreateDomainWithStartPolicyAdminOnly");
  }

  @Test
  public void test7CreateDomainPVReclaimPolicyRecycle() throws Exception {
    Assume.assumeFalse(
        System.getenv("QUICKTEST") != null && System.getenv("QUICKTEST").equalsIgnoreCase("true"));

    logTestBegin("test7CreateDomainPVReclaimPolicyRecycle");
    logger.info("Checking if operator1 is running, if not creating");
    if (operator1 == null) {
      operator1 = TestUtils.createOperator(op1YamlFile);
    }
    logger.info("Creating Domain domain7 & verifing the domain creation");
    // create domain7
    Domain domain7 = TestUtils.createDomain(domain7YamlFile);
    domain7.shutdown();
    domain7.deletePVCAndCheckPVReleased();
    logger.info("SUCCESS - test7CreateDomainPVReclaimPolicyRecycle");
  }

  @Test
  public void test8CreateDomainOnExistingDir() throws Exception {
    Assume.assumeFalse(
        System.getenv("QUICKTEST") != null && System.getenv("QUICKTEST").equalsIgnoreCase("true"));

    logTestBegin("test8CreateDomainOnExistingDir");
    if (operator1 == null) {
      operator1 = TestUtils.createOperator(op1YamlFile);
    }

    Domain domain8 = TestUtils.createDomain(domain8YamlFile);
    // create domain on existing dir
    domain8.destroy();

    domain8.createDomainOnExistingDirectory();
    logger.info("SUCCESS - test8CreateDomainOnExistingDir");
  }

  // @Test
  public void testACreateDomainApacheLB() throws Exception {
    Assume.assumeFalse(
        System.getenv("QUICKTEST") != null && System.getenv("QUICKTEST").equalsIgnoreCase("true"));
    logTestBegin("testACreateDomainApacheLB");
    logger.info("Creating Domain domain9 & verifing the domain creation");
    if (operator1 == null) {
      operator1 = TestUtils.createOperator(op1YamlFile);
    }

    // create domain7
    Domain domain9 = TestUtils.createDomain(domain9YamlFile);
    domain9.verifyAdminConsoleViaLB();
    domain9.destroy();
    logger.info("SUCCESS - testACreateDomainApacheLB");
  }

  @Test
  public void testBCreateDomainWithDefaultValuesInSampleInputs() throws Exception {
    Assume.assumeFalse(
        System.getenv("QUICKTEST") != null && System.getenv("QUICKTEST").equalsIgnoreCase("true"));
    logTestBegin("testBCreateDomainWithDefaultValuesInSampleInputs");
    logger.info("Creating Domain domain10 & verifing the domain creation");
    if (operator1 == null) {
      operator1 = TestUtils.createOperator(op1YamlFile);
    }

    // create domain10
    Domain domain10 = testAllUseCasesForADomain(operator1, domain10YamlFile);
    domain10.destroy();
    logger.info("SUCCESS - testBCreateDomainWithDefaultValuesInSampleInputs");
  }

  @Test
  public void testDeleteOneDomain() throws Exception {
    Assume.assumeFalse(
        System.getenv("QUICKTEST") != null && System.getenv("QUICKTEST").equalsIgnoreCase("true"));
    logTestBegin("Deleting one domain.");

    if (operatorForDel1 == null) {
      logger.info("About to create operator");
      operatorForDel1 = TestUtils.createOperator(opForDelYamlFile1);
    }
    final Domain domain = TestUtils.createDomain(domain1ForDelValueYamlFile);
    TestUtils.verifyBeforeDeletion(domain);

    logger.info("About to delete domain: " + domain.getDomainUid());
    TestUtils.deleteWeblogicDomainResources(domain.getDomainUid());

    TestUtils.verifyAfterDeletion(domain);
  }

  @Test
  public void testDeleteTwoDomains() throws Exception {
    Assume.assumeFalse(
        System.getenv("QUICKTEST") != null && System.getenv("QUICKTEST").equalsIgnoreCase("true"));
    logTestBegin("Deleting two domains.");

    if (operatorForDel2 == null) {
      logger.info("About to create operator");
      operatorForDel2 = TestUtils.createOperator(opForDelYamlFile2);
    }
    final Domain domainDel1 = TestUtils.createDomain(domain2ForDelValueYamlFile);
    final Domain domainDel2 = TestUtils.createDomain(domain3ForDelValueYamlFile);

    TestUtils.verifyBeforeDeletion(domainDel1);
    TestUtils.verifyBeforeDeletion(domainDel2);

    final String domainUidsToBeDeleted =
        domainDel1.getDomainUid() + "," + domainDel2.getDomainUid();
    logger.info("About to delete domains: " + domainUidsToBeDeleted);
    TestUtils.deleteWeblogicDomainResources(domainUidsToBeDeleted);

    TestUtils.verifyAfterDeletion(domainDel1);
    TestUtils.verifyAfterDeletion(domainDel2);
  }

  private void testCreateOperatorManagingDefaultAndTest1NS() throws Exception {
    logger.info("Creating Operator & waiting for the script to complete execution");
    // create operator1
    operator1 = TestUtils.createOperator(op1YamlFile);
  }

  private Domain testAllUseCasesForADomain(Operator operator, String domainYamlFile)
      throws Exception {
    logger.info("Creating Domain & verifing the domain creation");
    // create domain1
    Domain domain = testDomainCreation(domainYamlFile);
    testClusterScaling(operator, domain);
    testDomainLifecyle(operator, domain);
    testOperatorLifecycle(operator, domain);
    return domain;
  }

  private Domain testDomainCreation(String domainYamlFile) throws Exception {
    Domain domain = TestUtils.createDomain(domainYamlFile);
    testAdminT3Channel(domain);
    testAdminServerExternalService(domain);
    return domain;
  }
}
