package uk.co.triad.configtool.service;

import java.io.File;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gfountas
 * Date: 04/09/14
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public interface YamlManipulationService {

    Map<String,Map<String,String>> getPropertyMapFromYamlFile(File yamlFile);
}
