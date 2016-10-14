package org.knowm.xchange.itbit.v1.service.polling;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.itbit.v1.ItBit;
import org.knowm.xchange.itbit.v1.ItBitAuthenticated;
import org.knowm.xchange.itbit.v1.service.ItBitHmacPostBodyDigest;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.polling.BasePollingService;

import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestProxyFactory;

public class ItBitBasePollingService extends BaseExchangeService implements BasePollingService {

  protected final String apiKey;
  protected final ItBitAuthenticated itBitAuthenticated;
  protected final ParamsDigest signatureCreator;

  protected final ItBit itBitPublic;

  protected String userId;
  protected String walletId;

  /**
   * Constructor
   *
   * @param exchange
   */
  public ItBitBasePollingService(Exchange exchange) {

    super(exchange);

    this.itBitAuthenticated = RestProxyFactory.createProxy(ItBitAuthenticated.class,
        (String) exchange.getExchangeSpecification().getExchangeSpecificParametersItem("authHost"));
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.signatureCreator = ItBitHmacPostBodyDigest.createInstance(apiKey, exchange.getExchangeSpecification().getSecretKey());

    this.itBitPublic = RestProxyFactory.createProxy(ItBit.class, exchange.getExchangeSpecification().getSslUri());

    this.userId = (String) exchange.getExchangeSpecification().getExchangeSpecificParametersItem("userId");
    this.walletId = (String) exchange.getExchangeSpecification().getExchangeSpecificParametersItem("walletId");

    if ((this.userId == null || this.walletId == null) && exchange.getExchangeSpecification().getUserName() != null) {
      String[] userIdAndWalletId = exchange.getExchangeSpecification().getUserName().split("/");
      if (userIdAndWalletId.length != 2) {
        throw new IllegalArgumentException(
            "Please specify the userId and walletId either in the ExchangeSpecification specific parameters, or in the userName field as userId/walletId.");
      }
      this.userId = userIdAndWalletId[0];
      this.walletId = userIdAndWalletId[1];
    }
  }
}
