package uk.co.triad.configtool.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

@Service
public class YamlManipulationServiceImpl implements YamlManipulationService {

    private static final Logger LOG = LogManager.getLogger(YamlManipulationServiceImpl.class.getName());

    @Override
    public Map<String, Map<String, String>> getPropertyMapFromYamlFile(File yamlFile) {
        Yaml yaml = new Yaml();
        try {
            InputStream inputStream = new FileInputStream(yamlFile);
            Map<String,Map<String,String>> properties = (Map<String,Map<String,String>>)yaml.load(inputStream);
            return properties;

        }
        catch(FileNotFoundException fne){
            LOG.error(fne);
                    }
        return null;
    }
}
