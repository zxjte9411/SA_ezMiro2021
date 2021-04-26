package ntut.csie.team5.adapter.controller.rest.springboot.figure.post;

import ntut.csie.sslab.ddd.adapter.presenter.cqrs.CqrsCommandPresenter;
import ntut.csie.sslab.ddd.adapter.presenter.cqrs.CqrsCommandViewModel;
import ntut.csie.team5.usecase.figure.note.post.PostNoteInput;
import ntut.csie.team5.usecase.figure.note.post.PostNoteUseCase;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.awt.*;

@RestController
public class PostNoteController {

    private PostNoteUseCase postNoteUseCase;

    @Autowired
    public void setPostNoteUseCase(PostNoteUseCase postNoteUseCase) {
        this.postNoteUseCase = postNoteUseCase;
    }

    @PostMapping(path = "/notes", consumes = "application/json", produces = "application/json")
    public CqrsCommandViewModel postNote(@QueryParam("boardId") String boardId, @RequestBody String noteInfo) {
        int x = 0;
        int y = 0;
        Color color = Color.BLACK;
        try {
            JSONObject noteJSON = new JSONObject(noteInfo);
            x = noteJSON.getInt("x");
            y = noteJSON.getInt("y");
            color = Color.getColor(noteJSON.getString("color"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostNoteInput input = postNoteUseCase.newInput();
        input.setBoardId(boardId);
        input.setPosition(new Point(x, y));
        input.setColor(color);

        CqrsCommandPresenter presenter = CqrsCommandPresenter.newInstance();

        postNoteUseCase.execute(input, presenter);
        return presenter.buildViewModel();
    }

}
