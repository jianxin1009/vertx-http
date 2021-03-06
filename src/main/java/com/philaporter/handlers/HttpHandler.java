package com.philaporter.handlers;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/** @author Philip Porter */
public class HttpHandler {

  private Vertx vertx;
  private EventBus eb;
  public static final String PROCESSING_HANDLER = "processingHandler";

  public HttpHandler(Vertx vertx) {
    this.vertx = vertx;
    eb = this.vertx.eventBus();
  }

  public void handleGetEmployees(RoutingContext routingContext) {
    final JsonObject json = new JsonObject().put("action", "getAll");
    eb.publish(PROCESSING_HANDLER, json);
    routingContext
        .response()
        .putHeader("content-type", "application/json")
        .end(json.encodePrettily());
    // TODO: Connect the response from the RedisHandler with the response here
  }

  public void handleGetEmployee(RoutingContext routingContext) {
    String empId = routingContext.request().getParam("empId");
    HttpServerResponse response = routingContext.response();
    if (empId != null) {
      final JsonObject json = new JsonObject().put("empId", empId).put("action", "get");
      eb.publish(PROCESSING_HANDLER, json);
      response.putHeader("content-type", "application/json").end(json.encodePrettily());
      // TODO: Consider changing what is actually return as a request, instead of emphasizing the console info
    } else {
      sendError(418, response);
    }
  }

  public void handleAddEmployee(RoutingContext routingContext) {
    String empId = routingContext.request().getParam("empId");
    HttpServerResponse response = routingContext.response();
    if (empId != null) {
      JsonObject employee = routingContext.getBodyAsJson();
      if (employee != null) {
        final JsonObject json = new JsonObject().put("employee", employee).put("action", "add");
        eb.publish(PROCESSING_HANDLER, json);
        response.putHeader("content-type", "application/json").end(json.encodePrettily());
      } else {
        sendError(418, response);
      }
    } else {
      sendError(418, response);
    }
  }

  public void handleUpdateAddEmployee(RoutingContext routingContext) {
    String empId = routingContext.request().getParam("empId");
    HttpServerResponse response = routingContext.response();
    if (empId != null) {
      JsonObject employee = routingContext.getBodyAsJson();
      if (employee != null) {
        final JsonObject json = new JsonObject().put("employee", employee).put("action", "update");
        eb.publish(PROCESSING_HANDLER, json);
        response.putHeader("content-type", "application/json").end(json.encodePrettily());
      } else {
        sendError(418, response);
      }
    } else {
      sendError(418, response);
    }
  }

  public void handleRemoveEmployee(RoutingContext routingContext) {
    String empId = routingContext.request().getParam("empId");
    HttpServerResponse response = routingContext.response();
    if (empId != null) {
      final JsonObject json = new JsonObject().put("empId", empId).put("action", "remove");
      eb.publish(PROCESSING_HANDLER, json);
      response.putHeader("content-type", "application/json").end(json.encodePrettily());
    } else {
      sendError(418, response);
    }
  }

  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }
}
