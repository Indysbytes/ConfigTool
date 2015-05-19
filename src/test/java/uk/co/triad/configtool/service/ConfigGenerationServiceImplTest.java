package uk.co.triad.configtool.service;


import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
public class ConfigGenerationServiceImplTest {

    @Autowired
    ConfigGenerationService configGenerationService;

    private static final String EXAMPLE_TEMPLATE_FILE_PATH = "src/test/resources/config/tomcat/web.xml.template";
    private static final String EXAMPLE_FINAL_FILE_PATH = "src/test/resources/config/tomcat/web.xml";

    private static final String CONFIGURATION_DIRECTORY = "src/test/resources/config/";
    private static final int NUMBER_OF_CONFIGURATION_FILES_IN_CONF_DIR=2;
    private static final String EXAMPLE_TEMPLATE_FILE1 = "src/test/resources/config/java/local.properties.template";
    private static final String EXAMPLE_TEMPLATE_FILE2 = "src/test/resources/config/tomcat/web.xml.template";

    private static final String KEY_VALUE_PAIR_DIRECTORY = "src/test/resources/keyValuePair/";
    private static final int NUMBER_OF_KEY_VALUE_PAIR_FILES_IN_DIR=4;
    private static final String GLOBAL_KEY_VALUE_PAIR_FILE = "src/test/resources/keyValuePair/global.yml";
    private static final String LOCAL_KEY_VALUE_PAIR_FILE = "src/test/resources/keyValuePair/local.yml";
    private static final String PROD_KEY_VALUE_PAIR_FILE = "src/test/resources/keyValuePair/prod.yml";
    private static final String TEST_KEY_VALUE_PAIR_FILE = "src/test/resources/keyValuePair/test.yml";

    private static final String YAML_GLOBAL_KEY_VALUE_PAIR_FILE ="src/test/resources/keyValuePair/global.yml";
    private static final String YAML_EXAMPLE_NON_GLOBAL_KEY_VALUE_PAIR_FILE ="src/test/resources/keyValuePair/local.yml";

    private static final String HOSTNAME_IN_PROD_FILE = "hostname1";
    private static final int PROD_YAML_FILELIST_SIZE = 2;

    private static final String HOSTNAME_IN_TEST_FILE = "testhostname2";
    private static final int TEST_YAML_FILELIST_SIZE = 2;


    @Test
    public void getFinalFileFromTemplateFileTest(){

        assertNotNull("configGenerationService is Null", configGenerationService);
        File templateFile = new File(EXAMPLE_TEMPLATE_FILE_PATH);
        File finalFile = new File(EXAMPLE_FINAL_FILE_PATH);

        assertEquals(finalFile.getAbsolutePath(),configGenerationService.getFinalFileFromTemplateFile(templateFile).getAbsolutePath());

    }

    @Test
    public void enumerateTemplateFileTest(){

        List<File> templateFiles = configGenerationService.enumerateTemplateFiles(CONFIGURATION_DIRECTORY);

        assertEquals(NUMBER_OF_CONFIGURATION_FILES_IN_CONF_DIR,templateFiles.size());
        assertTrue(fileListContainsFile(templateFiles,new File(EXAMPLE_TEMPLATE_FILE1)));
        assertTrue(fileListContainsFile(templateFiles,new File(EXAMPLE_TEMPLATE_FILE2)));



    }

    @Test
    public void enumerateYamlFileTest(){
        List<File> yamlFiles = configGenerationService.enumerateYamlFiles(KEY_VALUE_PAIR_DIRECTORY);
        assertEquals(NUMBER_OF_KEY_VALUE_PAIR_FILES_IN_DIR, yamlFiles.size());
        assertTrue(fileListContainsFile(yamlFiles,new File(GLOBAL_KEY_VALUE_PAIR_FILE)));
        assertTrue(fileListContainsFile(yamlFiles,new File(LOCAL_KEY_VALUE_PAIR_FILE)));
        assertTrue(fileListContainsFile(yamlFiles,new File(PROD_KEY_VALUE_PAIR_FILE)));
        assertTrue(fileListContainsFile(yamlFiles,new File(TEST_KEY_VALUE_PAIR_FILE)));
    }



