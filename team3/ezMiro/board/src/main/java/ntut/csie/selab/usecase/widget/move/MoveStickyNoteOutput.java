package ntut.csie.selab.usecase.widget.move;

import ntut.csie.selab.entity.model.widget.Coordinate;

public class MoveStickyNoteOutput {
    private String stickyNoteId;
    private Coordinate coordinate;

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getStickyNoteId() {
        return stickyNoteId;
    }

    public void setStickyNoteId(String stickyNoteId) {
        this.stickyNoteId = stickyNoteId;
    }
}
