package ntut.csie.sslab.miro.adapter.controller.rest.springboot.line.delete;

import ntut.csie.sslab.ddd.adapter.presenter.cqrs.CqrsCommandPresenter;
import ntut.csie.sslab.ddd.adapter.presenter.cqrs.CqrsCommandViewModel;
import ntut.csie.sslab.miro.usecase.figure.sticker.delete.DeleteStickerInput;
import ntut.csie.sslab.miro.usecase.figure.sticker.delete.DeleteStickerUseCase;
import ntut.csie.sslab.miro.usecase.line.delete.DeleteLineInput;
import ntut.csie.sslab.miro.usecase.line.delete.DeleteLineUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;

@RestController
@CrossOrigin
public class DeleteLineController {
    private DeleteLineUseCase deleteLineUseCase;

    @Autowired
    public void setDeleteLineUseCase(DeleteLineUseCase deleteLineUseCase) {
        this.deleteLineUseCase = deleteLineUseCase;
    }

    @PutMapping(path = "${MIRO_PREFIX}/board/line/delete")
    public CqrsCommandViewModel deleteLine(@RequestParam("lineId") String lineId) {
        DeleteLineInput input = deleteLineUseCase.newInput();
        input.setLineId(lineId);
        CqrsCommandPresenter presenter = CqrsCommandPresenter.newInstance();
        deleteLineUseCase.execute(input, presenter);
        return presenter.buildViewModel();
    }
}
