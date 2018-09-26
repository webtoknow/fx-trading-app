package com.banking.sofware.design.fxtrading.converters;

import java.math.BigDecimal;

import org.springframework.core.convert.converter.Converter;

import com.banking.sofware.design.fxtrading.entities.Transaction;
import com.banking.sofware.design.fxtrading.util.MiscUtil;
import com.banking.sofware.design.fxtrading.vo.TransactionVo;

public class Transaction2TransactionVo implements Converter<Transaction, TransactionVo>{

	@Override
	public TransactionVo convert(Transaction source) {
		TransactionVo vo= new TransactionVo();
		vo.setId(source.getTransactionId());
		vo.setUsername(source.getUsername());
		vo.setPrimaryCCY(source.getPrimaryCcy());
		vo.setSecondaryCCY(source.getSecondaryCcy());
		vo.setRate(source.getRate().divide(MiscUtil.RATE_MULTIPLIER));
		vo.setAction(source.getAction());
		vo.setNotional(source.getNotional());
		vo.setTenor(source.getTenor());
		vo.setDate(source.getDate() != null ? source.getDate().getTime() : null);
		return vo;
	}

	
}
