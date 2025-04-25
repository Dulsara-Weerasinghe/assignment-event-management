package com.event.service.util;

import java.util.UUID;

public class EventUtil {

  public static String generateEventId() {
    return UUID.randomUUID().toString();
  }

}
