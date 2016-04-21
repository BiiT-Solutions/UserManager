package com.biit.usermanager.entity.pool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.biit.usermanager.entity.IElement;
import com.biit.usermanager.entity.pool.config.PoolConfigurationReader;
import com.biit.utils.pool.SimplePool;

public abstract class BasePool<ElementId, Type extends IElement<ElementId>> extends SimplePool<ElementId, Type> {

	// Classification by string.
	private Map<String, Long> elementsTagTime; // tag -> time.
	private Map<String, Set<Type>> elementsByTag;

	public BasePool() {
		reset();
	}

	public void addElementByTag(Set<Type> elements, String tag) {
		if (tag != null && elements != null) {
			elementsTagTime.put(tag, System.currentTimeMillis());
			Set<Type> existingGroups = elementsByTag.get(tag);
			if (existingGroups == null) {
				existingGroups = new HashSet<Type>();
			}
			existingGroups.addAll(elements);
			elementsByTag.put(tag, existingGroups);
		}
	}

	public void addElementByTag(Type element, String tag) {
		if (tag != null && element != null) {
			addElement(element);
			elementsTagTime.put(tag, System.currentTimeMillis());
			Set<Type> elements = elementsByTag.get(tag);
			if (elements == null) {
				elements = new HashSet<Type>();
			}
			elements.add(element);
			elementsByTag.put(tag, elements);
		}
	}

	public Set<Type> getElementsByTag(String tag) {
		long now = System.currentTimeMillis();
		String nextGroupTag = null;
		if (elementsTagTime.size() > 0) {
			Iterator<String> e = new HashMap<String, Set<Type>>(elementsByTag).keySet().iterator();
			while (e.hasNext()) {
				nextGroupTag = e.next();
				if (elementsTagTime.get(nextGroupTag) != null && (now - elementsTagTime.get(nextGroupTag)) > getExpirationTime()) {
					// object has expired
					removeElementsByTag(nextGroupTag);
					nextGroupTag = null;
				} else {
					if (tag.equals(nextGroupTag)) {
						return elementsByTag.get(nextGroupTag);
					}
				}
			}
		}
		return null;
	}

	public void removeElementsByTag(String tag) {
		if (tag != null) {
			elementsByTag.remove(tag);
			elementsTagTime.remove(tag);
		}
	}

	public void removeElementsByTag(String tag, IElement<Long> element) {
		if (tag != null) {
			if (elementsByTag.get(tag) != null) {
				elementsByTag.get(tag).remove(element);
			}
		}
	}

	@Override
	public void reset() {
		super.reset();
		elementsTagTime = new HashMap<String, Long>();
		elementsByTag = new HashMap<String, Set<Type>>();
	}

	/**
	 * Never expires elements here.
	 * 
	 * @param elementId
	 * @return
	 */
	@Override
	public boolean isDirty(Type element) {
		return false;
	}

	@Override
	public long getExpirationTime() {
		return PoolConfigurationReader.getInstance().getExpirationTime();
	}
}
