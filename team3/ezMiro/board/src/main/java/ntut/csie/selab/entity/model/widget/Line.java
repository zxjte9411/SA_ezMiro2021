package ntut.csie.selab.entity.model.widget;

import ntut.csie.selab.entity.model.widget.event.LineLinked;
import ntut.csie.selab.entity.model.widget.event.LinkedStickyNoteIdDeleted;

import java.util.Date;

public class Line extends Widget {

    private String headWidgetId;
    private String tailWidgetId;

    public Line(String id, String boardId, Coordinate coordinate) {
        super(id, boardId, coordinate, WidgetType.LINE.getType());
    }

    public String getHeadWidgetId() {
        return headWidgetId;
    }

    public void setHeadWidgetId(String headWidgetId) {
        this.headWidgetId = headWidgetId;
    }

    public String getTailWidgetId() {
        return tailWidgetId;
    }

    public void setTailWidgetId(String tailWidgetId) {
        this.tailWidgetId = tailWidgetId;
    }

    public void link(String endPoint, String widgetId) {
        if(endPoint.equals("head")) {
            headWidgetId = widgetId;
        } else {
            tailWidgetId = widgetId;
        }

        addDomainEvent((new LineLinked(
                new Date(),
                this.boardId,
                getId()
        )));
    }

    public void removeLinkedWidget(String widgetId) {
        if(headWidgetId != null && headWidgetId.equals(widgetId)) {
            this.headWidgetId = null;
        }
        if(tailWidgetId != null && tailWidgetId.equals(widgetId)) {
            this.tailWidgetId = null;
        }

        addDomainEvent((new LinkedStickyNoteIdDeleted(
                new Date()
        )));
    }
}
