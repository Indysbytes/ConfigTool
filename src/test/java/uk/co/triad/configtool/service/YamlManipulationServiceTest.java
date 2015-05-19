package uk.co.triad.configtool.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class YamlManipulationServiceTest {
    private YamlManipulationService yamlManipulationService;

    private static final String EMPTY_YAML_FILE = "src/test/resources/keyValuePair/local.yml";
    private static final String GLOBAL_YAML_FILE = "src/test/resources/keyValuePair/global.yml";

    private static final int GLOBAL_YAML_FILE_PROPERTY_TYPES_SIZE = 1;
    private static final int GLOBAL_YAML_FILE_NUMBER_OF_PROPERTIES = 2;

    private static final String GLOBAL_YAML_FILE_PROPERTY1_KEY = "application.name";
    private static final String GLOBAL_YAML_FILE_PROPERTY1_VALUE = "testApp";

    private static final String GLOBAL_YAML_FILE_PROPERTY2_KEY = "application.version";
    private static final String GLOBAL_YAML_FILE_PROPERTY2_VALUE = "1.0";

    private static final String COMMON_PROPERTIES_KEY = "common";

    private static final String PROD_YAML_FILE = "src/test/resources/keyValuePair/prod.yml";
    private static final int PROD_YAML_FILE_PROPERTY_TYPES_SIZE = 3;

    private static final int  PROD_YAML_FILE_NUMBER_OF_COMMON_PROPERTIES = 2;
    private static final int  PROD_YAML_FILE_NUMBER_OF_HOST1_PROPERTIES = 3;
    private static final int  PROD_YAML_FILE_NUMBER_OF_HOST2_PROPERTIES = 3;

    private static final String  PROD_YAML_FILE_COMMON_PROPERTY1_KEY = "adapter";
    private static final String  PROD_YAML_FILE_COMMON_PROPERTY1_VALUE = "postgres";

    private static final String  PROD_YAML_FILE_COMMON_PROPERTY2_KEY = "jdk.version";
    private static final String  PROD_YAML_FILE_COMMON_PROPERTY2_VALUE = "1.6";

    private static final String PROD_HOST1_PROPERTIES_KEY="hostname1";
    private static final String  PROD_YAML_FILE_HOST1_PROPERTY1_KEY = "database";
    private static final String  PROD_YAML_FILE_HOST1_PROPERTY1_VALUE = "db1.hostname.com";

    private static final String  PROD_YAML_FILE_HOST1_PROPERTY2_KEY = "payment.api.url";
    private static final String  PROD_YAML_FILE_HOST1_PROPERTY2_VALUE = "http://api1.payment.com";

    private static final String  PROD_YAML_FILE_HOST1_PROPERTY3_KEY = "host";
    private static final String  PROD_YAML_FILE_HOST1_PROPERTY3_VALUE = "hostname1";

    private static final String PROD_HOST2_PROPERTIES_KEY="hostname2";

    private static final String  PROD_YAML_FILE_HOST2_PROPERTY1_KEY = "database";
    private static final String  PROD_YAML_FILE_HOST2_PROPERTY1_VALUE = "db2.hostname.com";

    private static final String  PROD_YAML_FILE_HOST2_PROPERTY2_KEY = "payment.api.url";
    private static final String  PROD_YAML_FILE_HOST2_PROPERTY2_VALUE = "http://api2.payment.com";

    private static final String  PROD_YAML_FILE_HOST2_PROPERTY3_KEY = "host";
    private static final String  PROD_YAML_FILE_HOST2_PROPERTY3_VALUE = "hostname2";


    private static final String TEST_YAML_FILE = "src/test/resources/keyValuePair/test.yml";

    @Before
    public void init(){
        yamlManipulationService = new YamlManipulationServiceImpl();
    }

    @After
    public void tearDown(){
        yamlManipulationService = null;
    }

    @Test
    public void generatePropertyMapForEmptyFileTest(){
        Map<String,Map<String,String>> propertyMap = yamlManipulationService.getPropertyMapFromYamlFile(new File(EMPTY_YAML_FILE));

        assertNull(propertyMap);

    }
    @Test
    public void generatePropertyMapForGlobalFileTest(){
        Map<String,Map<String,String>> propertyMap = yamlManipulationService.getPropertyMapFromYamlFile(new File(GLOBAL_YAML_FILE));

        assertEquals(GLOBAL_YAML_FILE_PROPERTY_TYPES_SIZE,propertyMap.size());
        assertTrue(propertyMap.containsKey(COMMON_PROPERTIES_KEY));
        assertEquals(GLOBAL_YAML_FILE_NUMBER_OF_PROPERTIES,propertyMap.get(COMMON_PROPERTIES_KEY).size());
        assertTrue(propertyMap.get(COMMON_PROPERTIES_KEY).containsKey(GLOBAL_YAML_FILE_PROPERTY1_KEY));
        assertTrue(propertyMap.get(COMMON_PROPERTIES_KEY).containsKey(GLOBAL_YAML_FILE_PROPERTY2_KEY));
        assertEquals(GLOBAL_YAML_FILE_PROPERTY1_VALUE,propertyMap.get(COMMON_PROPERTIES_KEY).get(GLOBAL_YAML_FILE_PROPERTY1_KEY));
        assertEquals(GLOBAL_YAML_FILE_PROPERTY2_VALUE,propertyMap.get(COMMON_PROPERTIES_KEY).get(GLOBAL_YAML_FILE_PROPERTY2_KEY));
    }

    @Test
    public void generatePropertyMapForProdFileTest(){
        Map<String,Map<String,String>> propertyMap = yamlManipulationService.getPropertyMapFromYamlFile(new File(PROD_YAML_FILE));

        assertEquals(PROD_YAML_FILE_PROPERTY_TYPES_SIZE,propertyMap.size());
        assertTrue(propertyMap.containsKey(COMMON_PROPERTIES_KEY));
        assertEquals(PROD_YAML_FILE_NUMBER_OF_COMMON_PROPERTIES,propertyMap.get(COMMON_PROPERTIES_KEY).size());
        assertTrue(propertyMap.get(COMMON_PROPERTIES_KEY).containsKey(PROD_YAML_FILE_COMMON_PROPERTY1_KEY));
        assertTrue(propertyMap.get(COMMON_PROPERTIES_KEY).containsKey(PROD_YAML_FILE_COMMON_PROPERTY2_KEY));
        assertEquals(PROD_YAML_FILE_COMMON_PROPERTY1_VALUE,propertyMap.get(COMMON_PROPERTIES_KEY).get(PROD_YAML_FILE_COMMON_PROPERTY1_KEY));
        assertEquals(PROD_YAML_FILE_COMMON_PROPERTY2_VALUE,propertyMap.get(COMMON_PROPERTIES_KEY).get(PROD_YAML_FILE_COMMON_PROPERTY2_KEY));

        assertEquals(PROD_YAML_FILE_NUMBER_OF_HOST1_PROPERTIES,propertyMap.get(PROD_HOST1_PROPERTIES_KEY).size());
        assertTrue(propertyMap.get(PROD_HOST1_PROPERTIES_KEY).containsKey(PROD_YAML_FILE_HOST1_PROPERTY1_KEY));
        assertTrue(propertyMap.get(PROD_HOST1_PROPERTIES_KEY).containsKey(PROD_YAML_FILE_HOST1_PROPERTY2_KEY));
        assertTrue(propertyMap.get(PROD_HOST1_PROPERTIES_KEY).containsKey(PROD_YAML_FILE_HOST1_PROPERTY3_KEY));
        assertEquals(PROD_YAML_FILE_HOST1_PROPERTY1_VALUE,propertyMap.get(PROD_HOST1_PROPERTIES_KEY).get(PROD_YAML_FILE_HOST1_PROPERTY1_KEY));
        assertEquals(PROD_YAML_FILE_HOST1_PROPERTY2_VALUE,propertyMap.get(PROD_HOST1_PROPERTIES_KEY).get(PROD_YAML_FILE_HOST1_PROPERTY2_KEY));
        assertEquals(PROD_YAML_FILE_HOST1_PROPERTY3_VALUE,propertyMap.get(PROD_HOST1_PROPERTIES_KEY).get(PROD_YAML_FILE_HOST1_PROPERTY3_KEY));

        assertEquals(PROD_YAML_FILE_NUMBER_OF_HOST2_PROPERTIES,propertyMap.get(PROD_HOST2_PROPERTIES_KEY).size());
        assertTrue(propertyMap.get(PROD_HOST2_PROPERTIES_KEY).containsKey(PROD_YAML_FILE_HOST2_PROPERTY1_KEY));
        assertTrue(propertyMap.get(PROD_HOST2_PROPERTIES_KEY).containsKey(PROD_YAML_FILE_HOST2_PROPERTY2_KEY));
        assertTrue(propertyMap.get(PROD_HOST2_PROPERTIES_KEY).containsKey(PROD_YAML_FILE_HOST2_PROPERTY3_KEY));
        assertEquals(PROD_YAML_FILE_HOST2_PROPERTY1_VALUE,propertyMap.get(PROD_HOST2_PROPERTIES_KEY).get(PROD_YAML_FILE_HOST2_PROPERTY1_KEY));
        assertEquals(PROD_YAML_FILE_HOST2_PROPERTY2_VALUE,propertyMap.get(PROD_HOST2_PROPERTIES_KEY).get(PROD_YAML_FILE_HOST2_PROPERTY2_KEY));
        assertEquals(PROD_YAML_FILE_HOST2_PROPERTY3_VALUE,propertyMap.get(PROD_HOST2_PROPERTIES_KEY).get(PROD_YAML_FILE_HOST2_PROPERTY3_KEY));


    }




}
