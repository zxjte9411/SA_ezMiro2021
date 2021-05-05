package ntut.csie.islab.miro.usecase.board;

import ntut.csie.islab.miro.adapter.presenter.GetBoardContentPresenter;
import ntut.csie.islab.miro.adapter.repository.board.BoardRepository;
import ntut.csie.islab.miro.entity.model.board.Board;
import ntut.csie.islab.miro.entity.model.board.event.BoardContentMightExpire;
import ntut.csie.islab.miro.adapter.repository.textFigure.TextFigureRepository;
import ntut.csie.islab.miro.entity.model.textFigure.TextFigure;
import ntut.csie.islab.miro.usecase.textFigure.TextFigureDto;
import ntut.csie.sslab.ddd.model.DomainEventBus;
import ntut.csie.sslab.ddd.usecase.cqrs.ExitCode;

import java.util.List;

public class GetBoardContentUseCase {

    private DomainEventBus domainEventBus;
    private BoardRepository boardRepository;
    private TextFigureRepository textFigureRepository;

    public GetBoardContentUseCase(DomainEventBus domainEventBus, BoardRepository boardRepository , TextFigureRepository textFigureRepository) {
        this.domainEventBus = domainEventBus;
        this.boardRepository = boardRepository;
        this.textFigureRepository = textFigureRepository;
    }

    public GetBoardContentInput newInput() {
        return new GetBoardContentInput();
    }


    public void execute(GetBoardContentInput input, GetBoardContentPresenter presenter) {
        Board board = this.boardRepository.findById(input.getBoardId()).orElse(null);
        if (null == board){
            presenter.setBoardId(input.getBoardId())
                    .setExitCode(ExitCode.FAILURE)
                    .setMessage("Get board failed: board not found, board id = " + input.getBoardId());
            domainEventBus.post(new BoardContentMightExpire(input.getBoardId()));
            return;
        }


        List<TextFigure> textFigureList = this.textFigureRepository.findFiguresByBoardId(board.getBoardId());
        List<TextFigureDto> textFigureDtos = ConvertFigureToDto.transform(textFigureList);


        presenter.setBoardId(board.getBoardId()).setFigures(textFigureDtos);
    }
}