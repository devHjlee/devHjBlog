package com.websocketclient.websocket;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OkHttpWebSocketClient {
    private WebSocket ws = null;

    @PostConstruct
    public void connect() throws InterruptedException {
        final OkHttpClient client = new OkHttpClient();
        JsonArray root = new JsonArray();
        JsonObject type = new JsonObject();
        JsonArray codesObj = new JsonArray();

        codesObj.add("KRW-XRP");
        root.add(new JsonObject());
        root.get(0).getAsJsonObject().addProperty("ticket", UUID.randomUUID().toString());
        type.addProperty("type", "ticker");
        type.add("codes", codesObj);
        root.add(type);

        Request request = new Request.Builder()
                .url("wss://api.upbit.com/websocket/v1")
                .addHeader("options", root.toString())
                .build();
        log.info(root.toString());
        ws = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull okhttp3.Response response) {
                log.info("WebSocket Open!!!");
                webSocket.send(Objects.requireNonNull(webSocket.request().header("options")));
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
                log.info("OKHTTP Client : " +bytes.string(StandardCharsets.UTF_8));
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) { }

            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) { }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, okhttp3.Response response) { }
        });

        client.dispatcher().executorService().awaitTermination(5, TimeUnit.SECONDS);
        ws.send(root.toString());
    }

}