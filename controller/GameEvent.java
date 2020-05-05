package controller;

/**
 * GameEvents
 * <p>
 * Is used by the model to signal its listeners (mainly the view) when certain specific events
 * relevant for the BreakOut game occur. Usually when state of model changes significantly. Minor
 * changes are handle be Property Bindings.
 * <p>
 * 03.01.2018
 * @author Frank Kopp
 */
public class GameEvent {

    private final GameEventType eventType;
  private Object args;

  /**
   * Creates a GameEvent of a certain type.
   * @param eventType
   * @param args
   */
  public GameEvent(GameEventType eventType, Object...args) {
    this.eventType = eventType;
    this.args = args;
  }

  /**
   * @return event type of the event
   */
  public GameEventType getEventType() {
    return eventType;
  }

  /**
   * @return returns parameters which might have been added to an event. Usually very
   * event specific.
   */
  public Object getEventParameter() {
    return this.args;
  } 

  @Override
  public String toString() {
    return "GameEvent [eventType=" + eventType + "]";
  }
  
  public enum GameEventType {
	START,
	WIN,
    BALLSTOP,
	MOVEMENT;
  }
}