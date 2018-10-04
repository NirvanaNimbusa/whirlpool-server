package com.samourai.whirlpool.server.controllers.websocket;

import com.samourai.whirlpool.protocol.WhirlpoolProtocol;
import com.samourai.whirlpool.protocol.websocket.messages.SubscribePoolResponse;
import com.samourai.whirlpool.server.services.CryptoService;
import com.samourai.whirlpool.server.services.PoolService;
import com.samourai.whirlpool.server.services.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.security.Principal;

@Controller
public class SubscribePoolController extends AbstractWebSocketController {
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private PoolService poolService;
  private CryptoService cryptoService;

  @Autowired
  public SubscribePoolController(PoolService poolService, WebSocketService webSocketService, CryptoService cryptoService) {
    super(webSocketService);
    this.poolService = poolService;
    this.cryptoService = cryptoService;
  }

  @SubscribeMapping(WhirlpoolProtocol.SOCKET_SUBSCRIBE_USER_PRIVATE + WhirlpoolProtocol.SOCKET_SUBSCRIBE_USER_REPLY)
  public void subscribePool(Principal principal, StompHeaderAccessor headers) throws Exception {
      // don't validate headers here, so user is able to receive protocol version mismatch errors

      String username = principal.getName();
      if (log.isDebugEnabled()) {
          log.info("[controller] subscribe:"+ headers.getDestination() + ": username=" + username);
      }

      // validate poolId & reply poolStatusNotification
      String headerPoolId = getHeaderPoolId(headers);
      SubscribePoolResponse subscribePoolResponse = poolService.computeSubscribePoolResponse(headerPoolId);
      getWebSocketService().sendPrivate(username, subscribePoolResponse);
  }

  private String getHeaderPoolId(StompHeaderAccessor headers) {
      return headers.getFirstNativeHeader(WhirlpoolProtocol.HEADER_POOL_ID);
  }

  @MessageExceptionHandler
  public void handleException(Exception exception, Principal principal) {
    super.handleException(exception, principal);
  }

}