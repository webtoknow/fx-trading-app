package com.banking.sofware.design.fxtrading.configuration;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import com.banking.sofware.design.fxtrading.converters.Transaction2TransactionVo;

@Configuration
public class ConversionConfiguration {
	
	@Bean(name="conversionService")
	public ConversionService getConversionService() {
	    ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
	    
	    Set<Converter<?, ?>> converters = new HashSet<>();
	    converters.add(new Transaction2TransactionVo());
	    bean.setConverters(converters); //add converters
	    bean.afterPropertiesSet();
	    return bean.getObject();
	}
}

