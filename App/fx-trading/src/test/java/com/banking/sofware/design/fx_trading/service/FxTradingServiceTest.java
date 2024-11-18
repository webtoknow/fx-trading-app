package com.banking.sofware.design.fx_trading.service;

import com.banking.sofware.design.fx_trading.entities.Transaction;
import com.banking.sofware.design.fx_trading.pojo.QuoteResponse;
import com.banking.sofware.design.fx_trading.repo.FxTradingRepository;
import com.banking.sofware.design.fx_trading.vo.TransactionVo;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

public class FxTradingServiceTest {

    @Test
    public void makeTransaction() throws Exception{

        //setup
        FxTradingService service = new FxTradingService();
        QuoteProxyService quoteMock = Mockito.mock(QuoteProxyService.class);
        FxTradingRepository repositoryMock = Mockito.mock(FxTradingRepository.class);

        FieldSetter.setField(service,
                service.getClass().getDeclaredField("proxyRatesService"),
                quoteMock);
        FieldSetter.setField(service,
                service.getClass().getDeclaredField("repository"),
                repositoryMock);

        TransactionVo vo = new TransactionVo();
        vo.setAction("BUY");
        vo.setNotional(BigDecimal.valueOf(10000));
        vo.setTenor("SP");
        vo.setPrimaryCcy("EUR");
        vo.setSecondaryCcy("RON");
        Mockito.when(quoteMock.getRate(vo.getPrimaryCcy(), vo.getSecondaryCcy()))
                .thenReturn(new QuoteResponse(BigDecimal.valueOf(1.1234),BigDecimal.valueOf(1.4321)));


        //method under test
        service.makeTransaction(vo);

        //assert
        ArgumentCaptor<Transaction> capturedTransaction = ArgumentCaptor.forClass(Transaction.class);
        verify(repositoryMock).save(capturedTransaction.capture());
        assertEquals(BigDecimal.valueOf(11234), capturedTransaction.getValue().getRate());
    }
}