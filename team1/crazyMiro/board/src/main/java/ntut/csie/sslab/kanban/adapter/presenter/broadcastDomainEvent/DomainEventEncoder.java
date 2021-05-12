package ntut.csie.sslab.kanban.adapter.presenter.broadcastDomainEvent;


import com.google.gson.Gson;
import ntut.csie.sslab.ddd.model.DomainEvent;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class DomainEventEncoder implements Encoder.Text<DomainEvent> {

    @Override
    public String encode(DomainEvent domainEvent) throws EncodeException {

        String event = domainEvent.getClass().getSimpleName();
        String obstr = new Gson().toJson(domainEvent);
        return obstr;
    }


    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {

    }
}
