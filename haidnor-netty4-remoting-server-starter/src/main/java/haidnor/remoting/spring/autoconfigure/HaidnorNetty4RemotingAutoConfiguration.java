package haidnor.remoting.spring.autoconfigure;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"haidnor.remoting.spring"}, nameGenerator = HaidnorNetty4RemotingAutoConfiguration.UniqueNameGenerator.class)
public class HaidnorNetty4RemotingAutoConfiguration {

    static class UniqueNameGenerator extends AnnotationBeanNameGenerator {
        @Override
        public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
            return definition.getBeanClassName();
        }
    }

}