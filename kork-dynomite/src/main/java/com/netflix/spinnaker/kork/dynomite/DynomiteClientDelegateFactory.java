/*
 * Copyright 2018 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.spinnaker.kork.dynomite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.spinnaker.kork.jedis.RedisClientDelegateFactory;
import com.netflix.spinnaker.kork.jedis.RedisClientConfiguration.Driver;

import java.util.Map;

import static com.netflix.spinnaker.kork.jedis.RedisClientConfiguration.Driver.DYNOMITE;

public class DynomiteClientDelegateFactory implements RedisClientDelegateFactory<DynomiteClientDelegate> {

  private ObjectMapper objectMapper;
  private DiscoveryClient discoveryClient;

  public DynomiteClientDelegateFactory(ObjectMapper objectMapper, DiscoveryClient discoveryClient) {
    this.objectMapper = objectMapper;
    this.discoveryClient = discoveryClient;
  }

  @Override
  public boolean supports(Driver driver) {
    return driver == DYNOMITE;
  }

  @Override
  public DynomiteClientDelegate build(String name, Map<String, Object> properties) {
    DynomiteDriverProperties props = objectMapper.convertValue(properties, DynomiteDriverProperties.class);
    return new DynomiteClientDelegate(
      name,
      new DynomiteClientFactory().discoveryClient(discoveryClient).build(props)
    );
  }
}