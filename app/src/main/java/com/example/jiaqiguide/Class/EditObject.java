package com.example.jiaqiguide.Class;

public interface EditObject {
    void TurnOnEditMode(Object ojb);

    void TurnOffEditMode(Object ojb);
    enum EditType{
        EditZone,
        EditMarker
    }
}

