export const authApi = 'http://localhost:8200'
export const tradeApi = 'http://localhost:8210'
export const quoteApi = 'http://localhost:8220'

export const backendUrl = {
  authService: {
    authenticate: `${authApi}/user/authenticate`,
    register: `${authApi}/user/register`,
  },
  fxTradeService: {
    getTransactions: `${tradeApi}/transactions`,
    saveTransaction: `${tradeApi}/transactions`,
  },
  quoteService: {
    getCurrencies: `${quoteApi}/currencies`,
    getFxRate: `${quoteApi}/fx-rate`
  }
}
