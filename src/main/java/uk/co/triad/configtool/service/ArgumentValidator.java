package uk.co.triad.configtool.service;

/**
 * Created with IntelliJ IDEA.
 * User: gfountas
 * Date: 08/09/14
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public interface ArgumentValidator {
    boolean areArgumentsValid(String[] arguments);
    boolean configDirectoryExists(String configDirectory);
    boolean keyValuePairDirectoryExists(String keyValuePairDirectory);
    boolean configDirectoryContainsTemplateFiles(String configDirectory);
    boolean keyValuePairDirectoryContainKeyValuePairFiles(String keyValuePairDirectory);


}
