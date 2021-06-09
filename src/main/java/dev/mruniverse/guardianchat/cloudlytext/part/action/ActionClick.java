package dev.mruniverse.guardianchat.cloudlytext.part.action;

public class ActionClick {

    private final ActionTypeClick actionTypeClick;
    private final String value;

    public ActionClick(ActionTypeClick actionTypeClick, String value){
        this.value = value;
        this.actionTypeClick = actionTypeClick;
    }

    public ActionTypeClick getActionTypeClick(){
        return actionTypeClick;
    }

    public String getValue(){
        return value;
    }

}
