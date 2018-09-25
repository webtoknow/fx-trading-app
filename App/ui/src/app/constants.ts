export const authApi = 'http://localhost:4500'
export const tradeApi = 'http://localhost:3000'
export const quoteApi = 'http://localhost:8001'

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
