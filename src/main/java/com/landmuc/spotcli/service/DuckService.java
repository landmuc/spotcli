package com.landmuc.spotcli.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landmuc.spotcli.client.DuckApiClient;
import com.landmuc.spotcli.model.DuckResponse;


@Service
public class DuckService {

  private final DuckApiClient duckApiClient;

  @Autowired
  DuckService(DuckApiClient duckApiClient) {
    this.duckApiClient = duckApiClient;
  }

  public DuckResponse getDuck() {
    return duckApiClient.getDuck().block();

  }
}
