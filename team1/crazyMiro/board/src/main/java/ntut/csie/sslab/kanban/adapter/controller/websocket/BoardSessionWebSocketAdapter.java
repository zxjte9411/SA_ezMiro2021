package ntut.csie.sslab.kanban.adapter.controller.websocket;


import ntut.csie.sslab.ddd.adapter.presenter.cqrs.CqrsCommandPresenter;
import ntut.csie.sslab.ddd.usecase.cqrs.CqrsCommandOutput;
import ntut.csie.sslab.kanban.adapter.presenter.broadcastDomainEvent.DomainEventEncoder;
import ntut.csie.sslab.kanban.application.springboot.web.config.websocket.EndpointConfigure;
import ntut.csie.sslab.kanban.entity.model.Coordinate;
import ntut.csie.sslab.kanban.entity.model.cursor.Cursor;
import ntut.csie.sslab.kanban.entity.model.cursor.event.CursorMoved;
import ntut.csie.sslab.kanban.usecase.BoardSessionBroadcaster;
import ntut.csie.sslab.kanban.usecase.board.BoardRepository;
import ntut.csie.sslab.kanban.usecase.cursor.CursorRepository;
import ntut.csie.sslab.kanban.usecase.cursor.create.CreateCursorInput;
import ntut.csie.sslab.kanban.usecase.cursor.create.CreateCursorUseCase;
import ntut.csie.sslab.kanban.usecase.cursor.delete.DeleteCursorInput;
import ntut.csie.sslab.kanban.usecase.cursor.delete.DeleteCursorUseCase;
import ntut.csie.sslab.kanban.usecase.cursor.delete.DeleteCursorUseCaseImpl;
import ntut.csie.sslab.kanban.usecase.cursor.move.MoveCursorInput;
import ntut.csie.sslab.kanban.usecase.cursor.move.MoveCursorUseCase;
import ntut.csie.sslab.kanban.usecase.figure.FigureRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/websocket/{boardId}/{ip}",
        encoders = {DomainEventEncoder.class},
        configurator = EndpointConfigure.class)
@Component
public class BoardSessionWebSocketAdapter {

    private MoveCursorUseCase moveCursorUseCase;

    private CreateCursorUseCase createCursorUseCase;
    private DeleteCursorUseCase deleteCursorUseCase;
    private BoardSessionBroadcaster boardSessionBroadcaster;

    @Autowired
    public void setCreateCursorUseCase(CreateCursorUseCase createCursorUseCase) {
        this.createCursorUseCase = createCursorUseCase;
    }

    @Autowired
    public void setDeleteCursorUseCase(DeleteCursorUseCase deleteCursorUseCase) {
        this.deleteCursorUseCase = deleteCursorUseCase;
    }

    @Autowired
    public void setMoveCursorUseCase(MoveCursorUseCase moveCursorUseCase) {
        this.moveCursorUseCase = moveCursorUseCase;
    }

    @Autowired
    public void setBoardSessionBroadcaster(BoardSessionBroadcaster boardSessionBroadcaster) {
        this.boardSessionBroadcaster = boardSessionBroadcaster;
    }



    @OnMessage
    public void onMessage(String message, Session session)  {
        String event = "";
        JSONObject info;
        try {
            JSONObject jsonObject = new JSONObject(message);
            event = jsonObject.getString("event");
            info = jsonObject.getJSONObject("info");
            websocketEventHandler(event, info);
        }catch (JSONException err){
            System.out.println(err);
        }

    }

    private void websocketEventHandler(String event, JSONObject info) {
        if(CursorMoved.class.getSimpleName().equals(event)){
            handleCursorMoved(info);
        }
    }



    @OnOpen
    public void onOpen(Session session,
                       @PathParam("boardId") String boardId,
                       @PathParam("ip") String ip) throws IOException {
        ((WebSocketBroadcaster)boardSessionBroadcaster).addSession(session.getId(), session);

        CreateCursorInput input = createCursorUseCase.newInput();
        CqrsCommandPresenter presenter = CqrsCommandPresenter.newInstance();
        input.setBoardId(boardId);
        input.setIp(ip);
        input.setSessionId(session.getId());
        createCursorUseCase.execute(input, presenter);

//        ((WebSocketBroadcaster)boardSessionBroadcaster).addSession(presenter.getId(), session);
//        boardSessionBroadcaster.broadcast(new CursorMoved("123", new Coordinate(0, 0)), presenter.getId());
//        boardSessionBroadcaster.broadcast(new CursorMoved("123", new Coordinate(0, 0)), presenter.getId());

    }

    @OnClose
    public void onClose(Session session) {
        DeleteCursorInput input = deleteCursorUseCase.newInput();
        CqrsCommandOutput output = CqrsCommandPresenter.newInstance();
        input.setCursorId(session.getId());

        deleteCursorUseCase.execute(input, output);
        ((WebSocketBroadcaster)boardSessionBroadcaster).removeSession(session.getId());
    }

    private void handleCursorMoved(JSONObject info) {
        Coordinate newPosition = null;
        String cursorId = "";
        try {
            Long x = info.getJSONObject("position").getLong("x");
            Long y = info.getJSONObject("position").getLong("y");
            newPosition = new Coordinate(x, y);
            cursorId = info.getString("cursorId");
        }catch (JSONException err){
            System.out.println(err);
            return;
        }
        MoveCursorInput input = moveCursorUseCase.newInput();
        CqrsCommandOutput output = CqrsCommandPresenter.newInstance();
        input.setCursorId(cursorId);
        input.setPosition(newPosition);
        moveCursorUseCase.execute(input, output);
    }
}