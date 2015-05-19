package uk.co.triad.configtool.service;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ConfigGenerationService {

    void generateConfig(String configurationDirectory,String keyValuePairDirectory,String hostname);

    List<File> findYamlFileForHostname(String hostname,String keyValuePairDirectory);

    Map<String,String> createPropertyMapFromYamlFiles(List<File> yamlFiles,String hostname);

    List<File> enumerateTemplateFiles(String configurationDirectory);

    void convertTemplateFilesToFinalFiles(Map<File,File> templateToFinalFilesMap,Map<String,String> configurationProperties);

    void convertTemplateFileToFinalFile(File templateFile,File finalFile, Map<String,String> configurationProperties);

    File getFinalFileFromTemplateFile(File templateFile);

    List<File> enumerateYamlFiles(String keyValuePairDirectory);

    boolean isGlobalYamlFile(File file);
}
