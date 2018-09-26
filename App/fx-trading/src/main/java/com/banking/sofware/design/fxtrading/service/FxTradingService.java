package com.banking.sofware.design.fxtrading.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import com.banking.sofware.design.fxtrading.entities.Transaction;
import com.banking.sofware.design.fxtrading.repo.FxTradingRepository;
import com.banking.sofware.design.fxtrading.vo.TransactionVo;


@Service
public class FxTradingService {
	
	@Autowired
	FxTradingRepository repository;
	
	@Autowired
	ConversionService conversionService;

	public List<TransactionVo> getTransactions() {
		return (List<TransactionVo>) conversionService.convert(repository.findAll(),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Transaction.class)), 
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(TransactionVo.class))
				);
	}
	
}
