package com.landmuc.spotcli.domain;

import java.time.Duration;

import reactor.core.publisher.Mono;

public class DelayedResponseHandler {

  private static final int DEFAULT_DELAY = 200;
  private static final int DEFAULT_REPETITION = 5;

  public static <T> Mono<T> handleDelayedResponse(Mono<T> responseObject, T objectToCompareTo) {
    return handleDelayedResponse(responseObject, objectToCompareTo, DEFAULT_DELAY, DEFAULT_REPETITION);
  }

  public static <T> Mono<T> handleDelayedResponse(Mono<T> responseObject, T objectToCompareTo, int delayInMillis) {
    return handleDelayedResponse(responseObject, objectToCompareTo, delayInMillis, DEFAULT_REPETITION);
  }

  public static <T> Mono<T> handleDelayedResponse(Mono<T> responseObject, T objectToCompareTo, int delayInMillis,
      int repetion) {
    return responseObject
        .filter(response -> !response.equals(objectToCompareTo))
        .repeatWhenEmpty(repeat -> repeat
            .delayElements(Duration.ofMillis(delayInMillis))
            .take(repetion));
  }
}
