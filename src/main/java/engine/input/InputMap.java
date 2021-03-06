package engine.input;

import java.util.HashMap;
import java.util.Map;

public abstract class InputMap {

	public String deviceName;
	public int deviceNumber;

	protected Map<String, Float> actionToValueMap = new HashMap<>();

	public float getValueForAction(String action) {
		Float rt = actionToValueMap.get(action);
		return rt == null ? 0f : rt;
	}

	public void setValue(String action, float value) {
		actionToValueMap.put(action, value);
	}
}