export const authApi = 'http://localhost:4500'
export const tradeApi = 'http://localhost:4800'
export const quoteApi = 'http://localhost:5000'

export const backendUrl = {
  authService: {
    authenticate: `${authApi}/authenticate`,
    register: `${authApi}/register`,
  },
  fxTradeService: {
    getTransactions: `${tradeApi}/transactions`,
    saveTransaction: `${tradeApi}/transaction`,
  },
  quoteService: {
    getCurrencies: `${quoteApi}/currencies`,
    getFxRate: `${quoteApi}/fx-rate`
  }
}
