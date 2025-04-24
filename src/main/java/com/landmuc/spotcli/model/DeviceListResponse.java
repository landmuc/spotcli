package com.landmuc.spotcli.model;

import java.util.List;

public record DeviceListResponse(
    List<DeviceResponse> devices) {
}