    @Test
    public void testIsGlobalYamlFile(){

        assertTrue(configGenerationService.isGlobalYamlFile(new File(YAML_GLOBAL_KEY_VALUE_PAIR_FILE)));
        assertFalse(configGenerationService.isGlobalYamlFile(new File(YAML_EXAMPLE_NON_GLOBAL_KEY_VALUE_PAIR_FILE)));

    }

    @Test
    public void testFindYamlFileForHostname(){

        List<File> prodYamlFiles = configGenerationService.findYamlFileForHostname(HOSTNAME_IN_PROD_FILE,KEY_VALUE_PAIR_DIRECTORY);

        assertEquals(PROD_YAML_FILELIST_SIZE,prodYamlFiles.size());
        assertTrue(fileListContainsFile(prodYamlFiles,new File(PROD_KEY_VALUE_PAIR_FILE)));
        assertTrue(fileListContainsFile(prodYamlFiles,new File(GLOBAL_KEY_VALUE_PAIR_FILE)));

        List<File> testYamlFiles =  configGenerationService.findYamlFileForHostname(HOSTNAME_IN_TEST_FILE,KEY_VALUE_PAIR_DIRECTORY);
        assertEquals(TEST_YAML_FILELIST_SIZE,testYamlFiles.size());
        assertTrue(fileListContainsFile(testYamlFiles,new File(TEST_KEY_VALUE_PAIR_FILE)));
        assertTrue(fileListContainsFile(testYamlFiles,new File(GLOBAL_KEY_VALUE_PAIR_FILE)));


    }


    @Test
    public void testCreatePropertyMapFromYaml(){
        Map<String,String> productionPropertyMap = configGenerationService.createPropertyMapFromYamlFiles(configGenerationService.findYamlFileForHostname(HOSTNAME_IN_PROD_FILE,KEY_VALUE_PAIR_DIRECTORY),HOSTNAME_IN_PROD_FILE);

        assertTrue(productionPropertyMap.containsKey("application.name"));
        assertTrue(productionPropertyMap.containsKey("application.version"));
        assertTrue(productionPropertyMap.containsKey("adapter"));
        assertTrue(productionPropertyMap.containsKey("jdk.version"));
        assertTrue(productionPropertyMap.containsKey("database"));
        assertTrue(productionPropertyMap.containsKey("payment.api.url"));
        assertTrue(productionPropertyMap.containsKey("host"));

        assertEquals(productionPropertyMap.get("application.name"),"testApp");
        assertEquals(productionPropertyMap.get("application.version"),"1.0");
        assertEquals(productionPropertyMap.get("adapter"),"postgres");
        assertEquals(productionPropertyMap.get("jdk.version"),"1.6");
        assertEquals(productionPropertyMap.get("database"),"db1.hostname.com");
        assertEquals(productionPropertyMap.get("payment.api.url"),"http://api1.payment.com");
        assertEquals(productionPropertyMap.get("host"),"hostname1");
    }

    @Test
    public void convertTemplateFileTest(){

        Map<String,String> productionPropertyMap = configGenerationService.createPropertyMapFromYamlFiles(configGenerationService.findYamlFileForHostname(HOSTNAME_IN_PROD_FILE,KEY_VALUE_PAIR_DIRECTORY),HOSTNAME_IN_PROD_FILE);

        File templateFile = new File("src/test/resources/templateToFinalTest/local.properties.template");

        File finalFile = configGenerationService.getFinalFileFromTemplateFile(templateFile);

        configGenerationService.convertTemplateFileToFinalFile(templateFile,finalFile,productionPropertyMap);
        String finalFileContent=null;
        try {
            finalFileContent = FileUtils.readFileToString(finalFile,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("application.name=testApp" + System.getProperty("line.separator") +
                "jdbc.url=db1.hostname.com",finalFileContent);

    }


    @Before
    public void generateConfigTest(){
        configGenerationService.generateConfig(CONFIGURATION_DIRECTORY,KEY_VALUE_PAIR_DIRECTORY,HOSTNAME_IN_TEST_FILE);

    }


    public boolean fileListContainsFile(List<File> fileList,File file){
        for (File fileListFile:fileList){
            if (fileListFile.getAbsolutePath().equals(file.getAbsolutePath())) return true;
        }
        return false;
    }

}
