export const authApi = 'http://localhost:6000'
export const tradeApi = 'http://localhost:7000'
export const quoteApi = 'http://localhost:8000'

export const backendUrl = {
  authService: {
    authenticate: `${authApi}/authenticate`,
    register: `${authApi}/register`,
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
