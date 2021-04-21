package ntut.csie.selab.usecase;

import ntut.csie.selab.adapter.widget.WidgetRepositoryImpl;
import ntut.csie.selab.entity.model.widget.Coordinate;
import ntut.csie.selab.entity.model.widget.StickyNote;
import ntut.csie.selab.entity.model.widget.Widget;
import ntut.csie.selab.usecase.widget.edit.EditTextOfStickyNoteInput;
import ntut.csie.selab.usecase.widget.edit.EditTextOfStickyNoteOutput;
import ntut.csie.selab.usecase.widget.edit.EditTextOfStickyNoteUseCase;
import ntut.csie.selab.usecase.widget.WidgetRepository;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class EditTextOfStickyNoteTest {

    @Test
    public void edit_text_of_sticky_note_should_succeed() {
        // Arrange
        WidgetRepository widgetRepository = new WidgetRepositoryImpl();
        String stickyNoteId = "1";
        Coordinate stickyNoteCoordinate = new Coordinate(1, 1, 2, 2);
        Widget stickyNote = new StickyNote(stickyNoteId, "0", stickyNoteCoordinate);
        widgetRepository.add(stickyNote);
        EditTextOfStickyNoteUseCase editTextOfStickyNoteUseCase = new EditTextOfStickyNoteUseCase(widgetRepository);
        EditTextOfStickyNoteInput input = new EditTextOfStickyNoteInput();
        EditTextOfStickyNoteOutput output = new EditTextOfStickyNoteOutput();
        input.setStickyNoteId("1");
        input.setText("modified text");

        // Act
        editTextOfStickyNoteUseCase.execute(input, output);

        // Assert
        Assert.assertEquals("modified text", output.getModifiedText());
    }
}
