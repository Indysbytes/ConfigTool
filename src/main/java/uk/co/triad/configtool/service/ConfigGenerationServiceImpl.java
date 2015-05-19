package uk.co.triad.configtool.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: gfountas
 * Date: 08/09/14
 * Time: 10:51
 *
 */
@Service
public class ConfigGenerationServiceImpl implements ConfigGenerationService {

    @Autowired
    private YamlManipulationService yamlManipulationService;

    private static final String[] TEMPLATE_FILE_EXTENSIONS = {"template"};
    private static final String[] KEY_VALUE_PAIR_FILE_EXTENSIONS = {"yml"};

    private static final String GLOBAL_KEY_VALUE_PAIR_FILENAME = "global.yml";
    private static final String COMMON_PROPERTIES_KEY ="common";
    private static final String UTF8 = "UTF-8";
    private static final String DYNAMIC_PROPERTY_PREFIX="~~";
    private static final String DYNAMIC_PROPERTY_SUFFIX="~~";

    private static final Logger LOG = LogManager.getLogger(ConfigGenerationServiceImpl.class.getName());

    @Override
    public void generateConfig(String configurationDirectory, String keyValuePairDirectory, String hostname) {

        List<File> yamlFiles = findYamlFileForHostname(hostname,keyValuePairDirectory);
        Map<String,String> propertyMap = createPropertyMapFromYamlFiles(yamlFiles,hostname);

        List<File> templateFiles = enumerateTemplateFiles(configurationDirectory);

        for (File templateFile:templateFiles){
            File finalFile = getFinalFileFromTemplateFile(templateFile);
            convertTemplateFileToFinalFile(templateFile,finalFile,propertyMap);
        }

    }

    @Override
    public List<File> enumerateYamlFiles(String keyValuePairDirectory) {

        return new ArrayList(FileUtils.listFiles(new File(keyValuePairDirectory),KEY_VALUE_PAIR_FILE_EXTENSIONS,true));
    }

    @Override
    public boolean isGlobalYamlFile(File file) {
        return (file!=null && file.getName().equals(GLOBAL_KEY_VALUE_PAIR_FILENAME));
    }

    @Override
    public List<File> findYamlFileForHostname(String hostname, String keyValuePairDirectory) {

        List<File> allYamlFiles = enumerateYamlFiles(keyValuePairDirectory);
        List<File> yamlFiles = new ArrayList<File>();

        for (File yamlFile:allYamlFiles){
            if (isGlobalYamlFile(yamlFile)) {
                yamlFiles.add(yamlFile);

            }
            else {
                Map<String,Map<String,String>> propertyMap = yamlManipulationService.getPropertyMapFromYamlFile(yamlFile);
                if (propertyMap!=null && propertyMap.containsKey(hostname)) {
                    yamlFiles.add(yamlFile);
                }
            }
        }
        return yamlFiles;
    }

    @Override
    public Map<String, String> createPropertyMapFromYamlFiles(List<File> yamlFiles,String hostname) {

        Map<String,String> propertyMap = new HashMap<String, String>();
        Map<String,String> globalFileMap =new HashMap<String, String>();
        Map<String,String> hostSpecificMap= new HashMap<String, String>();

        for (File yamlFile:yamlFiles){
            if (isGlobalYamlFile(yamlFile)){
                Map<String,Map<String,String>> globalPropertiesMap =  yamlManipulationService.getPropertyMapFromYamlFile(yamlFile);
                if (globalPropertiesMap!=null && globalPropertiesMap.containsKey(COMMON_PROPERTIES_KEY)){
                    globalFileMap.putAll(globalPropertiesMap.get(COMMON_PROPERTIES_KEY));
                }
            }
            else {
                Map<String,Map<String,String>> nonGlobalPropertiesMap = yamlManipulationService.getPropertyMapFromYamlFile(yamlFile);
                if (nonGlobalPropertiesMap!=null&& nonGlobalPropertiesMap.containsKey(COMMON_PROPERTIES_KEY)){
                    hostSpecificMap.putAll(nonGlobalPropertiesMap.get(COMMON_PROPERTIES_KEY));
                }
                if (nonGlobalPropertiesMap!=null&& nonGlobalPropertiesMap.containsKey(hostname)){
                    hostSpecificMap.putAll(nonGlobalPropertiesMap.get(hostname));
                }
            }
            propertyMap.putAll(globalFileMap);
            propertyMap.putAll(hostSpecificMap);
        }
       return propertyMap;
    }

    @Override
    public List<File> enumerateTemplateFiles(String configurationDirectory) {
         List<File> files = new ArrayList(FileUtils.listFiles(new File(configurationDirectory),TEMPLATE_FILE_EXTENSIONS,true));
        return files;
    }

    @Override
    public void convertTemplateFileToFinalFile(File templateFile, File finalFile, Map<String, String> configurationProperties) {

        try {
            String templateFileContent = FileUtils.readFileToString(templateFile,UTF8);
            StrSubstitutor strSubstitutor = new StrSubstitutor(configurationProperties,DYNAMIC_PROPERTY_PREFIX,DYNAMIC_PROPERTY_SUFFIX);
            String resolvedFileContent = strSubstitutor.replace(templateFileContent);
            FileUtils.writeStringToFile(finalFile,resolvedFileContent,UTF8);

        } catch (IOException e) {
            LOG.error(e);
        }
    }

    @Override
    public void convertTemplateFilesToFinalFiles(Map<File, File> templateToFinalFilesMap, Map<String, String> configurationProperties) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public File getFinalFileFromTemplateFile(File templateFile) {
        String templateFilename = templateFile.getAbsolutePath();
        String finalFilename = FilenameUtils.removeExtension(templateFilename);
        return new File(finalFilename);
    }

    public YamlManipulationService getYamlManipulationService() {
        return yamlManipulationService;
    }

    public void setYamlManipulationService(YamlManipulationService yamlManipulationService) {
        this.yamlManipulationService = yamlManipulationService;
    }
}
